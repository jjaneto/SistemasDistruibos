package com.jjaneto.chatmessenger;

import com.google.protobuf.ByteString;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import Protocol.MessageProto;

/**
 * Created by jjaneto on 06/10/2017.
 */

public class classGrupo {

    Protocol.MessageProto.Mensagem msgAEnviar;

    public ConnectionFactory makeFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("ec2-54-218-117-155.us-west-2.compute.amazonaws.com");
        factory.setUsername("usuario");
        factory.setPassword("senha");
//        factory.setHost("sidewinder.rmq.cloudamqp.com");
//        factory.setUsername("qgsqvfci");
//        factory.setVirtualHost("qgsqvfci");
//        factory.setPassword("ZomFOJkXWL-V6yeaPUEgmOCymstYwds2");
        return factory;
    }

    public void makeMessageProtocol(String grupo, String emissor, String smensagem){
        MessageProto.Mensagem.Conteudo conteudoMensagem = MessageProto.Mensagem.Conteudo.newBuilder()
                .setBody(ByteString.copyFrom(smensagem.getBytes()))
                .setName("none")
                .setType("none")
                .build();
        msgAEnviar = Protocol.MessageProto.Mensagem.newBuilder()
                .setDate("nada")
                .setGroup(grupo)
                .setSender(emissor)
                .setTime(getHora())
                .addContent(conteudoMensagem)
                .build();
    }

    public String getHora(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-3:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));

        String localTime = date.format(currentLocalTime);
        return localTime;
    }

    public boolean sendMessageToGroup(final String user, final String group, final String message) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Connection connection = makeFactory().newConnection();
                    Channel channel = connection.createChannel();
                    makeMessageProtocol(group, user, message);
                    channel.basicPublish(group, "", null, msgAEnviar.toByteArray());
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }

    public boolean addUserToGroup(final String user, final String group) {
        System.err.println("(classGroup) adicionando " + user + " ao grupo " + group);
        new Thread() {
            @Override
            public void run() {
                try {
                    Connection connection = makeFactory().newConnection();
                    Channel channel = connection.createChannel();

                    channel.queueBind(user, group, "");
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }

    public boolean deleteUser(final String user, final String group) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Connection connection = makeFactory().newConnection();
                    Channel channel = connection.createChannel();
                    //channel.exchangeUnbind(grupo, user, "");
                    channel.queueUnbind(user, group, "");
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }

    public boolean createGroup(final String user, final String group) {
        System.err.println("(classGrupo) Criando o grupo " + group + " com o user " + user);
        new Thread(){
            @Override
            public void run(){
                try {
                    Connection connection = makeFactory().newConnection();
                    Channel channel = connection.createChannel();

                    channel.exchangeDeclare(group, "fanout");
                    channel.queueBind(user, group, "");
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return true;
    }

    public boolean deleteGroup(final String group) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Connection connection = makeFactory().newConnection();
                    Channel channel = connection.createChannel();


                    channel.exchangeDelete(group);
                    channel.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }
}
