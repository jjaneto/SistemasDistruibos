package SD;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jjaneto
 */
public class Group {

    private LocalDateTime ldt;

    public Group() {
    }

    public String dataEnvio() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return ldt.format(formatador);
    }

    public String horaEnvio() {
        return ldt.getHour() + ":" + ldt.getMinute();
    }

    public ConnectionFactory makeFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("sidewinder.rmq.cloudamqp.com");
        factory.setUsername("qgsqvfci");
        factory.setVirtualHost("qgsqvfci");
        factory.setPassword("ZomFOJkXWL-V6yeaPUEgmOCymstYwds2");
        return factory;
    }

    public boolean sendMessageToGroup(String user, String group, String message) {
        try {
            Connection connection = makeFactory().newConnection();
            Channel channel = connection.createChannel();

            channel.basicPublish(group, "", null, null);
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean addUserToGroup(String group, String user) {
        try {
            Connection connection = makeFactory().newConnection();
            Channel channel = connection.createChannel();

            //channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.queueBind(user, group, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteUser(String group, String user) {
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
        return true;
    }

    public boolean createGroup(String group) {
        try {
            Connection connection = makeFactory().newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(group, "fanout");
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteGroup(String group) {
        try {
            Connection connection = makeFactory().newConnection();
            Channel channel = connection.createChannel();
            
            
            channel.exchangeDelete(group);
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
