package network.thenull.api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * User roles, like "USER" or "ADMIN" or "BANNED" etc.
 */
@Entity
@Data
public class UserRole {
	@Id
	private Integer id;
	
	@Column(nullable=false, length=10)
	private String name;
}
