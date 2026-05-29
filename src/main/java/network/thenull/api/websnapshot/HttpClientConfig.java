package network.thenull.api.websnapshot;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {
	
	@Bean
	RestClient restClient() {
		HttpComponentsClientHttpRequestFactory factory =
			new HttpComponentsClientHttpRequestFactory()
		;
		factory.setConnectionRequestTimeout(Duration.ofSeconds(5));
		factory.setReadTimeout(Duration.ofSeconds(10));
		
		return RestClient
			.builder()
			.requestFactory(factory)
			.build()
		;
	}
}
