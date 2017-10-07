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

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.ArrayList;

/**
 * Created by jjaneto on 06/10/2017.
 */

public class chat_usuario extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<mensagem> mensagens;
    private ArrayAdapter<mensagem> adapter;
    private Button bt_send;
    private EditText et_text;
    private String receptor;
    private String usuario;
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


//    public void sendMessage(final mensagem msg){
//        new Thread(){
//            @Override
//            public void run(){
//                try {
//                    ConnectionFactory factory = new ConnectionFactory();
//                    factory.setHost("sidewinder.rmq.cloudamqp.com");
//                    factory.setUsername("qgsqvfci");
//                    factory.setVirtualHost("qgsqvfci");
//                    factory.setPassword("ZomFOJkXWL-V6yeaPUEgmOCymstYwds2");
//
//                    Connection connection = factory.newConnection();
//                    Channel channel = connection.createChannel();
//                    channel.queueDeclare(receptor, false, false, false, null);
//                    System.err.println("Mandando mensagem para " + receptor + ": " + msg);
//
////                    makeMessageProtocol();
//                    channel.basicPublish("", receptor, null, msg.toByteArray());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//    }

    public void sendMessage(final String msg){
        new Thread(){
            @Override
            public void run(){
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("sidewinder.rmq.cloudamqp.com");
                    factory.setUsername("qgsqvfci");
                    factory.setVirtualHost("qgsqvfci");
                    factory.setPassword("ZomFOJkXWL-V6yeaPUEgmOCymstYwds2");

                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();
                    channel.queueDeclare(receptor, false, false, false, null);
                    System.err.println("Mandando mensagem para " + receptor + ": " + msg);

//                    makeMessageProtocol();
                    channel.basicPublish("", receptor, null, msg.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private Protocol.MessageProto.Mensagem.Conteudo conteudoMensagem;
    private Protocol.MessageProto.Mensagem mensagem;

    public void transformMessage(byte[] arrBytes) {
        try {
            mensagem = Protocol.MessageProto.Mensagem.newBuilder().mergeFrom(arrBytes).build();
            //mensagem.parseFrom(arrBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }



    public void receiveMessage() {
        new Thread() {
            @Override
            public void run() {
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("sidewinder.rmq.cloudamqp.com");
                    factory.setUsername("qgsqvfci");
                    factory.setVirtualHost("qgsqvfci");
                    factory.setPassword("ZomFOJkXWL-V6yeaPUEgmOCymstYwds2");
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();
                    channel.queueDeclare(receptor, false, false, false, null);

                    QueueingConsumer consumer = new QueueingConsumer(channel);
                    channel.basicConsume(receptor, true, consumer);
                    while (true) {
                        System.out.println("true");
                        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//                        transformMessage(delivery.getBody());
//                        System.err.println("Recebi mensagem de:" + mensagem.getSender());
//                        System.err.println("----- contem: " + new String(mensagem.getContent(0).getBody().toByteArray()));
//                        mensagem received = new mensagem(delivery.getBody());
//                        System.err.println("Chegou mensagem de: " + received.getSender());
//                        System.err.println("Mensagem eh: " + received);
//                        if (received.getGroup().equals("none") && received.getSender().equals(usuario)) {
//                            System.err.println("Stop!");
//                            continue;
//                        }
//
//                        System.out.println("Recebi " + received);
//                        atualizaListView(received);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
            mensagem novamensagem = new mensagem(usuario, receptor, typed);
//            new classSend();
//            sendMessage("porra");
            et_text.setText("");
            adapter.add(novamensagem);
        }
        if(v == et_text){

        }
    }
}
