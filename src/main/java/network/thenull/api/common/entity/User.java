package network.thenull.api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class User {	
	@Id
	private Long id;
	
	@Column(nullable=false, length=40)
	private String username;
	
	@Column(nullable=false, length=100)
	private String passwordHash;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="role", nullable=false)
	private UserRole role;
}
