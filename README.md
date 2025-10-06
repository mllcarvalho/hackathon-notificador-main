# hackathon-notificador

Sistema de notificação para Hackathon FIAP, responsável por receber mensagens de uma fila SQS, processar notificações e enviar e-mails.

## Sumário

- [Descrição](#descrição)
- [Funcionalidades](#funcionalidades)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração](#configuração)
- [Execução](#execução)
- [Testes](#testes)
- [Docker](#docker)
- [Contribuição](#contribuição)
- [Licença](#licença)

---

## Descrição

O `hackathon-notificador` é um microserviço Java Spring Boot que integra com AWS SQS para receber notificações e envia e-mails a partir dessas mensagens. O projeto foi desenvolvido para o Hackathon FIAP com foco em escalabilidade e integração com serviços AWS.

## Funcionalidades

- Consome mensagens de uma fila AWS SQS.
- Processa notificações de vídeo e mensagens genéricas.
- Envia e-mails a partir das notificações recebidas.
- Configuração flexível via `application.yaml`.

## Estrutura do Projeto

```
hackathon-notificador/
├── .github/workflows/ci-main.yaml   # Pipeline CI
├── Dockerfile                       # Containerização
├── pom.xml                          # Dependências Maven
├── src/
│   ├── main/
│   │   ├── java/br/com/on/fiap/hackthonnotificador/
│   │   │   ├── HackthonNotificadorApplication.java
│   │   │   ├── config/AwsClientsConfig.java
│   │   │   ├── dto/NotificacaoVideo.java, NotificationMessage.java
│   │   │   ├── service/EmailSender.java
│   │   │   └── sqs/NotificationQueueListener.java
│   │   └── resources/application.yaml
│   └── test/
│       └── java/br/com/on/fiap/hackthonnotificador/
│           ├── config/AwsClientsConfigTest.java
│           ├── fixture/NotificacaoVideoFixture.java, NotificationMessageFixture.java
│           ├── service/EmailSenderTest.java
│           └── sqs/NotificationQueueListenerTest.java
```

## Configuração

1. **Pré-requisitos**
   - Java 17+
   - Maven 3.8+
   - AWS CLI configurado (opcional para testes locais)
   - Docker (opcional)

2. **Variáveis de Ambiente / application.yaml**
   - Configure as credenciais AWS e parâmetros da fila SQS no arquivo `src/main/resources/application.yaml`.

## Execução

Para rodar localmente:

```bash
mvn spring-boot:run
```

Ou, para buildar o jar:

```bash
mvn clean package
java -jar target/hackathon-notificador-*.jar
```

## Testes

Execute os testes unitários com:

```bash
mvn test
```

## Docker

Para construir e rodar o container:

```bash
docker build -t hackathon-notificador .
docker run -e AWS_ACCESS_KEY_ID=xxx -e AWS_SECRET_ACCESS_KEY=xxx hackathon-notificador
```

## Contribuição

1. Fork este repositório
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas alterações (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT.