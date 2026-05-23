package network.thenull.api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

/**
 * Tags that are applied to posts for search filtering & discoverability.
 * 
 * Each tag falls under a category and may map to another tag if it's a 
 * synonym for that other tag.
 */
@Entity
@Data
public class Tag {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="tag_ids_sequence")
	@SequenceGenerator(
		name="tag_ids_sequence",
		sequenceName="tag_ids_sequence",
		allocationSize=1
	)
	private Long id;
	
	@Column(nullable=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="category_id", nullable=false)
	private TagCategory category;
	
	/**
	 * For tags that are aliases for other tags, such as terms translated 
	 * between languages.
	 */
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="canonical_tag_id", nullable=true)
	private Tag canonicalTag;
}
