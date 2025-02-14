package springworkflow.tasks.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import springworkflow.tasks.SpringWorkFlowSpringApplication;
import springworkflow.tasks.entity.AggregateByItem;
import springworkflow.util.DbTestExecutionListner;
import springworkflow.util.DbUnitUtil;

/**
 */
@SpringBootTest(classes = SpringWorkFlowSpringApplication.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbTestExecutionListner.class })
public class OrderMapperCreateOrderTotalListByItemTest {

	@Nested
	class Correct {

		@Autowired
		OrderMapper sut; // テスト対象クラス

		// ファイル読み込み用設定
		static final String CUSTOMER_TABLE_NAME = "customer";
		static final String CUSTOMER_NUMBERING_TABLE_NAME = "customer_numbering";
		static final String EMPLOYEE_TABLE_NAME = "employee";
		static final String ITEM_TABLE_NAME = "item";
		static final String ORDERS_TABLE_NAME = "orders";
		static final String ORDER_DETAILS_TABLE_NAME = "order_details";
		static final String TEST_DATA_PATH = "TestData/OrderMapper/createOrderTotalListByItem/";

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.clearTableData(CUSTOMER_TABLE_NAME, CUSTOMER_NUMBERING_TABLE_NAME, EMPLOYEE_TABLE_NAME,
					ITEM_TABLE_NAME, ORDERS_TABLE_NAME, ORDER_DETAILS_TABLE_NAME);
		}

		@Test
		@DisplayName("リストが取得できる場合")
		public void testReturnsList() throws Exception {
			DbUnitUtil.insertData(TEST_DATA_PATH);
			ArrayList<AggregateByItem> expected = new ArrayList<>();
			expected.add(new AggregateByItem("AX0001", "ミネラルウォーター（500ml）", 25, 80, 2000));
			expected.add(new AggregateByItem("BX0001", "黄金にんにく・10日分", 20, 1200, 24000));
			expected.add(new AggregateByItem("CX0001", "食べる納豆キナーゼ・10日分", 10, 600, 6000));
			ArrayList<AggregateByItem> actual = sut.createOrderTotalListByItem("KA0001");
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		@DisplayName("空のリストが取得できる場合")
		public void testReturnsEmpty() throws Exception {
			ArrayList<AggregateByItem> expected = new ArrayList<>();
			ArrayList<AggregateByItem> actual = sut.createOrderTotalListByItem("KA0005");
			assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class DatabaseWrong {

		static final String ORDER_DETAILS_TABLE_NAME = "orders";
		static final String WRONG_TABLE_NAME = "orders2";

		@Autowired
		OrderMapper sut; // テスト対象クラス

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.renameTable(ORDER_DETAILS_TABLE_NAME, WRONG_TABLE_NAME);
		}

		@AfterEach
		public void tearDown() throws Exception {
			DbUnitUtil.renameTable(WRONG_TABLE_NAME, ORDER_DETAILS_TABLE_NAME);
		}

		@Test
		@DisplayName("DB状態が不正な場合")
		public void testNotFoundOrderTable() throws Exception {
			assertThrows(DataAccessException.class, () -> {
				sut.createOrderTotalListByItem("KA0001");
			});
		}

	}
}
