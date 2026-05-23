package network.thenull.api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Data;

/**
 * Definitions for tags. Not referenced by Tag items themselves, as the 
 * definition content may be long and shouldn't be included every time the 
 * Tags themselves are queried.
 */
@Entity
@Data
public class TagDefinition {
	@Id
	private Long tagId;
	
	@OneToOne(fetch=FetchType.LAZY, optional=false)
	@MapsId
	@JoinColumn(name="tag_id", nullable=false)
	private Tag tag;
	
	@Column(nullable=false, columnDefinition="TEXT")
	private String content;
}
