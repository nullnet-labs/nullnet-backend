package network.thenull.api.common.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;
import network.thenull.api.common.entity.id.HyperlinkId;

@Entity
@Data
public class Hyperlink {
	
	@EmbeddedId
	private HyperlinkId id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId("linkedFromId")
	@JoinColumn(name="linked_from_id", nullable=false)
	private Post linkedFrom;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId("linkedToId")
	@JoinColumn(name="linked_to_id", nullable=false)
	private Post linkedTo;
}
