package springworkflow.tasks.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm implements Serializable {

	@NotBlank
	private String custName;

	@Pattern(regexp = "^\\d{2,4}-?\\d{2,4}-?\\d{3,4}", message = "電話番号は適切な形式(ハイフンを入れる)で入れてください")
	@NotBlank
	private String telNo;

	@Pattern(regexp = "^\\d{3}\\-?\\d{4}$", message = "郵便番号は適切な形式(ハイフンを入れる)で入れてください")
	@NotBlank
	private String postalCode;

	@NotBlank
	private String address;

	@PositiveOrZero
	private int discountRate;

}
