package springworkflow.tasks.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.service.CustomerRegisterLogicIF;
import springworkflow.util.TestCustomer;

public class CustomerRegisterControllerTest {
	@Mock
	private CustomerRegisterLogicIF customerRegisterLogic;

	@InjectMocks
	private CustomerRegisterController sut; // テスト対象クラス

	private MockMvc mockMvc;

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("初期画面表示")
	public void testRegisterView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/customer/register"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/202_01CustomerRegisterView"))
				.andExpect(model().attributeExists("registerForm"));
	}

	@Test
	@DisplayName("登録に成功した場合")
	public void testRegister() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(customerRegisterLogic).register(Mockito.any());
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customer/register")
				.param("custName", customer.getCustName())
				.param("telNo", customer.getTelNo())
				.param("postalCode", customer.getPostalCode())
				.param("address", customer.getAddress())
				.param("discountRate", ((Integer) customer.getDiscountRate()).toString()))
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(view().name("redirect:/customer/register/result"))
				.andReturn();
		Customer actualCustomer = (Customer) result.getFlashMap().get("customer");
		assertThat(actualCustomer.getAddress()).isEqualTo(customer.getAddress());
		assertThat(actualCustomer.getCustName()).isEqualTo(customer.getCustName());
		assertThat(actualCustomer.getDiscountRate()).isEqualTo(customer.getDiscountRate());
		assertThat(actualCustomer.getPostalCode()).isEqualTo(customer.getPostalCode());
		assertThat(actualCustomer.getTelNo()).isEqualTo(customer.getTelNo());
		Mockito.verify(customerRegisterLogic, Mockito.times(1)).register(Mockito.any());
	}

	@Test
	@DisplayName("削除成功後にリダイレクトした場合")
	public void testResult() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(customerRegisterLogic).register(Mockito.any());
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customer/register/result")
				.flashAttr("customer", customer))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/202_02CustomerRegisterResultView"))
				.andReturn();

		Customer actualCustomer = (Customer) result.getModelAndView().getModel().get("customer");
		assertThat(actualCustomer.getAddress()).isEqualTo(customer.getAddress());
		assertThat(actualCustomer.getCustName()).isEqualTo(customer.getCustName());
		assertThat(actualCustomer.getDiscountRate()).isEqualTo(customer.getDiscountRate());
		assertThat(actualCustomer.getPostalCode()).isEqualTo(customer.getPostalCode());
		assertThat(actualCustomer.getTelNo()).isEqualTo(customer.getTelNo());

	}

	@Test
	@DisplayName("登録時にエラーがあった場合")
	public void testRegisterHasError() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/register")
				.param("custName", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/202_01CustomerRegisterView"))
				.andExpect(model().attributeExists("registerForm"))
				.andExpect(model().hasErrors());
	}

}
