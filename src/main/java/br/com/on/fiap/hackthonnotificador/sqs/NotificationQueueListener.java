package br.com.on.fiap.hackthonnotificador.sqs;

import br.com.on.fiap.hackthonnotificador.dto.NotificacaoVideo;
import br.com.on.fiap.hackthonnotificador.dto.NotificationMessage;
import br.com.on.fiap.hackthonnotificador.service.EmailSender;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationQueueListener {

    private final EmailSender emailSender;

    public NotificationQueueListener(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @SqsListener("${notifier.sqs-queue-name}")
    public void onMessage(@Payload NotificacaoVideo message) {
        log.info("Received message: {}", message);
        String subject = String.format("Processamento %s concluido.", message.nomeVideo());
        emailSender.send(new NotificationMessage(subject, message.descricao(), message.email()));
        log.info("Email successfully: {}", message);
    }
}
