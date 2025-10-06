package br.com.on.fiap.hackthonnotificador.dto;

public record NotificacaoVideo(
        String idUsuario, String nomeVideo, String caminhoSaida, String situacao, String descricao, String email) {}
