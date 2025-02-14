package springworkflow.tasks.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregateByItem implements Serializable {

	private String itemCode;

	private String itemName;

	private int totalAmount;

	private int price;

	private int totalPrice;

}