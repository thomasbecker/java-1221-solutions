import java.util.List;
import java.util.stream.Collectors;

public class SalesAnalyticsExercise {

    record Sale(String product, double amount, String category) {}

    static class SalesStats {
        String category;
        double averageAmount;
        double totalAmount;
        long numberOfSales;

        SalesStats(
                String category,
                double averageAmount,
                double totalAmount,
                long numberOfSales
        ) {
            this.category = category;
            this.averageAmount = averageAmount;
            this.totalAmount = totalAmount;
            this.numberOfSales = numberOfSales;
        }

        @Override
        public String toString() {
            return "Category: %s, Average: $%.2f, Total: $%.2f, Count: %d"
                    .formatted(category, averageAmount, totalAmount, numberOfSales);
        }
    }

    static List<Sale> getSalesByCategory(List<Sale> sales, String category) {
        return sales.stream()
                .filter(sale -> sale.category().equals(category))
                .toList(); // Java 16+ convenience method
    }

    static SalesStats calculateStats(List<Sale> sales, String category) {
        return sales.stream()
                .filter(sale -> sale.category().equals(category))
                .collect(
                        Collectors.teeing(
                                Collectors.summingDouble(Sale::amount),
                                Collectors.counting(),
                                (sum, count) -> new SalesStats(
                                        category,
                                        count > 0 ? sum / count : 0,
                                        sum,
                                        count
                                )
                        )
                );
    }

    public static void main(String[] args) {
        List<Sale> sales = List.of(
                new Sale("Laptop", 999.99, "Electronics"),
                new Sale("Headphones", 99.99, "Electronics"),
                new Sale("Book", 19.99, "Books"),
                new Sale("Coffee Maker", 79.99, "Appliances"),
                new Sale("Tablet", 299.99, "Electronics"),
                new Sale("Novel", 24.99, "Books")
        );

        // Get unique categories using toList()
        List<String> categories = sales.stream()
                .map(Sale::category)
                .distinct()
                .toList();

        System.out.println("Sales Analysis by Category:");
        System.out.println("--------------------------");

        // Calculate and print stats for each category
        categories.forEach(category -> {
            System.out.println("\nCategory: " + category);

            // Demonstrate getSalesByCategory
            List<Sale> categorySales = getSalesByCategory(sales, category);
            System.out.println("Number of products: " + categorySales.size());

            // Demonstrate calculateStats with teeing collector
            SalesStats stats = calculateStats(sales, category);
            System.out.println(stats);
        });
    }
}
