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
 * Data regarding updates that are requested for posts.
 * 
 * Holds data that can be submitted to update the corresponding Post-table 
 * records, if the requested update is committed to the platform.
 */
@Entity
@Data
public class PostUpdateRequest {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="post_update_request_ids_sequence")
	@SequenceGenerator(
		name="post_update_request_ids_sequence",
		sequenceName="post_update_request_ids_sequence",
		allocationSize=1
	)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="post_id", nullable=false)
	private Post post;
	
	@Column(nullable=false)
	private String title;
	
	@Column(nullable=true)
	private String imageUrl;
	
	@Column(nullable=true)
	private String thumbnailUrl;
	
	@Column(nullable=false)
	private Instant updateRequestTime;
}
