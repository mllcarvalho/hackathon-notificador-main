package br.com.on.fiap.hackthonnotificador.dto;

import jakarta.validation.constraints.NotBlank;

public record NotificationMessage(@NotBlank String subject, @NotBlank String body, String toEmail) {}
