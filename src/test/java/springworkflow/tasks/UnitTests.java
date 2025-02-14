package springworkflow.tasks;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@SuppressWarnings("deprecation")
@RunWith(JUnitPlatform.class)
@SelectClasses({

	/** controller **/
	springworkflow.tasks.controller.CustomerChangeControllerTest.class,
	springworkflow.tasks.controller.CustomerDeleteControllerTest.class,
	springworkflow.tasks.controller.CustomerFindControllerTest.class,
	springworkflow.tasks.controller.CustomerListControllerTest.class,
	springworkflow.tasks.controller.CustomerRegisterControllerTest.class,
	springworkflow.tasks.controller.MenuControllerTest.class,
	springworkflow.tasks.controller.LoginControllerTest.class,
	springworkflow.tasks.controller.AggregateOrderControllerTest.class,

	/** logic **/
	springworkflow.tasks.logic.CustomerChangeLogicTest.class,
	springworkflow.tasks.logic.CustomerDeleteLogicTest.class,
	springworkflow.tasks.logic.CustomerFindLogicTest.class,
	springworkflow.tasks.logic.CustomerListLogicTest.class,
	springworkflow.tasks.logic.CustomerRegisterLogicTest.class,
	springworkflow.tasks.logic.LoginLogicTest.class,

	/** mapper **/
	springworkflow.tasks.mapper.CustomerMapperDeleteTest.class,
	springworkflow.tasks.mapper.CustomerMapperFindAllTest.class,
	springworkflow.tasks.mapper.CustomerMapperFindIgnoreDeleteFlagTest.class,
	springworkflow.tasks.mapper.CustomerMapperFindTest.class,
	springworkflow.tasks.mapper.CustomerMapperInsertTest.class,
	springworkflow.tasks.mapper.CustomerMapperUpdateTest.class,
	springworkflow.tasks.mapper.CustomerNumberingMapperFindTest.class,
	springworkflow.tasks.mapper.CustomerNumberingMapperIncrementTest.class,
	springworkflow.tasks.mapper.EmployeeMapperFindTest.class,
	springworkflow.tasks.mapper.OrderMapperCreateOrderTotalListByCustomerTest.class,
	springworkflow.tasks.mapper.OrderMapperCreateOrderTotalListByItemTest.class,
})
public class UnitTests {}