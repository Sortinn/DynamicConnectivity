package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Scanner;

public class Percolation {

    private WeightedQuickUnionUF uf;
    private boolean[][] grid;
    private int size;
    private int bottom;
    private int count;

    public Percolation(int N) {      // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new IllegalArgumentException(N + " is illegal!");
        }
        this.grid = new boolean[N][N];
        this.size = N;
        this.bottom = N * N + 1;
        uf = new WeightedQuickUnionUF(N * N + 2);


    }

    public void open(int i, int j) {// open site (row i, column j) if it is not already
        if (isOpen(i, j)) {
            return;
        }

        if (!isOpen(i, j)) {
            grid[i - 1][j - 1] = true;//如果是阻塞的，那就打开它
            count++;

            int site = gridSite(i, j);

            if (i == 1) {
                uf.union(0, site);
            }

            if (i == site) {
                uf.union(bottom, site);
            }

            int rowOfSite, columnOfSite;
            for (int k = 0; k < 4; k++) {
                rowOfSite = i;
                columnOfSite = j;
                switch (k) {
                    case 0:
                        columnOfSite--;  //site左边的格子
                        break;
                    case 1:
                        rowOfSite--;   //site上边的格子
                        break;
                    case 2:
                        columnOfSite++; //site右边的格子
                        break;
                    case 3:
                        rowOfSite++;    //site下边的格子
                        break;
                }

                if (isValidGrid(rowOfSite, columnOfSite)) {
                    if (isOpen(rowOfSite, columnOfSite)) {      //如果site四围的某一个格子是打开的,就连接site和该格子
                        int relation = gridSite(rowOfSite, columnOfSite);
                        uf.union(relation, site);
                    }
                }
            }
        }
    }


    public boolean isOpen(int i, int j) {   //is site (row i, column j) open?
        validate(i, j);
        return grid[i - 1][j - 1];
    }


    public boolean isFull(int i, int j) {       //is site full?
        validate(i, j);
        int site = gridSite(i, j);
        return uf.connected(site, 0);       //如果该格子和顶部的虚拟格子(用0表示，即和第一排所有格子)相连通,返回true
    }

    public boolean percolates() {         //does the system percolate?
        return uf.connected(0, bottom);
    }

    public boolean isValidIndex(int index) {
        return index <= size && index > 0;
    }

    public boolean isValidGrid(int i, int j) {
        return isValidIndex(i) && isValidIndex(j);
    }

    public void validate(int i, int j) {
        if (!isValidGrid(i, j)) {
            throw new IndexOutOfBoundsException("Invalid " + i + " and " + j);
        }
    }

    public int gridSite(int i, int j) {
        return (i - 1) * size + j;
    }


    public void result(int n, Percolation test) {
        while (!test.percolates()) {
            int r = 1 + (int) (Math.random() * n);
            System.out.println("Row number:" + r);

            int c = 1 + (int) (Math.random() * n);
            System.out.println("Column number:" + c);
            if (test.isOpen(r, c)) {
                System.out.println("(" + r + "," + c + ")" + " is open." + "The grid has " + test.count + " open sites.");
            } else {
                test.open(r, c);
                System.out.println("(" + r + "," + c + ")" + " has been opened." + "The grid has " + test.count + " open sites.");
            }
            System.out.println();
        }
        System.out.println("System has percolated.Totally " + test.count + " open sites.");
    }

    public double threshold(int n, Percolation test) {      //渗透阈值的估计
        while (!test.percolates()) {
            int r = 1 + (int) (Math.random() * n);

            int c = 1 + (int) (Math.random() * n);
            if (test.isOpen(r, c)) {

            } else {
                test.open(r, c);
            }
        }
        return (double) count / (n * n);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a number to generate a system:");
        int N = input.nextInt();
        Percolation test = new Percolation(N);
        test.result(N, test);
        System.out.println(test.threshold(N, test));
    }


}
