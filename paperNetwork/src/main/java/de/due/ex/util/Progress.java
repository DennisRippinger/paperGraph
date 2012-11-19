package de.due.ex.util;

public class Progress {
	public static void updateProgress(Integer current, Integer maximum) {
		final int width = 50; // progress bar width in chars

		double currentDouble = (double) current;
		double maximumDouble = (double) maximum;

		double progressPercentage = currentDouble / maximumDouble;

		System.out.print("\r[");
		int i = 0;
		for (; i <= (int) (progressPercentage * width); i++) {
			System.out.print(".");
		}
		for (; i < width; i++) {
			System.out.print(" ");
		}
		System.out.print("]");
	}
}