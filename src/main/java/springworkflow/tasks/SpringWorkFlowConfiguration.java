/**
 * Ufn58lConfiguration.java
 * All Rights Reserved, Copyright(c) Fujitsu Learning Media Limited
 */

package springworkflow.tasks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springworkflow.tasks.common.AuthenticationAspect;
import springworkflow.tasks.common.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class SpringWorkFlowConfiguration {


	/**
	 * ResourceBundleMessageSourceのBean定義
	 * @return {@code ResourceBundleMessageSource}
	 */
/*
	@Bean
	ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource
			= new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasenames("ufn58l-messages");
		return messageSource;
	}
*/
	/**
	 * LoggingAspectクラスのBean定義
	 * @return {@code LoggingAspect}
	 */
	@Bean
	LoggingAspect loggingAspect() {
		return new LoggingAspect();
	}

	/**
	 * AuthenticationAspectクラスのBean定義
	 * @return {@code AuthenticationAspect}
	 */
	@Bean
	AuthenticationAspect authenticationAspect() {
		return new AuthenticationAspect();
	}
}
