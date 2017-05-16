package jjc.codechef.knots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jjc.codechef.knots.Cell.Dir;

/**
 * https://www.codechef.com/problems/ACMKANPA
 */
public class Main2 {

	static public void main(String[] args) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		int caseNum = 0;
		while (true) {
			caseNum++;
			String line = r.readLine();
			if (line == null) break;
			String[] rc = line.split(" ");
			int rows = Integer.parseInt(rc[0]);
			int cols = Integer.parseInt(rc[1]);
			Matrix matrix = new Matrix(rows, cols);
			for (int thisRow = 0; thisRow < rows; thisRow++) {
				matrix.applyRow(thisRow, r.readLine());
			}
			String result = matrix.isKnot() ? "knotted" : "straightened";
			System.out.format("Case %d: %s%n", caseNum, result);
		}
	}

}

class Matrix {
	Cell cells[][];
	int rows;
	int cols;
	Cell endA, endB;

	public Matrix(int r, int c) {
		rows = r;
		cols = c;
		cells = new Cell[r][c];
		for (int thisRow = 0; thisRow < rows; thisRow++) {
			for (int thisCol = 0; thisCol < cols; thisCol++) {
				cells[thisRow][thisCol] = new Cell();
			}
		}
		for (int thisRow = 0; thisRow < rows; thisRow++) {
			for (int thisCol = 0; thisCol < cols; thisCol++) {
				if (thisRow == 0) cells[thisRow][thisCol].u = null;
				else cells[thisRow][thisCol].u = cells[thisRow - 1][thisCol];
				if (thisRow == rows - 1) cells[thisRow][thisCol].d = null;
				else cells[thisRow][thisCol].d = cells[thisRow + 1][thisCol];
				if (thisCol == 0) cells[thisRow][thisCol].l = null;
				else cells[thisRow][thisCol].l = cells[thisRow][thisCol - 1];
				if (thisCol == cols - 1) cells[thisRow][thisCol].r = null;
				else cells[thisRow][thisCol].r = cells[thisRow][thisCol + 1];
			}
		}
	}

	public void findEnds() {
		List<Cell> ends = new ArrayList<Cell>();
		for (int thisRow = 0; thisRow < rows; thisRow++) {
			if (cells[thisRow][0].c == '*') {
				ends.add(cells[thisRow][0]);
				cells[thisRow][0].last = Dir.LEFT;
			}
			if (cells[thisRow][cols - 1].c == '*') {
				ends.add(cells[thisRow][cols - 1]);
				cells[thisRow][cols - 1].last = Dir.RIGHT;
			}
		}
		for (int thisCol = 1; thisCol < cols - 1; thisCol++) {
			if (cells[0][thisCol].c == '*') {
				ends.add(cells[0][thisCol]);
				cells[0][thisCol].last = Dir.UP;
			}
			if (cells[rows - 1][thisCol].c == '*') {
				ends.add(cells[rows - 1][thisCol]);
				cells[rows - 1][thisCol].last = Dir.DOWN;
			}
		}
		endA = ends.get(0);
		endB = ends.get(1);
	}

	public String getCrux() {
		StringBuilder out = new StringBuilder();
		Cell pointer = endA;

		while (pointer != endB) {
			Cell next = null;
			if (pointer.hasRight() && pointer.last != Dir.RIGHT) {
				next = pointer.r;
				if (next.c == '-') out.append('o');
			} else if (pointer.hasDown() && pointer.last != Dir.DOWN) next = pointer.d;
			else if (pointer.hasLeft() && pointer.last != Dir.LEFT) next = pointer.l;
			else next = pointer.u;
		}

		List<Cell> ends = new ArrayList<Cell>();
		for (int thisRow = 0; thisRow < rows; thisRow++) {
			if (cells[thisRow][0].c == '*') ends.add(cells[thisRow][0]);
			if (cells[thisRow][cols - 1].c == '*') ends.add(cells[thisRow][cols - 1]);
		}
		for (int thisCol = 1; thisCol < cols - 1; thisCol++) {
			if (cells[0][thisCol].c == '*') ends.add(cells[0][thisCol]);
			if (cells[rows - 1][thisCol].c == '*') ends.add(cells[rows - 1][thisCol]);
		}
		endA = ends.get(0);
		endB = ends.get(1);
	}

	// public void applyRowSimple(int row, String data) {
	// char[] array = data.toCharArray();
	// for (int thisCol = 0; thisCol < array.length; thisCol++) {
	// cells[row][thisCol].c = array[thisCol];
	// }
	// }

	public void applyRow(int row, String data) {
		char[] array = data.toCharArray();
		for (int thisCol = 0; thisCol < array.length; thisCol++) {
			char x = array[thisCol];
			if (x == '|' || x == '-' || x == '+') cells[row][thisCol].c = '*';
			else if (x == 'I') cells[row][thisCol].c = '|';
			else if (x == 'H') cells[row][thisCol].c = '-';
		}
	}

	public void reset() {
		for (int thisRow = 0; thisRow < rows; thisRow++) {
			for (int thisCol = 0; thisCol < cols; thisCol++) {
				cells[thisRow][thisCol].reset();
			}
		}
	}

	// public boolean isKnot() {
	// reset();
	// // Try up/down
	// Cell a = endA;
	// Cell b = endB;
	// while (true) {
	// // this is so simple to describe. Here it needs to be codified
	// if (a.u.isString())
	//
	// }
	//
	// reset();
	// // Try down/up
	//
	// }
}

class Cell {
	public enum Dir {
		UP, LEFT, DOWN, RIGHT
	}

	public char c; // " " * | -
	public Cell u, d, l, r;
	public boolean cleared = false;
	public Dir last = null;
	public int id;

	public Cell() {
		c = ' ';
	}

	public void reset() {
		cleared = false;
	}

	public void clear() {
		cleared = true;
	}

	public boolean isString() {
		return c == '*';
	}

	public boolean hasUp() {
		return has(u);
	}

	public boolean hasDown() {
		return has(d);
	}

	public boolean hasLeft() {
		return has(l);
	}

	public boolean hasRight() {
		return has(r);
	}

	private boolean has(Cell other) {
		return other != null && other.c != ' ';
	}
}