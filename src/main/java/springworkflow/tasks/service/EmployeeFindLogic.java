package springworkflow.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Employee;
import springworkflow.tasks.mapper.EmployeeMapper;

@Service
public class EmployeeFindLogic implements EmployeeFindLogicIF {
	@Autowired
	private EmployeeMapper employeeMapper;

	@Override
	public Employee find(String employeeNo) {
		Employee employee = employeeMapper.find(employeeNo);

		return employee;
	}

	@Override
	public Employee login(String employeeNo, String password) {
		Employee employee = employeeMapper.login(employeeNo, password);
		if (employee == null) {
			throw new BusinessException("ログインに失敗しました。");
		}

		return employee;
	}
}
