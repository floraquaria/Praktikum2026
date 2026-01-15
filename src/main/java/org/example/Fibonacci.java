package org.example;

public class Fibonacci {
    public static void main(String[] args) {
        int n = 10; // Change this value to compute a different Fibonacci number
        int result = fibonacci(n);
        System.out.printf("Fibonacci of %d is %d%n", n, result);
    }

    public static int fibonacci(int n) {
        if (n <= 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
}
