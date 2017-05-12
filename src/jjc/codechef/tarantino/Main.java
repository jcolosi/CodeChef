package jjc.codechef.tarantino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

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
			System.out.println(isTarantino(input) ? "yes" : "no");
		}
	}

	static public boolean isTarantino(final int[] input) {
		TreeSet<Integer> set = new TreeSet<Integer>();
		int last = 0;
		boolean order = true;
		for (int x : input) {
			set.add(x);
			if (x != last + 1) order = false;
			last = x;
		}
		if (order) return false;
		if (set.size() < input.length) return false;
		if (set.last() > input.length) return false;

		return true;
	}

}