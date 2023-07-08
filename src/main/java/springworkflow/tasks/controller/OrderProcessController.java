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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.entity.Employee;
import springworkflow.tasks.entity.Item;
import springworkflow.tasks.entity.ItemWithAmount;
import springworkflow.tasks.form.OrderDetailForm;
import springworkflow.tasks.form.OrderPurchaseForm;
import springworkflow.tasks.service.CustomerFindLogicIF;
import springworkflow.tasks.service.CustomerListLogicIF;
import springworkflow.tasks.service.EmployeeFindLogicIF;
import springworkflow.tasks.service.EmployeeListLogicIF;
import springworkflow.tasks.service.OrderProcessLogicIF;

@SessionAttributes(types = ArrayList.class)
@Controller
@RequestMapping("/order")
public class OrderProcessController {

	@Autowired
	private OrderProcessLogicIF orderProcessLogic;

	@Autowired
	private EmployeeFindLogicIF employeeFindLogic;

	@Autowired
	private EmployeeListLogicIF employeeListLogic;

	@Autowired
	private CustomerFindLogicIF customerFindLogic;

	@Autowired
	private CustomerListLogicIF customerListLogic;

	@ModelAttribute(value = "shoppingCart")
	public ArrayList<ItemWithAmount> initList() {
		return new ArrayList<ItemWithAmount>();
	}

	@GetMapping("")
	public String orderForm(
			@ModelAttribute("shoppingCart") ArrayList<ItemWithAmount> shoppingCart,
			Model model) {
		model.addAttribute("orderDetailForm", new OrderDetailForm());

		ArrayList<Item> items = orderProcessLogic.getAllItems();
		model.addAttribute("items", items);
		model.addAttribute("notifyMessage", "");
		model.addAttribute("shoppingCart", new ArrayList<ItemWithAmount>());

		return "500_order/500_01OrderPurchaseForItems";
	}

	@PostMapping("/shoppingCart")
	public String addToShoppingCart(
			@ModelAttribute("shoppingCart") ArrayList<ItemWithAmount> shoppingCart,
			@Validated OrderDetailForm orderDetailForm, BindingResult result, Model model) {
		ArrayList<Item> items = orderProcessLogic.getAllItems();
		model.addAttribute("items", items);
		if (result.hasErrors()) {
			return "500_order/500_01OrderPurchaseForItems";
		}

		ItemWithAmount item = new ItemWithAmount(orderProcessLogic.getItem(orderDetailForm.getItemCode()),
				orderDetailForm.getAmount());

		model.addAttribute("shoppingCart", shoppingCart.add(item));

		model.addAttribute("notifyMessage",
				"ショッピングカートに" + item.getItemName() + "を" + orderDetailForm.getAmount() + "（個）格納しました。");

		return "500_order/500_01OrderPurchaseForItems";
	}

	@GetMapping("/purchase")
	public String purchase(
			@ModelAttribute("shoppingCart") ArrayList<ItemWithAmount> shoppingCart,
			Model model) {
		if (shoppingCart.size() == 0) {
			ArrayList<Item> items = orderProcessLogic.getAllItems();
			model.addAttribute("items", items);
			model.addAttribute("orderDetailForm", new OrderDetailForm());
			model.addAttribute("message", "ショッピングカートは空です");
			return "500_order/500_01OrderPurchaseForItems";
		}

		OrderPurchaseForm orderPurchaseForm = orderProcessLogic.checkShoppingCart(shoppingCart);

		ArrayList<Employee> empList = employeeListLogic.findAll();
		ArrayList<Customer> custList = customerListLogic.findAll();

		model.addAttribute("empList", empList);
		model.addAttribute("custList", custList);
		model.addAttribute("orderPurchaseForm", orderPurchaseForm);
		return "500_order/500_02OrderPurchaseTotal";
	}

	@PostMapping("/confirm")
	public String confirm(
			@ModelAttribute("shoppingCart") ArrayList<ItemWithAmount> shoppingCart,
			@Validated OrderPurchaseForm orderPurchaseForm,
			BindingResult result, Model model) {
		if (shoppingCart.size() == 0) {
			ArrayList<Item> items = orderProcessLogic.getAllItems();
			model.addAttribute("items", items);
			model.addAttribute("orderDetailForm", new OrderDetailForm());
			model.addAttribute("message", "ショッピングカートは空です");
			return "500_order/500_01OrderPurchaseForItems";
		}

		orderProcessLogic.orderPurchase(orderPurchaseForm, shoppingCart);

		Customer customer = customerFindLogic.find(orderPurchaseForm.getCustCode());
		Employee employee = employeeFindLogic.find(orderPurchaseForm.getEmployeeNo());

		model.addAttribute("customer", customer);
		model.addAttribute("employee", employee);
		model.addAttribute("orderPurchaseForm", orderPurchaseForm);
		model.addAttribute("notifyMessage", "注文が確定しました");
		return "500_order/500_03OrderPurchageConfirmed";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException.class)
	public String handleBusinessException(Model model, Exception e) {
		model.addAttribute("message", e.getMessage());
		model.addAttribute("orderTotalByItemForm", new OrderDetailForm());
		return "500_order/500_01OrderPurchaseForItems";
	}

}
