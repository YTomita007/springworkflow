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
import springworkflow.tasks.form.ChangeForm;
import springworkflow.tasks.service.CustomerChangeLogicIF;
import springworkflow.util.TestCustomer;

public class CustomerChangeControllerTest {
	@Mock
	private CustomerChangeLogicIF customerChangeLogic;

	@InjectMocks
	private CustomerChangeController sut; // テスト対象クラス

	private MockMvc mockMvc;

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("初期画面表示")
	public void testChangeView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/customer/change"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/204_01CustomerChangeView"))
				.andExpect(model().attributeExists("changeTargetForm"))
				.andExpect(model().attributeDoesNotExist("changeForm"))
				.andExpect(model().attributeDoesNotExist("custCode"));
	}

	@Test
	@DisplayName("正常に検索ができる場合")
	public void testFindTarget() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(customerChangeLogic).find(customer.getCustCode());
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customer/change/find")
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/204_01CustomerChangeView"))
				.andExpect(model().attributeDoesNotExist("changeTargetForm"))
				.andExpect(model().attribute("custCode", customer.getCustCode()))
				.andReturn();
		ChangeForm form = (ChangeForm) result.getModelAndView().getModel().get("changeForm");
		assertThat(form.getAddress()).isEqualTo(customer.getAddress());
		assertThat(form.getCustName()).isEqualTo(customer.getCustName());
		assertThat(form.getDiscountRate()).isEqualTo(customer.getDiscountRate());
		assertThat(form.getPostalCode()).isEqualTo(customer.getPostalCode());
		assertThat(form.getTelNo()).isEqualTo(customer.getTelNo());
		Mockito.verify(customerChangeLogic, Mockito.times(1)).find(customer.getCustCode());
	}

	@Test
	@DisplayName("入力にエラーがある場合")
	public void testFindTargetHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/change/find")
				.param("custCode", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/204_01CustomerChangeView"))
				.andExpect(model().attributeExists("changeTargetForm"))
				.andExpect(model().attributeDoesNotExist("changeForm"))
				.andExpect(model().attributeDoesNotExist("custCode"));
	}

	@Test
	@DisplayName("得意先が見つからない場合")
	public void testFindTargetNotFound() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doThrow(new BusinessException("該当する得意先コードは存在しません。")).when(customerChangeLogic)
				.find(customer.getCustCode());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/change/find")
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("200_customer/204_01CustomerChangeView"))
				.andExpect(model().attributeExists("changeTargetForm"))
				.andExpect(model().attributeDoesNotExist("changeForm"))
				.andExpect(model().attributeDoesNotExist("custCode"))
				.andExpect(model().attribute("message", "該当する得意先コードは存在しません。"))
				.andReturn();
		Mockito.verify(customerChangeLogic, Mockito.times(1)).find(customer.getCustCode());
	}

	@Test
	@DisplayName("更新が成功した場合")
	public void testUpdate() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(customerChangeLogic).update(Mockito.any());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/change/update")
				.param("custName", customer.getCustName())
				.param("telNo", customer.getTelNo())
				.param("postalCode", customer.getPostalCode())
				.param("address", customer.getAddress())
				.param("discountRate", ((Integer) customer.getDiscountRate()).toString())
				.sessionAttr("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(view().name("redirect:/customer/change/result"));
	}

	@Test
	@DisplayName("更新成功後にリダイレクトした場合")
	public void testResult() throws Exception {
		Customer customer = TestCustomer.create();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customer/change/result")
				.flashAttr("customer", customer))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/204_02CustomerChangeResultView"))
				.andExpect(model().attribute("customer", customer))
				.andReturn();
		Customer actualCustomer = (Customer) result.getModelAndView().getModel().get("customer");
		assertThat(actualCustomer.getAddress()).isEqualTo(customer.getAddress());
		assertThat(actualCustomer.getCustName()).isEqualTo(customer.getCustName());
		assertThat(actualCustomer.getDiscountRate()).isEqualTo(customer.getDiscountRate());
		assertThat(actualCustomer.getPostalCode()).isEqualTo(customer.getPostalCode());
		assertThat(actualCustomer.getTelNo()).isEqualTo(customer.getTelNo());
	}

	@Test
	@DisplayName("更新時にエラーがあった場合")
	public void testUpdateHasError() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(customerChangeLogic).update(Mockito.any());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/change/update")
				.param("custName", "")
				.param("telNo", "")
				.param("postalCode", "")
				.param("address", "")
				.param("discountRate", "")
				.sessionAttr("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/204_01CustomerChangeView"))
				.andExpect(model().hasErrors());
	}

	@Test
	@DisplayName("更新対象が見つからない場合")
	public void testUpdateNotFound() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doThrow(new BusinessException("該当する得意先コードは存在しません。")).when(customerChangeLogic)
				.update(Mockito.any());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/change/update")
				.param("custName", customer.getCustName())
				.param("telNo", customer.getTelNo())
				.param("postalCode", customer.getPostalCode())
				.param("address", customer.getAddress())
				.param("discountRate", ((Integer) customer.getDiscountRate()).toString())
				.sessionAttr("custCode", customer.getCustCode())
				.sessionAttr("customer", customer))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("200_customer/204_01CustomerChangeView"))
				.andExpect(model().attributeDoesNotExist("customer"))
				.andExpect(model().attribute("message", "該当する得意先コードは存在しません。"));
	}

	@Test
	@DisplayName("更新に失敗した場合")
	public void testUpdateFail() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doThrow(new BusinessException("得意先の変更に失敗しました。")).when(customerChangeLogic)
				.update(Mockito.any());
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/change/update")
				.param("custName", customer.getCustName())
				.param("telNo", customer.getTelNo())
				.param("postalCode", customer.getPostalCode())
				.param("address", customer.getAddress())
				.param("discountRate", ((Integer) customer.getDiscountRate()).toString())
				.sessionAttr("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("200_customer/204_01CustomerChangeView"))
				.andExpect(model().attributeDoesNotExist("customer"))
				.andExpect(model().attribute("message", "得意先の変更に失敗しました。"));
	}

}
