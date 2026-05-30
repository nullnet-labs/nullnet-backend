package network.thenull.api.posts;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
	
	@Bean
	public HttpClient httpClient() {
		return HttpClient.newBuilder()
			// resolves 3XX redirects
			.followRedirects(HttpClient.Redirect.NORMAL)
			.connectTimeout(Duration.ofSeconds(5))
			.build()
		;
	}
}
