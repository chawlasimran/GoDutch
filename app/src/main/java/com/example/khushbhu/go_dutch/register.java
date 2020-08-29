package com.example.khushbhu.go_dutch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {

    private EditText username,userpassword,name,phonep;
    private Button regbutton;
    private TextView userlogin;
    private RadioGroup rg;
    private RadioButton rb;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText) findViewById(R.id.t1);
        userpassword=(EditText) findViewById(R.id.editText2);
        regbutton=(Button)findViewById(R.id.button2);
        userlogin=(TextView) findViewById(R.id.textView5);
        name = (EditText)findViewById(R.id.name_id);
        phonep=(EditText)findViewById(R.id.editText9);
        rg=(RadioGroup)findViewById(R.id.radioGroup);

        firebaseauth = FirebaseAuth.getInstance();


        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( validate())
               {
                   String uname=username.getText().toString().trim();
                   String upass=userpassword.getText().toString().trim();
                   String phonenumber=phonep.getText().toString().trim();
                   String pname=name.getText().toString().trim();
                   if(TextUtils.isEmpty(uname) || TextUtils.isEmpty(upass) || TextUtils.isEmpty(phonenumber) || TextUtils.isEmpty(pname)){
                       Toast.makeText(register.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(register.this,register.class));

                   }
                   else if(!phonenumber.matches("[0-9]{10}")){
                       Toast.makeText(register.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(register.this,register.class));
                   }
                   else if(!uname.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")){
                       Toast.makeText(register.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(register.this,register.class));
                   }
                   else {
                       firebaseauth.createUserWithEmailAndPassword(uname, upass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                           @Override
                                                                                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                               if (task.isSuccessful()) {
                                                                                                                   sendEmailVerification();
                                                                                                               }
                                                                                                               else {
                                                                                                                   Toast.makeText(register.this, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                                                                                                               }
                                                                                                           }
                                                                                                       }
                       );
                   }
               }
            }
        });
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this,MainActivity.class));
            }
        });

    }

public void rbclick(View v){
        int radiobuttonid=rg.getCheckedRadioButtonId();
        rb=(RadioButton)findViewById(radiobuttonid);
}


    private Boolean validate() {
        Boolean result=false;
        String name=username.getText().toString();
        String password=username.getText().toString();
        if(name.isEmpty() && password.isEmpty()){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT);
        }
        else {
            result = true;
        }
        return result;
    }

    private  void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseauth.getCurrentUser();
       if( firebaseUser!=null) {
           firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       Toast.makeText(register.this,"Successfully registerd,Email verification has sent!",Toast.LENGTH_SHORT).show();
                       firebaseauth.signOut();
                       finish();
                       startActivity(new Intent(register.this,MainActivity.class));
                   }
                   else {
                       Toast.makeText(register.this," Verification Mail has not been sent!",Toast.LENGTH_SHORT).show();
                   }
               }
           });
       }
    }
}
