package jjc.codechef.beautifularrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Main {

	static public void main(String[] args) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(r.readLine());

		int count = 0;
		while (count++ < T) {
			int len = Integer.parseInt(r.readLine());
			String[] inputStrings = r.readLine().split(" ");
			int[] input = new int[len];
			for (int i = 0; i < len; i++) {
				input[i] = Integer.parseInt(inputStrings[i]);
			}
			System.out.println(isBeautiful(input) ? "yes" : "no");
		}
	}

	static public boolean isBeautifulHash(final int[] input) {
		int len = input.length;

		HashSet<Integer> set = new HashSet<Integer>();
		for (int x : input) {
			set.add(x);
		}

		for (int i = 0; i < len; i++) {
			for (int j = i + 1; j < len; j++) {
				int n = input[i] * input[j];
				if (!set.contains(n)) return false;
			}
		}
		return true;
	}

	static public boolean isBeautiful(final int[] input) {
		if (input.length < 2) return true;
		int o = 0; // Other
		int n = 0; // -1
		int z = 0; // 0
		int p = 0; // 1
		for (int x : input) {
			if (x == -1) {
				n++;
				if (o > 0) return false;
			} else if (x == 0) z++;
			else if (x == 1) p++;
			else {
				o++;
				if (o > 1) return false;
				if (n > 0) return false;
			}
		}
		if (n > 1 && p == 0) return false;
		return true;
	}

}
