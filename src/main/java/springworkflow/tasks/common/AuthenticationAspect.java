package springworkflow.tasks.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;

@Aspect
public class AuthenticationAspect {

	@Autowired
	private HttpSession httpSession;

	@Before(value = "execution(* springworkflow.tasks.controller..*(..)) && !execution(* springworkflow.tasks.controller.LoginController.*(..))")
	public void checkAuthentication(JoinPoint joinPoint) throws Exception {

		if (httpSession.getAttribute("employee") == null) {
			throw new SystemException("ログイン情報が確認できませんでした。");
		}
	}

}
