package SD;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author jjaneto
 */
public class Receive extends Thread {

    private String receptor;
    private MainClass mcs;
    private Protocol.MessageProto.Mensagem mensagem;

    public Receive(String receptor, MainClass mcs) {
        this.receptor = receptor;
        this.mcs = mcs;
    }

//    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
//        ByteArrayInputStream in = new ByteArrayInputStream(data);
//        ObjectInputStream is = new ObjectInputStream(in);
//        return is.readObject();
//    }
    public void transformMessage(byte[] arrBytes) {
        try {
            mensagem = Protocol.MessageProto.Mensagem.newBuilder().mergeFrom(arrBytes).build();
            //mensagem.parseFrom(arrBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

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
            //  System.out.println(" [*] Esperando por mensagem. Para sair aperte CTRL+C");

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(receptor, true, consumer);
//            System.out.println("Iniciei processo de receber mensagens do usuario " + receptor);
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                transformMessage(delivery.getBody());
//                System.out.println("Recebi mensagem");


                if(mensagem.getGroup().equals("none")){
                    System.out.println("(" + mensagem.getDate() + " às " + mensagem.getTime() + ") "
                            + mensagem.getSender() + " diz: "
                            + new String(mensagem.getContent(0).getBody().toByteArray()));
                }else{
                    System.out.println("(" + mensagem.getDate() + " às " + mensagem.getTime() + ") "
                            + mensagem.getSender() + "/" + mensagem.getGroup() + " diz: "
                            + new String(mensagem.getContent(0).getBody().toByteArray()));
                }
                if (mcs.getReceptor().isEmpty()) {
                    System.out.print(">> ");
                } else {
                    if (mcs.ehGrupo()) {
                        System.out.print(mcs.getReceptor() + "* >> ");
                    } else {
                        System.out.print(mcs.getReceptor() + " >> ");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
