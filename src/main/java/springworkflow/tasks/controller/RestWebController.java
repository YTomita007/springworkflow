package springworkflow.tasks.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import springworkflow.tasks.common.SystemException;
import springworkflow.tasks.entity.Application;

@SessionAttributes(value = "applications")
@RestController
@RequestMapping("/api/application")
public class RestWebController {

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/all")
	public String getApplications(Model model) {
		String jsonMsg = null;
		ArrayList<Application> applications = new ArrayList<Application>();
		Application application = new Application();

		if (applications.size() == 0) {
			application.setTitle("first application");
			application.setStart("20230606");
			applications.add(application);

			application = new Application();
			application.setTitle("second application");
			application.setStart("20230611");
			application.setEnd("20230616");
			applications.add(application);
			model.addAttribute("applications", applications);
		} else {
			applications = (ArrayList<Application>) model.getAttribute("applications");
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonMsg = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(applications);
		} catch (IOException ioex) {
			throw new SystemException("カレンダー表示にて致命的なエラーが発生しました");
		}

		return jsonMsg;
	}
}
