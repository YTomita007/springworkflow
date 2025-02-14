package springworkflow.tasks.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;

@Service
public class CustomerListLogic implements CustomerListLogicIF {
	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public ArrayList<Customer> findAll() {
		ArrayList<Customer> list = customerMapper.findAll();

		return list;
	}

	@Override
	public ArrayList<Customer> sortedList(String column, String sortTpe) {
		ArrayList<Customer> customers = customerMapper.findAllWithSort(column, sortTpe);
		return customers;
	}

	//	@Override
	//	public ArrayList<Customer> sortedList(String sortId) {
	//
	//		List<Customer> customers = customerMapper.findAll();
	//
	//		customers = customers.stream().sorted(new Comparator<Customer>(){
	//		    @Override
	//		    public int compare(Customer cust1, Customer cust2) {
	//		    	switch(sortId) {
	//		    		case "1", "2":
	//				        return cust1.getCustCode().compareTo(cust2.getCustCode());
	//		    		case "3", "4":
	//				        return cust1.getCustName().compareTo(cust2.getCustName());
	//		    		case "5", "6":
	//				        return cust1.getTelNo().compareTo(cust2.getTelNo());
	//		    		case "7", "8":
	//				        return cust1.getPostalCode().compareTo(cust2.getPostalCode());
	//		    		case "9", "10":
	//				        return cust1.getAddress().compareTo(cust2.getAddress());
	//		    		case "11", "12":
	//						return Integer.compare(cust1.getDiscountRate(), cust2.getDiscountRate());
	//		    	}
	//		    	return 0;
	//			}
	//
	//		}).collect(Collectors.toList());
	//
	//		if(Integer.parseInt(sortId) % 2 == 0) {
	//			Collections.reverse(customers);
	//		}
	//
	//		return (ArrayList<Customer>) customers;
	//	}
}
