package springworkflow.tasks.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

	@Before(value = "execution(* springworkflow.tasks.controller..*(..))")
	public void logBefore(JoinPoint point) {
		LOGGER.debug(getSignature(point) + " : START");

		String[] methodArgNames = ((CodeSignature) point.getSignature()).getParameterNames();

		Object[] methodArgValues = point.getArgs();

		for (int i = 0; i < methodArgNames.length; i++) {
			LOGGER.debug(methodArgNames[i] + "=" + String.valueOf(methodArgValues[i]));
		}
	}

	@AfterReturning(value = "execution(* springworkflow.tasks.controller..*(..))", returning = "returnValue")
	public void logAfterReturn(JoinPoint point, Object returnValue) {
		LOGGER.debug(getSignature(point) + " : END");
		LOGGER.debug(" ->> RETURNS : " + returnValue);
	}

	@AfterThrowing(value = "execution(* springworkflow.tasks..*(..))", throwing = "exception")
	public void logAfterThrow(Exception exception) {
		LOGGER.debug(exception.toString());
	}

	private String getSignature(JoinPoint point) {
		return point.getSignature().getDeclaringType().getSimpleName() + "#" + point.getSignature().getName();
	}
}
