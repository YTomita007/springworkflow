package springworkflow.tasks.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "商品コード", "商品名", "価格", "在庫" })
public class Item implements Serializable {

	public Item(Item item) {
		this.itemCode = item.getItemCode();
		this.itemName = item.getItemName();
		this.price = item.getPrice();
		this.stock = item.getStock();
	}

	@JsonProperty("itemCode")
	private String itemCode;

	@JsonProperty("itemName")
	private String itemName;

	@JsonProperty("price")
	private int price;

	@JsonProperty("stock")
	private int stock;
}