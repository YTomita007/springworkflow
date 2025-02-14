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
import springworkflow.tasks.service.CustomerChangeLogic;

public class CustomerChangeLogicTest {
	@Mock
	private CustomerMapper customerMapper;

	@InjectMocks
	CustomerChangeLogic sut; // テスト対象クラス

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
	@DisplayName("更新が成功する場合")
	public void testUpdate() {
		Customer customer = new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
		Mockito.doReturn(customer).when(customerMapper).find(customer.getCustCode());
		Mockito.doReturn(true).when(customerMapper).update(customer);

		Customer actual = sut.update(customer);
		assertThat(actual).isEqualTo(customer);
		Mockito.verify(customerMapper, Mockito.times(1)).find(customer.getCustCode());
		Mockito.verify(customerMapper, Mockito.times(1)).update(customer);
	}

	@Test
	@DisplayName("更新が失敗する場合（顧客検索が失敗）")
	public void testUpdateNotFound() {
		Customer customer = new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
		Mockito.doReturn(null).when(customerMapper).find(customer.getCustCode());
		assertThrows(BusinessException.class, () -> {
			sut.update(customer);
		});
		Mockito.verify(customerMapper, Mockito.times(1)).find(customer.getCustCode());
		Mockito.verify(customerMapper, Mockito.times(0)).update(customer);
	}

	@Test
	@DisplayName("更新が失敗する場合（顧客更新が失敗）")
	public void testUpdateNotUpdate() {
		Customer customer = new Customer("KA0001", "Aストア", "045-128-3581", "220-0001", "横浜市西区北幸２－１", 0);
		Mockito.doReturn(customer).when(customerMapper).find(customer.getCustCode());
		Mockito.doReturn(false).when(customerMapper).update(customer);

		assertThrows(BusinessException.class, () -> {
			sut.update(customer);
		});
		Mockito.verify(customerMapper, Mockito.times(1)).find(customer.getCustCode());
		Mockito.verify(customerMapper, Mockito.times(1)).update(customer);
	}

}
