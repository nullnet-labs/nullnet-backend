package network.thenull.api.common.entity;

import java.time.Instant;

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
 * Comments users can make on posts.
 */
@Entity
@Data
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="comment_ids_sequence")
	@SequenceGenerator(
		name="comment_ids_sequence",
		sequenceName="comment_ids_sequence",
		allocationSize=1
	)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="post_id", nullable=false)
	private Post post;
	
	@Column(nullable=false, columnDefinition="TEXT")
	private String content;
	
	@Column(nullable=false)
	private Instant creationTime;
}
