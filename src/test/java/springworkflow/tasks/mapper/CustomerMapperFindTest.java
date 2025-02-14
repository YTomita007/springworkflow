package springworkflow.tasks.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
public class CustomerMapperFindTest {

	@Nested
	class Correct {

		@Autowired
		CustomerMapper sut; // テスト対象クラス

		// ファイル読み込み用設定
		static final String CUSTOMER_TABLE_NAME = "customer";
		static final String ORDERS_TABLE_NAME = "orders";
		static final String ORDER_DETAILS_TABLE_NAME = "order_details";
		static final String TEST_DATA_PATH = "TestData/CustomerMapper/find/";

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.clearTableData(CUSTOMER_TABLE_NAME, ORDERS_TABLE_NAME, ORDER_DETAILS_TABLE_NAME);
			DbUnitUtil.insertData(TEST_DATA_PATH);
		}

		@Test
		@DisplayName("検索が成功する場合")
		public void testFind() throws Exception {
			Customer customer = new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
			Customer actual = sut.find("KA0001");
			assertThat(actual).isEqualTo(customer);
		}

		@Test
		@DisplayName("検索が失敗する場合（削除済）")
		public void testDeletedCustomer() throws Exception {
			Customer actual = sut.find("KA0002");
			assertThat(actual).isNull();
		}

		@Test
		@DisplayName("検索が失敗する場合（不存在）")
		public void testIncorrectCode() throws Exception {
			Customer actual = sut.find("KA9999");
			assertThat(actual).isNull();
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
			assertThrows(DataAccessException.class, () -> {
				sut.find("");
			});
		}

	}
}
