package SD;

import Protocol.MessageProto;
import com.rabbitmq.client.ConnectionFactory;
import Protocol.MessageProto.Mensagem.Builder;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
    private LocalDateTime ldt;
    
    private Protocol.MessageProto.Mensagem mensagem;
    private Protocol.MessageProto.Mensagem.Conteudo conteudoMensagem;

    public Send(String emissor, String receptor, String smensagem, String nomeGrupo) {
        this.receptor = receptor;
        this.emissor = emissor;
        this.smensagem = smensagem;
        this.nomeGrupo = nomeGrupo;
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

    public String getNomeGrupo() {
        return nomeGrupo;
    }

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

//    public static byte[] serialize(Object obj) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ObjectOutputStream os = new ObjectOutputStream(out);
//        os.writeObject(obj);
//        return out.toByteArray();
//    }

    public void makeMessageProtocol(){
        conteudoMensagem = Protocol.MessageProto.Mensagem.Conteudo.newBuilder()
                .setBody(ByteString.copyFrom(smensagem.getBytes()))
                .setName("none")
                .setType("none")
                .build();
        mensagem = Protocol.MessageProto.Mensagem.newBuilder()
                .setDate(dataEnvio())
                .setGroup(getNomeGrupo())
                .setSender(getEmissor())
                .setTime(horaEnvio())
                .setContent(MAX_PRIORITY, conteudoMensagem)
                .build();
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
            makeMessageProtocol();
            channel.basicPublish("", receptor, null, );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
