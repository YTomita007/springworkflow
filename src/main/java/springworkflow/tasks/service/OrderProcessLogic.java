package springworkflow.tasks.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springworkflow.tasks.common.SystemException;
import springworkflow.tasks.entity.Item;
import springworkflow.tasks.entity.ItemWithAmount;
import springworkflow.tasks.form.OrderPurchaseForm;
import springworkflow.tasks.mapper.ItemMapper;
import springworkflow.tasks.mapper.OrderMapper;

@Service
public class OrderProcessLogic implements OrderProcessLogicIF {

	@Autowired
	ItemMapper itemMapper;

	@Autowired
	OrderMapper orderMapper;

	@Override
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> items = itemMapper.getAllItems();
		return items;
	}

	@Override
	public Item getItem(String itemCode) {
		Item item = itemMapper.getItem(itemCode);
		return item;
	}

	@Override
	public OrderPurchaseForm checkShoppingCart(ArrayList<ItemWithAmount> shoppingCart) {
		int totalPrice = 0;
		Iterator<ItemWithAmount> itrr = shoppingCart.iterator();
		while (itrr.hasNext()) {
			ItemWithAmount itemWithAmount = (ItemWithAmount) itrr.next();
			totalPrice += itemWithAmount.getPrice() * itemWithAmount.getAmount();
		}

		String orderNo = orderMapper.maxOrderNo();
		orderNo = String.format("%06d", Integer.parseInt(orderNo) + 1);

		return new OrderPurchaseForm(
				orderNo, null, null,
				totalPrice, shoppingCart.size(),
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
				shoppingCart);
	}

	@Override
	@Transactional
	public void orderPurchase(
			OrderPurchaseForm orderPurchaseForm,
			ArrayList<ItemWithAmount> shoppingCart) {
		int resultOrder = orderMapper.createOrder(orderPurchaseForm);
		int resultOrderDetail = 0;
		Iterator<ItemWithAmount> itrr = shoppingCart.iterator();
		while (itrr.hasNext()) {
			ItemWithAmount itemWithAmount = (ItemWithAmount) itrr.next();
			resultOrderDetail += orderMapper.createOrderDetail(orderPurchaseForm, itemWithAmount);
		}
		if (resultOrder == 0 || resultOrderDetail != shoppingCart.size()) {
			throw new SystemException("更新時に致命的なエラーが発生しました");
		}
	}
}
