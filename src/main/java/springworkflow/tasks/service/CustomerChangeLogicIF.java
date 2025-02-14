package springworkflow.tasks.service;

import springworkflow.tasks.entity.Customer;

public interface CustomerChangeLogicIF {
	public Customer find(String custCode);

	public Customer update(Customer customer);
}
