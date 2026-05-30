package network.thenull.api.posts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import network.thenull.api.posts.dto.PostBrowsingPageDto;
import network.thenull.api.websnapshot.WebSnapshotService;

@Service
@Slf4j
public class PostService {
	
	private final PostRepository postRepo;
	private final WebSnapshotService webSnapshotService;
	private final UrlValidator urlValidator;
	
	public PostService(PostRepository postRepo, WebSnapshotService webSnapshotService, UrlValidator urlValidator) {
		this.postRepo = postRepo;
		this.webSnapshotService = webSnapshotService;
		this.urlValidator = urlValidator;
	}
	
	public URI getValidatedUriOf(String url) {
		try {
			return urlValidator.getValidatedUriOf(url);
		} catch (URISyntaxException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The input URL does not follow URI syntax");
		} catch (UnknownHostException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The input URL's domain has no identifiable IP address");
		} catch (MalformedURLException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The input URL can't be parsed as a URL");
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	public Long getExistingPostId(String domain) {
		return postRepo.findIdByDomain(domain).orElse(null);
	}
	
	public String getPageTitle(String url) throws IOException {
		return webSnapshotService.getPageTitle(url);
	}
	
	public byte[] getPageScreenshot(String url) {
		return webSnapshotService.getPageScreenshot(url);
	}
	
	public PostBrowsingPageDto getPostBrowsingPageData(Integer page, String search) {
		String[]searchTokens = search.split(" ");
		
		System.out.println("Page:");
		System.out.println(page);
		System.out.println("Search:");
		System.out.println(search);
		System.out.println("Search tokens:");
		
		for(String token : searchTokens) {
			System.out.println(token);
		}
		
		return null;
	}
}
