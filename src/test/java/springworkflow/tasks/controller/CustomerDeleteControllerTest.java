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
import springworkflow.tasks.service.CustomerDeleteLogicIF;
import springworkflow.util.TestCustomer;

public class CustomerDeleteControllerTest {
	@Mock
	private CustomerDeleteLogicIF customerDeleteLogic;

	@InjectMocks
	private CustomerDeleteController sut;

	private MockMvc mockMvc;

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("初期画面表示")
	public void testDeleteView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/customer/delete"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/203_01CustomerDeleteView"))
				.andExpect(model().attributeExists("deleteForm"))
				.andExpect(model().attributeDoesNotExist("customer"));
	}

	@Test
	@DisplayName("正常に検索ができる場合")
	public void testFindTarget() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(customerDeleteLogic).find(customer.getCustCode());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/delete/find")
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/203_01CustomerDeleteView"))
				.andExpect(model().attribute("customer", customer));
		Mockito.verify(customerDeleteLogic, Mockito.times(1)).find(customer.getCustCode());

	}

	@Test
	@DisplayName("入力にエラーがある場合")
	public void testFindTargetHasError() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/delete/find")
				.param("custCode", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/203_01CustomerDeleteView"))
				.andExpect(model().hasErrors());
	}

	@Test
	@DisplayName("得意先が見つからない場合")
	public void testFindTargetNotFound() throws Exception {
		String custCode = "KA0001";
		Mockito.doThrow(new BusinessException("該当する得意先コードは存在しません。")).when(customerDeleteLogic).find(custCode);
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/delete/find")
				.param("custCode", custCode))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("200_customer/203_01CustomerDeleteView"))
				.andExpect(model().attributeDoesNotExist("customer"));
		Mockito.verify(customerDeleteLogic, Mockito.times(1)).find(custCode);
	}

	@Test
	@DisplayName("削除が成功した場合")
	public void testDelete() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(customerDeleteLogic).delete(customer.getCustCode());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/delete/delete")
				.sessionAttr("customer", customer)
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(view().name("redirect:/customer/delete/result"));
		Mockito.verify(customerDeleteLogic, Mockito.times(1)).delete(customer.getCustCode());
	}

	@Test
	@DisplayName("削除成功後にリダイレクトした場合")
	public void testResult() throws Exception {
		Customer customer = TestCustomer.create();
		mockMvc.perform(MockMvcRequestBuilders.get("/customer/delete/result")
				.flashAttr("deletedCustomer", customer))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/203_02CustomerDeleteResultView"))
				.andExpect(model().attribute("deletedCustomer", customer))
				.andExpect(request().sessionAttributeDoesNotExist("customer"));
	}

	@Test
	@DisplayName("削除対象が見つからない場合")
	public void testDeleteNotFound() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doThrow(new BusinessException("該当する得意先コードは存在しません。")).when(customerDeleteLogic)
				.delete(customer.getCustCode());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/delete/delete")
				.param("custCode", customer.getCustCode())
				.sessionAttr("customer", customer))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("200_customer/203_01CustomerDeleteView"))
				.andExpect(model().attributeDoesNotExist("customer"));
		Mockito.verify(customerDeleteLogic, Mockito.times(1)).delete(customer.getCustCode());
	}

	@Test
	@DisplayName("削除時にエラーがあった場合")
	public void testDeleteFail() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doThrow(new BusinessException("得意先の削除に失敗しました。")).when(customerDeleteLogic)
				.delete(customer.getCustCode());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/delete/delete")
				.param("custCode", customer.getCustCode())
				.sessionAttr("customer", customer))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("200_customer/203_01CustomerDeleteView"))
				.andExpect(model().attributeDoesNotExist("customer"));
		Mockito.verify(customerDeleteLogic, Mockito.times(1)).delete(customer.getCustCode());
	}

}
