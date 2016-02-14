package net.collaud.hashcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.collaud.hashcode.common.data.Matrix;
import net.collaud.hashcode.common.data.Point2DInt;
import net.collaud.hashcode.common.reader.InputReader;
import net.collaud.hashcode.common.writer.OutputWriter;
import net.collaud.hashcode.data.Command;
import net.collaud.hashcode.data.Drone;
import net.collaud.hashcode.data.Game;
import net.collaud.hashcode.data.ItemType;
import net.collaud.hashcode.data.Order;
import net.collaud.hashcode.data.Warehouse;

/**
 *
 * @author Gaetan Collaud
 */
@RequiredArgsConstructor
public class HashCode {

	private final String inputFile;
	private final String outputFile;
	private final Game game = new Game();
	private final List<Command> commands = new ArrayList<>();

	public void solve() {
		long start = System.currentTimeMillis();
		readInput();
		doSolve();
		writeOutput();
		long end = System.currentTimeMillis();

		System.out.println("Solve " + inputFile + " took " + ((end - start) / 1000) + "s");
	}

	private void doSolve() {
		for (int turn = 0; turn < game.getNbTurns(); turn++) {
			for (Drone drone : game.getDrones()) {
				if (drone.getBusyUntilTurn() < turn) {
					//assign operation
					final Optional<Order> optOrder = getBestOrderForDrone(drone);
					if (optOrder.isPresent()) {
						executeOrderForDrone(game, turn, drone, optOrder.get());
					}
				}
			}
		}
	}

	private Optional<Order> getBestOrderForDrone(Drone d) {
		if (game.getOrders().isEmpty()) {
			return Optional.empty();
		}
//		Collections.sort(game.getOrders(), Comparator.comparingInt(o -> o.getDestination().euclidianDistanceCeil(d.getCurrentPosition())));$
		try {
			Collections.sort(game.getOrders(), Comparator.comparingInt(o -> commandsCost(o.makeCommandsForThisOrder(game, d, false))));
		} catch (Exception ex) {
		}
		return Optional.of(game.getOrders().remove(0));
	}

	private int commandsCost(List<Command> list) {
		int cost = 0;
		Command lastCommand = null;
		for (Command command : list) {
			cost += command.computeCost();
			if (lastCommand != null) {
				if (command.samePlace(lastCommand)) {
					cost--;
				}
			}

			lastCommand = command;
		}
		return cost;
	}

	private void executeOrderForDrone(Game game, int turn, Drone drone, Order order) {
		List<Command> listC = order.makeCommandsForThisOrder(game, drone, true);
		final int endTurn = turn + commandsCost(listC);
		drone.setBusyUntilTurn(endTurn);
		commands.addAll(listC);
	}

	private void readInput() {
		InputReader reader = new InputReader(inputFile);
		final List<List<Integer>> lines = reader.getLines();
		int rows = lines.get(0).get(0);
		int colums = lines.get(0).get(1);
		int nbDrones = lines.get(0).get(2);
		int turns = lines.get(0).get(3);
		int dronePayload = lines.get(0).get(4);

		//matrix
		game.setMatrix(new Matrix<>(rows, colums, 0));

		//drone
		List<Drone> drones = new ArrayList<>(nbDrones);
		for (int i = 0; i < nbDrones; i++) {
			drones.add(new Drone(i, dronePayload));
		}
		game.setDrones(drones);

		game.setNbTurns(turns);

		//types
		int nbTypes = lines.get(1).get(0);
		List<ItemType> types = new ArrayList<>(nbTypes);
		for (int i = 0; i < nbTypes; i++) {
			types.add(new ItemType(i, lines.get(2).get(i)));
		}
		game.setItemTypes(types);

		//warehouses
		int nbWarehouses = lines.get(3).get(0);
		List<Warehouse> warehouses = new ArrayList<>(nbWarehouses);
		for (int i = 0; i < nbWarehouses; i++) {
			int iS = 4 + i * 2;
			Warehouse w = new Warehouse(i, new Point2DInt(lines.get(iS).get(0), lines.get(iS).get(1)));
			for (int j = 0; j < nbTypes; j++) {
				w.addStock(types.get(j), lines.get(iS + 1).get(j));
			}
			warehouses.add(w);
		}
		game.setWarehouses(warehouses);

		int start = 4 + 2 * nbWarehouses;
		int nbOrders = lines.get(start++).get(0);
		List<Order> orders = new LinkedList<>();
		for (int i = 0; i < nbOrders; i++) {
			int iS = start + i * 3;
			Order order = new Order(i, new Point2DInt(lines.get(iS).get(0), lines.get(iS).get(1)));
			int nbItems = lines.get(iS + 1).get(0);
			for (int j = 0; j < nbItems; j++) {
				order.addItem(types.get(lines.get(iS + 2).get(j)), 1);
			}
			orders.add(order);
		}

		drones.forEach(d -> d.setCurrentPosition(new Point2DInt(warehouses.get(0).getPosition())));

		game.setOrders(orders);
	}

	private void writeOutput() {
		OutputWriter ow = new OutputWriter(outputFile);
		StringBuilder sb = ow.getStringBuilder();
		sb.append(commands.size()).append('\n');
		for (Command command : commands) {
			command.print(sb);
		}
		ow.writeFile();
		//System.out.println(ow.getContent());
	}
}
