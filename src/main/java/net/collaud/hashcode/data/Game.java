package net.collaud.hashcode.data;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.collaud.hashcode.common.data.Matrix;

/**
 *
 * @author Gaetan Collaud
 */
@Getter
@Setter
public class Game {
	private int nbTurns;
	private List<Drone> drones;
	private List<ItemType> itemTypes;
	private List<Warehouse> warehouses;
	private List<Order> orders;
	private Matrix<Integer> matrix;
	
	
	
}
