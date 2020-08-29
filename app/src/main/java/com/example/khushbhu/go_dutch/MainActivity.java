package com.example.khushbhu.go_dutch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText name,password;
    private Button login;
    private TextView info,ur;
    private int counter=5;
    private TextView forgotpassword;
    FirebaseAuth firebaseauth;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(EditText)findViewById(R.id.editText3);
        password=(EditText)findViewById(R.id.editText4);
        login=(Button)findViewById(R.id.button);
        ur=(TextView)findViewById(R.id.textView4);
        info=(TextView)findViewById(R.id.textView3);
        forgotpassword=(TextView) findViewById(R.id.Forgotpassword);
        info.setText("Number of attempts Remaining: 5");

        firebaseauth= FirebaseAuth.getInstance();
        progressdialog=new ProgressDialog(this);

        FirebaseUser user = firebaseauth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(MainActivity.this,thirdActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(), password.getText().toString());
            }

        });
        ur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,register.class));
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });
    }
    private void validate(String nam,String pass){

        progressdialog.setMessage("Wait till you are being verified");
        progressdialog.show();

        firebaseauth.signInWithEmailAndPassword(nam,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressdialog.dismiss();
                    checkemailverification();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"LOGIN FAILED",Toast.LENGTH_SHORT).show();
                    counter--;
                    info.setText("Number of attempts Remaining:"+String.valueOf(counter));
                    progressdialog.dismiss();
                    if(counter==0)
                    {
                        login.setEnabled(false);
                    }
                }
            }
        });
    }
    private void checkemailverification()
    {
        FirebaseUser firebaseuser=firebaseauth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseuser.isEmailVerified();
        if(emailflag){
            finish();
            startActivity(new Intent(MainActivity.this,thirdActivity.class));
        }
        else {
            Toast.makeText(MainActivity.this,"Verify your email",Toast.LENGTH_SHORT).show();
            firebaseauth.signOut();
        }
    }

}
