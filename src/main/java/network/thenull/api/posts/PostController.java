package network.thenull.api.posts;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import network.thenull.api.posts.dto.PostBrowsingPageDto;
import network.thenull.api.posts.dto.PostPreviewDto;

/**
 * Handles requests for post retrieval
 */
@Validated
@RestController
@Slf4j
@RequestMapping("/api/posts")
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/preview")
	public ResponseEntity<PostPreviewDto> previewNewPost(
		@RequestParam(required=true) String url
	) {
		if(!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "https://" + url;
		}
		
		try {
			// implicitly validates that the input string is a URL
			URI uri = new URI(url.toLowerCase());
			String domain = uri.getHost();
			
			Long existingPostId = postService.getExistingPostId(domain);
			// implicitly validates that the URL points to a valid Web page
			String pageTitle = postService.getPageTitle(url);
			
			return ResponseEntity.ok()
				.body(new PostPreviewDto(
					existingPostId != null,
					existingPostId,
					pageTitle,
					url
				))
			;
		} catch(URISyntaxException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed URL");
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested Web page couldn't be reached");
		}
	}
	
	@GetMapping("/preview/screenshot")
	public ResponseEntity<byte[]> getPreviewScreenshot(
		@RequestParam(required=true) String url
	) {
		if(!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "https://" + url;
		}
		
		return ResponseEntity.ok()
			.contentType(MediaType.IMAGE_PNG)
			.header("Content-Disposition", "filename=\"screenshot.png\"")
			.body(postService.getPageScreenshot(url))
		;
	}
	
	@PostMapping("/update/request")
	public ResponseEntity<String> requestPostUpdate() {
		return ResponseEntity.ok()
			.body("OK! Endpoint hit for requesting an update to an existing post.")
		;
	}
	
	@PatchMapping("/update/commit")
	public ResponseEntity<String> updatePost() {
		return ResponseEntity.ok()
			.body("OK! Endpoint hit for committing an update to an existing post.")
		;
	}
	
	@PostMapping("/create")
	public ResponseEntity<String> createPost() {
		return ResponseEntity.ok()
			.body("OK! Endpoint hit for creating a new post.")
		;
	}
	
	@GetMapping("/browse")
	public ResponseEntity<PostBrowsingPageDto> getPostListings(
		@RequestParam(defaultValue="1") @Min(1) int page,
		@RequestParam(required=false, name="q") String search
	) {
		
		
		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(postService.getPostBrowsingPageData(page, search))
		;
	}
}
