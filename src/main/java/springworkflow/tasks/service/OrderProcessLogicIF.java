package springworkflow.tasks.service;

import java.util.ArrayList;

import springworkflow.tasks.entity.Item;
import springworkflow.tasks.entity.ItemWithAmount;
import springworkflow.tasks.form.OrderPurchaseForm;

public interface OrderProcessLogicIF {
	public ArrayList<Item> getAllItems();

	public Item getItem(String itemCode);

	public OrderPurchaseForm checkShoppingCart(ArrayList<ItemWithAmount> shoppingCart);

	void orderPurchase(OrderPurchaseForm orderPurchaseForm,
			ArrayList<ItemWithAmount> shoppingCart);
}