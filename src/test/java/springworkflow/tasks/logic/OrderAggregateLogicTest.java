package springworkflow.tasks.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.AggregateByCustomer;
import springworkflow.tasks.entity.AggregateByItem;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;
import springworkflow.tasks.mapper.OrderMapper;
import springworkflow.tasks.service.OrderAggregateLogic;
import springworkflow.util.TestCustomer;

public class OrderAggregateLogicTest {
	@Mock
	private OrderMapper orderMapper;
	
	@Mock
	private CustomerMapper customerMapper;

	@InjectMocks
	OrderAggregateLogic sut; // テスト対象クラス

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
	}
	
	//年次集計
	@Test
	@DisplayName("年次集計が成功する場合")
	public void testTotalOfYear() {
		ArrayList<AggregateByCustomer> expected = new ArrayList<>();
		expected.add(new AggregateByCustomer("KA0001","Aストア",13200));
		expected.add(new AggregateByCustomer("KA0015","Oストア",18000));
		Mockito.doReturn(expected).when(orderMapper).totalOfYearOrMonthByCustomer("2021/1/1", "2021/12/31");

		ArrayList<AggregateByCustomer> actual = sut.totalOfYear(2021);
		assertThat(actual).isEqualTo(expected);

		Mockito.verify(orderMapper, Mockito.times(1)).totalOfYearOrMonthByCustomer("2021/1/1", "2021/12/31");
	}

	@Test
	@DisplayName("年次集計が空の場合")
	public void testTotalOfYearEmpty() {
		ArrayList<AggregateByCustomer> expected = new ArrayList<>();
		Mockito.doReturn(expected).when(orderMapper).totalOfYearOrMonthByCustomer("2021/1/1", "2021/12/31");

		Throwable exception = assertThrows(BusinessException.class, () -> {
			sut.totalOfYear(2021);
		});
		assertEquals("該当する受注はありません。", exception.getMessage());

		Mockito.verify(orderMapper, Mockito.times(1)).totalOfYearOrMonthByCustomer("2021/1/1", "2021/12/31");
	}

	//月次集計
	@Test
	@DisplayName("月次集計が成功する場合")
	public void testTotalOfMonth() {
		ArrayList<AggregateByCustomer> expected = new ArrayList<>();
		expected.add(new AggregateByCustomer("KA0001", "Aストア", 13200));
		expected.add(new AggregateByCustomer("KA0015", "Oストア", 18000));
		Mockito.doReturn(expected).when(orderMapper).totalOfYearOrMonthByCustomer("2021/12/1", "2021/12/31");

		ArrayList<AggregateByCustomer> actual = sut.totalOfMonth(2021, 12);
		assertThat(actual).isEqualTo(expected);

		Mockito.verify(orderMapper, Mockito.times(1)).totalOfYearOrMonthByCustomer("2021/12/1", "2021/12/31");
	}

	@Test
	@DisplayName("月次集計が空の場合")
	public void testTotalOfMonthEmpty() {
		ArrayList<AggregateByCustomer> expected = new ArrayList<>();
		Mockito.doReturn(expected).when(orderMapper).totalOfYearOrMonthByCustomer("2021/12/1", "2021/12/31");

		Throwable exception = assertThrows(BusinessException.class, () -> {
			sut.totalOfMonth(2021, 12);
		});
		assertEquals("該当する受注はありません。", exception.getMessage());

		Mockito.verify(orderMapper, Mockito.times(1)).totalOfYearOrMonthByCustomer("2021/12/1", "2021/12/31");
	}

	//得意先別集計
	@Test
	@DisplayName("得意先別集計が成功する場合")
	public void testTotal() {
		ArrayList<AggregateByItem> expected = new ArrayList<>();
		expected.add(new AggregateByItem("AX0001", "ミネラルウォーター（500ml）", 25, 80, 2000));
		expected.add(new AggregateByItem("BX0001", "黄金にんにく・10日分", 20, 1200, 24000));
		expected.add(new AggregateByItem("CX0001", "食べる納豆キナーゼ・10日分", 10, 600, 6000));
		Mockito.doReturn(expected).when(orderMapper).createOrderTotalListByItem("KA0001");

		ArrayList<AggregateByItem> actual = sut.totalByItem("KA0001");
		assertThat(actual).isEqualTo(expected);

		Mockito.verify(orderMapper, Mockito.times(1)).createOrderTotalListByItem("KA0001");
	}

	@Test
	@DisplayName("得意先別集計が空の場合")
	public void testTotalEmpty() {
		ArrayList<AggregateByItem> expected = new ArrayList<>();
		Mockito.doReturn(expected).when(orderMapper).createOrderTotalListByItem("KA0001");

		Throwable exception = assertThrows(BusinessException.class, () -> {
			sut.totalByItem("KA0001");
		});
		assertEquals("該当する受注はありません。", exception.getMessage());

		Mockito.verify(orderMapper, Mockito.times(1)).createOrderTotalListByItem("KA0001");
	}

	@Test
	@DisplayName("顧客検索が成功する場合")
	public void testGetCustomer() {
		Customer expected = TestCustomer.create();
		Mockito.doReturn(expected).when(customerMapper).findIgnoreDeleteFlag("KA0001");

		Customer actual = sut.getCustomer("KA0001");
		assertThat(actual).isEqualTo(expected);

		Mockito.verify(customerMapper, Mockito.times(1)).findIgnoreDeleteFlag("KA0001");

	}

	@Test
	@DisplayName("顧客検索が失敗する場合")
	public void testGetCustomerNotFound() {
		Mockito.doReturn(null).when(customerMapper).findIgnoreDeleteFlag("KA0001");

		Throwable exception = assertThrows(BusinessException.class, () -> {
			sut.getCustomer("KA0001");
		});
		assertEquals("該当する得意先コードは存在しません。", exception.getMessage());

		Mockito.verify(customerMapper, Mockito.times(1)).findIgnoreDeleteFlag("KA0001");

	}
}
