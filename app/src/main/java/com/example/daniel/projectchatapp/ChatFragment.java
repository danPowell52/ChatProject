package com.example.daniel.projectchatapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Daniel on 05/03/2017.
 */

public class ChatFragment extends Fragment implements OnClickListener {
    ListView messagelist ;
    ArrayList<Message> messages = new ArrayList<>();
    ChatAdapter adapter;
    EditText msg_edittext;
    Intent sentMessageClient;
    Intent sentMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



        View contentView = inflater.inflate(R.layout.chat_layout, null);

        ImageButton sendButton = (ImageButton) contentView.findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);
        ImageButton fileButton = (ImageButton) contentView.findViewById(R.id.sendFileButton);
        fileButton.setOnClickListener(this);
        msg_edittext = (EditText) contentView.findViewById(R.id.messageEditText);
        messagelist = (ListView) contentView.findViewById(R.id.msgListView);



        adapter = new ChatAdapter(getActivity(),R.layout.incoming_bubble, messages);
        messagelist.setAdapter(adapter);

        return contentView;
    }

    public void setOwner(){
        PeerActivity activity = (PeerActivity) getActivity();
        Boolean owner = activity.owner;
        if (owner.equals(true)){
            sentMessage = new Intent(getActivity(), ChatServer.class);
        } else if (owner.equals(false)){
            sentMessage = new Intent(getActivity(), ChatClient.class);
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println("clicked");
        switch (v.getId()) {

            case R.id.sendMessageButton:
                sendMessage(v);
                break;
            case R.id.sendFileButton:
                System.out.println("send file button clicked");
                sendFile(v);
                break;
        }
    }

    public void receiveMessage(String message){
        Message receivedMessage = new Message(message, false);
        messages.add(receivedMessage);
        adapter.notifyDataSetChanged();
    }

    public void sendFile(View view){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 20);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("IN THE RESULT");
        if (requestCode == 20 & data != null) {
            System.out.println("Requestcode is 20");
            Uri uri = data.getData();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            String mimeType = getActivity().getContentResolver().getType(uri);
            System.out.println("file type = " + type);
            System.out.println("mimetype = " + mimeType);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try {
                InputStream fileStream = getActivity().getApplicationContext().getContentResolver().openInputStream(Uri.parse(uri.toString()));
                int line;
                byte[] bytes = new byte[16384];
                while ((line = fileStream.read(bytes, 0 , bytes.length)) != -1) {
                    buffer.write(line);
                }
                //send intent to service
                setOwner();
                sentMessage.putExtra("message","hasFile");
                System.out.println("file uri 1 "+ uri.toString());
                sentMessage.putExtra("fileUri", uri.toString());
                sentMessage.putExtra("fileType", type);
                sentMessage.putExtra("hasFile", true);
                sendToThread(sentMessage);
                buffer.toByteArray();
                messages.add(new Message("file sent " + uri.toString(), true));
                adapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void sendMessage(View view){
        //cahnge for when parent is chat activity
        Message message = new Message();


        Log.d("Payara","edits "+msg_edittext.getEditableText().toString());
        message.setMessage(msg_edittext.getEditableText().toString());
        messages.add(message);
        adapter.notifyDataSetChanged();
        setOwner(); //resets the intent
        sentMessage.putExtra("message",msg_edittext.getEditableText().toString()); //needs /n for streamreader
        sentMessage.putExtra("hasFile", false);
        sendToThread(sentMessage);

        /**
        if(owner.equals(true)){
            Log.d("Payara","message server");
            activity.startService(sentMessage);
        } else if(owner.equals(false)) {
            Log.d("Payara","message client");
            activity.startService(sentMessageClient);
        }
         **/
    }

    public void sendToThread(Intent bundle){
        getActivity().startService(bundle);
    }

}
