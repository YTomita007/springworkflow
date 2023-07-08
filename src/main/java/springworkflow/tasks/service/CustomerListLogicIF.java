package springworkflow.tasks.service;

import java.util.ArrayList;

import springworkflow.tasks.entity.Customer;

public interface CustomerListLogicIF {
	public ArrayList<Customer> findAll();

	public ArrayList<Customer> sortedList(String sortId, String sortType);
}
