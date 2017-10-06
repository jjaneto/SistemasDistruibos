package com.jjaneto.chatmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jjaneto on 05/10/2017.
 */

public class chat extends AppCompatActivity {

    ListView lv;
    List<String> listas;
    List<mensagem> listaDeMensagem;
    Button btSend;
    EditText editMessage;

    private String receptor;

    private void associa(){
        lv = (ListView) findViewById(R.id.list_chat_tela);
        btSend = (Button) findViewById(R.id.btnSend);
        editMessage = (EditText) findViewById(R.id.txt);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listas);

        lv.setAdapter(adapter);

        btSend.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                listas.add(editMessage.getText().toString());
                adapter.notifyDataSetChanged();
                editMessage.setText("");
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_tela);




    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(chat.this, listuser.class));
//        moveTaskToBack(true);  // "Hide" your current Activity
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void sendMessage(){
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("sidewinder.rmq.cloudamqp.com");
            factory.setUsername("qgsqvfci");
            factory.setVirtualHost("qgsqvfci");
            factory.setPassword("ZomFOJkXWL-V6yeaPUEgmOCymstYwds2");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(receptor, false, false, false, null);
            makeMessageProtocol();
            channel.basicPublish("", receptor, null, mensagem.toByteArray());
        }catch (Exception e){

        }
    }

    public void receiveMessage(){



    }


}
