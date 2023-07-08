package springworkflow.tasks.controller;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.http.HttpServletResponse;
import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Employee;
import springworkflow.tasks.form.LoginForm;
import springworkflow.tasks.service.EmployeeFindLogicIF;

@SessionAttributes(value = "employee")
@RequestMapping("")
@Controller
public class LoginController {
	@Autowired
	private EmployeeFindLogicIF employeeFindLogic;

	@GetMapping("/login")
	public String home(Model model) {
		LoginForm form = new LoginForm();
		model.addAttribute("loginForm", form);

		return "100_main/101_01Login";
	}

	@PostMapping("/login")
	public String login(@Validated LoginForm loginForm, BindingResult result, Model model,
			HttpServletResponse response) {
		if (result.hasErrors()) {
			return "100_main/101_01Login";
		}
		Employee employee;
		employee = employeeFindLogic.login(loginForm.getEmployeeNo(), loginForm.getPassword());
		model.addAttribute("employee", employee);

		return "redirect:/menu";
	}

	@GetMapping("/logout")
	public String logout(Model model, SessionStatus sessionStatus) {
		model.addAttribute("loginForm", new LoginForm());
		model.addAttribute("notifyMessage", "ログアウトしました");
		sessionStatus.setComplete();
		return "redirect:/login";
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(BusinessException.class)
	public String handleBusinessException(Model model, Exception e) {
		model.addAttribute("message", e.getMessage());
		model.addAttribute("loginForm", new LoginForm());
		return "100_main/101_01Login";
	}
}
