package com.example.firebaseintro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    EditText msg;
    Button send;
    LinearLayout ll;
    ProgressBar pb;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth=FirebaseAuth.getInstance();

        myRef=FirebaseDatabase.getInstance().getReference().child("messages");

        msg=findViewById(R.id.txt_msg);
        ll=findViewById(R.id.ll);
        send=findViewById(R.id.sendbtn);
        pb=findViewById(R.id.chat_pb);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMsg();
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ll.removeAllViews();
                pb.setVisibility(View.INVISIBLE);
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    Intent in =getIntent();
                    String n=in.getStringExtra("nme");
                    TextView textView=new TextView(ChatActivity.this);
                    textView.setText(""+n);
                    textView.setTextSize(8);
                    TextView tv=new TextView(ChatActivity.this);
                    tv.setText(child.getValue().toString());
                    tv.setTextSize(18);
                    ll.addView(tv);
                    ll.addView(textView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }


    public void saveMsg()
    {
        Intent in =getIntent();
        String n=in.getStringExtra("nme");

        TextView tv1=new TextView(this);
        tv1.setText(msg.getText().toString());
        tv1.setTextSize(20);


        TextView tv2=new TextView(this);
        tv2.setText(""+n);
        tv2.setTextSize(10);

        String m=msg.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("messages");
        ref.push().setValue(m);

        Toast.makeText(this, "Information saved successfully", Toast.LENGTH_SHORT).show();
        ll.addView(tv1);
        ll.addView(tv2);
        msg.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.chat_logout)
        {
            finish();
            mAuth.signOut();
            startActivity(new Intent(ChatActivity.this,MainActivity.class));
        }
        else if(item.getItemId()==R.id.chat_exit)
        {
            finish();
        }
        else if(item.getItemId()==R.id.chat_editprofile)
        {
            startActivity(new Intent(ChatActivity.this,ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}







