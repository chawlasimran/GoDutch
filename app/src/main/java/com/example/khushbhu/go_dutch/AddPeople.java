package com.example.khushbhu.go_dutch;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddPeople extends AppCompatActivity {
    EditText name;
    EditText number;
    Button add;
    DatabaseReference db;

    FirebaseAuth firebaseAuth;
    FloatingActionButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        name=(EditText) findViewById(R.id.editText5);
        number=(EditText) findViewById(R.id.editText6);
        add=(Button) findViewById(R.id.button5);
        btn=(FloatingActionButton) findViewById(R.id.fab);
        firebaseAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPeople.this,thirdActivity.class));
            }
        });




        db=FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addpeople();
            }
        });


    }
    void addpeople()
    {
         final String person=name.getText().toString().trim().toUpperCase();
         final String phone=number.getText().toString().trim();

         final int balance=0;
                    if (!TextUtils.isEmpty(person) && phone.matches("[0-9]{10}"))
                    {
                        final String id = db.push().getKey();
                        final str STR = new str(id, person, phone, balance);

                        Query namequery=FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid()).orderByChild("person").equalTo(person);
                        namequery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0) {
                                    Toast.makeText(AddPeople.this, "Duplicate Names not allowed", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    db.child(id).setValue(STR);
                                    Toast.makeText(AddPeople.this, "PERSON ADDED", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(AddPeople.this,thirdActivity.class));
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                    else {
                        Toast.makeText(AddPeople.this, "Enter appropriate name and phone number", Toast.LENGTH_LONG).show();
                    }
                }
            }









