package springworkflow.tasks.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

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

import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.service.CustomerListLogicIF;
import springworkflow.util.TestCustomer;

public class CustomerListControllerTest {
	@Mock
	private CustomerListLogicIF customerListLogic;

	@InjectMocks
	private CustomerListController sut; // テスト対象クラス

	private MockMvc mockMvc;

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("初期画面表示")
	public void testListView() throws Exception {
		Customer customer = TestCustomer.create();
		ArrayList<Customer> customerList = new ArrayList<>();
		customerList.add(customer);
		Mockito.doReturn(customerList).when(customerListLogic).findAll();
		mockMvc.perform(MockMvcRequestBuilders.get("/customer/list"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("200_customer/205_01CustomerListView"))
				.andExpect(model().attribute("customerList", customerList));
	}

}
