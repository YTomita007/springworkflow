package springworkflow.tasks.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SystemException extends RuntimeException {
	public SystemException(String message) {
		super(message);
	}
}
