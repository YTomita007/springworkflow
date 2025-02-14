package springworkflow.tasks.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import springworkflow.tasks.entity.Item;

public interface SummaryDownloadLogicIF {
	public ArrayList<Item> getAllItems();

	public ResponseEntity<byte[]> download(ArrayList<Item> items) throws IOException;

	public String getCsvText(ArrayList<Item> items) throws JsonProcessingException;

	public ArrayList<Item> getAllItemsBySort(String column, String sort);
}