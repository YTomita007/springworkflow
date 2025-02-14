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

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.AggregateByCustomer;
import springworkflow.tasks.entity.AggregateByItem;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.service.OrderAggregateLogicIF;
import springworkflow.util.TestAggregateByCustomer;
import springworkflow.util.TestAggregateByItem;
import springworkflow.util.TestCustomer;

public class AggregateOrderControllerTest {
	@Mock
	private OrderAggregateLogicIF orderAggregateLogic;

	@InjectMocks
	private AggregateOrderController sut; // テスト対象クラス

	private MockMvc mockMvc;

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
	}

	@Test
	@DisplayName("年次集計画面表示")
	public void testFindByYearView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/aggregate/year"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/301_01AggregateByYearView"))
				.andExpect(model().attributeExists("aggregateByCustomerForm"));
	}

	@Test
	@DisplayName("年次集計検索ができる場合")
	public void testFindByYearResultView() throws Exception {
		ArrayList<AggregateByCustomer> orderList = TestAggregateByCustomer.createArrayList();
		int sumTotal = 10;
		Integer year = 2022;
		Mockito.doReturn(orderList).when(orderAggregateLogic).totalOfYear(Mockito.anyInt());
		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/year")
				.param("year", year.toString())
				.param("month", "01"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/301_01AggregateByYearView"))
				.andExpect(model().attributeExists("aggregateByCustomerForm"))
				.andExpect(model().attribute("orderList", orderList))
				.andExpect(model().attribute("sumTotal", sumTotal));
	}

	@Test
	@DisplayName("年次集計検索の入力にエラーがある場合")
	public void testFindByYearResultHasError() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/year")
				.param("year", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/301_01AggregateByYearView"))
				.andExpect(model().attributeExists("aggregateByCustomerForm"))
				.andExpect(model().hasErrors());
	}

	@Test
	@DisplayName("年次集計結果が空の場合")
	public void testFindByYearResultEmpty() throws Exception {
		Mockito.doThrow(new BusinessException("該当する受注はありません。", "301_01AggregateByYearView")).when(orderAggregateLogic)
				.totalOfYear(Mockito.anyInt());
		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/year")
				.param("year", "2018")
				.param("month", "01"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("300_aggregate/301_01AggregateByYearView"))
				.andExpect(model().attributeExists("aggregateByCustomerForm"))
				.andExpect(model().attribute("message", "該当する受注はありません。"));
	}

	@Test
	@DisplayName("月次集計初期画面表示")
	public void testFindByMonthView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/aggregate/month"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/302_01AggregateByMonthView"))
				.andExpect(model().attributeExists("aggregateByCustomerForm"));
	}

	@Test
	@DisplayName("月次集計検索ができる場合")
	public void testFindByMonthResultView() throws Exception {
		ArrayList<AggregateByCustomer> orderList = TestAggregateByCustomer.createArrayList();
		int sumTotal = 10;
		Integer year = 2022;
		Integer month = 12;
		Mockito.doReturn(orderList).when(orderAggregateLogic).totalOfMonth(Mockito.anyInt(), Mockito.anyInt());
		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/month")
				.param("year", year.toString())
				.param("month", month.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/302_01AggregateByMonthView"))
				.andExpect(model().attributeExists("aggregateByCustomerForm"))
				.andExpect(model().attribute("orderList", orderList))
				.andExpect(model().attribute("sumTotal", sumTotal));

	}

	@Test
	@DisplayName("月次集計検索の入力にエラーがある場合")
	public void testFindByMonthResultHasError() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/month")
				.param("year", "")
				.param("month", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/302_01AggregateByMonthView"))
				.andExpect(model().attributeExists("aggregateByCustomerForm"))
				.andExpect(model().hasErrors());
	}

	@Test
	@DisplayName("月次集計検索結果が空の場合")
	public void testFindByMonthResultEmpty() throws Exception {
		Mockito.doThrow(new BusinessException("該当する受注はありません。", "302_01AggregateByMonthView")).when(orderAggregateLogic)
				.totalOfMonth(Mockito.anyInt(), Mockito.anyInt());
		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/month")
				.param("year", "2018")
				.param("month", "12"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("300_aggregate/302_01AggregateByMonthView"))
				.andExpect(model().attributeExists("aggregateByCustomerForm"))
				.andExpect(model().attribute("message", "該当する受注はありません。"));
	}

	@Test
	@DisplayName("商品別検索初期画面表示")
	public void testFindByItemView() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/aggregate/item"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/303_01AggregateByItemView"))
				.andExpect(model().attributeExists("AggregateByItemForm"));
	}

	@Test
	@DisplayName("商品別検索ができる場合")
	public void testFindByItemResult() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doReturn(customer).when(orderAggregateLogic).getCustomer(customer.getCustCode());
		ArrayList<AggregateByItem> totalList = TestAggregateByItem.createArrayList();
		Mockito.doReturn(totalList).when(orderAggregateLogic).totalByItem(customer.getCustCode());

		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/item")
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/303_01AggregateByItemView"))
				.andExpect(model().attributeExists("AggregateByItemForm"))
				.andExpect(model().attribute("customer", customer))
				.andExpect(model().attribute("totalList", totalList))
				.andExpect(model().attribute("sumTotal", 2000))
				.andReturn();
		Mockito.verify(orderAggregateLogic, Mockito.times(1)).totalByItem(customer.getCustCode());
	}

	@Test
	@DisplayName("商品別検索の入力にエラーがある場合")
	public void testFindByItemHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/item")
				.param("custCode", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("300_aggregate/303_01AggregateByItemView"))
				.andExpect(model().attributeExists("AggregateByItemForm"))
				.andExpect(model().hasErrors());
		Mockito.verify(orderAggregateLogic, Mockito.times(0)).totalByItem(Mockito.any());
	}

	@Test
	@DisplayName("得意先が見つからない場合")
	public void testFindByItemResultNotFound() throws Exception {
		Customer customer = TestCustomer.create();
		Mockito.doThrow(new BusinessException("該当する得意先コードは存在しません。", "303_01AggregateByItemView")).when(orderAggregateLogic)
				.getCustomer(customer.getCustCode());
		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/item")
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("300_aggregate/303_01AggregateByItemView"))
				.andExpect(model().attributeExists("AggregateByItemForm"))
				.andExpect(model().attribute("message", "該当する得意先コードは存在しません。"))
				.andReturn();
		Mockito.verify(orderAggregateLogic, Mockito.times(0)).totalByItem(Mockito.any());
	}

	@Test
	@DisplayName("商品別検索結果が空の場合")
	public void testFindByItemResultEmpty() throws Exception {
		Customer customer = TestCustomer.create2();
		Mockito.doReturn(customer).when(orderAggregateLogic).getCustomer(customer.getCustCode());
		Mockito.doThrow(new BusinessException("該当する受注はありません。", "303_01AggregateByItemView")).when(orderAggregateLogic)
				.totalByItem(customer.getCustCode());

		mockMvc.perform(MockMvcRequestBuilders.post("/aggregate/item")
				.param("custCode", customer.getCustCode()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(view().name("300_aggregate/303_01AggregateByItemView"))
				.andExpect(model().attributeExists("AggregateByItemForm"))
				.andExpect(model().attribute("message", "該当する受注はありません。"))
				.andReturn();
		Mockito.verify(orderAggregateLogic, Mockito.times(1)).totalByItem(Mockito.any());
	}

}
