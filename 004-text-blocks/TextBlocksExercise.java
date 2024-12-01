public class TextBlocksExercise {

    // HTML card template with placeholders
    static String getHtmlCard(String title, String content, String footer) {
        return """
                <div class="card">
                    <div class="card-header">
                        %s
                    </div>
                    <div class="card-body">
                        %s
                    </div>
                    <div class="card-footer">
                        %s
                    </div>
                </div>""".formatted(title, content, footer);
    }

    // SQL query template with placeholders
    static String getSqlQuery(String table, String column, String value) {
        return """
                SELECT *
                FROM %s
                WHERE %s = '%s'
                ORDER BY id DESC
                LIMIT 10;""".formatted(table, column, value);
    }

    // Demonstration of escape sequences
    static String getEscapeSequenceDemo() { return """
            1. Preserving trailing spaces:\s\s\s
            2. Adding a new line:\n   New line with indent
            3. Using triple quotes: \"\"\"Hello\"\"\"
            4. No escapes needed for "single" quotes
            5. Preserving indent:
               |--This vertical bar shows indent
               |--This line matches too"""; }

    public static void main(String[] args) {
        // Test HTML card
        System.out.println("HTML Card Example:");
        System.out.println(
                getHtmlCard(
                        "Welcome",
                        "This is a text block demo",
                        "Footer text"
                )
        );
        System.out.println();

        // Test SQL query
        System.out.println("SQL Query Example:");
        System.out.println(
                getSqlQuery(
                        "users",
                        "email",
                        "john@example.com"
                )
        );
        System.out.println();

        // Test escape sequences
        System.out.println("Escape Sequences Example:");
        System.out.println(getEscapeSequenceDemo());
    }
}
