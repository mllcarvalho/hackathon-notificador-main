package br.com.on.fiap.hackthonnotificador.sqs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.com.on.fiap.hackthonnotificador.dto.NotificacaoVideo;
import br.com.on.fiap.hackthonnotificador.dto.NotificationMessage;
import br.com.on.fiap.hackthonnotificador.fixture.NotificacaoVideoFixture;
import br.com.on.fiap.hackthonnotificador.service.EmailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationQueueListenerTest {

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private NotificationQueueListener listener;

    @Test
    @DisplayName("Deve chamar o EmailSender com os dados corretos quando mensagem for recebida")
    void givenValidNotificacaoVideo_whenOnMessage_thenCallEmailSenderWithNotificationMessage() {
        NotificacaoVideo incoming = NotificacaoVideoFixture.builder()
                .nomeVideo("video_teste.mp4")
                .descricao("Descrição de teste")
                .email("destino@example.com")
                .build();

        ArgumentCaptor<NotificationMessage> msgCaptor = ArgumentCaptor.forClass(NotificationMessage.class);

        listener.onMessage(incoming);

        verify(emailSender, times(1)).send(msgCaptor.capture());
        NotificationMessage captured = msgCaptor.getValue();

        assertThat(captured.toEmail()).isEqualTo("destino@example.com");
        assertThat(captured.body()).isEqualTo("Descrição de teste");
        assertThat(captured.subject()).isEqualTo("Processamento video_teste.mp4 concluido.");
    }

    @Test
    @DisplayName("Deve propagar exceção caso o EmailSender lance erro")
    void givenEmailSenderThrowsException_whenOnMessage_thenPropagateException() {
        NotificacaoVideo incoming = NotificacaoVideoFixture.builder()
                .nomeVideo("falha.mp4")
                .descricao("Vai falhar")
                .email("fail@example.com")
                .build();

        doThrow(new RuntimeException("SES error")).when(emailSender).send(any(NotificationMessage.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> listener.onMessage(incoming));

        assertThat(ex.getMessage()).isEqualTo("SES error");
    }
}
