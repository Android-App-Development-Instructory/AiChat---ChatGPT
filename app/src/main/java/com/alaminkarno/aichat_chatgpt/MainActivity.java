package com.alaminkarno.aichat_chatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alaminkarno.aichat_chatgpt.adapters.MessageAdapter;
import com.alaminkarno.aichat_chatgpt.model.Message;
import com.alaminkarno.aichat_chatgpt.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private EditText messageET;
    private ImageButton sendBTN;
    
    private MessageAdapter adapter;
    private List<Message> messageList;

    String message;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initialize();

        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = messageET.getText().toString();


                if(!message.isEmpty()){
                    messageET.setText("");
                    addToChatList(message, AppConstants.SEND_BY_ME);
                    callChatGPT(message);
                }
            }
        });
    }

    private void addToChatList(String message, String sender) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sender));
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });

    }

    private void callChatGPT(String message){

        messageList.add(new Message("Typing...",AppConstants.SEND_BY_BOT));

        JSONObject jsonBody = new JSONObject();

        try{
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",message);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0);
        }
        catch (JSONException e){
            Log.e("Error",e.getMessage());
        }


        RequestBody body = RequestBody.create(String.valueOf(jsonBody), JSON);

        Request request = new Request.Builder()
                .url(AppConstants.BASE_URL)
                .header("Authorization","Bearer "+AppConstants.APP_API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                messageList.remove(messageList.size() -1);
                addToChatList("Failed to get reply because of"+e.getMessage()+". Try Again!!",AppConstants.SEND_BY_BOT);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String botMessage = jsonArray.getJSONObject(0).getString("text");
                        messageList.remove(messageList.size() -1);
                        addToChatList(botMessage.trim(),AppConstants.SEND_BY_BOT);
                    }
                    catch (JSONException e){
                        Log.e("Error",e.getMessage());
                    }
                }else{
                    messageList.remove(messageList.size() -1);
                    addToChatList("Failed to get reply because of"+response.body().toString()+". Try Again!!",AppConstants.SEND_BY_BOT);
                }
            }
        });

    }

    private void initialize() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        messageET = (EditText) findViewById(R.id.messageET);
        sendBTN = (ImageButton) findViewById(R.id.sendBTN);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}