package springworkflow.tasks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
	@GetMapping("/menu")
	public String login(Model model) {
		return "100_main/101_02MainMenu";
	}

	@GetMapping("/customer/menu")
	public String customerManagementMenu(Model model) {
		return "100_main/102_01CustomerManagementMenu";
	}

	@GetMapping("/aggregate/menu")
	public String orderAggregateMenu(Model model) {
		return "100_main/103_01AggregateOrderMenu";
	}

	@GetMapping("/employee/menu")
	public String employeeMenu(Model model) {
		return "100_main/104_01EmployeeMenu";
	}

	@GetMapping("/order/menu")
	public String orderProcess(Model model) {
		return "redirect:/order";
	}

}