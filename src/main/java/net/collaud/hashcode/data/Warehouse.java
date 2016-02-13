package net.collaud.hashcode.data;

import java.util.HashMap;
import java.util.Map;
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
@ToString(exclude = "stock")
public class Warehouse {

	@Getter
	private final int id;
	
	@Getter
	private final Point2DInt position;
	
	private final Stock<ItemType> stock = new Stock();

	public void addStock(ItemType item, int amount) {
		stock.add(item, amount);
	}
	
	public void addStock(Stock<ItemType> stockToAdd){
		for (Map.Entry<ItemType, Integer> i : stockToAdd.getAll().entrySet()) {
			addStock(i.getKey(), i.getValue());
		}
	}

	public int getStock(ItemType item) {
		return stock.count(item);
	}
	
	public boolean inStock(ItemType item){
		return stock.inStock(item);
	}

	public void takeFromStock(ItemType item, int amount) {
		stock.remove(item, amount);
	}
}
