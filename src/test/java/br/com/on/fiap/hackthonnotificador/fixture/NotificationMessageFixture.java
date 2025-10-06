package br.com.on.fiap.hackthonnotificador.fixture;

import br.com.on.fiap.hackthonnotificador.dto.NotificationMessage;

public final class NotificationMessageFixture {

    private NotificationMessageFixture() {}

    public static NotificationMessage sample() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder().subject("Assunto Teste").body("Corpo Teste").toEmail("to@example.com");
    }

    public static final class Builder {
        private String subject;
        private String body;
        private String toEmail;

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder toEmail(String toEmail) {
            this.toEmail = toEmail;
            return this;
        }

        public NotificationMessage build() {
            return new NotificationMessage(subject, body, toEmail);
        }
    }
}
