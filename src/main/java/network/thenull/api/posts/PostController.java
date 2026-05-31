package network.thenull.api.posts;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import network.thenull.api.posts.dto.PostBrowsingPageDto;
import network.thenull.api.posts.dto.PostPreviewDto;

/**
 * Handles requests for post retrieval
 */
@RestController
@Slf4j
@RequestMapping("/api/posts")
public class PostController {
	
	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	@Operation(
		summary="Preview data for a new post.", 
		description="Retrieve post-preview data for a post that the user is requesting to add to the service."
			+ " Does not include image / screenshot data, as this is carried out by the \"preview/screenshot\""
			+ " endpoint instead. Identifies the post ID if the page already exists in the application."
			+ " Also validates the input URL and resolves any redirects (i.e., \"google.com\" will become"
			+ " \"https://www.google.com\").",
		responses= {
			@ApiResponse(
				responseCode="200",
				description="Successfully validated the input URL and populated post-preview data",
				content=@Content(
					schema=@Schema(
						implementation=PostPreviewDto.class
					)
				)
			),
			@ApiResponse(
				responseCode="400",
				description="The URL couldn't be validated, or the target page couldn't be reached",
				content=@Content(
					mediaType="application/json",
					schema=@Schema(implementation=ProblemDetail.class)
				)
			)
		}
	)
	@GetMapping("/preview")
	public ResponseEntity<PostPreviewDto> previewNewPost(
		@RequestParam(required=true) String url
	) {
		URI validatedUri = postService.getValidatedUriOf(url);
		String validatedUrl = validatedUri.toString();
		Long existingPostId = postService.getExistingPostId(validatedUri.getHost());
		
		try {
			String pageTitle = postService.getPageTitle(validatedUrl);
			
			return ResponseEntity.ok()
				.body(new PostPreviewDto(
					existingPostId != null,
					existingPostId,
					pageTitle,
					validatedUrl
				))
			;
		} catch (IOException e) {
			return ResponseEntity.ok()
				.body(new PostPreviewDto(
					existingPostId != null,
					existingPostId,
					"!!!COULDN'T RETRIEVE PAGE TITLE!!!",
					validatedUrl
				))
			;
		}
	}
	
	@Operation(
		summary="Screenshot retrieval for a new post.", 
		description="Retrieve screenshot data for a URL. Validates URL before retrieving screenshot data.",
		responses= {
			@ApiResponse(
				responseCode="200",
				description="Successfully acquired screenshot",
				content=@Content(
					mediaType="image/png",
					schema=@Schema(
						type="string",
						format="binary"
					)
				)
			),
			@ApiResponse(
				responseCode="400",
				description="The URL couldn't be validated, or the target page couldn't be reached",
				content=@Content(
					mediaType="application/json",
					schema=@Schema(implementation=ProblemDetail.class)
				)
			)
		}
	)
	@GetMapping("/preview/screenshot")
	public ResponseEntity<byte[]> getPreviewScreenshot(
		@RequestParam(required=true) String url
	) {
		return ResponseEntity.ok()
			.contentType(MediaType.IMAGE_PNG)
			.header("Content-Disposition", "filename=\"screenshot.png\"")
			.body(postService.getPageScreenshot(postService.getValidatedUriOf(url).toString()))
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
