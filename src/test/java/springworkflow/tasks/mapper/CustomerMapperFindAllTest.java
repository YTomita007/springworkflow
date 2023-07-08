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
import springworkflow.tasks.entity.Customer;
import springworkflow.util.DbTestExecutionListner;
import springworkflow.util.DbUnitUtil;

@SpringBootTest(classes = SpringWorkFlowSpringApplication.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbTestExecutionListner.class })
public class CustomerMapperFindAllTest {

	@Nested
	class Correct {

		@Autowired
		CustomerMapper sut; // テスト対象クラス

		// ファイル読み込み用設定
		static final String CUSTOMER_TABLE_NAME = "customer";
		static final String ORDERS_TABLE_NAME = "orders";
		static final String ORDER_DETAILS_TABLE_NAME = "order_details";
		static final String TEST_DATA_PATH_CORRECT = "TestData/CustomerMapper/findAll/correct/";
		static final String TEST_DATA_PATH_DELETED = "TestData/CustomerMapper/findAll/deleted/";

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.clearTableData(CUSTOMER_TABLE_NAME, ORDERS_TABLE_NAME, ORDER_DETAILS_TABLE_NAME);
		}

		@Test
		@DisplayName("リストが取得できる場合")
		public void testCorrect() throws Exception {
			DbUnitUtil.insertData(TEST_DATA_PATH_CORRECT);
			ArrayList<Customer> expected = new ArrayList<>();
			expected.add(new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0));
			expected.add(new Customer("KA0003", "Cストア", "045-128-3583", "220-0003", "横浜市西区みなとみらい２－１", 0));
			expected.add(new Customer("KA0004", "Dストア", "045-128-3584", "220-0004", "横浜市西区南幸１－４", 0));
			expected.add(new Customer("KA0005", "Eストア", "045-128-3585", "220-0005", "横浜市西区北幸２－５", 0));
			expected.add(new Customer("KA0006", "Fストア", "045-128-3586", "220-0006", "横浜市西区北幸２－６", 0));
			expected.add(new Customer("KA0007", "Gストア", "045-128-3587", "220-0007", "横浜市西区高島１－１", 0));
			expected.add(new Customer("KA0008", "Hストア", "045-128-3588", "220-0008", "横浜市西区高島１－２", 0));
			expected.add(new Customer("KA0009", "Iストア", "045-128-3589", "220-0009", "横浜市西区高島２－３", 0));
			expected.add(new Customer("KA0010", "Jストア", "045-150-3590", "220-0010", "横浜市西区高島２－４", 0));
			expected.add(new Customer("KA0011", "Kストア", "045-150-3591", "220-0011", "横浜市西区みなとみらい２－１", 0));
			expected.add(new Customer("KA0012", "Lストア", "045-150-3592", "220-0012", "横浜市西区みなとみらい２－２", 0));
			expected.add(new Customer("KA0013", "Mストア", "045-150-3593", "220-0013", "横浜市西区みなとみらい２－３", 0));
			expected.add(new Customer("KA0014", "Nストア", "045-150-3594", "220-0014", "横浜市西区みなとみらい２－４", 0));
			expected.add(new Customer("KA0015", "Oストア", "045-150-3595", "220-0015", "横浜市西区みなとみらい２－５", 0));

			ArrayList<Customer> actual = sut.findAll();
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		@DisplayName("空のリストを取得する場合（テーブルデータ０件）")
		public void testEmpty() throws Exception {
			ArrayList<Customer> expected = new ArrayList<>();
			ArrayList<Customer> actual = sut.findAll();
			assertThat(actual).isEqualTo(expected);
		}

		@Test
		@DisplayName("空のリストを取得する場合（全得意先が削除済）")
		public void testDeleted() throws Exception {
			DbUnitUtil.insertData(TEST_DATA_PATH_DELETED);
			ArrayList<Customer> expected = new ArrayList<>();
			ArrayList<Customer> actual = sut.findAll();
			assertThat(actual).isEqualTo(expected);
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
				sut.findAll();
			});
		}

	}
}
