package br.com.on.fiap.hackthonnotificador.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient.Builder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;

class AwsClientsConfigTest {

    @Test
    @DisplayName("given properties when sesClient then builder configured with region, creds, httpClient and endpoint")
    void givenProperties_whenSesClient_thenBuilderConfiguredProperly() {
        AwsClientsConfig cfg = new AwsClientsConfig();
        ReflectionTestUtils.setField(cfg, "accessKey", "test-ak");
        ReflectionTestUtils.setField(cfg, "secretKey", "test-sk");
        ReflectionTestUtils.setField(cfg, "region", "us-east-1");
        ReflectionTestUtils.setField(cfg, "sesEndpoint", "http://localhost:4566");

        SesClientBuilder sesBuilder = mock(SesClientBuilder.class, RETURNS_SELF);
        SesClient sesMock = mock(SesClient.class);

        Builder httpClientBuilder = mock(Builder.class, RETURNS_SELF);

        try (MockedStatic<SesClient> sesStatic = mockStatic(SesClient.class);
                MockedStatic<UrlConnectionHttpClient> httpStatic = mockStatic(UrlConnectionHttpClient.class);
                MockedStatic<StaticCredentialsProvider> credStatic = mockStatic(StaticCredentialsProvider.class);
                MockedStatic<AwsBasicCredentials> basicCredStatic = mockStatic(AwsBasicCredentials.class)) {

            sesStatic.when(SesClient::builder).thenReturn(sesBuilder);
            when(sesBuilder.build()).thenReturn(sesMock);

            httpStatic.when(UrlConnectionHttpClient::builder).thenReturn(httpClientBuilder);

            AwsBasicCredentials basicCreds = mock(AwsBasicCredentials.class);
            StaticCredentialsProvider staticProvider = mock(StaticCredentialsProvider.class);

            basicCredStatic
                    .when(() -> AwsBasicCredentials.create("test-ak", "test-sk"))
                    .thenReturn(basicCreds);
            credStatic.when(() -> StaticCredentialsProvider.create(basicCreds)).thenReturn(staticProvider);

            SesClient result = cfg.sesClient();

            assertThat(result).isSameAs(sesMock);

            ArgumentCaptor<Region> regionCaptor = ArgumentCaptor.forClass(Region.class);
            verify(sesBuilder).region(regionCaptor.capture());
            assertThat(regionCaptor.getValue().id()).isEqualTo("us-east-1");

            ArgumentCaptor<StaticCredentialsProvider> credsCaptor =
                    ArgumentCaptor.forClass(StaticCredentialsProvider.class);
            verify(sesBuilder).credentialsProvider(credsCaptor.capture());
            assertThat(credsCaptor.getValue()).isSameAs(staticProvider);

            verify(sesBuilder).httpClientBuilder(httpClientBuilder);

            ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
            verify(sesBuilder).endpointOverride(uriCaptor.capture());
            assertThat(uriCaptor.getValue().toString()).hasToString("http://localhost:4566");

            verify(sesBuilder).build();
            verifyNoMoreInteractions(httpClientBuilder);
        }
    }
}
