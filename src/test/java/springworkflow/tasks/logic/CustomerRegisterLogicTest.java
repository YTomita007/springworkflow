package springworkflow.tasks.logic;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;
import springworkflow.tasks.mapper.CustomerNumberingMapper;
import springworkflow.tasks.service.CustomerRegisterLogic;

public class CustomerRegisterLogicTest{
	@Mock
	private CustomerMapper customerMapper;

	@Mock
	private CustomerNumberingMapper customerNumberingMapper;

	@InjectMocks
	CustomerRegisterLogic sut; // テスト対象クラス

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("登録が成功する場合")
	public void testRegister() {
		Customer customer = new Customer("KA0002", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
		Mockito.doReturn(1).when(customerNumberingMapper).find();

		Customer actual = sut.register(customer);
		assertThat(actual).isEqualTo(customer);

		Mockito.verify(customerNumberingMapper, Mockito.times(1)).find();
		Mockito.verify(customerMapper, Mockito.times(1)).insert(customer);
		Mockito.verify(customerNumberingMapper, Mockito.times(1)).increment();
	}
}
