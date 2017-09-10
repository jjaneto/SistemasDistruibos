package SD;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 *
 * @author jjaneto
 */
public class Receive extends Thread {
    
    private String receptor;
    
    public Receive(String receptor) {
        this.receptor = receptor;
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
