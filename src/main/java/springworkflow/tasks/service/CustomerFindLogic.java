package springworkflow.tasks.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;

@Service
public class CustomerFindLogic implements CustomerFindLogicIF {
	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public Customer find(String custCode) {
		Customer customer = customerMapper.find(custCode);
		if (customer == null) {
			throw new BusinessException("該当する得意先コードは存在しません。");
		}

		return customer;
	}

	@Override
	public ArrayList<Customer> findByTelNo(String telNo) {
		ArrayList<Customer> customers = customerMapper.findByTelNo(telNo);
		if (customers.size() == 0) {
			throw new BusinessException("該当する得意先コードは存在しません。");
		}

		return customers;
	}
}
