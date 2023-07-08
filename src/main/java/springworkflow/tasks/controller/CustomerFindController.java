package springworkflow.tasks.controller;

import java.util.ArrayList;

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
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.form.FindForm;
import springworkflow.tasks.service.CustomerFindLogicIF;

@Controller
@RequestMapping("/customer/find")
public class CustomerFindController {
	@Autowired
	private CustomerFindLogicIF customerFindLogic;

	@GetMapping("")
	public String find(Model model) {
		model.addAttribute("findForm", new FindForm());

		return "200_customer/201_01CustomerFindView";
	}

	@PostMapping("")
	public String findResult(@Validated FindForm findForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("message", "得意先コード、電話番号のどちらかを入力してください");
			return "200_customer/201_01CustomerFindView";
		}
		Customer customer;
		if (findForm.getCustCode().equals("")) {
			ArrayList<Customer> customers = customerFindLogic.findByTelNo(findForm.getTelNo());
			model.addAttribute("customers", customers);
			return "200_customer/201_03CustomerFindResultView";
		}
		customer = customerFindLogic.find(findForm.getCustCode());
		model.addAttribute("customer", customer);

		return "200_customer/201_02CustomerFindResultView";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public String handleBusinessException(Model model, Exception e) {
		model.addAttribute("message", e.getMessage());
		model.addAttribute("findForm", new FindForm());
		return "200_customer/201_01CustomerFindView";
	}
}
