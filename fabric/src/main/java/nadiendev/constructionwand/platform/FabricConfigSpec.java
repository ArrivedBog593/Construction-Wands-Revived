//? if fabric {
package nadiendev.constructionwand.platform;

import nadiendev.constructionwand.ConstructionWand;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Minimal config storage for Fabric, which has no config system of its own.
 * <p>
 * Values are kept in a TOML-like file ({@code [section]} headers, {@code key = value} lines, {@code #} comments)
 * with the same layout NeoForge and Forge use. The file is read once on startup and rewritten with every entry,
 * its comment and its default, so players can discover and edit all options.
 */
public final class FabricConfigSpec {

    /** One option of the config. */
    record Entry(String section, String key, List<String> comment, Object defaultValue, Predicate<Object> validator) {
        String id() {
            return section.isEmpty() ? key : section + "." + key;
        }
    }

    private final Map<String, Entry> entries = new LinkedHashMap<>();
    private final Map<String, Object> values = new LinkedHashMap<>();

    void define(Entry entry) {
        entries.put(entry.id(), entry);
    }

    /** Current value of an option; the default until the file has been loaded. */
    Object get(String id) {
        Object value = values.get(id);
        return value != null ? value : entries.get(id).defaultValue();
    }

    /** Reads the file (when it exists), replaces invalid values with their defaults and writes the file back. */
    public void load(Path file) {
        Map<String, Object> parsed = new LinkedHashMap<>();
        if (Files.exists(file)) {
            try {
                parsed = parse(Files.readAllLines(file, StandardCharsets.UTF_8));
            } catch (IOException | RuntimeException e) {
                ConstructionWand.LOGGER.warn("Could not read config " + file.getFileName() + ", using defaults: " + e.getMessage());
            }
        }

        values.clear();
        for (Entry entry : entries.values()) {
            Object value = parsed.get(entry.id());
            if (value == null || !entry.validator().test(value)) {
                if (value != null) {
                    ConstructionWand.LOGGER.warn("Invalid value for " + entry.id() + " in " + file.getFileName() + ", using the default");
                }
                value = entry.defaultValue();
            }
            values.put(entry.id(), value);
        }

        try {
            Files.createDirectories(file.getParent());
            Files.write(file, render(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            ConstructionWand.LOGGER.warn("Could not write config " + file.getFileName() + ": " + e.getMessage());
        }
    }

    // ── Writing ───────────────────────────────────────────────────────────────

    private List<String> render() {
        List<String> lines = new ArrayList<>();
        String section = null;
        for (Entry entry : entries.values()) {
            if (!entry.section().equals(section)) {
                section = entry.section();
                if (!lines.isEmpty()) lines.add("");
                if (!section.isEmpty()) lines.add("[" + section + "]");
            }
            for (String comment : entry.comment()) lines.add("\t#" + comment);
            lines.add("\t" + entry.key() + " = " + format(values.get(entry.id())));
        }
        return lines;
    }

    private static String format(Object value) {
        if (value instanceof List<?> list) {
            StringBuilder out = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) out.append(", ");
                out.append(quote(String.valueOf(list.get(i))));
            }
            return out.append("]").toString();
        }
        return value instanceof String string ? quote(string) : String.valueOf(value);
    }

    private static String quote(String text) {
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    // ── Reading ───────────────────────────────────────────────────────────────

    private static Map<String, Object> parse(List<String> lines) {
        Map<String, Object> result = new LinkedHashMap<>();
        String section = "";
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            if (line.startsWith("[") && line.endsWith("]") && !line.contains("=")) {
                section = line.substring(1, line.length() - 1).trim();
                continue;
            }
            int equals = line.indexOf('=');
            if (equals < 0) continue;
            String key = line.substring(0, equals).trim();
            Object value = parseValue(line.substring(equals + 1).trim());
            if (value != null) result.put(section.isEmpty() ? key : section + "." + key, value);
        }
        return result;
    }

    private static Object parseValue(String text) {
        if (text.equals("true")) return Boolean.TRUE;
        if (text.equals("false")) return Boolean.FALSE;
        if (text.startsWith("\"")) return unquote(text, 0)[0];
        if (text.startsWith("[")) {
            List<String> items = new ArrayList<>();
            int i = 1;
            while (i < text.length()) {
                char c = text.charAt(i);
                if (c == '"') {
                    Object[] parsed = unquote(text, i);
                    items.add((String) parsed[0]);
                    i = (Integer) parsed[1];
                } else if (c == ']') {
                    break;
                } else {
                    i++;
                }
            }
            return items;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Reads a quoted string starting at {@code start}; returns the text and the index after the closing quote. */
    private static Object[] unquote(String text, int start) {
        StringBuilder out = new StringBuilder();
        int i = start + 1;
        while (i < text.length()) {
            char c = text.charAt(i++);
            if (c == '\\' && i < text.length()) {
                out.append(text.charAt(i++));
            } else if (c == '"') {
                break;
            } else {
                out.append(c);
            }
        }
        return new Object[]{out.toString(), i};
    }
}
//?}
