package springworkflow.tasks.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import springworkflow.tasks.common.DownloadHelper;
import springworkflow.tasks.entity.Item;
import springworkflow.tasks.mapper.ItemMapper;

@Service
public class SummaryDownloadLogic implements SummaryDownloadLogicIF {

	@Autowired
	DownloadHelper downloadHelper;

	@Autowired
	ItemMapper itemMapper;

	@Override
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> items = itemMapper.getAllItems();
		return items;
	}

	@Override
	public ResponseEntity<byte[]> download(ArrayList<Item> items) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		downloadHelper.addContentDisposition(headers, "Item.csv");
		return new ResponseEntity<>(getCsvText(items).getBytes("MS932"), headers, HttpStatus.OK);
	}

	@Override
	public String getCsvText(ArrayList<Item> items) throws JsonProcessingException {
		CsvMapper mapper = new CsvMapper();
		//文字列にダブルクオートをつける
		mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
		//ヘッダをつける
		CsvSchema schema = mapper.schemaFor(Item.class).withHeader();
		//データを埋め込む
		return mapper.writer(schema).writeValueAsString(items);
	}

	@Override
	public ArrayList<Item> getAllItemsBySort(String column, String sort) {
		ArrayList<Item> items = itemMapper.getAllItemsBySort(column, sort);
		return items;
	}
}