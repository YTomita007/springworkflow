package springworkflow.tasks.service;

import springworkflow.tasks.entity.Customer;

public interface CustomerDeleteLogicIF {
	public Customer find(String custCode);

	public Customer delete(String custCode);
}
