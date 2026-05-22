package network.thenull.api.common.entity.id;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class TagImplicationId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6114139336152915012L;

	private Long implyingTagId;
	
	private Long impliedTagId;
}
