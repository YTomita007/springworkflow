package springworkflow.tasks.form;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springworkflow.tasks.entity.ItemWithAmount;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPurchaseForm implements Serializable {

	private String orderNo;

	private String employeeNo;

	private String custCode;

	@Min(1)
	private int totalPrice;

	@Min(1)
	private int detailNum;

	@NotBlank
	private String deliverDate;

	@NotBlank
	private String orderDate;

	private ArrayList<ItemWithAmount> shoppingCart;
}
