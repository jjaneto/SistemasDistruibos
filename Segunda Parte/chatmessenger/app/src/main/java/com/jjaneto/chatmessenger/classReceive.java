package com.jjaneto.chatmessenger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Created by jjaneto on 06/10/2017.
 */

public class classReceive extends Thread{

    private mensagem msgrecebida;
    public static boolean sinal;

    private String usuario;

    public classReceive(String usuario){
        this.usuario = usuario;
    }

    @Override
    public void run(){
        try {
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
            System.err.println("(classReceive) usuario eh " + usuario);
            channel.queueDeclare(usuario, false, false, false, null);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(usuario, true, consumer);
            while (true) {
                System.out.println("true");
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                System.err.println("-------- RECEBI NNN " + new String(delivery.getBody()));
                msgrecebida = new mensagem(delivery.getBody());
                System.out.println("Protocol: " + msgrecebida.getSender() + " | " + msgrecebida.getMessage() + " |");
                sinal = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public mensagem getMensagem(){
        return this.msgrecebida;
    }

}
