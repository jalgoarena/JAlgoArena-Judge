class Solution
  def fib(n)
    return n if n < 2

    @memo ||= {}
    @memo[n] ||= fib(n - 1) + fib(n - 2)
  end
end
