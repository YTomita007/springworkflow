package springworkflow.tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.form.DeleteForm;
import springworkflow.tasks.service.CustomerDeleteLogicIF;

@SessionAttributes(value = "customer")
@Controller
@RequestMapping("/customer/delete")
public class CustomerDeleteController {
	@Autowired
	private CustomerDeleteLogicIF customerDeleteLogic;

	@GetMapping("")
	public String deleteView(Model model) {
		model.addAttribute("deleteForm", new DeleteForm());
		model.addAttribute("customer", null);
		return "200_customer/203_01CustomerDeleteView";
	}

	@PostMapping("/find")
	public String findTarget(@Validated DeleteForm deleteForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "200_customer/203_01CustomerDeleteView";
		}
		Customer customer = customerDeleteLogic.find(deleteForm.getCustCode());
		model.addAttribute("customer", customer);
		return "200_customer/203_01CustomerDeleteView";
	}

	@PostMapping("/delete")
	public String delete(@ModelAttribute("customer") Customer customer, Model model, SessionStatus status,
			RedirectAttributes redirectAttributes) {
		Customer deletedCustomer = customerDeleteLogic.delete(customer.getCustCode());
		redirectAttributes.addFlashAttribute("deletedCustomer", deletedCustomer);
		status.setComplete();

		return "redirect:/customer/delete/result";
	}

	@GetMapping("/result")
	public String result(Model model, @ModelAttribute("deletedCustomer") Customer deletedCustomer) {
		// 引数の @ModelAttribute("deleteResult") Customer deleteCustomer と この addAttribtue はリロード対策
		model.addAttribute("deletedCustomer", deletedCustomer);
		return "200_customer/203_02CustomerDeleteResultView";

	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public String handleSalesBusinessException(Model model, Exception e) {
		model.addAttribute("message", e.getMessage());
		model.addAttribute("deleteForm", new DeleteForm());
		return "200_customer/203_01CustomerDeleteView";
	}

}
