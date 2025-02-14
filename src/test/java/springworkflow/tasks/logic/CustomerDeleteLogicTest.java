package springworkflow.tasks.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import springworkflow.tasks.common.BusinessException;
import springworkflow.tasks.entity.Customer;
import springworkflow.tasks.mapper.CustomerMapper;
import springworkflow.tasks.service.CustomerDeleteLogic;

public class CustomerDeleteLogicTest {
	@Mock
	private CustomerMapper customerMapper;

	@InjectMocks
	CustomerDeleteLogic sut; // テスト対象クラス

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("検索が成功する場合")
	public void testFind() {
		Customer customer = new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
		Mockito.doReturn(customer).when(customerMapper).find("KA0001");

		Customer actual = sut.find("KA0001");
		assertThat(actual).isEqualTo(customer);
	}

	@Test
	@DisplayName("検索が失敗する場合")
	public void testFindWhenNotFound() {
		Mockito.doReturn(null).when(customerMapper).find("KA0001");

		assertThrows(BusinessException.class, () -> {
			sut.find("KA0001");
		});
	}

	@Test
	@DisplayName("削除が成功する場合")
	public void testDelete() {
		Customer customer = new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
		Mockito.doReturn(customer).when(customerMapper).find(customer.getCustCode());
		Mockito.doReturn(true).when(customerMapper).delete(customer);

		Customer actual = sut.delete(customer.getCustCode());
		assertThat(actual).isEqualTo(customer);
		Mockito.verify(customerMapper, Mockito.times(1)).find(customer.getCustCode());
		Mockito.verify(customerMapper, Mockito.times(1)).delete(customer);
	}

	@Test
	@DisplayName("削除が失敗する場合（顧客検索が失敗）")
	public void testDeleteNotFound() {
		Customer customer = new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
		Mockito.doReturn(null).when(customerMapper).find(customer.getCustCode());

		assertThrows(BusinessException.class, () -> {
			sut.delete(customer.getCustCode());
		});
		Mockito.verify(customerMapper, Mockito.times(1)).find(customer.getCustCode());
		Mockito.verify(customerMapper, Mockito.times(0)).delete(customer);
	}

	@Test
	@DisplayName("削除が失敗する場合（顧客削除が失敗）")
	public void testDeleteNotUpdate() {
		Customer customer = new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
		Mockito.doReturn(customer).when(customerMapper).find(customer.getCustCode());
		Mockito.doReturn(false).when(customerMapper).delete(customer);

		assertThrows(BusinessException.class, () -> {
			sut.delete(customer.getCustCode());
		});
		Mockito.verify(customerMapper, Mockito.times(1)).find(customer.getCustCode());
		Mockito.verify(customerMapper, Mockito.times(1)).delete(customer);
	}

}
