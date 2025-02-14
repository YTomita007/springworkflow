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
import springworkflow.tasks.form.ChangeForm;
import springworkflow.tasks.form.ChangeTargetForm;
import springworkflow.tasks.service.CustomerChangeLogicIF;

@SessionAttributes(value = { "custCode" })
@Controller
@RequestMapping("/customer/change")
public class CustomerChangeController {
	@Autowired
	private CustomerChangeLogicIF customerChangeLogic;

	@GetMapping("")
	public String change(Model model) {
		model.addAttribute("changeTargetForm", new ChangeTargetForm());
		model.addAttribute("changeForm", null);
		model.addAttribute("custCode", null);
		return "200_customer/204_01CustomerChangeView";
	}

	@RequestMapping("/find")
	public String findTarget(@Validated ChangeTargetForm changeTargetForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "200_customer/204_01CustomerChangeView";
		}
		String custCode = changeTargetForm.getCustCode();
		Customer customer = customerChangeLogic.find(custCode);

		ChangeForm changeForm = new ChangeForm();
		changeForm.setCustName(customer.getCustName());
		changeForm.setTelNo(customer.getTelNo());
		changeForm.setPostalCode(customer.getPostalCode());
		changeForm.setAddress(customer.getAddress());
		changeForm.setDiscountRate(customer.getDiscountRate());
		model.addAttribute("changeTargetForm", null);
		model.addAttribute("changeForm", changeForm);
		model.addAttribute("custCode", custCode);
		return "200_customer/204_01CustomerChangeView";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute("custCode") String custCode, @Validated ChangeForm changeForm,
			BindingResult result, Model model, SessionStatus status, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "200_customer/204_01CustomerChangeView";
		}

		Customer customer = new Customer(custCode, changeForm.getCustName(), changeForm.getTelNo(),
				changeForm.getPostalCode(), changeForm.getAddress(), changeForm.getDiscountRate());

		customerChangeLogic.update(customer);

		// redirectする場合には このaddFlashAttributeを使う
		redirectAttributes.addFlashAttribute("customer", customer);
		status.setComplete();

		return "redirect:/customer/change/result";
	}

	@GetMapping("/result")
	public String result(Model model, @ModelAttribute("customer") Customer customer) {
		// 引数の @ModelAttribute("customer") Customer customer と この addAttribtue はリロード対策
		model.addAttribute("customer", customer);
		return "200_customer/204_02CustomerChangeResultView";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public String handleSalesBusinessException(Model model, Exception e) {
		model.addAttribute("changeTargetForm", new ChangeTargetForm());
		model.addAttribute("message", e.getMessage());
		return "200_customer/204_01CustomerChangeView";
	}

}
