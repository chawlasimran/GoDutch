package com.example.khushbhu.go_dutch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class retrieved extends AppCompatActivity {
    DatabaseReference db;
    private ListView listViewstr;
    List<str> listitem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieved);

        listViewstr =  findViewById(R.id.li);

        db=FirebaseDatabase.getInstance().getReference("NITK");
        listitem= new ArrayList<>();
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // listitem.clear();
                for (DataSnapshot strSnapshot : dataSnapshot.getChildren()) {
                    str STR = strSnapshot.getValue(str.class);
                    listitem.add(STR);
                }

                group_list adapter = new group_list(retrieved.this, listitem);
                listViewstr.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
