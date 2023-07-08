package springworkflow.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;
import springworkflow.tasks.mapper.CustomerNumberingMapper;

@Service
public class CustomerRegisterLogic implements CustomerRegisterLogicIF {
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private CustomerNumberingMapper customerNumberingMapper;

	@Override
	@Transactional
	public Customer register(Customer customer) {

		int latestCustomerCode = customerNumberingMapper.find();
		String newCustomerCode = "KA" + String.format("%04d", latestCustomerCode + 1);
		customer.setCustCode(newCustomerCode);

		customerMapper.insert(customer);
		customerNumberingMapper.increment();
		return customer;
	}
}
