package jjc.codechef.colorfulgrid;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * This is not my solution. I spent a lot of time trying to figure out how to
 * account for the various symmetries in this problem. This solution just has a
 * magical constant 250000002. I'm not sure why it works. FWIW, my spreadsheet
 * had some of these values, like C^5.
 *
 */
public class Main2 {

	static final long MOD = 1000000007L;
	static final BigInteger zero = new BigInteger(String.valueOf(0));
	static final BigInteger one = new BigInteger(String.valueOf(1));
	static final BigInteger two = new BigInteger(String.valueOf(2));
	static final BigInteger three = new BigInteger(String.valueOf(3));
	static final BigInteger four = new BigInteger(String.valueOf(4));

	static long pow(long base, BigInteger exp) {
		if (exp.equals(zero)) {
			return 1;
		}

		long nextBase = (base * base) % MOD;
		if (exp.mod(two).equals(one)) {
			return (base * pow(nextBase, exp.divide(two))) % MOD;
		}

		return pow(nextBase, exp.divide(two));
	}

	static long colorfulGrids(BigInteger N, long C) {
		final BigInteger NQ = N.multiply(N);
		final BigInteger NQ5 = NQ.multiply(new BigInteger(String.valueOf(5)));
		long result;

		if (N.mod(two).equals(one)) {
			result = pow(C, NQ5) + pow(C, NQ5.add(three).divide(four)) * 2 + pow(C, NQ5.add(one).divide(two));
			System.out.print(">>> " + result + "   ");
		} else {
			result = pow(C, NQ5) + pow(C, NQ5.divide(four)) * 2 + pow(C, NQ5.divide(two));
		}

		return ((result % MOD) * 250000002) % MOD;
	}

	public static void main(String[] args) {
		for (int i = 2; i < 21; i++) {
			System.out.println(colorfulGrids(new BigInteger("1"), i));
		}
	}

	public static void main2(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int T = scanner.nextInt();

		for (int i = 0; i < T; i++) {
			BigInteger N = scanner.nextBigInteger();
			long C = scanner.nextLong();

			System.out.println(colorfulGrids(N, C));
		}

	}
}