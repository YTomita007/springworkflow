package springworkflow.tasks.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import springworkflow.tasks.entity.AggregateByCustomer;
import springworkflow.tasks.entity.AggregateByItem;
import springworkflow.tasks.entity.ItemWithAmount;
import springworkflow.tasks.form.OrderPurchaseForm;

@Mapper
public interface OrderMapper {
	public ArrayList<AggregateByCustomer> totalOfMonthByCustomer(int year);
	public ArrayList<AggregateByCustomer> totalOfYearOrMonthByCustomer(String startDate, String endDate);
	public ArrayList<AggregateByItem> createOrderTotalListByItem(String code);
	public String maxOrderNo();
	public int createOrder(OrderPurchaseForm orderPurchaseForm);
	public int createOrderDetail(OrderPurchaseForm orderPurchaseForm, ItemWithAmount itemWithAmpAmount);
}
