package network.thenull.api.posts.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Abbreviated data for posts that are just being browsed, not focused. For instance, during search.")
@JsonInclude(JsonInclude.Include.NON_NULL) // matches the frontend by excluding tagIds when it's not present
public record PostListingDto(
	@Schema(description="The post's ID. Needed to target the post's details page.", example="1")
	Long id,
	
	@Schema(description="The post's thumbnail URL.", example="https://cdn.example.com/thumbs/12/34/56/123456.jpg")
	String thumb,
	
	@Schema(description="The URL of the page being spotlighted by the post.", example="https://example.com")
	String url,
	
	@Schema(description="The title of the page being spotlighted by the post.", example="Example Domain")
	String title,
	
	@Schema(description="A concatenation of the names of tags that are applied to the post.", example="nonprofit no_ads pure_vanilla")
	String tags,
	
	@Schema(description="The IDs of tags that are applied to the post.", example="[3, 70, 92]")
	List<Long> tagIds,
	
	@Schema(description="Whether a post is marked as Featured.", example="true")
	Boolean featured
) {}
