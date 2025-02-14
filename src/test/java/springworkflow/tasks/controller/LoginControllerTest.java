package springworkflow.tasks.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.entity.Employee;
import springworkflow.tasks.service.EmployeeFindLogicIF;
import springworkflow.util.TestCustomer;
import springworkflow.util.TestEmployee;

public class LoginControllerTest {
	@Mock
	private EmployeeFindLogicIF employeeFindLogic;

	@InjectMocks
	private LoginController sut; // テスト対象クラス

	private MockMvc mockMvc;

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("初期画面表示")
	public void testLoginView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/login"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(model().attributeExists("loginForm"));
	}

	@Test
	@DisplayName("ログインに成功した場合")
	public void testLogin() throws Exception {
		Employee employee = TestEmployee.create();
		Mockito.doReturn(employee).when(employeeFindLogic).login(employee.getEmployeeNo(), employee.getPassword());
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.param("employeeNo", employee.getEmployeeNo())
				.param("password", employee.getPassword()))
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(view().name("redirect:/menu"));
	}

	@Test
	@DisplayName("入力にエラーがある場合")
	public void testLoginHasError() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("100_main/101_01Login"))
				.andReturn();
	}

	@Test
	@DisplayName("ログインに失敗した場合")
	public void testLoginError() throws Exception {
		Employee employee = TestEmployee.create();
		Mockito.doThrow(new BusinessException("ログインに失敗しました。")).when(employeeFindLogic).login(Mockito.any(),
				Mockito.any());
		mockMvc.perform(MockMvcRequestBuilders.post("/login")
				.param("employeeNo", employee.getEmployeeNo())
				.param("password", employee.getPassword()))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized())
				.andExpect(view().name("100_main/101_01Login"))
				.andExpect(model().attribute("message", "ログインに失敗しました。"));
	}

	@Test
	@DisplayName("ログアウトの場合")
	public void testLogout() throws Exception {
		Customer customer = TestCustomer.create();

		mockMvc.perform(MockMvcRequestBuilders.get("/logout")
				.sessionAttr("customer", customer))
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(view().name("redirect:/login"))
				.andExpect(request().sessionAttributeDoesNotExist("employee"));
	}
}
