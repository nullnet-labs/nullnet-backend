package network.thenull.api.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class PostTag {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="post_tag_ids_sequence")
	@SequenceGenerator(
		name="post_tag_ids_sequence",
		sequenceName="post_tag_ids_sequence",
		allocationSize=1
	)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Post post;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Tag tag;
}
