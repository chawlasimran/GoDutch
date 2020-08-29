package com.example.khushbhu.go_dutch;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DecimalFormat;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddBalance extends AppCompatActivity {

    public Double total;
    TextView tv,totalText;
    EditText tv1,tv2;
    DatabaseReference DB1,DB2;
    Button btd,sms,bth;

    FirebaseAuth firebaseAuth;
    ListView listt;
    List<Balance> balanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);

        tv = (TextView) findViewById(R.id.textView6);
        bth = (Button) findViewById(R.id.button6);
        btd = (Button) findViewById(R.id.button9);
        firebaseAuth=FirebaseAuth.getInstance();
        tv1 = (EditText) findViewById(R.id.editText7);
        tv2 = (EditText) findViewById(R.id.editText8);
        listt = (ListView) findViewById(R.id.listt);
        totalText=(TextView) findViewById(R.id.textView8);
        sms=(Button) findViewById(R.id.button11);

        Intent intent = getIntent();

        balanceList = new ArrayList<>();
        String person= intent.getStringExtra(secondActivity.name);
        bth.setText("Borrowed");
        btd.setText("Lent");
        tv.setText(person);

        DB1 = FirebaseDatabase.getInstance().getReference("Expenses").child(firebaseAuth.getUid()).child(person);

        // Db2=FirebaseDatabase.getInstance().getReference()
      //  totalText.setText(total.toString());

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String numb= intent.getStringExtra(thirdActivity.number);
                Uri uri = Uri.parse("smsto:"+numb);
                Intent num = new Intent(Intent.ACTION_SENDTO, uri);

                if(total<0){
                    if(Math.ceil(total) == Math.floor(total))
                        num.putExtra("sms_body", "Hey I owe you Rs."+total.intValue()*-1);
                    else
                        num.putExtra("sms_body", "Hey I owe you Rs."+ String.format("%.2f",total*-1));
                }

                else{
                    if(Math.ceil(total) == Math.floor(total))
                        num.putExtra("sms_body", "Hey I owe you Rs."+total.intValue());
                    else
                        num.putExtra("sms_body","Hey you owe me Rs."+ String.format("%.2f",total));
                }

                startActivity(num);
            }
        });
        bth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBalance6();
            }
        });

        btd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBalance9();
            }
        });

      listt.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
       {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
           {

               Balance balance = balanceList.get(position);

               showUpdateDialog(balance.getBalid(),balance.getDesc());
               return false;
           }
       });
    }
    @Override
    protected void onStart() {
        super.onStart();

        DB1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                balanceList.clear();
                total=0.0;
                for(DataSnapshot balsnapshot: dataSnapshot.getChildren() )
                {
                    Balance balance =  balsnapshot.getValue(Balance.class);
                    Double exp= Double.parseDouble(balance.getBal());
                    total+=exp;
                    balanceList.add(balance);
                }
                //DecimalFormat dec = new DecimalFormat("#0.00");
                if(total<0) {
                    total=total*-1;
                    if(Math.ceil(total) == Math.floor(total))
                        totalText.setText("You Owe: " + total.intValue());
                    else
                        totalText.setText("You Owe: " + String.format("%.2f",total));
                    total=total*-1;

                }
                else{
                    if(Math.ceil(total) == Math.floor(total))
                        totalText.setText("You are Owed: " + total.intValue());
                    else
                        totalText.setText("You are Owed: " + String.format("%.2f",total));
                }

                balList adapter = new balList(AddBalance.this,balanceList);
                listt.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showUpdateDialog(final String id,final String desc)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialogBuilder.setView(dialogView);
        final EditText editText=(EditText) dialogView.findViewById(R.id.editText10);
        final Button bt1 = (Button) dialogView.findViewById(R.id.button7);
        final Button bt2 = (Button)  dialogView.findViewById(R.id.button8);
              //final AlertDialog ad = dialogBuilder.show();
       bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bal=editText.getText().toString().trim();
                if(TextUtils.isEmpty(bal)) {
                    editText.setError("Please Enter Amount");
                    return;
                }
                else {
                    updateBalance(id,desc,bal);
                    //ad.dismiss();
                }

            }
        });

       bt2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               deleteEntry(id);
              // ad.dismiss();
           }
       });
        dialogBuilder.setTitle("Updating Balance for "+ desc);


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


    }

    private  void deleteEntry(String id)
    {
        Intent intent = getIntent();
        String person= intent.getStringExtra(secondActivity.name);
        DatabaseReference DB3 = FirebaseDatabase.getInstance().getReference("Expenses").child(firebaseAuth.getUid()).child(person).child(id);

        DB3.removeValue();
        Toast.makeText(this,"Settled Up!!",Toast.LENGTH_LONG).show();
    }

    private boolean updateBalance(String id,String desc,String bal)
    {
        Intent intent = getIntent();
        String person= intent.getStringExtra(secondActivity.name);
        DatabaseReference DB2 = FirebaseDatabase.getInstance().getReference("Expenses").child(firebaseAuth.getUid()).child(person).child(id);

        Balance balance = new Balance(id,desc,bal);
        DB2.setValue(balance);
        Toast.makeText(this,"Balance Updated successfully",Toast.LENGTH_LONG).show();
        return true;
    }
    private void saveBalance6(){

        String balDesc = tv1.getText().toString().trim();
        String exp = tv2.getText().toString().trim();
        exp="-"+exp;
        if(!TextUtils.isEmpty(balDesc) && !exp.equals("-")){
            String id1 = DB1.push().getKey();
            Balance balance = new Balance(id1,balDesc,exp);
            DB1.child(id1).setValue(balance);
            Toast.makeText(this,"Balance saved successfully",Toast.LENGTH_SHORT).show();
            tv1.setText("");
            tv2.setText("");
        }
        else if(exp.equals("-")){
            Toast.makeText(this,"Balance should not be empty",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Description should not be empty",Toast.LENGTH_SHORT).show();
        }



    }

    private void saveBalance9(){

        String balDesc = tv1.getText().toString().trim();
        String exp = tv2.getText().toString().trim();
        if(!TextUtils.isEmpty(balDesc) && !TextUtils.isEmpty(exp)){
            String id1 = DB1.push().getKey();

            Balance balance = new Balance(id1,balDesc,exp);
            DB1.child(id1).setValue(balance);

            Toast.makeText(this,"Balance saved successfully",Toast.LENGTH_SHORT).show();
            tv1.setText("");
            tv2.setText("");
        }
        else if(exp.isEmpty()){
            Toast.makeText(this,"Balance should not be empty",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Description should not be empty",Toast.LENGTH_SHORT).show();
        }



    }
}
