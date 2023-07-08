package springworkflow.tasks.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceForm implements Serializable {

	@NotBlank
	private String employeeNo;

	@NotBlank
	private String employeeName;

	private boolean attendance;
}
