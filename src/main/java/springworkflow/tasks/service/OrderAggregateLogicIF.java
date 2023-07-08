package springworkflow.tasks.service;

import java.util.ArrayList;

import springworkflow.tasks.entity.AggregateByCustomer;
import springworkflow.tasks.entity.AggregateByItem;
import springworkflow.tasks.entity.Customer;

public interface OrderAggregateLogicIF {
	public ArrayList<AggregateByCustomer> totalOfYear(int year);

	public ArrayList<AggregateByCustomer> totalOfMonth(int year, int month);

	public ArrayList<AggregateByCustomer> totalPerMonths(int year);

	public ArrayList<AggregateByItem> totalByItem(String custCode);

	public Customer getCustomer(String custCode);
}
