package springworkflow.tasks.service;

import springworkflow.tasks.entity.Employee;

public interface EmployeeFindLogicIF {
	public Employee find(String employeeNo);

	public Employee login(String employeeNo, String password);
}
