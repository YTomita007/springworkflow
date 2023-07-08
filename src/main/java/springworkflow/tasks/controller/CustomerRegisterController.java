package springworkflow.tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.form.RegisterForm;
import springworkflow.tasks.service.CustomerRegisterLogicIF;

@Controller
@RequestMapping("/customer/register")
public class CustomerRegisterController {
	@Autowired
	private CustomerRegisterLogicIF customerRegisterLogic;

	@GetMapping("")
	public String register(Model model) {
		model.addAttribute("registerForm", new RegisterForm());

		return "200_customer/202_01CustomerRegisterView";
	}

	@PostMapping("")
	public String registerResult(@Validated RegisterForm form, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "200_customer/202_01CustomerRegisterView";
		}
		Customer formCustomer = new Customer();
		formCustomer.setCustName(form.getCustName());
		formCustomer.setPostalCode(form.getPostalCode());
		formCustomer.setTelNo(form.getTelNo());
		formCustomer.setAddress(form.getAddress());
		formCustomer.setDiscountRate(form.getDiscountRate());

		Customer newCustomer = customerRegisterLogic.register(formCustomer);
		redirectAttributes.addFlashAttribute("customer", newCustomer);

		return "redirect:/customer/register/result";
	}

	@GetMapping("/result")
	public String result(Model model, @ModelAttribute("customer") Customer customer) {
		model.addAttribute("customer", customer);
		return "200_customer/202_02CustomerRegisterResultView";
	}
}
