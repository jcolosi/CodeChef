package jjc.codechef.beautifularrays;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * https://www.codechef.com/problems/ICPC16B
 */
public class MainTest {
	Random random = new Random(0);

	@Test
	public void test1() {
		int[] test = new int[100000];
		for (int i = 0; i < test.length; i++) {
			test[i] = random.nextInt(2);
		}
		System.out.println(Main.isBeautiful(test) ? "yes" : "no");
	}

	@Test
	public void test2() {
		int[] test = new int[100000];
		for (int i = 0; i < test.length; i++) {
			test[i] = i;
		}
		System.out.println(Main.isBeautiful(test) ? "yes" : "no");
	}

	@Test
	public void test3() {
		for (int i = 0; i < 100000; i++) {
			_test3();
		}
	}

	private void _test3() {
		int[] test = new int[5];
		for (int i = 0; i < test.length; i++) {
			test[i] = random.nextInt(4);
			if (test[i] == 3) test[i] = -1;
		}
		boolean old = Main.isBeautifulHash(test);
		boolean now = Main.isBeautiful(test);
		if (old != now) {
			System.out.println(toString(test) + ": " + old + "/" + now);
		}

	}

	public String toString(int[] array) {
		StringBuilder out = new StringBuilder();
		for (int item : array) {
			out.append(" " + item);
		}
		return out.toString();
	}

}
