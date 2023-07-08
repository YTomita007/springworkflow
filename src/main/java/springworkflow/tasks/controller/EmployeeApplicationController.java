package springworkflow.tasks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee/application")
public class EmployeeApplicationController {

	@GetMapping("")
	public String fullCalender(Model model) {

		return "400_employee/403_01ApplicationCalender";
	}
}
