package com.alaminkarno.aichat_chatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alaminkarno.aichat_chatgpt.adapters.MessageAdapter;
import com.alaminkarno.aichat_chatgpt.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private EditText messageET;
    private ImageButton sendBTN;
    
    private MessageAdapter adapter;
    private List<Message> messageList;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initialize();

        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = messageET.getText().toString();

                Toast.makeText(MainActivity.this, "Clicked: "+message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        messageET = (EditText) findViewById(R.id.messageET);
        sendBTN = (ImageButton) findViewById(R.id.sendBTN);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}