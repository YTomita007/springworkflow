package springworkflow.util;

import javax.sql.DataSource;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DbTestExecutionListner extends AbstractTestExecutionListener {
    @Override
    public void beforeTestMethod(TestContext testContext) {
        DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
        DbUnitUtil.setUpDbUnitUtil(dataSource);
    }
}
