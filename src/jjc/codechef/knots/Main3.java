package jjc.codechef.knots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jjc.codechef.knots.Turn.Kind;

/**
 * https://www.codechef.com/problems/ACMKANPA
 */
public class Main3 {

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
			matrix.findEnds();
			// System.out.format("Case %d: %s%n", caseNum,
			// matrix.getMatrixString());
			matrix.buildCrux();
			// System.out.format("Case %d: %s%n", caseNum,
			// matrix.getCruxString());

			String result = matrix.isKnot() ? "knotted" : "straightened";
			System.out.format("Case %d: %s%n", caseNum, result);
		}
	}

}

class Matrix {
	public enum Dir {
		UP, LEFT, DOWN, RIGHT
	}

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
				// cells[thisRow][0].last = Dir.LEFT;
			}
			if (cells[thisRow][cols - 1].c == '*') {
				ends.add(cells[thisRow][cols - 1]);
				// cells[thisRow][cols - 1].last = Dir.RIGHT;
			}
		}
		for (int thisCol = 1; thisCol < cols - 1; thisCol++) {
			if (cells[0][thisCol].c == '*') {
				ends.add(cells[0][thisCol]);
				// cells[0][thisCol].last = Dir.UP;
			}
			if (cells[rows - 1][thisCol].c == '*') {
				ends.add(cells[rows - 1][thisCol]);
				// cells[rows - 1][thisCol].last = Dir.DOWN;
			}
		}
		endA = ends.get(0);
		endB = ends.get(1);
	}

	static ArrayList<Turn> turns = null;

	public String getCruxString() {
		StringBuilder out = new StringBuilder();
		for (Turn turn : turns) {
			out.append(turn.kind == Kind.OVER ? 'O' : 'U');
			out.append("[" + turn.nexus + "] ");
		}
		return out.toString();
	}

	public String getMatrixString() {
		StringBuilder out = new StringBuilder();
		for (int thisRow = 0; thisRow < rows; thisRow++) {
			for (int thisCol = 0; thisCol < cols; thisCol++) {
				out.append(cells[thisRow][thisCol].c);
			}
			out.append("\n");
		}
		return out.toString();
	}

	public void buildCrux() {
		turns = new ArrayList<Turn>();
		Cell pointer = endA;

		Dir last = null;
		if (pointer.l == null) last = Dir.LEFT;
		else if (pointer.r == null) last = Dir.RIGHT;
		else if (pointer.u == null) last = Dir.UP;
		else if (pointer.d == null) last = Dir.DOWN;

		while (pointer != endB) {
			// System.out.println(">>> " + pointer.id);

			if (pointer.hasRight() && last != Dir.RIGHT) {
				last = Dir.LEFT;
				pointer = pointer.r;
				if (pointer.c == '-') {
					turns.add(new Turn(Kind.OVER, pointer.id));
					pointer = pointer.r;
				}
				if (pointer.c == '|') {
					turns.add(new Turn(Kind.UNDER, pointer.id));
					pointer = pointer.r;
				}
			} else if (pointer.hasDown() && last != Dir.DOWN) {
				last = Dir.UP;
				pointer = pointer.d;
				if (pointer.c == '-') {
					turns.add(new Turn(Kind.UNDER, pointer.id));
					pointer = pointer.d;
				}
				if (pointer.c == '|') {
					turns.add(new Turn(Kind.OVER, pointer.id));
					pointer = pointer.d;
				}
			} else if (pointer.hasLeft() && last != Dir.LEFT) {
				last = Dir.RIGHT;
				pointer = pointer.l;
				if (pointer.c == '-') {
					turns.add(new Turn(Kind.OVER, pointer.id));
					pointer = pointer.l;
				}
				if (pointer.c == '|') {
					turns.add(new Turn(Kind.UNDER, pointer.id));
					pointer = pointer.l;
				}
			} else {
				last = Dir.DOWN;
				pointer = pointer.u;
				if (pointer.c == '-') {
					turns.add(new Turn(Kind.UNDER, pointer.id));
					pointer = pointer.u;
				}
				if (pointer.c == '|') {
					turns.add(new Turn(Kind.OVER, pointer.id));
					pointer = pointer.u;
				}
			}
			// System.out.println(getCruxString());
		}
	}

	public boolean isKnot() {
		return isKnot(Kind.OVER, Kind.OVER) && isKnot(Kind.OVER, Kind.UNDER) && isKnot(Kind.UNDER, Kind.OVER)
				&& isKnot(Kind.UNDER, Kind.UNDER);
	}

	public boolean isKnot(Kind aKind, Kind bKind) {
		int size = turns.size();
		int a = 0;
		int b = size - 1;
		HashSet<Integer> cleared = new HashSet<>();

		while (a < b) {
			if (turns.get(a).kind == aKind || cleared.contains(turns.get(a).nexus)) {
				cleared.add(turns.get(a).nexus);
				a++;
			} else if (turns.get(b).kind == bKind || cleared.contains(turns.get(b).nexus)) {
				cleared.add(turns.get(b).nexus);
				b--;
			} else return true;
		}
		return false;
	}

	// public void applyRowSimple(int row, String data) {
	// char[] array = data.toCharArray();
	// for (int thisCol = 0; thisCol < array.length; thisCol++) {
	// cells[row][thisCol].c = array[thisCol];
	// }
	// }

	static private int cellId = 0;

	public void applyRow(int row, String data) {
		char[] array = data.toCharArray();
		for (int thisCol = 0; thisCol < array.length; thisCol++) {
			char x = array[thisCol];
			if (x == '|' || x == '-' || x == '+') cells[row][thisCol].c = '*';
			else if (x == 'I') cells[row][thisCol].c = '|';
			else if (x == 'H') cells[row][thisCol].c = '-';
			cells[row][thisCol].id = cellId++;
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

class Turn {
	public enum Kind {
		OVER, UNDER
	}

	public Kind kind;
	public int nexus;

	public Turn(Kind kind, int nexus) {
		this.kind = kind;
		this.nexus = nexus;
	}

}

class Cell {

	public char c; // " " * | -
	public Cell u, d, l, r;
	public boolean cleared = false;
	public int id;

	public boolean equals(Object o) {
		return (o != null && o instanceof Cell && ((Cell) o).id == id);
	}

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