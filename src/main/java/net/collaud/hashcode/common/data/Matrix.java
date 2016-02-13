package net.collaud.hashcode.common.data;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author Gaetan Collaud
 * @param <T>
 */
public class Matrix<T> {
	private final List<List<T>> matrix;
	
	@Getter
	private final int rows;
	
	@Getter
	private final int columns;

	public Matrix(int rows, int columns, T initialValue) {
		this.rows = rows;
		this.columns = columns;
		matrix = new ArrayList<>(rows);
		for (int i = 0; i < rows; i++) {
			final List<T> arrRow = new ArrayList<>(columns);
			matrix.add(arrRow);
			for (int j = 0; j < columns; j++) {
				arrRow.add(initialValue);
			}
		}
	}
	
}
