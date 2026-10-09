
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaSourceScanner {

    public static class ScanResult {
        public final String source;
        public final int commentLines;

        ScanResult(String source, int commentLines) {
            this.source = source;
            this.commentLines = commentLines;
        }
    }

    public static ScanResult scan(Path path) throws IOException {
        String input = new String(
                Files.readAllBytes(path),
                StandardCharsets.UTF_8
        );

        StringBuilder output = new StringBuilder(input.length());

        // Each array position represents one physical source line.
        boolean[] hasComment = new boolean[input.length() + 1];

        final int NORMAL = 0;
        final int LINE_COMMENT = 1;
        final int BLOCK_COMMENT = 2;
        final int STRING = 3;
        final int CHARACTER = 4;
        final int TEXT_BLOCK = 5;

        int state = NORMAL;
        int line = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            char next = i + 1 < input.length()
                    ? input.charAt(i + 1) : '\0';
            char next2 = i + 2 < input.length()
                    ? input.charAt(i + 2) : '\0';

            if (state == NORMAL) {
                if (c == '/' && next == '/') {
                    hasComment[line] = true;
                    state = LINE_COMMENT;
                    output.append("  ");
                    i++;
                } else if (c == '/' && next == '*') {
                    hasComment[line] = true;
                    state = BLOCK_COMMENT;
                    output.append("  ");
                    i++;
                } else if (c == '"' && next == '"' && next2 == '"') {
                    state = TEXT_BLOCK;
                    output.append("   ");
                    i += 2;
                } else if (c == '"') {
                    state = STRING;
                    output.append(' ');
                } else if (c == '\'') {
                    state = CHARACTER;
                    output.append(' ');
                } else {
                    output.append(c);
                }

            } else if (state == LINE_COMMENT) {
                if (c == '\n') {
                    output.append('\n');
                    state = NORMAL;
                } else if (c == '\r') {
                    output.append('\r');
                } else {
                    hasComment[line] = true;
                    output.append(' ');
                }

            } else if (state == BLOCK_COMMENT) {
                if (c == '*' && next == '/') {
                    hasComment[line] = true;
                    output.append("  ");
                    i++;
                    state = NORMAL;
                } else if (c == '\n') {
                    output.append('\n');
                } else if (c == '\r') {
                    output.append('\r');
                } else {
                    hasComment[line] = true;
                    output.append(' ');
                }

            } else if (state == STRING || state == CHARACTER) {
                char closing = state == STRING ? '"' : '\'';

                if (c == '\\' && i + 1 < input.length()) {
                    output.append("  ");
                    i++;
                } else if (c == closing) {
                    output.append(' ');
                    state = NORMAL;
                } else if (c == '\n') {
                    output.append('\n');
                } else if (c == '\r') {
                    output.append('\r');
                } else {
                    output.append(' ');
                }

            } else if (state == TEXT_BLOCK) {
                if (c == '"' && next == '"' && next2 == '"') {
                    output.append("   ");
                    i += 2;
                    state = NORMAL;
                } else if (c == '\n') {
                    output.append('\n');
                } else if (c == '\r') {
                    output.append('\r');
                } else {
                    output.append(' ');
                }
            }

            // Advance the physical line number exactly once per newline.
            if (c == '\n') {
                line++;
            }
        }

        int commentLines = 0;

        for (boolean containsComment : hasComment) {
            if (containsComment) {
                commentLines++;
            }
        }

        return new ScanResult(output.toString(), commentLines);
    }
}
