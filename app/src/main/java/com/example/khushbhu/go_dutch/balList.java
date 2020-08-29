package com.example.khushbhu.go_dutch;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class balList extends ArrayAdapter<Balance> {

    private Activity context;
    private List<Balance> baLList;

    public balList(Activity context, List<Balance> baLList)
    {
        super(context,R.layout.bal_layout,baLList);
        this.context=context;
        this.baLList=baLList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.bal_layout,null,true);
        TextView textView = (TextView) listViewItem.findViewById(R.id.textViewN);
        TextView textView1= (TextView) listViewItem.findViewById(R.id.textViewM);
        // TextView textView2 = (TextView) listViewItem.findViewById(R.id.balance);

        Balance balance = baLList.get(position);
        textView.setText(balance.getDesc());
        textView1.setText(balance.getBal());
        //textView2.setText(STR.getPerson());
        return listViewItem;


    }
}
