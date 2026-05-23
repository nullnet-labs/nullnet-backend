package network.thenull.api.tags;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Tag data assorted under the respective tag categories, hierarchically shaped as it is in the UI.")
public record CategorizedTagsDto(
	@Schema(description="The name of the category.", example="General")
	String name
) {}
