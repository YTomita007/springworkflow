package springworkflow.tasks.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeForm implements Serializable {

	@NotBlank
	private String custName;

	@NotBlank
	private String telNo;

	@NotBlank
	private String postalCode;

	@NotBlank
	private String address;

	@PositiveOrZero
	private int discountRate;

}
