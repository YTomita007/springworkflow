package springworkflow.tasks.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.AggregateByCustomer;
import springworkflow.tasks.entity.AggregateByItem;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;
import springworkflow.tasks.mapper.OrderMapper;

@Service
public class OrderAggregateLogic implements OrderAggregateLogicIF {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public ArrayList<AggregateByCustomer> totalOfYear(int year) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/M/d");
		LocalDate startLocalDate = LocalDate.of(year, 1, 1);
		String startDate = startLocalDate.format(dtf);
		String endDate = LocalDate.of(year, 12, 31).format(dtf);

		ArrayList<AggregateByCustomer> list = orderMapper.totalOfYearOrMonthByCustomer(startDate, endDate);
		if (list.isEmpty()) {
			throw new BusinessException("該当する受注はありません。", "301_01AggregateByYearView");
		}
		return list;
	}

	@Override
	public ArrayList<AggregateByCustomer> totalPerMonths(int year) {
		ArrayList<AggregateByCustomer> list = orderMapper.totalOfMonthByCustomer(year);
		if (list.isEmpty()) {
			throw new BusinessException("該当する受注はありません。");
		}
		return list;
	}

	@Override
	public ArrayList<AggregateByCustomer> totalOfMonth(int year, int month) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/M/d");
		LocalDate startLocalDate = LocalDate.of(year, month, 1);
		String startDate = startLocalDate.format(dtf);
		String endDate = LocalDate.of(year, month, startLocalDate.lengthOfMonth()).format(dtf);

		ArrayList<AggregateByCustomer> list = orderMapper.totalOfYearOrMonthByCustomer(startDate, endDate);
		if (list.isEmpty()) {
			throw new BusinessException("該当する受注はありません。", "302_01AggregateByMonthView");
		}
		return list;
	}

	@Override
	public ArrayList<AggregateByItem> totalByItem(String custCode) {
		ArrayList<AggregateByItem> list = orderMapper.createOrderTotalListByItem(custCode);
		if (list.isEmpty()) {
			throw new BusinessException("該当する受注はありません。", "303_01AggregateByItemView");
		}

		return list;
	}

	@Override
	public Customer getCustomer(String custCode) {
		Customer customer = customerMapper.findIgnoreDeleteFlag(custCode);
		if (customer == null) {
			throw new BusinessException("該当する得意先コードは存在しません。", "303_01AggregateByItemView");
		}

		return customer;
	}
}