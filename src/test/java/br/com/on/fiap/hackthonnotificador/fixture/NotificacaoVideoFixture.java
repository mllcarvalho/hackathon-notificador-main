package br.com.on.fiap.hackthonnotificador.fixture;

import br.com.on.fiap.hackthonnotificador.dto.NotificacaoVideo;
import java.util.UUID;

public final class NotificacaoVideoFixture {

    private NotificacaoVideoFixture() {}

    public static NotificacaoVideo sample() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder()
                .idUsuario("user-" + UUID.randomUUID())
                .nomeVideo("video_teste.mp4")
                .caminhoSaida("/tmp/out")
                .situacao("CONCLUIDO")
                .descricao("Processamento realizado com sucesso")
                .email("destino@example.com");
    }

    public static final class Builder {
        private String idUsuario;
        private String nomeVideo;
        private String caminhoSaida;
        private String situacao;
        private String descricao;
        private String email;

        public Builder idUsuario(String idUsuario) {
            this.idUsuario = idUsuario;
            return this;
        }

        public Builder nomeVideo(String nomeVideo) {
            this.nomeVideo = nomeVideo;
            return this;
        }

        public Builder caminhoSaida(String caminhoSaida) {
            this.caminhoSaida = caminhoSaida;
            return this;
        }

        public Builder situacao(String situacao) {
            this.situacao = situacao;
            return this;
        }

        public Builder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public NotificacaoVideo build() {
            return new NotificacaoVideo(idUsuario, nomeVideo, caminhoSaida, situacao, descricao, email);
        }
    }
}
