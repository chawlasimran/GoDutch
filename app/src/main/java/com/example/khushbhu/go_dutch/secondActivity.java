package com.example.khushbhu.go_dutch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class secondActivity extends AppCompatActivity {

    DatabaseReference db;
    public static final String name = "Person";
    public static final String type = "id";
    public static final String number = "phone";
    private ListView listViewstr;
    List<str> listitem;
    private FirebaseAuth firebaseauth;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


          //db = getIntent().getExtras().getParcelable("db");
        setContentView(R.layout.activity_second);
        firebaseauth=FirebaseAuth.getInstance();
        listViewstr =  findViewById(R.id.li);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(secondActivity.this,AddPeople.class));
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        db=FirebaseDatabase.getInstance().getReference(firebaseauth.getUid());
        listitem= new ArrayList<>();

        listViewstr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                str STR = listitem.get(position);
                Intent intent = new Intent(getApplicationContext(),AddBalance.class);
                intent.putExtra(name,STR.getPerson());
                intent.putExtra(type,STR.getStrid());
                intent.putExtra(number,STR.getPhone());
                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
           /* case R.id.logoutmenu:{
                firebaseauth.signOut();
                finish();
                startActivity(new Intent(secondActivity.this,MainActivity.class));
            }*/
            case R.id.contacts:{
                startActivity(new Intent(secondActivity.this,AddPeople.class));
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listitem.clear();
                for (DataSnapshot strSnapshot : dataSnapshot.getChildren()) {
                    str STR = strSnapshot.getValue(str.class);
                    listitem.add(STR);
                }

                group_list adapter = new group_list(secondActivity.this, listitem);
                listViewstr.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
