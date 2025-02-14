package springworkflow.tasks.common;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerMaster {

	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(HttpSessionRequiredException.class)
	public String sessionError(Model model, HttpSessionRequiredException e, HttpServletRequest request) {
		//Todo:
		model.addAttribute("message", "セッションエラー");
		return "error";
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler()
	public String occurOtherException(Exception exception, Model model, HttpServletRequest request) {
		model.addAttribute("timestamp", new Timestamp(System.currentTimeMillis()));
		model.addAttribute("status", HttpStatus.values());
		model.addAttribute("error", exception.getStackTrace().toString());
		model.addAttribute("exception", exception.getClass());
		model.addAttribute("message", exception.getMessage());
		model.addAttribute("URL:" + request.getRequestURI());
		model.addAttribute("errors", request.getParameterNames());
		return "error";
	}
}