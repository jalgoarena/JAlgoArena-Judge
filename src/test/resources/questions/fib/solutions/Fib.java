public class Solution {
    /**
     * @param n id of fibonacci term to be returned
     * @return  N'th term of Fibonacci sequence
     */
    public int Fibonacci(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;

        return Fibonacci(n - 1) + Fibonacci(n - 2);
    }
}