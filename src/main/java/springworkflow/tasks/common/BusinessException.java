package springworkflow.tasks.common;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException {
	private ArrayList<String> messageList = new ArrayList<>();
	private String page;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, String page) {
		super(message);
		this.page = page;
	}
}
