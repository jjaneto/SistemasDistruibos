package SD;

import ChatMessenger.MessageProto;
import com.rabbitmq.client.ConnectionFactory;
import ChatMessenger.MessageProto.Mensagem.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jjaneto
 */
public class Send extends Thread {

    private String receptor;

    public Send(String receptor) {
        this.receptor = receptor;
    }
    
    public String dataEnvio(){
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.now().format(formatador);
    }
    
    public String horaEnvio(){
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.getHour() + ":" + ldt.getMinute();
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

            channel.basicPublish("", receptor, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
