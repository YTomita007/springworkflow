package springworkflow.util;

import springworkflow.tasks.entity.Customer;

public class TestCustomer {

	public static Customer create() {
		return new Customer(
				"KA0001",
				"Test Name",
				"000-0000-0000",
				"999-9999",
				"test address",
				10);
	}

	public static Customer create2() {
		return new Customer(
				"KA0999",
				"Test Name",
				"000-0000-0000",
				"999-9999",
				"test address",
				10);
	}
}