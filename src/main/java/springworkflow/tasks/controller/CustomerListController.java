package springworkflow.tasks.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.service.CustomerListLogicIF;

@Controller
@RequestMapping("/customer/list")
public class CustomerListController {
	@Autowired
	private CustomerListLogicIF customerListLogic;

	@GetMapping("")
	public String list(Model model) {
		ArrayList<Customer> customers = customerListLogic.findAll();
		model.addAttribute("customerList", customers);

		return "200_customer/205_01CustomerListView";
	}

	@GetMapping(params = { "column", "sortType" })
	public String sort(Model model, String column, String sortType) {
		ArrayList<Customer> sortedList = customerListLogic.sortedList(column, sortType);
		model.addAttribute("customerList", sortedList);

		return "200_customer/205_01CustomerListView";
	}

	// 設計上このcontrollerはSalesBusinessExceptionのハンドリングは無しになるはず
}
