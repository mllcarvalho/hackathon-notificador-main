package br.com.on.fiap.hackthonnotificador.service;

import br.com.on.fiap.hackthonnotificador.dto.NotificationMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailSender {

    private final SesClient ses;
    private final String from;

    public EmailSender(SesClient ses, @Value("${notifier.ses.from}") String from) {
        this.ses = ses;
        this.from = from;
    }

    public void send(NotificationMessage msg) {
        Destination destination =
                Destination.builder().toAddresses(msg.toEmail()).build();

        Content subject = Content.builder().data(msg.subject()).build();
        Content textBody = Content.builder().data(msg.body()).build();
        Body body = Body.builder().text(textBody).build();

        Message message = Message.builder().subject(subject).body(body).build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(from)
                .destination(destination)
                .message(message)
                .build();

        ses.sendEmail(request);
    }
}
