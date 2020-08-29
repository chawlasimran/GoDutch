package com.example.khushbhu.go_dutch;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.TextView;

import java.util.List;

public class group_list extends ArrayAdapter<str> {

    private Activity context;
    private List<str> strList;

    public group_list(Activity context, List<str> strList)
    {
        super(context,R.layout.list_layout,strList);
        this.context=context;
        this.strList=strList;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
       LayoutInflater inflater = context.getLayoutInflater();
       View listViewItem = inflater.inflate(R.layout.list_layout,null,true);
        TextView textView = (TextView) listViewItem.findViewById(R.id.nav_email);
        TextView textView1= (TextView) listViewItem.findViewById(R.id.textView9);
       // TextView textView2 = (TextView) listViewItem.findViewById(R.id.balance);

        str STR = strList.get(position);
        textView.setText(STR.getPerson());
        textView1.setText(STR.getPhone());
        //textView2.setText(STR.getPerson());
        return listViewItem;


    }
}
