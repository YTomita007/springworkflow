package springworkflow.tasks.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregateByCustomer implements Serializable {

	private String custCode;

	private String custName;

	private int totalPrice;

}
