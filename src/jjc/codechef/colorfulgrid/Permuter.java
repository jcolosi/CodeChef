package jjc.codechef.colorfulgrid;

import java.util.HashSet;

/**
 * c=2 h=12<br>
 * c=3 h=72<br>
 * c=4 h=280<br>
 * c=5 h=825<br>
 * c=6 h=2016<br>
 * c=7 h=4312<br>
 * c=8 h=8352<br>
 * c=9 h=14985<br>
 * c=10 h=25300<br>
 * c=11 h=40656<br>
 * c=12 h=62712<br>
 * c=13 h=93457<br>
 * c=14 h=135240<br>
 * c=15 h=190800<br>
 * c=16 h=263296<br>
 * c=17 h=356337<br>
 * c=18 h=474012<br>
 * c=19 h=620920<br>
 * c=20 h=802200<br>
 */
public class Permuter {

	static public void main(String[] args) {
		for (int i = 2; i < 21; i++) {
			permute(i);
		}
	}

	static public void permute(int c) {
		int[] input = new int[5];
		HashSet<String> hash = new HashSet<String>();
		do {
			int[] test = copy(input);
			if (!hash.contains(toString(test))) {
				rotate(test);
				if (!hash.contains(toString(test))) {
					rotate(test);
					if (!hash.contains(toString(test))) {
						rotate(test);
						if (!hash.contains(toString(test))) {
							hash.add(toString(input));
						}
					}
				}
			}
			next(input, c);
		} while (!isEmpty(input));
		System.out.format("c=%d h=%d%n", c, hash.size());
		// readout(hash);
	}

	static public void rotate(int[] input) {
		int tmp = input[3];
		input[3] = input[2];
		input[2] = input[1];
		input[1] = input[0];
		input[0] = tmp;
	}

	static public void next(int[] input, int c) {
		if (input[4] + 1 < c) input[4]++;
		else {
			input[4] = 0;
			if (input[3] + 1 < c) input[3]++;
			else {
				input[3] = 0;
				if (input[2] + 1 < c) input[2]++;
				else {
					input[2] = 0;
					if (input[1] + 1 < c) input[1]++;
					else {
						input[1] = 0;
						if (input[0] + 1 < c) input[0]++;
						else input[0] = 0;
					}
				}
			}
		}
	}

	static public boolean isEmpty(int[] input) {
		return (input[0] + input[1] + input[2] + input[3] + input[4]) == 0;
	}

	static public int[] copy(int[] input) {
		int[] out = new int[5];
		out[0] = input[0];
		out[1] = input[1];
		out[2] = input[2];
		out[3] = input[3];
		out[4] = input[4];
		return out;
	}

	static public String toString(int[] input) {
		return new String(input[0] + " " + input[1] + " " + input[2] + " " + input[3] + " " + input[4]);
	}

	static public void readout(HashSet<String> hash) {
		int count = 0;
		for (String s : hash) {
			count++;
			System.out.println("<" + count + ">");
			readout(s);
		}
	}

	static public void readout(String s) {
		char[] c = s.toCharArray();
		System.out.format(" %s %n%s%s%s%n %s%n%n", c[0], c[3], c[4], c[1], c[2]);
	}

}
