package com.jjaneto.chatmessenger;

import com.google.protobuf.ByteString;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;



/**
 * Created by jjaneto on 06/10/2017.
 */

public class classSend extends Thread {


    private Protocol.MessageProto.Mensagem mensagem;
    private Protocol.MessageProto.Mensagem.Conteudo conteudoMensagem;

    String typed;
    String receptor;
    mensagem msgRecebida;

    public classSend(String receptor, mensagem msgRecebida){
        this.receptor = receptor;
        this.msgRecebida = msgRecebida;
    }



    @Override
    public void run() {
        try {
            System.out.println("-------------------------ENVIANDO MENSAGEM");
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost("ec2-54-218-117-155.us-west-2.compute.amazonaws.com");
            factory.setUsername("usuario");
            factory.setPassword("senha");
//            factory.setHost("sidewinder.rmq.cloudamqp.com");
//            factory.setUsername("qgsqvfci");
//            factory.setVirtualHost("qgsqvfci");
//            factory.setPassword("ZomFOJkXWL-V6yeaPUEgmOCymstYwds2");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(receptor, false, false, false, null);
            channel.basicPublish("", receptor, null, msgRecebida.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
