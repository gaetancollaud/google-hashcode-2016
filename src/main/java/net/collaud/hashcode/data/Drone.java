package net.collaud.hashcode.data;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.collaud.hashcode.common.data.Point2DInt;

/**
 *
 * @author Gaetan Collaud
 */
@RequiredArgsConstructor
@Getter
@Setter
@ToString(exclude = "inTrunk")
public class Drone {
	private final int id;
	private final int trunkSize;
	
	private Point2DInt currentPosition;
	private int busyUntilTurn = 0;
	
	private final Map<ItemType, Integer> inTrunk = new HashMap<>();
	
	public void load(ItemType type, int nb){
		if(!inTrunk.containsKey(type)){
			inTrunk.put(type, nb);
		}else{
			inTrunk.put(type, inTrunk.get(type)+nb);
		}
	}
	
	public void unloadAll(){
		inTrunk.clear();
	}
	
	public int trunkSizeLeft(){
		return trunkSize-currentTrunkSize();
	}
	
	private int currentTrunkSize(){
		return inTrunk.entrySet().stream()
				.mapToInt(es -> es.getKey().getWeight()*es.getValue())
				.sum();
	}
}
