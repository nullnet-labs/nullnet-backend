package network.thenull.api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PostStatus {
	@Id
	private Integer id;
	
	@Column(nullable=false, length=25)
	private String description;
}
