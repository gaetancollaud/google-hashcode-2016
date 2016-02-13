package net.collaud.hashcode.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Gaetan Collaud
 */
@RequiredArgsConstructor
@Getter
@ToString
public class ItemType {
	public final int id;
	public final int weight;
}
