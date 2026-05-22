package network.thenull.api.common.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;
import network.thenull.api.common.entity.id.TagImplicationId;

/**
 * For tags that imply that another tag also applies.
 * 
 * For instance, next.js implies react.
 */
@Entity
@Data
public class TagImplication {
	@EmbeddedId
	private TagImplicationId id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId("implyingTagId")
	@JoinColumn(name="implying_tag_id", nullable=false)
	private Tag implyingTag;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId("impliedTagId")
	@JoinColumn(name="implied_tag_id", nullable=false)
	private Tag impliedTag;
}
