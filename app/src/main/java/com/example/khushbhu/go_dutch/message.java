package com.example.khushbhu.go_dutch;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class message extends AppCompatActivity {
    TextView tv;
    EditText et;
    TextView email;
    EditText devid;
    Button submit;
    Firebase firebase;
    FirebaseUser current;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        tv = (TextView)findViewById(R.id.msgtxt);
        et = (EditText)findViewById(R.id.nametxt);
        email = (TextView)findViewById(R.id.youremail);
        submit = (Button)findViewById(R.id.send);
        devid = (EditText)findViewById(R.id.devmail);
        mauth = FirebaseAuth.getInstance();
        current = mauth.getCurrentUser();

        Firebase.setAndroidContext(this);
        String uid=Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
       // firebase=new Firebase("https://go-dutch-32cf8.firebaseio.com/Users" + uid);
       // firebase=new Firebase("FEEDBACK AND QUERIES");
        firebase=new Firebase("https://go-dutch-32cf8.firebaseio.com/USERS " + "FEEDBACK AND QUERIES");

        tv.setText(getIntent().getStringExtra("Name"));
        email.setText(current.getEmail());


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String text = tv.getText().toString();
                final String emaill = email.getText().toString();
                final String name = et.getText().toString();
                String developerid = devid.getText().toString();
                Intent ssend = new Intent(Intent.ACTION_SEND);
                ssend.putExtra(Intent.EXTRA_EMAIL,new String[]{developerid});
                ssend.putExtra(Intent.EXTRA_TEXT,text);
                //submit.putExtra(Intent.EXTRA_EMAIL,emaill);
                ssend.setType("message/rfc822");
                //submit.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(ssend,"Choose an app to send mail"));
                Firebase child_name = firebase.child(mauth.getUid()).child("Name");
                child_name.setValue(name);
                if(name.isEmpty())
                {
                    et.setError("This is the required field!");
                    submit.setEnabled(false);
                }
                else
                {
                    et.setError(null);
                    submit.setEnabled(true);
                }
                //startActivity(new Intent(message.this,MainActivity.class));
                Firebase child_email = firebase.child(mauth.getUid()).child("Email");
                child_email.setValue(emaill);
                if(emaill.isEmpty())
                {
                    email.setError("This is the required field!");
                    submit.setEnabled(false);
                }
                else
                {
                    email.setError(null);
                    submit.setEnabled(true);
                }

                Firebase child_message = firebase.child(mauth.getUid()).child("Message");
                child_message.setValue(text);
                if(text.isEmpty())
                {
                    tv.setError("This is the required field!");
                    submit.setEnabled(false);
                }
                else
                {
                    tv.setError(null);
                    submit.setEnabled(true);
                }
                Toast.makeText(message.this,"Your data has been sent to the server!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
