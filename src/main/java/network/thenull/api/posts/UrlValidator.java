package network.thenull.api.posts;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Validation component for checking URLs before the application uses them.
 * 
 * Includes checks to ensure that the URL points to a website that can be 
 * accessed and returns a 2XX OK response. If a site returns a 3XX response, 
 * redirects will be resolved until either a 2XX site is found or validation 
 * checks fail.
 * 
 * Also includes validation against SSRF attacks as outlined here:
 * https://codesignal.com/learn/courses/server-side-request-forgery-ssrf-prevention-in-spring-boot/lessons/preventing-ssrf-in-java
 */
@Component
@Slf4j
public class UrlValidator {
	
	private final HttpClient httpClient;
	
	public UrlValidator(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	/**
	 * Returns a validated URI for an input URL String.
	 * 
	 * Validations include ensuring that the URL isn't malformed, prevention 
	 * of SSRF-unsafe URLs, and assurance that the URL leads to a reachable 
	 * location that returns a 2XX response. If a site returns a 3XX response, 
	 * redirects will be resolved until either a 2XX site is found or 
	 * validation checks fail.
	 * 
	 * If the input URL String does not have an http:// or https:// scheme, 
	 * https:// will be added, and if https:// does not lead to a valid 
	 * reachable location, http:// will be tried.
	 * 
	 * Throws one of the listed Exceptions if the input URL can't be 
	 * validated.
	 * 
	 * @param url
	 * @return A String representing a validated URL
	 * @throws URISyntaxException for inputs that don't follow URI syntax
	 * @throws UnknownHostException for inputs whose domains/hosts have no IP address
	 * @throws MalformedURLException for inputs that can't be reached as URLs
	 * @throws IllegalArgumentException for inputs that fail validation checks
	 */
	public URI getValidatedUriOf(String url) throws URISyntaxException, UnknownHostException, MalformedURLException, IllegalArgumentException {
		// cleanse scheme
		if(url.startsWith("https://")) {
			url = url.substring(8);
		} else if(url.startsWith("http://")) {
			url = url.substring(7);
		}
		
		// check https first
		URI uri = new URI("https://" + url);
		
		// check for an SSRF-safe host
		validateHost(uri);
		
		// check the location being addressed
		return getResolvedUri(url);
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
	 * Throws one of the listed Exceptions if the input URL can't be 
	 * validated.
	 * 
	 * @param url
	 * @return A String representing a validated URL
	 * @throws URISyntaxException for inputs that are not URLs
	 * @throws UnknownHostException for inputs whose domains/hosts have no IP address
	 * @throws MalformedURLException for inputs that can't be reached as URLs
	 * @throws IllegalArgumentException for inputs that fail validation checks
	 */
	public String getValidatedUrlOf(String url) throws URISyntaxException, UnknownHostException, MalformedURLException, IllegalArgumentException {
		return getValidatedUriOf(url).toString();
	}
	
	/**
	 * Checks both the https:// and http:// versions of the input raw URL to 
	 * ensure they're reachable, resolving any 3XX redirects before returning 
	 * the final resolved URI.
	 * 
	 * If any redirects that change the host occur, the host is re-checked for 
	 * SSRF safety before it's allowed to be returned. Otherwise, the 
	 * appropriate Exception is thrown.
	 * 
	 * @param rawUrl user-input URL with the scheme removed (no "http://" or "https://" at the beginning)
	 * @return URI representing the final reachable URI that points to the requested location, after redirects resolve
	 * @throws URISyntaxException thrown when the input URL cannot be parsed as a URI
	 * @throws IllegalStateException thrown when the requested URL doesn't resolve in a URI that returns an HTTP 2XX OK status
	 * @throws IllegalArgumentException thrown when the the URL resolves to an unreachable or SSRF-unsafe location
	 */
	private URI getResolvedUri(String rawUrl) throws URISyntaxException, IllegalStateException, IllegalArgumentException {
		URI uri = new URI("https://" + rawUrl);
		
		try {
			uri = getFinalUri(uri);
		} catch (IOException | InterruptedException e) {
			// when location doesn't resolve in a 2XX OK, check non-TLS http address
			try {
				uri = getFinalUri(new URI("http://" + rawUrl));
			} catch (IOException | InterruptedException | URISyntaxException e1) {
				throw new IllegalArgumentException("The page at the input URL is not available");
			}
		}
		
		// throw new IllegalArgumentException("The page at the input URL is not available");
		
		return uri;
	}
	
	private URI getFinalUri(URI uri) throws IOException, InterruptedException {
		URI resolvedUri = resolveRedirectableUri(uri);
		
		if(!resolvedUri.toString().equals(uri.toString())) {
			uri = resolvedUri;
			validateHost(uri);
		}
		
		return uri;
	}
	
	private boolean validateHost(URI uri) throws UnknownHostException {
		String host = uri.getHost();
		
		if(host == null) {
			throw new IllegalArgumentException("No host found in the URL that the input resolves to");
		} else if(isSsrfUnsafe(host)) {
			throw new IllegalArgumentException("The input URL points to an internal or otherwise SSRF-unsafe host");
		}
		
		return true;
	}
	
	private boolean isSsrfUnsafe(String host) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(host);
		
		return address.isAnyLocalAddress()
			|| address.isLoopbackAddress()
			|| address.isLinkLocalAddress()
			|| address.isSiteLocalAddress()
		;
	}
	
	/**
	 * Inputs an initial URI, and returns the final URI after 3XX redirects.
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private URI resolveRedirectableUri(URI uri) throws IOException, InterruptedException {
		HttpResponse<Void> response = sendHead(uri);
		int httpStatusCode = response.statusCode();
		
		// when "405 METHOD NOT ALLOWED" returns upon HEAD requests
		if(httpStatusCode == 405) {
			sendGet(uri);
			httpStatusCode = response.statusCode();
		}
		
		if(httpStatusCode < 200 || httpStatusCode >= 300) {
			throw new IllegalStateException("URL " + uri.toString() + " returned HTTP status code " + httpStatusCode);
		}
		
		return response.uri();
	}
	
	private HttpResponse<Void> sendHead(URI uri) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder(uri)
			.method("HEAD", HttpRequest.BodyPublishers.noBody())
			.timeout(Duration.ofSeconds(5))
			.build()
		;
		
		return httpClient.send(
			request, 
			HttpResponse.BodyHandlers.discarding()
		);
	}
	
	private HttpResponse<Void> sendGet(URI uri) throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder(uri)
			.GET()
			.timeout(Duration.ofSeconds(5))
			.build()
		;
		
		return httpClient.send(
			request, 
			HttpResponse.BodyHandlers.discarding()
		);
	}
}
