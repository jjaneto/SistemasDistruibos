package SD;

import ChatMessenger.MessageProto;
import com.rabbitmq.client.ConnectionFactory;
import ChatMessenger.MessageProto.Mensagem.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jjaneto
 */
public class Send extends Thread {

    private String receptor;
    private String emissor;
    private String smensagem;
    private String nomeGrupo;
    
    private ChatMessenger.MessageProto.Mensagem mensagem;

    public Send(String emissor, String receptor, String smensagem, String nomeGrupo) {
        this.receptor = receptor;
        this.emissor = emissor;
        this.smensagem = smensagem;
        this.nomeGrupo = nomeGrupo;
    }

    public String dataEnvio() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.now().format(formatador);
    }

    public String horaEnvio() {
        LocalDateTime ldt = LocalDateTime.now();
        return ldt.getHour() + ":" + ldt.getMinute();
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public void makeMessageProtocol(){
        
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
            makeMessagemProtocol();
            channel.basicPublish("", receptor, null, serialize(mensagem));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
