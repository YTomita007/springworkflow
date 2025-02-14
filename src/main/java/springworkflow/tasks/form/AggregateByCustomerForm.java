package springworkflow.tasks.form;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregateByCustomerForm implements Serializable {
	@Min(2000)
	private int year;
	@Min(1)
	@Max(12)
	private int month;
}
