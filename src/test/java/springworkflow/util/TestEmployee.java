package springworkflow.util;

import springworkflow.tasks.entity.Employee;

public class TestEmployee {

	public static Employee create() {
		return new Employee(
				"H20001",
				"Test Employee",
				"password");
	}

}
