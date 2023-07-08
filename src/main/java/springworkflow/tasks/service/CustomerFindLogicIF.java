package springworkflow.tasks.service;

import java.util.ArrayList;

import springworkflow.tasks.entity.Customer;

public interface CustomerFindLogicIF {
	public Customer find(String custCode);

	public ArrayList<Customer> findByTelNo(String telNo);
}
