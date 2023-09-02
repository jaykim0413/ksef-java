package markov_chain;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of characters: ");
        int m = sc.nextInt();
        System.out.print("Enter the length of the cases to evaluate: ");
        int n = sc.nextInt();

        ExpectedValue E = new ExpectedValue(m ,n);
        E.getInfo();
        sc.close();
    }
}