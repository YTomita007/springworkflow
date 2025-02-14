package springworkflow.tasks.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm implements Serializable {

	@NotBlank
	private String employeeNo;

	@NotBlank
	private String password;

}
