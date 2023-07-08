package springworkflow.tasks.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springworkflow.tasks.common.SystemException;
import springworkflow.tasks.entity.Item;
import springworkflow.tasks.service.SummaryDownloadLogicIF;

@Controller
@RequestMapping("/employee/summary")
public class EmployeeItemController {

	private final String[] COLUMNS = { "item_code", "item_name", "price", "stock" };
	private final String[] SORTTYPE = { "asc", "desc" };

	@Autowired
	private SummaryDownloadLogicIF summaryDownloadLogic;

	@GetMapping("")
	public String salarySummary(Model model) {
		ArrayList<Item> items = summaryDownloadLogic.getAllItems();
		model.addAttribute("columns", COLUMNS);
		model.addAttribute("sortType", SORTTYPE);
		model.addAttribute("items", items);

		return "400_employee/402_01EmployeeItemSummary";
	}

	@PostMapping("/download")
	public ResponseEntity<byte[]> download(Model model) {
		ArrayList<Item> items = summaryDownloadLogic.getAllItems();
		model.addAttribute("columns", COLUMNS);
		model.addAttribute("sortType", SORTTYPE);
		model.addAttribute("items", items);

		try {
			return summaryDownloadLogic.download(items);
		} catch (IOException e) {
			throw new SystemException("ダウンロード処理にて致命的なエラーが発生しました");
		}
	}

	@PostMapping("/sort")
	public String sortItems(
			@RequestParam("column") String column,
			@RequestParam("sortType") String sort,
			Model model) {
		ArrayList<Item> items = summaryDownloadLogic.getAllItemsBySort(column, sort);
		model.addAttribute("columns", COLUMNS);
		model.addAttribute("sortType", SORTTYPE);
		model.addAttribute("items", items);

		return "400_employee/402_01EmployeeItemSummary";
	}

}
