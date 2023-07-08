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
import springworkflow.tasks.entity.Employee;
import springworkflow.util.DbTestExecutionListner;
import springworkflow.util.DbUnitUtil;

/**
 */
@SpringBootTest(classes = SpringWorkFlowSpringApplication.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbTestExecutionListner.class })
public class EmployeeMapperFindTest {

	@Nested
	class Correct {

		@Autowired
		EmployeeMapper sut; // テスト対象クラス

		// ファイル読み込み用設定
		static final String EMPLOYEE_TABLE_NAME = "employee";
		static final String ORDERS_TABLE_NAME = "orders";
		static final String ORDER_DETAILS_TABLE_NAME = "order_details";
		static final String TEST_DATA_PATH = "TestData/EmployeeMapper/find/";

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.clearTableData(EMPLOYEE_TABLE_NAME, ORDERS_TABLE_NAME, ORDER_DETAILS_TABLE_NAME);
			DbUnitUtil.insertData(TEST_DATA_PATH);
		}

		@Test
		@DisplayName("検索が成功する場合")
		public void testFind() throws Exception {
			Employee employee = new Employee("H20001", "安藤直也", "zy0001");
			Employee actual = sut.login("H20001", "zy0001");
			assertThat(actual).isEqualTo(employee);
		}

		@Test
		@DisplayName("検索が失敗する場合（従業員番号不正）")
		public void testIncorrectNo() throws Exception {
			Employee actual = sut.login("HAAAAA", "zy0001");
			assertThat(actual).isNull();
		}

		@Test
		@DisplayName("検索が失敗する場合（パスワード不正）")
		public void testIncorrectPassword() throws Exception {
			Employee actual = sut.login("H20001", "zyAAAA");
			assertThat(actual).isNull();
		}
	}

	@Nested
	class DatabaseWrong {

		static final String EMPLOYEE_TABLE_NAME = "employee";
		static final String WRONG_TABLE_NAME = "employee2";

		@Autowired
		EmployeeMapper sut; // テスト対象クラス

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.renameTable(EMPLOYEE_TABLE_NAME, WRONG_TABLE_NAME);
		}

		@AfterEach
		public void tearDown() throws Exception {
			DbUnitUtil.renameTable(WRONG_TABLE_NAME, EMPLOYEE_TABLE_NAME);
		}

		@Test
		@DisplayName("DB状態が不正な場合")
		public void testNotFoundEmployeeTable() throws Exception {
			assertThrows(DataAccessException.class, () -> {
				sut.login("H20001", "zy0001");
			});
		}

	}
}
