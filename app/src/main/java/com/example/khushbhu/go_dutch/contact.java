package com.example.khushbhu.go_dutch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class contact extends AppCompatActivity {

    EditText feedback;
    Button Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        feedback = (EditText)findViewById(R.id.questxt);
        Next = (Button)findViewById(R.id.nxtbtn);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = feedback.getText().toString();
                Intent intent = new Intent(contact.this,message.class);
                intent.putExtra("Name",text);
                startActivity(intent);
            }
        });
    }
}

