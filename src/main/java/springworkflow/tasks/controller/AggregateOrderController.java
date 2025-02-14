package springworkflow.tasks.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.AggregateByCustomer;
import springworkflow.tasks.entity.AggregateByItem;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.form.AggregateByCustomerForm;
import springworkflow.tasks.form.AggregateByItemForm;
import springworkflow.tasks.service.OrderAggregateLogicIF;

@Controller
@RequestMapping("/aggregate")
public class AggregateOrderController {
	@Autowired
	private OrderAggregateLogicIF orderAggregateLogic;

	@GetMapping("/year")
	public String findByYear(Model model) {
		model.addAttribute("aggregateByCustomerForm",
				new AggregateByCustomerForm(
						Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())), 1));
		return "300_aggregate/301_01AggregateByYearView";
	}

	@PostMapping("/year")
	public String findByYearResult(@Validated AggregateByCustomerForm aggregateByCustomerForm, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "300_aggregate/301_01AggregateByYearView";
		}
		ArrayList<AggregateByCustomer> orderList = orderAggregateLogic.totalOfYear(aggregateByCustomerForm.getYear());
		int sumTotal = 0;
		for (AggregateByCustomer order : orderList) {
			sumTotal += order.getTotalPrice();
		}
		model.addAttribute("orderList", orderList);
		model.addAttribute("sumTotal", sumTotal);

		return "300_aggregate/301_01AggregateByYearView";
	}

	@GetMapping("/month")
	public String findByMonth(Model model) {
		model.addAttribute("aggregateByCustomerForm",
				new AggregateByCustomerForm(
						Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())),
						Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))));
		return "300_aggregate/302_01AggregateByMonthView";
	}

	@PostMapping("/month")
	public String findByMonthResult(@Validated AggregateByCustomerForm aggregateByCustomerForm, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "300_aggregate/302_01AggregateByMonthView";
		}
		ArrayList<AggregateByCustomer> orderList = orderAggregateLogic.totalOfMonth(aggregateByCustomerForm.getYear(),
				aggregateByCustomerForm.getMonth());
		int sumTotal = 0;
		for (AggregateByCustomer order : orderList) {
			sumTotal += order.getTotalPrice();
		}
		model.addAttribute("orderList", orderList);
		model.addAttribute("sumTotal", sumTotal);

		return "300_aggregate/302_01AggregateByMonthView";
	}

	@GetMapping("/item")
	public String find(Model model) {
		model.addAttribute("AggregateByItemForm", new AggregateByItemForm());
		return "300_aggregate/303_01AggregateByItemView";
	}

	@PostMapping("/item")
	public String findResult(@Validated AggregateByItemForm AggregateByItemForm, BindingResult result, Model model) {
		model.addAttribute("AggregateByItemForm", new AggregateByItemForm());
		if (result.hasErrors()) {
			return "300_aggregate/303_01AggregateByItemView";
		}
		String custCode = AggregateByItemForm.getCustCode();
		Customer customer = orderAggregateLogic.getCustomer(custCode);
		ArrayList<AggregateByItem> totalList = orderAggregateLogic.totalByItem(custCode);
		int sumTotal = 0;
		for (AggregateByItem order : totalList) {
			sumTotal += order.getTotalPrice();
		}
		model.addAttribute("customer", customer);
		model.addAttribute("totalList", totalList);
		model.addAttribute("sumTotal", sumTotal);

		return "300_aggregate/303_01AggregateByItemView";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public String handleBusinessException(Model model, BusinessException e) {
		model.addAttribute("message", e.getMessage());
		if (e.getPage().equals("303_01AggregateByItemView")) {
			model.addAttribute("AggregateByItemForm", new AggregateByItemForm());
			return "300_aggregate/303_01AggregateByItemView";
		} else if (e.getPage().equals("302_01AggregateByMonthView")) {
			model.addAttribute("aggregateByCustomerForm", new AggregateByCustomerForm());
			return "300_aggregate/302_01AggregateByMonthView";
		}
		model.addAttribute("aggregateByCustomerForm", new AggregateByCustomerForm());
		return "300_aggregate/301_01AggregateByYearView";
	}

}
