package net.collaud.hashcode.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

	public void addItem(ItemType type, int amount) {
		itemList.add(type, amount);
	}

	public void delivered(ItemType type, int amount) {
		itemList.remove(type, amount);
	}

	public boolean isOrderCompleted() {
		return itemList.isEmpty();
	}

	public List<Command> makeCommandsForThisOrder(Game game, Drone drone, boolean applyChanges) {
		List<Command> commands = new ArrayList<>();
		if(!applyChanges){
			drone.setCurrentPosition(this.getDestination());
		}
		for (Map.Entry<ItemType, Integer> item : getItemList().getAll().entrySet()) {
			int maxInOrder = item.getValue();
			int maxInTrunk = drone.getTrunkSize() / item.getKey().weight;
			Map<Warehouse, Stock<ItemType>> rollback = new HashMap<>();
			while (maxInOrder != 0) {
				Warehouse w = null;
				try {
					w = closestWarehouse(game, drone, item.getKey());
				} catch (Exception ex) {
					System.out.println("ex");
				}
				int maxInWarehouse = w.getStock(item.getKey());
				int toTake = Math.min(Math.min(maxInWarehouse, maxInTrunk), maxInOrder);

				Command commandLoad = Command.LoadCommand(drone, w, item.getKey(), toTake);
				commands.add(commandLoad);

				w.takeFromStock(item.getKey(), toTake);
				if (!applyChanges) {
					rollback.computeIfAbsent(w, (wa) -> new Stock<>());
					rollback.get(w).add(item.getKey(), toTake);
				}

				Command commandDeliver = Command.DeliverCommand(drone, this, item.getKey(), toTake);
				commands.add(commandDeliver);

				maxInOrder -= toTake;
			}
			if (!applyChanges) {
				for (Map.Entry<Warehouse, Stock<ItemType>> rb : rollback.entrySet()) {
					rb.getKey().addStock(rb.getValue());
				}
			}
		}
		return commands;
	}

	private Warehouse closestWarehouse(Game game, Drone d, ItemType t) {
		return game.getWarehouses().stream()
				.filter(w -> w.inStock(t))
				.sorted(Comparator.comparingInt(w -> d.getCurrentPosition().euclidianDistanceCeil(w.getPosition())))
				.findFirst()
				.get();
	}

}
