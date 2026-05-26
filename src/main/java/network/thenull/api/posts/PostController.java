package network.thenull.api.posts;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;

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
	public ResponseEntity<String> previewNewPost() {
		return ResponseEntity.ok()
			.body("OK! Endpoint hit for previewing a post before creating.")
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
