package com.example.firebaseintro;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText userid,pass;
    Button login,signup;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userid=findViewById(R.id.login_userid);
        pass=findViewById(R.id.login_pass);
        login=findViewById(R.id.login_btn);
        signup=findViewById(R.id.login_signupbtn);
        progressBar=findViewById(R.id.login_pb);

        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent in =new Intent(MainActivity.this,SignupActivity.class);
                startActivity(in);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

    }


    public void userLogin() {
        String email = userid.getText().toString();
        String password = pass.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userid.setError("Enter valid email address");
            userid.requestFocus();
            return;
        }
        if (userid.length() == 0) {
            userid.setError("Enter email address");
            userid.requestFocus();
            return;
        } else if (pass.length() == 0) {
            pass.setError("Enter password");
            pass.requestFocus();
            return;
        } else if (pass.length() < 6) {
            pass.setError("Password must characters long");
            pass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(MainActivity.this,ChatActivity.class));
                            }
                            else
                                Toast.makeText(MainActivity.this, "Registration error", Toast.LENGTH_SHORT).show();

                        }
                    });
        } catch (Exception e) {
            System.out.println("Error----->" + e.getMessage());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null) {
            finish();
            Intent in =getIntent();
            String n=in.getStringExtra("nme");
            in.putExtra("nme",n);
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
        }
    }
}
