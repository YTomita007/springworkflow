package springworkflow.util;

import java.util.ArrayList;

import springworkflow.tasks.entity.AggregateByItem;

public class TestAggregateByItem {

	public static AggregateByItem create() {
		return new AggregateByItem(
				"AX0001",
				"ミネラルウォーター（500ml）",
				25,
				80,
				2000);
	}

	public static ArrayList<AggregateByItem> createArrayList() {
		ArrayList<AggregateByItem> list = new ArrayList<>();
		list.add(TestAggregateByItem.create());
		return list;
	}

}