
public class StringFormattingExercise {

    record Person(String name, int age, double salary) {}

    static class PersonFormatter {
        String formatTraditional(Person person) {
            return String.format(
                    "Person: %s (%d) - $%.2f",
                    person.name(),
                    person.age(),
                    person.salary()
            );
        }

        String formatModern(Person person) {
            return "Person: %s (%d) - $%.2f"
                    .formatted(person.name(), person.age(), person.salary());
        }

        String formatTemplate(Person person) {
            return STR."Person: \{person.name()} (\{person.age()}) - $\{person.salary()%.2f}";
        }
    }

    static String getExpressionTemplate(Person person) {
        return STR."\{person.name().toUpperCase()} earns $\{person.salary()%.2f} annually (monthly: $\{person.salary() / 12%.2f})";
    }

    static class MultiLineFormatter {
        String formatTraditional(Person person) {
            return String.format(
                    """
                            Person Details:
                              Name: %s
                              Age: %d years
                              Salary: $%,.2f
                              Tax (20%%): $%,.2f""",
                    person.name(),
                    person.age(),
                    person.salary(),
                    person.salary() * 0.2
            );
        }

        String formatModern(Person person) {
            return """
                    Person Details:
                      Name: %s
                      Age: %d years
                      Salary: $%,.2f
                      Tax (20%%): $%,.2f"""
                    .formatted(
                            person.name(),
                            person.age(),
                            person.salary(),
                            person.salary() * 0.2
                    );
        }

        String formatTemplate(Person person) {
            return STR."""
                Person Details:
                  Name: \{person.name()}
                  Age: \{person.age()} years
                  Salary: $\{String.format("%,.2f", person.salary())}
                  Tax (20%): $\{String.format("%,.2f", person.salary() * 0.2)}""";
        }
    }

    public static void main(String[] args) {
        Person person = new Person("John Doe", 30, 60000.0);
        PersonFormatter formatter = new PersonFormatter();
        MultiLineFormatter multiLine = new MultiLineFormatter();

        System.out.println("Single Line Formatting:");
        System.out.println("Traditional: " + formatter.formatTraditional(person));
        System.out.println("Modern: " + formatter.formatModern(person));
        System.out.println("Template: " + formatter.formatTemplate(person));

        System.out.println("\nExpression Template:");
        System.out.println(getExpressionTemplate(person));

        System.out.println("\nMulti-line Traditional:");
        System.out.println(multiLine.formatTraditional(person));
        System.out.println("\nMulti-line Modern:");
        System.out.println(multiLine.formatModern(person));
        System.out.println("\nMulti-line Template:");
        System.out.println(multiLine.formatTemplate(person));
    }
}
