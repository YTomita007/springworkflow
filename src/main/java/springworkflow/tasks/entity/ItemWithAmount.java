package springworkflow.tasks.entity;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithAmount extends Item implements Serializable {

	public ItemWithAmount(Item item, int amount) {
		super(item);
		this.amount = amount;
	}

	@Min(1)
	private int amount;
}