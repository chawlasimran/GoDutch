package com.example.khushbhu.go_dutch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class thirdActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseauth;
    FirebaseUser currentuser;
    //NavigationView navigationView;
    TextView tv;
    DatabaseReference db;
    static final String name = "Person";
    static final String type = "id";
    static final String number = "phone";

    private ListView listViewstr;
    List<str> listitem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        firebaseauth = FirebaseAuth.getInstance();
        listViewstr =  findViewById(R.id.li);
        tv=(TextView)findViewById(R.id.nav_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentuser = firebaseauth.getCurrentUser();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(thirdActivity.this,AddPeople.class));

            }
        });

        db=FirebaseDatabase.getInstance().getReference("Users").child(firebaseauth.getUid());
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


        listViewstr.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                str STR = listitem.get(position);
                showUpdateDialog(STR.getStrid(),STR.getPerson());
                return true;
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        update();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    private void showUpdateDialog(final String id,final String name)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_layout,null);
        dialogBuilder.setView(dialogView);
        final Button bt2 = (Button)  dialogView.findViewById(R.id.buttonh);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry(id,name);
            }
        });
        //setting title
        dialogBuilder.setTitle("This will remove "+name+" from the list.");
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


    }

    private  void deleteEntry(String id,String name)
    {
        final DatabaseReference DB3;
        final DatabaseReference DB4;

        DB3 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseauth.getUid()).child(id);

        DB4 = FirebaseDatabase.getInstance().getReference("Expenses").child(firebaseauth.getUid()).child(name);
        Query namequery=DB4;
        namequery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Toast.makeText(thirdActivity.this, "Expenses Exist", Toast.LENGTH_SHORT).show();
                }
                else {
                    DB3.removeValue();
                    DB4.removeValue();
                    Toast.makeText(thirdActivity.this,"Entry Deleted Successfully",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId())
        {
            case R.id.contacts:{
                startActivity(new Intent(thirdActivity.this,AddPeople.class));
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

                group_list adapter = new group_list(thirdActivity.this, listitem);
                listViewstr.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(thirdActivity.this,thirdActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_star) {
            startActivity(new Intent(thirdActivity.this,star.class));

        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(thirdActivity.this,contact.class));

        } else if (id == R.id.nav_logout) {

            firebaseauth.signOut();
            finish();
            startActivity(new Intent(thirdActivity.this,MainActivity.class));

        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"https://drive.google.com/drive/folders/1ZnatgmtmJ2GKwMUP7fFwFSPRPybmndNd?usp=sharing");
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent,"share via"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void update()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hview =  navigationView.getHeaderView(0);
        TextView uemail = hview.findViewById(R.id.nav_email);
        uemail.setText(currentuser.getEmail());
    }
}
