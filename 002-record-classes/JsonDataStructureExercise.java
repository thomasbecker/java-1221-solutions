import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class JsonDataStructureExercise {
    sealed interface JsonElement permits JsonString, JsonNumber, JsonBoolean, JsonArray, JsonObject {}

    record JsonString(String value) implements JsonElement {
        public JsonString {
            if (value == null) {
                throw new IllegalArgumentException("String value cannot be null");
            }
        }
    }

    record JsonNumber(double value) implements JsonElement {}

    record JsonBoolean(boolean value) implements JsonElement {}

    record JsonArray(List<JsonElement> elements) implements JsonElement {
        public JsonArray {
            elements = List.copyOf(elements);
        }
    }

    record JsonObject(Map<String, JsonElement> properties) implements JsonElement {
        public JsonObject {
            properties = Map.copyOf(properties);
        }
    }

    static String prettyPrint(JsonElement element, int indent) {
        return prettyPrint(element, indent, true);
    }

    private static String prettyPrint(JsonElement element, int indent, boolean isLast) {
        return switch (element) {
            case JsonString s -> "\"" + escapeString(s.value()) + "\"" + (isLast ? "" : ",");
            case JsonNumber n -> String.valueOf(n.value()) + (isLast ? "" : ",");
            case JsonBoolean b -> String.valueOf(b.value()) + (isLast ? "" : ",");
            case JsonArray a -> {
                if (a.elements().isEmpty()) {
                    yield "[]" + (isLast ? "" : ",");
                }
                StringBuilder sb = new StringBuilder("[\n");
                var elements = a.elements();
                IntStream.range(0, elements.size())
                        .mapToObj(
                                i -> " ".repeat(indent + 2) +
                                        prettyPrint(elements.get(i), indent + 2, i == elements.size() - 1) +
                                        '\n'
                        )
                        .forEach(sb::append);
                yield sb.append(" ".repeat(indent))
                        .append("]")
                        .append(isLast ? "" : ",")
                        .toString();
            }
            case JsonObject o -> {
                if (o.properties().isEmpty()) {
                    yield "{}" + (isLast ? "" : ",");
                }
                StringBuilder sb = new StringBuilder("{\n");
                var entries = o.properties().entrySet().stream().toList();
                for (int i = 0; i < entries.size(); i++) {
                    var entry = entries.get(i);
                    sb.append(" ".repeat(indent + 2))
                            .append("\"")
                            .append(escapeString(entry.getKey()))
                            .append("\": ")
                            .append(prettyPrint(entry.getValue(), indent + 2, i == entries.size() - 1))
                            .append('\n');
                }
                yield sb.append(" ".repeat(indent))
                        .append("}")
                        .append(isLast ? "" : ",")
                        .toString();
            }
        };
    }

    private static String escapeString(String str) {
        return str.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static final class JsonBuilder {
        private final Map<String, JsonElement> properties = new HashMap<>();

        private JsonBuilder() {}

        public static JsonBuilder create() {
            return new JsonBuilder();
        }

        public JsonBuilder put(String key, String value) {
            validateKey(key);
            properties.put(key, new JsonString(value));
            return this;
        }

        public JsonBuilder put(String key, Number value) {
            validateKey(key);
            properties.put(key, new JsonNumber(value.doubleValue()));
            return this;
        }

        public JsonBuilder put(String key, boolean value) {
            validateKey(key);
            properties.put(key, new JsonBoolean(value));
            return this;
        }

        public JsonBuilder put(String key, List<?> values) {
            validateKey(key);
            properties.put(
                    key,
                    new JsonArray(
                            values.stream()
                                    .map(JsonBuilder::toJsonElement)
                                    .toList()
                    )
            );
            return this;
        }

        public JsonBuilder put(String key, JsonElement value) {
            validateKey(key);
            properties.put(key, value);
            return this;
        }

        public JsonObject build() {
            return new JsonObject(new HashMap<>(properties));
        }

        private void validateKey(String key) {
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("Key cannot be null or blank");
            }
        }

        private static JsonElement toJsonElement(Object value) {
            if (value == null) {
                throw new IllegalArgumentException("JSON value cannot be null");
            }
            return switch (value) {
                case String s -> new JsonString(s);
                case Number n -> new JsonNumber(n.doubleValue());
                case Boolean b -> new JsonBoolean(b);
                case List<?> l -> new JsonArray(
                        l.stream()
                                .map(JsonBuilder::toJsonElement)
                                .toList()
                );
                case JsonElement e -> e;
                default -> throw new IllegalArgumentException("Unsupported type: " + value.getClass());
            };
        }
    }

    public static void main(String[] args) {
        // Create a complex JSON structure using the builder pattern
        JsonObject userProfile = JsonBuilder.create()
                .put("name", "John Doe")
                .put("age", 30)
                .put("isActive", true)
                .put("hobbies", List.of("reading", "gaming", "coding"))
                .put(
                        "address",
                        JsonBuilder.create()
                                .put("street", "123 Main St")
                                .put("city", "Techville")
                                .put("zipCode", "12345")
                                .build()
                )
                .build();

        System.out.println(prettyPrint(userProfile, 0));

        // Pattern matching example
        if (userProfile instanceof JsonObject obj) {
            JsonElement nameElement = obj.properties().get("name");
            if (nameElement instanceof JsonString name) {
                System.out.println("User name: " + name.value());
            }
        }
    }
}
