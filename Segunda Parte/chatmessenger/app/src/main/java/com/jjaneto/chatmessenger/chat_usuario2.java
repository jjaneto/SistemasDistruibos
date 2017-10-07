package com.jjaneto.chatmessenger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.ArrayList;

/**
 * Created by jjaneto on 06/10/2017.
 */

public class chat_usuario2 extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<mensagem> mensagens;
    private ArrayAdapter<mensagem> adapter;
    private Button bt_send;
    private EditText et_text;
    public String receptor;
    public String usuario;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_grupo_usuario);
        //
        Bundle extras = getIntent().getExtras();

        receptor = extras.get("receptor").toString();
        usuario  = extras.get("usuario").toString();
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chattela_usuario);
        TextView tv = (TextView) toolbar.findViewById(R.id.nome_inside_toolbar_usuario);
        tv.setText(receptor);
        //
        bt_send = (Button) findViewById(R.id.btnSend_tela_usuario);
        bt_send.setOnClickListener(this);

        et_text = (EditText) findViewById(R.id.txt_usuario);

        //
        lv = (ListView) findViewById(R.id.list_chat_tela_usuario);
        mensagens = new ArrayList<mensagem>();
        adapter = new ArrayAdapter<mensagem>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                mensagens);

        lv.setAdapter(adapter);


        receiveMessage();
    }

    public void receiveMessage() {
        final classReceive whatsnew = new classReceive(usuario);
        whatsnew.start();
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    if(classReceive.sinal == true){
                        atualizaListView(whatsnew.getMensagem());
                        classReceive.sinal = false;
                    }
                }
            }
        }.start();
    }

    public void atualizaListView(final mensagem received) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(received);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == bt_send){
            String typed = et_text.getText().toString();
            System.out.println("DIGITEI " + typed);
            new classSend(receptor, new mensagem(usuario, receptor, typed)).start();
            et_text.setText("");
            adapter.add(new mensagem(usuario, receptor, typed));
        }
        if(v == et_text){

        }
    }
}
