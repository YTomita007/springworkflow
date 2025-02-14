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
import springworkflow.tasks.entity.Employee;
import springworkflow.tasks.mapper.EmployeeMapper;
import springworkflow.tasks.service.EmployeeFindLogic;

public class LoginLogicTest {
	@Mock
	private EmployeeMapper employeeMapper;

	@InjectMocks
	EmployeeFindLogic sut; // テスト対象クラス

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("ログインが成功する場合")
	public void testLogin() {
		Employee employee = new Employee("H20001", "安藤直也", "zy0001");
		Mockito.doReturn(employee).when(employeeMapper).login(employee.getEmployeeNo(), employee.getPassword());

		Employee actual = sut.login(employee.getEmployeeNo(), employee.getPassword());
		assertThat(actual).isEqualTo(employee);

		Mockito.verify(employeeMapper, Mockito.times(1)).login(employee.getEmployeeNo(), employee.getPassword());
	}

	@Test
	@DisplayName("ログインが失敗する場合")
	public void testFindWhenNotFound() {
		Employee employee = new Employee("H20001", "安藤直也", "zy0001");
		Mockito.doReturn(null).when(employeeMapper).login(employee.getEmployeeNo(), employee.getPassword());

		Throwable exception = assertThrows(BusinessException.class, () -> {
			sut.login(employee.getEmployeeNo(), employee.getPassword());
		});
		assertEquals("ログインに失敗しました。", exception.getMessage());

		Mockito.verify(employeeMapper, Mockito.times(1)).login(employee.getEmployeeNo(), employee.getPassword());

	}
}
