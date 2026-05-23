package network.thenull.api.tags;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Tag data as it appears listed in the UI.")
public record TagListingDto(
	@Schema(description="The name of the tag.", example="no ads")
	String name,
	
	@Schema(description="The number of posts under the tag.", example="115")
	String posts // defined as string in case of a future where post counts are, for instance, "12K"
) {}
