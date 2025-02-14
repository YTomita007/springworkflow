package springworkflow.tasks.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import springworkflow.tasks.entity.Employee;

@Mapper
public interface EmployeeMapper {
	public Employee find(String employeeNo);
	public Employee login(String employeeNo, String password);
	public ArrayList<Employee> findAll();
}
