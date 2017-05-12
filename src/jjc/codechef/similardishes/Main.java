package jjc.codechef.similardishes;

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
			String[] a = r.readLine().split(" ");
			String[] b = r.readLine().split(" ");
			System.out.println(isSimilar(a, b) ? "similar" : "dissimilar");
		}
	}

	static public boolean isSimilar(final String[] a, final String[] b) {
		HashSet<String> set = new HashSet<String>();
		for (String x : a) {
			set.add(x);
		}

		int count = 0;
		for (String x : b) {
			if (set.contains(x)) count++;
		}
		return (count > 1);

	}

}