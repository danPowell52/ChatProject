package com.example.daniel.projectchatapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Daniel on 05/03/2017.
 */

public class ChatFragment extends Fragment implements OnClickListener {
    ListView messagelist ;
    ArrayList<Message> messages = new ArrayList<>();
    ChatAdapter adapter;
    EditText msg_edittext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



        View contentView = inflater.inflate(R.layout.chat_layout, null);

        ImageButton sendButton = (ImageButton) contentView.findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);
        msg_edittext = (EditText) contentView.findViewById(R.id.messageEditText);
        messagelist = (ListView) contentView.findViewById(R.id.msgListView);

        messages.add(new Message("boom baby", true));
        messages.add(new Message("sean", false));
        messages.add(new Message("ceri", true));

        adapter = new ChatAdapter(getActivity(),R.layout.incoming_bubble, messages);
        messagelist.setAdapter(adapter);

        return contentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                sendMessage(v);

        }
    }

    public void receiveMessage(String message){
        Message receivedMessage = new Message(message, false);
        messages.add(receivedMessage);
        adapter.notifyDataSetChanged();
    }



    public void sendMessage(View view){
        Message message = new Message();
        PeerActivity activity = (PeerActivity) getActivity();
        Boolean owner = activity.owner;

        Log.d("Payara","edits "+msg_edittext.getEditableText().toString());
        message.setMessage(msg_edittext.getEditableText().toString());
        messages.add(message);
        adapter.notifyDataSetChanged();
        Intent sentMessage = new Intent(activity, ChatServer.class);
        sentMessage.putExtra("message",msg_edittext.getEditableText().toString()); //needs /n for streamreader
        Intent sentMessageClient = new Intent(activity, ChatClient.class);
        sentMessageClient.putExtra("message",msg_edittext.getEditableText().toString());

        if(owner.equals(true)){
            Log.d("Payara","message server");
            activity.startService(sentMessage);
        } else if(owner.equals(false)) {
            Log.d("Payara","message client");
            activity.startService(sentMessageClient);
        }
    }



}
