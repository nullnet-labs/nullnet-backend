package network.thenull.api.common.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

/**
 * Full info for each Web page posting in the application.
 */
@Entity
@Data
public class Post {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="post_ids_sequence")
	@SequenceGenerator(
		name="post_ids_sequence",
		sequenceName="post_ids_sequence",
		allocationSize=1
	)
	private Long id;
	
	@Column(nullable=false)
	private String url;
	
	/**
	 * Fully qualified domain name.
	 * 
	 * At least at the start, only one posting will be allowed per domain.
	 */
	@Column(nullable=false)
	private String domain;
	
	@Column(nullable=false)
	private String title;
	
	@Column(nullable=true)
	private String imageUrl;
	
	/**
	 * May use naming scheme that makes thumbnail URL redundant. Remains to be 
	 * seen, but this field remains for now, during development while thumb & 
	 * image URLs are able to have potentially fluid relationships.
	 */
	@Column(nullable=true)
	private String thumbnailUrl;
	
	@OneToMany(mappedBy="post", fetch=FetchType.LAZY)
	private Set<PostTag> postTags = new HashSet<>();
	
	@OneToMany(mappedBy="linkedTo", fetch=FetchType.LAZY)
	private Set<Hyperlink> linkedToBy = new HashSet<>();
	
	@OneToMany(mappedBy="linkedFrom", fetch=FetchType.LAZY)
	private Set<Hyperlink> links = new HashSet<>();
	
	@OneToMany(mappedBy="post", fetch=FetchType.LAZY)
	private Set<Comment> comments = new HashSet<>();
	
	@OneToMany(mappedBy="post", fetch=FetchType.LAZY)
	private Set<PostUpdateRequest> updates = new HashSet<>();
	
	@Column(nullable=false)
	private Instant creationTime;
	
	@Column(nullable=false)
	private Instant lastUpdateTime;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="posted_by", nullable=false)
	private User postedBy;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="post_status", nullable=false)
	private PostStatus status;
}
