package jjc.codechef.knots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * https://www.codechef.com/problems/ACMKANPA
 */
public class Main5 {

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
			Main5 main = new Main5();
			Matrix matrix = main.new Matrix(rows, cols);
			for (int thisRow = 0; thisRow < rows; thisRow++) {
				matrix.applyRowSimple(thisRow, r.readLine());
			}
			// matrix.findEnds();
			// System.out.format("Case %d: %s%n", caseNum,
			// matrix.getMatrixString());
			matrix.buildCrux();
			// System.out.format("Case %d: %s%n", caseNum,
			// matrix.getCruxString());

			String result = matrix.isKnot() ? "knotted" : "straightened";
			System.out.format("Case %d: %s%n", caseNum, result);
		}
	}

	public enum Dir {
		UP, LEFT, DOWN, RIGHT
	}

	public enum Kind {
		OVER, UNDER
	}

	class Matrix {

		Cell cells[][];
		int rows;
		int cols;
		Cell endA, endB;
		private ArrayList<Turn> turns = null;
		private int cellId = 1;

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

		public void applyRowSimple(int row, String data) {
			char[] array = data.trim().toCharArray();
			for (int thisCol = 0; thisCol < array.length; thisCol++) {
				cells[row][thisCol].c = array[thisCol];
				cells[row][thisCol].id = cellId++;
			}
		}

		// public void findEnds() {
		// List<Cell> ends = new ArrayList<Cell>();
		// for (int thisRow = 0; thisRow < rows; thisRow++) {
		// if (cells[thisRow][0].c == '-') {
		// ends.add(cells[thisRow][0]);
		// }
		// if (cells[thisRow][cols - 1].c == '-') {
		// ends.add(cells[thisRow][cols - 1]);
		// }
		// }
		// for (int thisCol = 1; thisCol < cols - 1; thisCol++) {
		// if (cells[0][thisCol].c == '|') {
		// ends.add(cells[0][thisCol]);
		// // cells[0][thisCol].last = Dir.UP;
		// }
		// if (cells[rows - 1][thisCol].c == '|') {
		// ends.add(cells[rows - 1][thisCol]);
		// // cells[rows - 1][thisCol].last = Dir.DOWN;
		// }
		// }
		// endA = ends.get(0);
		// endB = ends.get(1);
		// // System.out.println("A >>> " + endA.id);
		// // System.out.println("B >>> " + endB.id);
		// }

		public void buildCrux() {
			turns = new ArrayList<Turn>();
			Cell pointer = null;
			Dir last = null;

			for (int thisRow = 0; thisRow < rows; thisRow++) {
				if (cells[thisRow][0].c == '-') {
					pointer = cells[thisRow][0];
					last = Dir.LEFT;
					break;
				}
				if (cells[thisRow][cols - 1].c == '-') {
					pointer = cells[thisRow][cols - 1];
					last = Dir.RIGHT;
					break;
				}
			}
			if (pointer == null) {
				for (int thisCol = 1; thisCol < cols - 1; thisCol++) {
					if (cells[0][thisCol].c == '|') {
						pointer = cells[0][thisCol];
						last = Dir.DOWN;
						break;
					}
					if (cells[rows - 1][thisCol].c == '|') {
						pointer = cells[rows - 1][thisCol];
						last = Dir.UP;
						break;
					}
				}
			}

			// . | - + H I
			// TODO
			int steps = 0;
			while (true) {
				steps++;
				// int thisId = pointer.id; // DEBUG
				// int thisTurns = turns.size(); // DEBUG
				// System.out.print(" " + pointer.id); // DEBUG

				char c = pointer.c;
				if (c == '-') {
					if (last == Dir.LEFT) pointer = pointer.r;
					else if (last == Dir.RIGHT) pointer = pointer.l;
				} else if (c == '|') {
					if (last == Dir.UP) pointer = pointer.d;
					else if (last == Dir.DOWN) pointer = pointer.u;
				} else if (c == '+') {
					if (last == Dir.UP || last == Dir.DOWN) {
						if (pointer.hasLeft() && pointer.l.c == '-') {
							pointer = pointer.l;
							last = Dir.RIGHT;
						} else {
							pointer = pointer.r;
							last = Dir.LEFT;
						}
					} else if (last == Dir.LEFT || last == Dir.RIGHT) {
						if (pointer.hasUp() && pointer.u.c == '|') {
							pointer = pointer.u;
							last = Dir.DOWN;
						} else {
							pointer = pointer.d;
							last = Dir.UP;
						}
					}
				} else if (c == 'H') {
					if (last == Dir.LEFT) {
						turns.add(new Turn(Kind.OVER, pointer.id));
						pointer = pointer.r;
					} else if (last == Dir.RIGHT) {
						turns.add(new Turn(Kind.OVER, pointer.id));
						pointer = pointer.l;
					} else if (last == Dir.UP) {
						turns.add(new Turn(Kind.UNDER, pointer.id));
						pointer = pointer.d;
					} else if (last == Dir.DOWN) {
						turns.add(new Turn(Kind.UNDER, pointer.id));
						pointer = pointer.u;
					}
				} else if (c == 'I') {
					if (last == Dir.LEFT) {
						turns.add(new Turn(Kind.UNDER, pointer.id));
						pointer = pointer.r;
					} else if (last == Dir.RIGHT) {
						turns.add(new Turn(Kind.UNDER, pointer.id));
						pointer = pointer.l;
					} else if (last == Dir.UP) {
						turns.add(new Turn(Kind.OVER, pointer.id));
						pointer = pointer.d;
					} else if (last == Dir.DOWN) {
						turns.add(new Turn(Kind.OVER, pointer.id));
						pointer = pointer.u;
					}
				}

				// Break out, we're at the end
				if (pointer.c == '-' && last == Dir.LEFT && pointer.r == null) break;
				if (pointer.c == '-' && last == Dir.RIGHT && pointer.l == null) break;
				if (pointer.c == '|' && last == Dir.DOWN && pointer.u == null) break;
				if (pointer.c == '|' && last == Dir.UP && pointer.d == null) break;

				// DEBUG
				// if (turns.size() > thisTurns) System.out.println(" >>> " +
				// getCruxString()); // DEBUG
				// if (pointer.id == thisId) {
				// System.out.println(" >>> DAG GUM IT, BAILOUT!!!");
				// break;
				// }

				// DEBUG
				if (steps > 1000) {
					turns = new ArrayList<Turn>();
					break;
				}
			}

			// System.out.println(" >>> Complete at " + pointer.id); // DEBUG

		}

		public void buildCruxX() {
			turns = new ArrayList<Turn>();
			Cell pointer = endA;

			Dir last = null;
			if (pointer.l == null) last = Dir.LEFT;
			else if (pointer.r == null) last = Dir.RIGHT;
			else if (pointer.u == null) last = Dir.UP;
			else if (pointer.d == null) last = Dir.DOWN;

			// . | - + H I
			// TODO
			while (pointer != endB) {
				// int thisId = pointer.id; // DEBUG
				// int thisTurns = turns.size(); // DEBUG
				// System.out.print(" " + pointer.id); // DEBUG

				char c = pointer.c;
				if (c == '-') {
					if (last == Dir.LEFT) pointer = pointer.r;
					else if (last == Dir.RIGHT) pointer = pointer.l;
				} else if (c == '|') {
					if (last == Dir.UP) pointer = pointer.d;
					else if (last == Dir.DOWN) pointer = pointer.u;
				} else if (c == '+') {
					if (last == Dir.UP || last == Dir.DOWN) {
						if (pointer.hasLeft() && pointer.l.c == '-') {
							pointer = pointer.l;
							last = Dir.RIGHT;
						} else {
							pointer = pointer.r;
							last = Dir.LEFT;
						}
					} else if (last == Dir.LEFT || last == Dir.RIGHT) {
						if (pointer.hasUp() && pointer.u.c == '|') {
							pointer = pointer.u;
							last = Dir.DOWN;
						} else {
							pointer = pointer.d;
							last = Dir.UP;
						}
					}
				} else if (c == 'H') {
					if (last == Dir.LEFT) {
						turns.add(new Turn(Kind.OVER, pointer.id));
						pointer = pointer.r;
					} else if (last == Dir.RIGHT) {
						turns.add(new Turn(Kind.OVER, pointer.id));
						pointer = pointer.l;
					} else if (last == Dir.UP) {
						turns.add(new Turn(Kind.UNDER, pointer.id));
						pointer = pointer.d;
					} else if (last == Dir.DOWN) {
						turns.add(new Turn(Kind.UNDER, pointer.id));
						pointer = pointer.u;
					}
				} else if (c == 'I') {
					if (last == Dir.LEFT) {
						turns.add(new Turn(Kind.UNDER, pointer.id));
						pointer = pointer.r;
					} else if (last == Dir.RIGHT) {
						turns.add(new Turn(Kind.UNDER, pointer.id));
						pointer = pointer.l;
					} else if (last == Dir.UP) {
						turns.add(new Turn(Kind.OVER, pointer.id));
						pointer = pointer.d;
					} else if (last == Dir.DOWN) {
						turns.add(new Turn(Kind.OVER, pointer.id));
						pointer = pointer.u;
					}
				}

				// DEBUG
				// if (turns.size() > thisTurns) System.out.println(" >>> " +
				// getCruxString()); // DEBUG
				// if (pointer.id == thisId) {
				// System.out.println(" >>> DAG GUM IT, BAILOUT!!!");
				// break;
				// }
			}
		}

		public boolean isKnot() {
			return isKnot(Kind.OVER, Kind.OVER) && isKnot(Kind.OVER, Kind.UNDER) && isKnot(Kind.UNDER, Kind.OVER)
					&& isKnot(Kind.UNDER, Kind.UNDER);
		}

		private boolean isKnot(Kind aKind, Kind bKind) {
			int size = turns.size();
			int a = 0;
			int b = size - 1;
			HashSet<Integer> cleared = new HashSet<>();

			// TODO
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

	}

	class Turn {

		public Kind kind;
		public int nexus;

		public Turn(Kind kind, int nexus) {
			this.kind = kind;
			this.nexus = nexus;
		}

	}

	class Cell {

		public char c; // . | - + H I
		public Cell u, d, l, r;
		public int id;

		public boolean equals(Object o) {
			return (o != null && o instanceof Cell && ((Cell) o).id == id);
		}

		public Cell() {
			c = '.';
		}

		public boolean hasUp() {
			return u != null && u.c != '.';
		}

		public boolean hasDown() {
			return d != null && d.c != '.';
		}

		public boolean hasLeft() {
			return l != null && l.c != '.';
		}

		public boolean hasRight() {
			return r != null && r.c != '.';
		}
	}

}
