package springworkflow.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;

@Service
public class CustomerChangeLogic implements CustomerChangeLogicIF {
	@Autowired
	private CustomerMapper customerMapper;

	public Customer find(String custCode) {
		Customer customer = customerMapper.find(custCode);
		if (customer == null) {
			throw new BusinessException("該当する得意先コードは存在しません。");
		}

		return customer;
	}

	@Transactional
	public Customer update(Customer customer) {
		Customer target = customerMapper.find(customer.getCustCode());
		if (target == null) {
			throw new BusinessException("該当する得意先コードは存在しません。");
		}

		boolean result = customerMapper.update(customer);

		if (!result) {
			throw new BusinessException("得意先の変更に失敗しました。");
		}

		return customer;
	}

}
