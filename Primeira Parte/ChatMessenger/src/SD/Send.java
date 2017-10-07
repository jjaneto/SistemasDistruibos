package SD;

 
import com.google.protobuf.ByteString;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
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
//    private String nomeGrupo;
    private LocalDateTime ldt;
    
    private Protocol.MessageProto.Mensagem mensagem;
    private Protocol.MessageProto.Mensagem.Conteudo conteudoMensagem;

    public Send(String emissor, String receptor, String smensagem) {
        this.receptor = receptor;
        this.emissor = emissor;
        this.smensagem = smensagem;
        ldt = LocalDateTime.now();
    }

    public String getReceptor() {
        return receptor;
    }

    public String getEmissor() {
        return emissor;
    }

    public String getSmensagem() {
        return smensagem;
    }

//    public String getNomeGrupo() {
//        return nomeGrupo;
//    }

    public String getMensagem() {
        return smensagem;
    }
    
    public String dataEnvio() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return ldt.format(formatador);
    }

    public String horaEnvio() {
        return ldt.getHour() + ":" + ldt.getMinute();
    }

    public void makeMessageProtocol(){
        conteudoMensagem = Protocol.MessageProto.Mensagem.Conteudo.newBuilder()
                .setBody(ByteString.copyFrom(smensagem.getBytes()))
                .setName("none")
                .setType("none")
                .build();
        mensagem = Protocol.MessageProto.Mensagem.newBuilder()
                .setDate(dataEnvio())
                .setGroup("none")
                .setSender(getEmissor())
                .setTime(horaEnvio())
                .addContent(conteudoMensagem)   
                .build();
    }
    
    @Override
    public void run() {
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
            channel.queueDeclare(receptor, false, false, false, null);
            makeMessageProtocol();
            channel.basicPublish("", receptor, null, mensagem.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
