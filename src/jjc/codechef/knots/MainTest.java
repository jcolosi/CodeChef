package jjc.codechef.knots;

import java.util.Random;

import org.junit.Test;

import jjc.codechef.knots.Main.Dir;
import jjc.codechef.knots.Main.Matrix;

public class MainTest {
	static private final int MAX = 40; // 120

	Random rand = new Random(0);

	@Test
	public void test() {
		for (int i = 0; i < 10; i++) {
			Matrix x = getRandomMatrix();
			System.out.println("\n\n>\n" + x.getMatrixString());
			x.buildCrux();
			System.out.format(">> %s%n", x.getCruxString());
			String result = x.isKnot() ? "knotted" : "straightened";
			System.out.format(">>> %s%n", result);
		}
	}

	private Matrix getRandomMatrix() {
		int r = rand.nextInt(MAX - 2) + 2;
		int c = rand.nextInt(MAX - 2) + 2;
		MyPoint.MAXROW = r - 1;
		MyPoint.MAXCOL = c - 1;
		char[][] data = new char[r][c];
		clear(data);
		MyPoint snake = new MyPoint();
		Dir direction;
		if (rand.nextBoolean()) {
			snake.row = rand.nextInt(r - 1);
			snake.col = 0;
			direction = Dir.RIGHT;
			put(data, snake, '-');
		} else {
			snake.row = 0;
			snake.col = rand.nextInt(c - 1);
			direction = Dir.DOWN;
			put(data, snake, '|');
		}
		// . | - + H I
		while (true) {
			snake.move(direction);
			char found = get(data, snake);

			if (direction == Dir.LEFT || direction == Dir.RIGHT) {
				if (found == '|') {
					if (rand.nextBoolean()) put(data, snake, 'I');
					else put(data, snake, 'H');
				} else {
					if (rand.nextDouble() < .25 && isOpenVertical(data, snake)) {
						put(data, snake, '+');
						direction = rand.nextBoolean() ? Dir.UP : Dir.DOWN;
					} else {
						put(data, snake, '-');
					}
				}
			} else {
				if (found == '-') {
					if (rand.nextBoolean()) put(data, snake, 'I');
					else put(data, snake, 'H');
				} else {
					if (rand.nextDouble() < .25 && isOpenHorizontal(data, snake)) {
						put(data, snake, '+');
						direction = rand.nextBoolean() ? Dir.LEFT : Dir.RIGHT;
					} else {
						put(data, snake, '|');
					}
				}
			}
			if (snake.end(direction)) break;
		}
		Main main = new Main();
		Matrix out = main.new Matrix(r, c);
		for (int row = 0; row < r; row++) {
			out.applyRowSimple(row, new String(data[row]));
		}
		return out;
	}

	boolean isOpenVertical(char[][] data, final MyPoint point) {
		MyPoint other;

		other = point.clone();
		if (!other.end(Dir.UP)) {
			other.move(Dir.UP);
			if (get(data, other) != '.') return false;
		}

		other = point.clone();
		if (!other.end(Dir.DOWN)) {
			other.move(Dir.DOWN);
			if (get(data, other) != '.') return false;
		}

		return true;
	}

	boolean isOpenHorizontal(char[][] data, final MyPoint point) {
		MyPoint other;

		other = point.clone();
		if (!other.end(Dir.LEFT)) {
			other.move(Dir.LEFT);
			if (get(data, other) != '.') return false;
		}

		other = point.clone();
		if (!other.end(Dir.RIGHT)) {
			other.move(Dir.RIGHT);
			if (get(data, other) != '.') return false;
		}

		return true;
	}

	void put(char[][] data, MyPoint point, char c) {
		data[point.row][point.col] = c;
	}

	char get(char[][] data, MyPoint point) {
		return data[point.row][point.col];
	}

	void clear(char[][] data) {
		for (int row = 0; row < data.length; row++) {
			for (int col = 0; col < data[0].length; col++) {
				data[row][col] = '.';
			}
		}
	}

}

class MyPoint {
	static public int MAXROW;
	static public int MAXCOL;
	public int row;
	public int col;

	boolean end(Dir dir) {
		if (dir == Dir.UP && row == 0) return true;
		if (dir == Dir.DOWN && row == MAXROW) return true;
		if (dir == Dir.LEFT && col == 0) return true;
		if (dir == Dir.RIGHT && col == MAXCOL) return true;
		return false;
	}

	public void move(Dir dir) {
		if (end(dir)) return;
		if (dir == Dir.UP) row--;
		if (dir == Dir.DOWN) row++;
		if (dir == Dir.LEFT) col--;
		if (dir == Dir.RIGHT) col++;
	}

	public MyPoint clone() {
		MyPoint out = new MyPoint();
		out.row = this.row;
		out.col = this.col;
		return out;
	}

	// public MyPoint(int row, int col) {
	// this.row = row;
	// this.col = col;
	// }
}