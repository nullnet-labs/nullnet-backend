package network.thenull.api.common.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class TagCategory {
	@Id
	private Integer id;
	
	@Column(nullable=false, length=20)
	private String name;
	
	@OneToMany(mappedBy="category", fetch=FetchType.LAZY)
	private List<Tag> tags = new ArrayList<>();
}
