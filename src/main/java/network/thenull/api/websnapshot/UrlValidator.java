package network.thenull.api.websnapshot;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Validation component for checking URLs before the application uses them.
 * 
 * Includes checks to ensure that the URL points to a website that can be 
 * accessed and returns a 2XX OK response.
 * 
 * Also includes validation against SSRF attacks as outlined here:
 * https://codesignal.com/learn/courses/server-side-request-forgery-ssrf-prevention-in-spring-boot/lessons/preventing-ssrf-in-java
 */
@Component
public class UrlValidator {
	
	private final RestClient restClient;
	
	public UrlValidator(RestClient restClient) {
		this.restClient = restClient;
	}
	
	/**
	 * Returns a validated version of an input URL String.
	 * 
	 * Validations include ensuring that the URL isn't malformed, prevention 
	 * of SSRF-unsafe URLs, and assurance that the URL leads to a reachable 
	 * location that returns a 2XX response.
	 * 
	 * If the input URL String does not have an http:// or https:// scheme, 
	 * https:// will be added, and if https:// does not lead to a valid 
	 * reachable location, http:// will be tried.
	 * 
	 * @param url
	 * @return A String representing a validated URL
	 * @throws URISyntaxException for inputs that are not URLs
	 * @throws UnknownHostException for inputs whose domains/hosts have no IP address
	 * @throws MalformedURLException for inputs that can't be reached as URLs
	 * @throws IllegalArgumentException for inputs that fail validation checks
	 */
	public String getValidatedUrlOf(String url) throws URISyntaxException, UnknownHostException, MalformedURLException, IllegalArgumentException {
		// cleanse scheme
		if(url.startsWith("https://")) {
			url = url.substring(8);
		} else if(url.startsWith("http://")) {
			url = url.substring(7);
		}
		
		// check https first
		String checkUrl = "https://" + url;
		URI uri = new URI(checkUrl);
		String host = uri.getHost();
		
		// check for an SSRF-safe host
		if(host == null) {
			throw new IllegalArgumentException("No host found in the input URL");
		} else if(!isSsrfSafe(host)) {
			throw new IllegalArgumentException("The input URL points to an internal or otherwise SSRF-unsafe host");
		}
		
		// check the location addressed by https
		if(isAvailable(uri)) {
			return checkUrl;
		}
		
		// check http location if https isn't available
		checkUrl = "http://" + url;
		if(isAvailable(new URI(checkUrl))) {
			return checkUrl;
		}
		
		throw new IllegalArgumentException("The input URL could not be reached");
	}
	
	private boolean isSsrfSafe(String host) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(host);
		
		return address.isAnyLocalAddress()
			|| address.isLoopbackAddress()
			|| address.isLinkLocalAddress()
			|| address.isSiteLocalAddress()
		;
	}
	
	private boolean isAvailable(URI uri) throws MalformedURLException {
		return restClient
			.head()
			.uri(uri)
			.retrieve()
			.toBodilessEntity()
			.getStatusCode()
			.is2xxSuccessful()
		;
	}
}
