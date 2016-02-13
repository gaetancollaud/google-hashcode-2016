package net.collaud.hashcode.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.collaud.hashcode.common.data.Point2DInt;
import net.collaud.hashcode.common.data.Stock;

/**
 *
 * @author Gaetan Collaud
 */

@RequiredArgsConstructor
@ToString(exclude = "itemList")
public class Order {
	
	@Getter
	private final int id;
	
	@Getter
	private final Point2DInt destination;
	
	@Getter
	private final Stock<ItemType> itemList = new Stock<>();
	
	public void addItem(ItemType type, int amount){
		itemList.add(type, amount);
	}
	
	public void delivered(ItemType type, int amount){
		itemList.remove(type, amount);
	}
	
	public boolean isOrderCompleted(){
		return itemList.isEmpty();
	}
	
	
}
