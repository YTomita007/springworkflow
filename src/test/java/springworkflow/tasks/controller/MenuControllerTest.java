package springworkflow.tasks.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class MenuControllerTest {
	@InjectMocks
	private MenuController sut; // テスト対象クラス

	private MockMvc mockMvc;

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("メインメニュー画面表示")
	public void testTopMenuView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/menu"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("100_main/101_02MainMenu"));
	}

	@Test
	@DisplayName("得意先管理メニュー画面表示")
	public void testCustomerMenuView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/customer/menu"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("100_main/102_01CustomerManagementMenu"));
	}

	@Test
	@DisplayName("集計管理メニュー画面表示")
	public void testAggregateOrderMenuView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/aggregate/menu"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("100_main/103_01AggregateOrderMenu"));
	}

	@Test
	@DisplayName("従業員メニュー画面表示")
	public void testEmployeeMenuView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/employee/menu"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("100_main/104_01EmployeeMenu"));
	}

	@Test
	@DisplayName("注文購入画面表示")
	public void testOrderPurchaseForItemsView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/order/menu"))
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(view().name("redirect:/order"));
	}
}