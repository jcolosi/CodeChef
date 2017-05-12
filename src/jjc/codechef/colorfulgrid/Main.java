package jjc.codechef.colorfulgrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

/**
 * https://www.codechef.com/problems/ICPC16E
 */
public class Main {

	static public void main(String[] args) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(r.readLine());

		int count = 0;
		while (count++ < T) {
			String[] input = r.readLine().split(" ");

			long n = Long.parseLong(input[0]);
			long c = Long.parseLong(input[1]);

			System.out.println(calculate(n, c));
		}
	}

	static public long calculate(final long n, final long c) {
		return 0;
	}

}