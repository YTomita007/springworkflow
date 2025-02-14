package springworkflow.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;

@Service
public class CustomerDeleteLogic implements CustomerDeleteLogicIF {
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
	public Customer delete(String custCode) {
		Customer customer = customerMapper.find(custCode);
		if (customer == null) {
			throw new BusinessException("該当する得意先コードは存在しません。");
		}

		boolean result = customerMapper.delete(customer);

		if (!result) {
			throw new BusinessException("得意先の削除に失敗しました。");
		}

		return customer;
	}
}
