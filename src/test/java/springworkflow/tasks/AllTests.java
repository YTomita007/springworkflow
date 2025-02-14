package springworkflow.tasks;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
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
public class AllTests {}