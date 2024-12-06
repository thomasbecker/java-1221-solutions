
public class ShapeAnalyzer {
    sealed interface Shape permits Rectangle, Circle, Composite {}

    record Rectangle(double width, double height) implements Shape {}

    record Circle(double radius) implements Shape {}

    record Composite(Shape primary, Shape secondary, String operation) implements Shape {}

    public static String analyzeShape(Shape shape) {
        return switch (shape) {
            case Rectangle r when r.width() == r.height() -> "Square " + r.width() + " × " + r.height();
            case Rectangle r -> "Rectangle " + r.width() + " × " + r.height();
            case Circle c when c.radius() < 5 -> "Small Circle (radius: " + c.radius() + ")";
            case Circle c -> "Large Circle (radius: " + c.radius() + ")";
            case Composite c -> switch (c.operation()) {
                case "joined" -> "Joined: " + analyzeShape(c.primary()) +
                        " with " + analyzeShape(c.secondary());
                case "overlapped" -> "Overlapped: " + analyzeShape(c.primary()) +
                        " over " + analyzeShape(c.secondary());
                default -> throw new IllegalArgumentException(
                        "Unknown operation: " +
                                c.operation()
                );
            };
        };
    }

    public static double calculateArea(Shape shape) {
        return switch (shape) {
            case Rectangle r -> r.width() * r.height();
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Composite c -> switch (c.operation()) {
                case "joined" -> calculateArea(c.primary()) + calculateArea(c.secondary());
                case "overlapped" -> Math.max(
                        calculateArea(c.primary()),
                        calculateArea(c.secondary())
                );
                default -> throw new IllegalArgumentException(
                        "Unknown operation: " +
                                c.operation()
                );
            };
        };
    }

    public static void main(String[] args) {
        Shape[] shapes = {
                          new Rectangle(5, 5),
                          new Circle(3),
                          new Composite(
                                  new Rectangle(4, 6),
                                  new Circle(2),
                                  "joined"
                          ),
                          new Composite(
                                  new Circle(5),
                                  new Circle(3),
                                  "overlapped"
                          )
        };

        for (Shape shape : shapes) {
            System.out.println("Analysis: " + analyzeShape(shape));
            System.out.println("Area: " + calculateArea(shape));
            System.out.println();
        }
    }

}
