package network.thenull.api.posts.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Data for a user to preview what will appear on a post, as well as whether a post already exists.")
public record PostPreviewDto(
	@Schema(description="Clear intent indicator of whether the requested post a request to update an existing post.")
	Boolean isUpdate,
		
	@Schema(description="The ID of an existing post that matches the given URL.")
	Long existingPostId,
	
	@Schema(description="The title of the page for the given post.")
	String postTitle
) {}
