package springworkflow.tasks.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.dbunit.Assertion;
import org.dbunit.dataset.SortedTable;
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
import springworkflow.tasks.entity.Customer;
import springworkflow.util.DbTestExecutionListner;
import springworkflow.util.DbUnitUtil;

/**
 */
@SpringBootTest(classes = SpringWorkFlowSpringApplication.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbTestExecutionListner.class })
public class CustomerMapperUpdateTest {

	@Nested
	class Correct {

		@Autowired
		CustomerMapper sut; // テスト対象クラス

		// ファイル読み込み用設定
		static final String CUSTOMER_TABLE_NAME = "customer";
		static final String ORDERS_TABLE_NAME = "orders";
		static final String ORDER_DETAILS_TABLE_NAME = "order_details";
		static final String SETUP_TEST_DATA_PATH = "TestData/CustomerMapper/update/setup/";
		static final String SUCCESS_TEST_DATA_PATH = "TestData/CustomerMapper/update/expected/";

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.clearTableData(CUSTOMER_TABLE_NAME, ORDERS_TABLE_NAME, ORDER_DETAILS_TABLE_NAME);
			DbUnitUtil.insertData(SETUP_TEST_DATA_PATH);
		}

		@Test
		@DisplayName("更新が成功する場合")
		public void testSuccess() throws Exception {
			Customer customer = new Customer("KA0001", "テストストア2", "111-1111-1111", "111-1111", "東京都港区港南", 0);
			Boolean actual = sut.update(customer);
			assertThat(actual).isTrue();

			String[] sortedColumns = { "customer_code" };
			SortedTable sortedExpected = DbUnitUtil.getExpectedTable(SUCCESS_TEST_DATA_PATH, CUSTOMER_TABLE_NAME,
					sortedColumns);
			SortedTable sortedActual = DbUnitUtil.getActualTable(CUSTOMER_TABLE_NAME, sortedColumns);
			Assertion.assertEquals(sortedExpected, sortedActual);

		}

		@Test
		@DisplayName("更新が失敗する場合（得意先が未登録）")
		public void testNotFound() throws Exception {
			Customer customer = new Customer("KA9999", "テストストア2", "111-1111-1111", "111-1111", "東京都港区港南", 0);
			Boolean actual = sut.update(customer);
			assertThat(actual).isFalse();

			String[] sortedColumns = { "customer_code" };
			SortedTable sortedExpected = DbUnitUtil.getExpectedTable(SETUP_TEST_DATA_PATH, CUSTOMER_TABLE_NAME,
					sortedColumns);
			SortedTable sortedActual = DbUnitUtil.getActualTable(CUSTOMER_TABLE_NAME, sortedColumns);
			Assertion.assertEquals(sortedExpected, sortedActual);

		}

		@Test
		@DisplayName("更新が失敗する場合（得意先が削除済み）")
		public void testDeleted() throws Exception {
			Customer customer = new Customer("KA0002", "テストストア2", "111-1111-1111", "111-1111", "東京都港区港南", 0);
			Boolean actual = sut.update(customer);
			assertThat(actual).isFalse();

			String[] sortedColumns = { "customer_code" };
			SortedTable sortedExpected = DbUnitUtil.getExpectedTable(SETUP_TEST_DATA_PATH, CUSTOMER_TABLE_NAME,
					sortedColumns);
			SortedTable sortedActual = DbUnitUtil.getActualTable(CUSTOMER_TABLE_NAME, sortedColumns);
			Assertion.assertEquals(sortedExpected, sortedActual);

		}

	}

	@Nested
	class DatabaseWrong {

		static final String CUSTOMER_TABLE_NAME = "customer";
		static final String WRONG_TABLE_NAME = "customer2";

		@Autowired
		CustomerMapper sut; // テスト対象クラス

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.renameTable(CUSTOMER_TABLE_NAME, WRONG_TABLE_NAME);
		}

		@AfterEach
		public void tearDown() throws Exception {
			DbUnitUtil.renameTable(WRONG_TABLE_NAME, CUSTOMER_TABLE_NAME);
		}

		@Test
		@DisplayName("DB状態が不正な場合")
		public void testNotFoundCustomerTable() throws Exception {
			Customer customer = new Customer("KA0001", "テストストア2", "111-1111-1111", "111-1111", "東京都港区港南", 0);
			assertThrows(DataAccessException.class, () -> {
				sut.update(customer);
			});
		}

	}
}
