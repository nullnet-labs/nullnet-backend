package network.thenull.api.common.entity.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class HyperlinkId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4198964399589704818L;

	private Long linkedFromId;
	
	private Long linkedToId;
}
