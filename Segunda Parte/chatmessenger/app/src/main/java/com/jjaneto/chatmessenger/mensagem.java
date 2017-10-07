package com.jjaneto.chatmessenger;

import android.support.annotation.Nullable;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jjaneto on 05/10/2017.
 */

public class mensagem {

    private String emissor;
    private String receptor;
    private String msgString;
    private String dataEnvio;
    private String horaEnvio;

    private Protocol.MessageProto.Mensagem mensagem;
    private Protocol.MessageProto.Mensagem.Conteudo conteudoMensagem;

    @Override
    public String toString(){
        return mensagem.getSender() + " às " + mensagem.getTime() +
        ": " + new String(mensagem.getContent(0).getBody().toByteArray());
    }

    public mensagem(Protocol.MessageProto.Mensagem mensagem){
        this.mensagem = mensagem;
    }

    public mensagem(byte[] byteArray){
        transformMessage(byteArray);
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

    public mensagem(String emissor, String receptor, String smensagem){
        this.emissor = emissor;
        this.receptor = receptor;
        this.msgString = smensagem;
        dataEnvio = "nada";
        horaEnvio = getHora();
        makeMessageProtocol();
    }

    public void makeMessageProtocol(String grupo){
        conteudoMensagem = Protocol.MessageProto.Mensagem.Conteudo.newBuilder()
                .setBody(ByteString.copyFrom(msgString.getBytes()))
                .setName("none")
                .setType("none")
                .build();
        mensagem = Protocol.MessageProto.Mensagem.newBuilder()
                .setDate(dataEnvio)
                .setGroup(grupo)
                .setSender(emissor)
                .setTime(horaEnvio)
                .addContent(conteudoMensagem)
                .build();
    }

    public void makeMessageProtocol(){
        conteudoMensagem = Protocol.MessageProto.Mensagem.Conteudo.newBuilder()
                .setBody(ByteString.copyFrom(msgString.getBytes()))
                .setName("none")
                .setType("none")
                .build();
        mensagem = Protocol.MessageProto.Mensagem.newBuilder()
                .setDate(dataEnvio)
                .setGroup("none")
                .setSender(emissor)
                .setTime(horaEnvio)
                .addContent(conteudoMensagem)
                .build();
    }

    public void transformMessage(byte[] arrBytes) {
        try {
            mensagem = Protocol.MessageProto.Mensagem.newBuilder().mergeFrom(arrBytes).build();
            //mensagem.parseFrom(arrBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public byte[] toByteArray(){
        return this.mensagem.toByteArray();
    }

    public String getGroup(){
        return mensagem.getGroup();
    }

    public String getSender(){
        return mensagem.getSender();
    }

    public String getMessage(){
        return new String(mensagem.getContent(0).getBody().toByteArray());
    }

}
