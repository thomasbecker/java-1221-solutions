public class DocumentProcessor {
    /**
     * Sealed interface establishing the document type hierarchy.
     * This interface is necessary for three key reasons:
     * 1. Enables polymorphic handling of different document types
     * 2. Provides compile-time type safety by restricting implementations
     * 3. Allows exhaustive pattern matching in switch expressions, with
     * compiler verification that all possible cases are handled
     * 
     * Without this sealed interface, we would lose the ability to:
     * - Treat different document types uniformly
     * - Guarantee exhaustive pattern matching
     * - Maintain a closed hierarchy of document types
     */
    sealed interface Document permits PDFDocument, TextDocument, SpreadsheetDocument {}

    record PDFDocument(String title, int pages) implements Document {}

    record TextDocument(String title, long wordCount) implements Document {}

    record SpreadsheetDocument(String title, int sheets) implements Document {}

    public static String extractMetadata(Document doc) {
        if (doc instanceof PDFDocument pdf) {
            return "PDF: " + pdf.title() + " (" + pdf.pages() + " pages)";
        } else if (doc instanceof TextDocument text) {
            return "Text: " + text.title() + " (" + text.wordCount() + " words)";
        } else if (doc instanceof SpreadsheetDocument sheet) {
            return "Spreadsheet: " + sheet.title() + " (" + sheet.sheets() + " sheets)";
        }
        return "Unknown document type"; // This line is technically unreachable
    }

    public static int getDocumentComplexity(Document doc) {
        return switch (doc) {
            case PDFDocument pdf -> pdf.pages() * 10;
            case TextDocument text -> (int) (text.wordCount() / 100);
            case SpreadsheetDocument sheet -> sheet.sheets() * 20;
        };
    }

    public static void main(String[] args) {
        Document[] documents = {
                                new PDFDocument("Annual Report", 30),
                                new TextDocument("README", 150),
                                new SpreadsheetDocument("Budget", 3)
        };

        for (Document doc : documents) {
            System.out.println(extractMetadata(doc));
            System.out.println("Complexity: " + getDocumentComplexity(doc));
        }
    }
}
