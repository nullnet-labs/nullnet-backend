package network.thenull.api.posts;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import network.thenull.api.tags.CategorizedTagsDto;

@Schema(description="Data package for a post-browsing page, like the home page & search-results pages.")
public record PostBrowsingPageDto(
	@Schema(description="The posts displayed on this page.")
	List<PostListingDto> posts,
	
	@Schema(description="The tags displayed on this page.")
	List<CategorizedTagsDto> tags,
	
	@Schema(description="The last page.")
	Integer lastPage
) {}
