import org.example.Fibonacci;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FibonacciTest {
    @Test
    public void testFibonacciNegativ() {
        int result = Fibonacci.fibonacci(-1);
        assertEquals(0, result);
    }

    @Test
    public void testFibonacci0() {
        int result = Fibonacci.fibonacci(0);
        assertEquals(0, result);
    }

    @Test
    public void testFibonacci1() {
        int result = Fibonacci.fibonacci(1);
        assertEquals(1, result);
    }

    @Test
    public void testFibonacci2() {
        int result = Fibonacci.fibonacci(1);
        assertEquals(1, result);
    }

    @Test
    public void testFibonacci25() {
        int result = Fibonacci.fibonacci(25);
        assertEquals(75025, result);
    }

    @RepeatedTest(10)
    public void testFibonacci10() {
        int result = Fibonacci.fibonacci(10);
        assertEquals(55, result);
    }
}
