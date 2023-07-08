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
import springworkflow.util.DbTestExecutionListner;
import springworkflow.util.DbUnitUtil;

/**
 */
@SpringBootTest(classes = SpringWorkFlowSpringApplication.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbTestExecutionListner.class })
public class CustomerNumberingMapperIncrementTest {

	@Nested
	class Correct {

		@Autowired
		CustomerNumberingMapper sut; // テスト対象クラス

		// ファイル読み込み用設定
		static final String CUSTOMER_NUMBERING_TABLE_NAME = "customer_numbering";
		static final String SETUP_TEST_DATA_PATH = "TestData/CustomerNumberingMapper/increment/setup/";
		static final String EXPECTED_TEST_DATA_PATH = "TestData/CustomerNumberingMapper/increment/expected/";

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.clearTableData(CUSTOMER_NUMBERING_TABLE_NAME);
			DbUnitUtil.insertData(SETUP_TEST_DATA_PATH);
		}

		@Test
		@DisplayName("更新が成功する場合")
		public void testReturn() throws Exception {
			assertThat(sut.increment()).isTrue();
			String[] sortedColumns = { "customer_code" };
			SortedTable sortedExpected = DbUnitUtil.getExpectedTable(EXPECTED_TEST_DATA_PATH,
					CUSTOMER_NUMBERING_TABLE_NAME,
					sortedColumns);
			SortedTable sortedActual = DbUnitUtil.getActualTable(CUSTOMER_NUMBERING_TABLE_NAME, sortedColumns);
			Assertion.assertEquals(sortedExpected, sortedActual);

		}
	}

	@Nested
	class DatabaseWrong {

		static final String CUSTOMER_NUMBERING_TABLE_NAME = "customer_numbering";
		static final String WRONG_TABLE_NAME = "customer_numbering2";

		@Autowired
		CustomerNumberingMapper sut; // テスト対象クラス

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.renameTable(CUSTOMER_NUMBERING_TABLE_NAME, WRONG_TABLE_NAME);
		}

		@AfterEach
		public void tearDown() throws Exception {
			DbUnitUtil.renameTable(WRONG_TABLE_NAME, CUSTOMER_NUMBERING_TABLE_NAME);
		}

		@Test
		@DisplayName("DB状態が不正な場合")
		public void testNotFoundCustomerTable() throws Exception {
			assertThrows(DataAccessException.class, () -> {
				sut.increment();
			});
		}

	}
}
