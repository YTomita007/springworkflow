package springworkflow.tasks.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import springworkflow.tasks.entity.Item;

@Mapper
public interface ItemMapper {
	public Item getItem(String itemCode);
	public ArrayList<Item> getAllItems();
	public ArrayList<Item> getAllItemsBySort(String column, String sort);
}
