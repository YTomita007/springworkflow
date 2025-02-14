package springworkflow.tasks.form;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailForm implements Serializable {

	@NotBlank
	private String itemCode;

	private String itemName;

	@Min(1)
	private int amount;
}
