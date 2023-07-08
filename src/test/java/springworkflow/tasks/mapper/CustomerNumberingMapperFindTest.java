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
import springworkflow.util.DbTestExecutionListner;
import springworkflow.util.DbUnitUtil;

/**
 */
@SpringBootTest(classes = SpringWorkFlowSpringApplication.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbTestExecutionListner.class })
public class CustomerNumberingMapperFindTest {

	@Nested
	class Correct {

		@Autowired
		CustomerNumberingMapper sut; // テスト対象クラス

		// ファイル読み込み用設定
		static final String CUSTOMER_NUMBERING_TABLE_NAME = "customer_numbering";
		static final String TEST_DATA_PATH = "TestData/CustomerNumberingMapper/find/";

		@BeforeEach
		public void setUp() throws Exception {
			DbUnitUtil.clearTableData(CUSTOMER_NUMBERING_TABLE_NAME);
			DbUnitUtil.insertData(TEST_DATA_PATH);
		}

		@Test
		@DisplayName("検索が成功する場合")
		public void testSuccess() throws Exception {
			int actual = sut.find();
			assertThat(actual).isEqualTo(15);
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
		public void testNotFoundCustomerNumberingTable() throws Exception {
			assertThrows(DataAccessException.class, () -> {
				sut.find();
			});
		}

	}
}
