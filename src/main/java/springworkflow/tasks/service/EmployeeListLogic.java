package springworkflow.tasks.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springworkflow.tasks.entity.Employee;
import springworkflow.tasks.mapper.EmployeeMapper;

@Service
public class EmployeeListLogic implements EmployeeListLogicIF {

	@Autowired
	private EmployeeMapper employeeMapper;

	@Override
	public ArrayList<Employee> findAll() {
		ArrayList<Employee> list = employeeMapper.findAll();

		return list;
	}
}
