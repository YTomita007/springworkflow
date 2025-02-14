package springworkflow.util;

import java.util.ArrayList;

import springworkflow.tasks.entity.AggregateByCustomer;

public class TestAggregateByCustomer {

	public static AggregateByCustomer create() {
		return new AggregateByCustomer(
				"KA0001",
				"Test Name",
				10);
	}

	public static ArrayList<AggregateByCustomer> createArrayList() {
		ArrayList<AggregateByCustomer> list = new ArrayList<>();
		list.add(TestAggregateByCustomer.create());
		return list;
	}

}
