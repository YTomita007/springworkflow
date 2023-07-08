package springworkflow.tasks.service;

import java.util.ArrayList;

import springworkflow.tasks.entity.Employee;

public interface EmployeeListLogicIF {
	public ArrayList<Employee> findAll();
}
