import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ExpressionLanguageExercise {
    sealed interface Expression permits
            NumberExpr, VariableExpr, BinaryExpr {}

    record NumberExpr(double value) implements Expression {}

    record VariableExpr(String name) implements Expression {}

    sealed interface BinaryExpr extends Expression
            permits AddExpr, SubtractExpr, MultiplyExpr, DivideExpr {
        Expression left();
        Expression right();
    }

    record AddExpr(Expression left, Expression right) implements BinaryExpr {}

    record SubtractExpr(Expression left, Expression right) implements BinaryExpr {}

    record MultiplyExpr(Expression left, Expression right) implements BinaryExpr {}

    record DivideExpr(Expression left, Expression right) implements BinaryExpr {}

    static class Environment {
        private final Map<String, Double> variables = new HashMap<>();

        public void setVariable(String name, double value) {
            variables.put(name, value);
        }

        public double getVariable(String name) {
            Double value = variables.get(name);
            if (value == null) {
                throw new IllegalArgumentException("Undefined variable: " + name);
            }
            return value;
        }
    }

    static class ExpressionEvaluator {
        private final Environment env;

        public ExpressionEvaluator(Environment env) {
            this.env = env;
        }

        public double evaluate(Expression expr) {
            return switch (expr) {
                case NumberExpr n -> n.value();
                case VariableExpr v -> env.getVariable(v.name());
                case BinaryExpr b -> evaluateBinaryExpr(b);
            };
        }

        private double evaluateBinaryExpr(BinaryExpr expr) {
            double left = evaluate(expr.left());
            double right = evaluate(expr.right());

            return switch (expr) {
                case AddExpr a -> left + right;
                case SubtractExpr s -> left - right;
                case MultiplyExpr m -> left * right;
                case DivideExpr d -> {
                    if (right == 0)
                        throw new ArithmeticException("Division by zero");
                    yield left / right;
                }
            };
        }
    }

    static class ExpressionParser {
        public Expression parse(String input) {
            // This is a simple parser for demonstration
            // A real parser would be more sophisticated
            String[] tokens = input.split("\\s+");
            Stack<Expression> stack = new Stack<>();

            for (String token : tokens) {
                switch (token) {
                    case "+", "-", "*", "/" -> {
                        Expression right = stack.pop();
                        Expression left = stack.pop();
                        stack.push(createOperation(token, left, right));
                    }
                    default -> {
                        try {
                            double value = Double.parseDouble(token);
                            stack.push(new NumberExpr(value));
                        } catch (NumberFormatException e) {
                            stack.push(new VariableExpr(token));
                        }
                    }
                }
            }
            return stack.pop();
        }

        private Expression createOperation(String op, Expression left, Expression right) {
            return switch (op) {
                case "+" -> new AddExpr(left, right);
                case "-" -> new SubtractExpr(left, right);
                case "*" -> new MultiplyExpr(left, right);
                case "/" -> new DivideExpr(left, right);
                default -> throw new IllegalArgumentException("Unknown operator: " + op);
            };
        }
    }

    public static void main(String[] args) {
        Environment env = new Environment();
        env.setVariable("x", 10);
        env.setVariable("y", 5);

        ExpressionEvaluator evaluator = new ExpressionEvaluator(env);
        ExpressionParser parser = new ExpressionParser();

        // Create and evaluate expressions
        Expression expr1 = new AddExpr(
                new NumberExpr(5),
                new MultiplyExpr(
                        new VariableExpr("x"),
                        new NumberExpr(2)
                )
        );

        Expression expr2 = parser.parse("x y + 5 *");

        System.out.println("Expression 1 result: " + evaluator.evaluate(expr1));
        System.out.println("Expression 2 result: " + evaluator.evaluate(expr2));

        // Demonstrate error handling
        try {
            evaluator.evaluate(
                    new DivideExpr(
                            new NumberExpr(1),
                            new NumberExpr(0)
                    )
            );
        } catch (ArithmeticException e) {
            System.out.println("Caught expected division by zero");
        }

        // Demonstrate variable resolution
        try {
            evaluator.evaluate(new VariableExpr("z")); // undefined variable
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected undefined variable error");
        }
    }
}
