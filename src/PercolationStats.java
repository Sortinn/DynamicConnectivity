import percolation.Percolation;

import java.util.ArrayList;
import java.util.Scanner;

public class PercolationStats {
    private double totalOfThreshold ; //阈值之和
    private int times;
    private ArrayList<Double> threshold = new ArrayList<>();

    public PercolationStats(int N, int T) {
        this.times = T;

        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N or T is illegal!");
        }
        while (T > 0) {
            T--;
            Percolation percolation = new Percolation(N);
            totalOfThreshold += percolation.threshold(N, percolation);
            threshold.add(percolation.threshold(N, percolation));
        }

    }

    public double mean() {
        return  totalOfThreshold / times;
    }

    public double stddev() {
        double stddev = 0;  //方差
        for (int i = 0; i < times; i++) {
            stddev += (threshold.get(i) - mean()) * (threshold.get(i) - mean());
        }
        return Math.sqrt(stddev) / (times - 1);
    }

    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(times));
    }

    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(times));
    }

    public static void main(String[] args) {
        System.out.println("Input N and T to start:");
        Scanner input = new Scanner(System.in);
        System.out.println("N is:");
        int N = input.nextInt();
        System.out.println("T is:");
        int T = input.nextInt();
        PercolationStats test = new PercolationStats(N, T);
        System.out.println(T + " times experiences' result:");
        System.out.println("The mean is " + test.mean() + "\n" + "The stddev is " + test.stddev() + "\n" + "" +
                "The confidence is [" + test.confidenceLo() + "," + test.confidenceHi() + "]");
    }
}
