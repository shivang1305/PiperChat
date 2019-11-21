package com.example.firebaseintro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    EditText emailid,pass,name,usernm;
    CheckBox cb;
    Button login,signup;
    String email,password,usnme,nme;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth=FirebaseAuth.getInstance();
        emailid=findViewById(R.id.signup_email);
        pass=findViewById(R.id.signup_pass);
        name=findViewById(R.id.signup_name);
        usernm=findViewById(R.id.signup_usnme);
        signup=findViewById(R.id.signup_btn);
        login=findViewById(R.id.signup_loginbtn);
        cb=findViewById(R.id.signup_checkbox);
        pb=findViewById(R.id.signup_progressbar);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent in =new Intent(SignupActivity.this,MainActivity.class);
                startActivity(in);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cb.isChecked())
                {
                    Toast.makeText(SignupActivity.this, "Please agree on Terms and Conditions", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser();
                }
            }
        });

    }

    public void registerUser() {

        email = emailid.getText().toString();
        password = pass.getText().toString();
        usnme=usernm.getText().toString();
        nme=name.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailid.setError("Enter valid email address");
            emailid.requestFocus();
            return;
        }
        else if (email.length() == 0) {
            emailid.setError("Enter email address");
            emailid.requestFocus();
            return;
        }
        else if(password.length()==0) {
            pass.setError("Enter password");
            pass.requestFocus();
            return;
        }
        else if(password.length()<6) {
            pass.setError("Password must characters long");
            pass.requestFocus();
            return;
        }
        else if(nme.length()==0)
            {
                name.setError("Enter name");
                name.requestFocus();
                return;
            }
        else if(usnme.length()==0)
        {
            usernm.setError("Enter valid username");
            usernm.requestFocus();
            return;
        }
            pb.setVisibility(View.VISIBLE);
            try {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "User is Registered Successfully", Toast.LENGTH_SHORT).show();
                                    saveUserInfo();
                                }

                            }
                        });
            } catch (Exception e) {
                System.out.println("Error----->" + e.getMessage());
            }
    }

    public void saveUserInfo()
    {
        String nm=name.getText().toString();
        String u=usernm.getText().toString();

        UserInfo userinfo=new UserInfo(nm,u);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        FirebaseUser user=mAuth.getCurrentUser();
        ref.child(user.getUid()).setValue(userinfo);

        finish();
        Intent in =new Intent(SignupActivity.this,ChatActivity.class);
        in.putExtra("nme",name.getText().toString());
        startActivity(in);
    }
}
