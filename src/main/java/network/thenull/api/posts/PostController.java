package network.thenull.api.posts;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles requests for post retrieval
 */
@RestController
@Slf4j
@RequestMapping("/api/posts")
public class PostController {
	
	@GetMapping("/browse")
	public ResponseEntity<PostBrowsingPageDto> getPostListings(
		@RequestParam(defaultValue="1") int page,
		@RequestParam(required=false, name="q") String search
	) {
		
		
		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(null)
		;
	}
}
