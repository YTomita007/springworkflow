package springworkflow.tasks.logic;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;
import springworkflow.tasks.service.CustomerListLogic;

public class CustomerListLogicTest {
	@Mock
	private CustomerMapper customerMapper;

	@InjectMocks
	CustomerListLogic sut; // テスト対象クラス

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testFindAll() {
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

		Mockito.doReturn(expected).when(customerMapper).findAll();

		ArrayList<Customer> actual = sut.findAll();
		assertThat(actual).isEqualTo(expected);
	}
}
