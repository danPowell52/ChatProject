package com.example.daniel.projectchatapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;

/**
 * Created by Daniel on 08/04/2017.
 */

public class WifiChatFragment extends ChatFragment {

    @Override
    public void sendMessage(View view){
        Message message = new Message();
        ChatActivity activity = (ChatActivity) getActivity();


        Log.d("Payara","edits "+msg_edittext.getEditableText().toString());
        message.setMessage(msg_edittext.getEditableText().toString());
        messages.add(message);
        adapter.notifyDataSetChanged();
        Intent sentMessage = new Intent(activity, WifiChatClient.class);
        sentMessage.putExtra("message",msg_edittext.getEditableText().toString()); //needs /n for streamreader

        activity.startService(sentMessage);
    }
}
