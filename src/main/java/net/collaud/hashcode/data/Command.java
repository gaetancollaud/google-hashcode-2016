package net.collaud.hashcode.data;

import lombok.AllArgsConstructor;
import net.collaud.hashcode.common.data.Point2DInt;

/**
 *
 * @author Gaetan Collaud
 */
@AllArgsConstructor
public class Command {

	private final Drone drone;
	private final char type;
	private final Warehouse warehouse;
	private final Order order;
	private final ItemType itemType;
	private final int amount;

	public static Command DeliverCommand(Drone d, Order o, ItemType t, int amount) {
		return new Command(d, 'D', null, o, t, amount);
	}

	public static Command LoadCommand(Drone d, Warehouse w, ItemType t, int amount) {
		return new Command(d, 'L', w, null, t, amount);
	}
	
	public int computeCost(){
		Point2DInt dest = warehouse == null ? order.getDestination() : warehouse.getPosition();
		return drone.getCurrentPosition().euclidianDistanceCeil(dest)+1;
	}

	public void print(StringBuilder sb) {
		sb.append(drone.getId());
		sb.append(' ');
		sb.append(type);
		sb.append(' ');
		sb.append(warehouse==null ? order.getId() : warehouse.getId());
		sb.append(' ');
		sb.append(itemType.getId());
		sb.append(' ');
		sb.append(amount);
		sb.append('\n');
	}

}
