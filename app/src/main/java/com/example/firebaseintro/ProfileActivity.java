package com.example.firebaseintro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button btn_cancel,btn_save;
    EditText name ,username;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getUid();
        myRef=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        btn_save=findViewById(R.id.profile_save);
        btn_cancel=findViewById(R.id.profile_cancel);
        name=findViewById(R.id.profile_name);
        username=findViewById(R.id.profile_username);



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in =new Intent(ProfileActivity.this,ChatActivity.class);
                startActivity(in);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });
    }

    public void saveUserInfo()
    {
        String nm=name.getText().toString();
        String p=username.getText().toString();

        UserInfo userinfo=new UserInfo(nm,p);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        FirebaseUser user=mAuth.getCurrentUser();
        ref.child(user.getUid()).setValue(userinfo);

        Toast.makeText(this, "Information saved successfully", Toast.LENGTH_SHORT).show();
        finish();
        Intent in =new Intent(ProfileActivity.this,ChatActivity.class);
        in.putExtra("nme",name.getText().toString());
        startActivity(in);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                username.setText(dataSnapshot.child("username").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
