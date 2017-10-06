package com.jjaneto.chatmessenger;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by jjaneto on 05/10/2017.
 */

public class mensagem {

    private String emissor;
    private String receptor;
    private String msgString;

    private Protocol.MessageProto.Mensagem mensagem;
    private Protocol.MessageProto.Mensagem.Conteudo conteudoMensagem;

    public mensagem(){

    }

    public void transformMessage(byte[] arrBytes) {
        try {
            mensagem = Protocol.MessageProto.Mensagem.newBuilder().mergeFrom(arrBytes).build();
            emissor = mensagem.getSender();
            msgString = new String(mensagem.getContent(0).getBody().toByteArray());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString(){
        return emissor + ": " + msgString;
    }
}
