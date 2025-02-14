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
public class CustomerMapperInsertTest {

	@Nested
	class Correct {

		@Autowired
		CustomerMapper sut; // テスト対象クラス

		// ファイル読み込み用設定
		static final String CUSTOMER_TABLE_NAME = "customer";
		static final String ORDERS_TABLE_NAME = "orders";
		static final String ORDER_DETAILS_TABLE_NAME = "order_details";
		static final String SETUP_TEST_DATA_PATH = "TestData/CustomerMapper/insert/setup/";
		static final String EXPECTED_TEST_DATA_PATH = "TestData/CustomerMapper/insert/expected/";

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.clearTableData(CUSTOMER_TABLE_NAME, ORDERS_TABLE_NAME, ORDER_DETAILS_TABLE_NAME);
			DbUnitUtil.insertData(SETUP_TEST_DATA_PATH);
		}

		@Test
		@DisplayName("登録が成功する場合")
		public void test() throws Exception {
			Customer customer = new Customer("KA0016", "テストストア", "000-000-0000", "000-0000", "東京都大田区蒲田", 0);
			Boolean actual = sut.insert(customer);
			assertThat(actual).isTrue();

			String[] sortedColumns = { "customer_code" };
			SortedTable sortedExpected = DbUnitUtil.getExpectedTable(EXPECTED_TEST_DATA_PATH, CUSTOMER_TABLE_NAME,
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
			Customer customer = new Customer("KA0016", "テストストア", "000-000-0000", "000-0000", "東京都大田区蒲田", 0);
			assertThrows(DataAccessException.class, () -> {
				sut.insert(customer);
			});
		}

	}
}
