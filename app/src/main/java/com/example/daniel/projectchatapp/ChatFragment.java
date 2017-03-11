package com.example.daniel.projectchatapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Daniel on 05/03/2017.
 */

public class ChatFragment extends Fragment implements OnClickListener {
    ListView messagelist ;
    ArrayList<String> messages = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View contentView = inflater.inflate(R.layout.chat_layout, null);
        messagelist = (ListView) contentView.findViewById(R.id.msgListView);

        messages.add("boom baby");
        messages.add("sean");
        messages.add("ceri");

        ChatAdapter adapter = new ChatAdapter(getActivity(),R.layout.chat_bubble , messages);
        messagelist.setAdapter(adapter);

        return contentView;
    }

    @Override
    public void onClick(View v) {

    }
}
