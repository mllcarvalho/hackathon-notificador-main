package br.com.on.fiap.hackthonnotificador.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.com.on.fiap.hackthonnotificador.dto.NotificationMessage;
import br.com.on.fiap.hackthonnotificador.fixture.NotificationMessageFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {

    @Mock
    private SesClient sesClient;

    private EmailSender emailSender;

    private final String fromAddress = "no-reply@example.com";

    @BeforeEach
    void setUp() {
        emailSender = new EmailSender(sesClient, fromAddress);
    }

    @Test
    @DisplayName("Deve montar o SendEmailRequest corretamente e chamar o SesClient")
    void givenValidNotificationMessage_whenSend_thenBuildAndSendEmailRequest() {
        NotificationMessage msg = NotificationMessageFixture.builder()
                .subject("Assunto Teste")
                .body("Corpo Teste")
                .toEmail("to@example.com")
                .build();

        when(sesClient.sendEmail(any(SendEmailRequest.class)))
                .thenReturn(SendEmailResponse.builder().messageId("mid-123").build());

        ArgumentCaptor<SendEmailRequest> reqCaptor = ArgumentCaptor.forClass(SendEmailRequest.class);

        emailSender.send(msg);

        verify(sesClient, times(1)).sendEmail(reqCaptor.capture());
        verifyNoMoreInteractions(sesClient);

        SendEmailRequest captured = reqCaptor.getValue();

        assertThat(captured.source()).isEqualTo(fromAddress);

        Destination destination = captured.destination();
        assertThat(destination.toAddresses()).containsExactly("to@example.com");

        Message message = captured.message();
        assertThat(message.subject().data()).isEqualTo("Assunto Teste");
        assertThat(message.body().text()).isNotNull();
        assertThat(message.body().text().data()).isEqualTo("Corpo Teste");
    }

    @Test
    @DisplayName("Deve propagar exceção do SesClient ao enviar e-mail")
    void givenSesClientThrows_whenSend_thenPropagateException() {
        NotificationMessage msg = NotificationMessageFixture.builder()
                .subject("Falha")
                .body("Teste de erro")
                .toEmail("fail@example.com")
                .build();

        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenThrow(new RuntimeException("SES error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> emailSender.send(msg));
        assertThat(ex.getMessage()).isEqualTo("SES error");

        verify(sesClient, times(1)).sendEmail(any(SendEmailRequest.class));
        verifyNoMoreInteractions(sesClient);
    }
}
