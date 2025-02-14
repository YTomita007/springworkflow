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

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.service.CustomerFindLogicIF;
import springworkflow.util.TestCustomer;

public class CustomerFindControllerTest {

	@Mock
	private CustomerFindLogicIF customerFindLogic;

	@InjectMocks
	private CustomerFindController sut; // テスト対象クラス

	private MockMvc mockMvc;

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("初期画面表示")
	public void testFindView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/customer/find"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/201_01CustomerFindView"))
				.andExpect(model().attributeExists("findForm"));
	}

	@Test
	@DisplayName("正常に検索ができる場合")
	public void testFindTarget() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(customerFindLogic).find(customer.getCustCode());
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customer/find")
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/201_02CustomerFindResultView"))
				.andExpect(model().attributeExists("findForm"))
				.andReturn();
		Customer actualCustomer = (Customer) result.getModelAndView().getModel().get("customer");
		assertThat(actualCustomer.getAddress()).isEqualTo(customer.getAddress());
		assertThat(actualCustomer.getCustName()).isEqualTo(customer.getCustName());
		assertThat(actualCustomer.getDiscountRate()).isEqualTo(customer.getDiscountRate());
		assertThat(actualCustomer.getPostalCode()).isEqualTo(customer.getPostalCode());
		assertThat(actualCustomer.getTelNo()).isEqualTo(customer.getTelNo());
		Mockito.verify(customerFindLogic, Mockito.times(1)).find(customer.getCustCode());
	}

	@Test
	@DisplayName("入力にエラーがある場合")
	public void testFindTargetHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/find")
				.param("custCode", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/201_01CustomerFindView"))
				.andExpect(model().attributeExists("findForm"));
	}

	@Test
	@DisplayName("得意先が見つからない場合")
	public void testFindTargetNotFound() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doThrow(new BusinessException("該当する得意先コードは存在しません。")).when(customerFindLogic)
				.find(customer.getCustCode());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/find")
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("200_customer/201_01CustomerFindView"))
				.andExpect(model().attributeExists("findForm"))
				.andExpect(model().attribute("message", "該当する得意先コードは存在しません。"))
				.andReturn();
		Mockito.verify(customerFindLogic, Mockito.times(1)).find(customer.getCustCode());
	}

}
