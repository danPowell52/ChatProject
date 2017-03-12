package com.example.daniel.projectchatapp;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Daniel on 05/03/2017.
 */

public class ChatAdapter extends ArrayAdapter {

    private ArrayList<Message> messages;

    public ChatAdapter(Context context, int resource, ArrayList<Message> messages2) {
        super(context, resource, messages2);
        messages = messages2;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Log.d("Payara","getView called");
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = convertView;
        //format for row of list
        if (messages.get(position).getIsMine()){
            newView = inflater.inflate(R.layout.incoming_bubble, null);
            LinearLayout parent_layout = (LinearLayout) newView
                    .findViewById(R.id.bubble_layout_parent);
            parent_layout.setGravity(Gravity.RIGHT);
        } else {
            newView = inflater.inflate(R.layout.outgoing_bubble, null);
        }

        TextView text = (TextView) newView.findViewById(R.id.message_text);
        text.setText(messages.get(position).getMessage());
        return newView;
    }

}
