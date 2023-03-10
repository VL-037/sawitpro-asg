package helper;

public class StringHelper {

    public static String applyColor(String result) {
        StringBuilder contentBuilder = new StringBuilder();
        result = convertToUnicode(result);
        String[] words = result.split(" ");
        for (String word : words) {
            int newLineCount = word.length() - word.replace("\n", "").length();
            String[] nonNewLineWords = word.split("\n");

            for (String nonNewLineWord : nonNewLineWords) {
                if (nonNewLineWord.toLowerCase().contains("o")) {
                    contentBuilder.append("<span style='color:blue;'>").append(nonNewLineWord).append("</span>");
                } else {
                    contentBuilder.append(nonNewLineWord);
                }
                if (newLineCount > 0) {
                    contentBuilder.append("\n");
                    newLineCount--;
                }
            }
            contentBuilder.append(" ");
        }
        return contentBuilder.toString();
    }

    private static String convertToUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                sb.append("&#").append(c & 0xFFFF).append(";");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
