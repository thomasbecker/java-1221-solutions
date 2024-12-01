void main() {
    // Instance methods can be called directly
    int result1 = add(5, 3);
    int result2 = subtract(10, 4);

    System.out.println("Calculator Demo Results:");
    System.out.println("5 + 3 = " + result1);
    System.out.println("10 - 4 = " + result2);

    // Static method calls (no instance needed)
    System.out.println("\nStatic Method Results:");
    System.out.println("Square of 5: " + square(5));
    System.out.println("Is 7 prime? " + isPrime(7));
    System.out.println("Max of 8 and 12: " + max(8, 12));

    // Show instance context benefits
    showOperationCount();
}

private int operationCount = 0;

int add(int a, int b) {
    operationCount++;
    return a + b;
}

int subtract(int a, int b) {
    operationCount++;
    return a - b;
}

void showOperationCount() {
    System.out.println("Total operations performed: " + operationCount);
}

// Static utility methods (no instance state needed)
static int square(int n) {
    return n * n;
}

static boolean isPrime(int n) {
    if (n <= 1)
        return false;
    for (int i = 2; i <= Math.sqrt(n); i++) {
        if (n % i == 0)
            return false;
    }
    return true;
}

static int max(int a, int b) {
    return (a > b) ? a : b;
}
