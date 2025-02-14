package springworkflow.tasks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes(value = "attendance")
@Controller
@RequestMapping("/employee/attendance")
public class EmployeeAttendanceController {

	private boolean attendance = false;

	@GetMapping("")
	public String attendanceMenu(Model model) {
		model.addAttribute("attendance", attendance);
		return "400_employee/401_01EmployeeAttendanceManager";
	}

	@PostMapping("")
	public String changeAttendance(Model model) {
		if (model.getAttribute("attendance") != null) {
			if (model.getAttribute("attendance").equals(false)) {
				attendance = true;
				model.addAttribute("message", "ただいま出勤しました");
			} else {
				attendance = false;
				model.addAttribute("message", "ただいま退勤しました");
			}
		}
		model.addAttribute("attendance", attendance);
		return "400_employee/401_01EmployeeAttendanceManager";
	}
}
