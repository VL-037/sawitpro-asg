package helper;

import constant.Language;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

    public static String applyColor(String result) {
        StringBuilder contentBuilder = new StringBuilder();
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

    public static String convertToUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (getLanguage(c).equals(Language.CHI)) {
                sb.append("&#").append(c & 0xFFFF).append(";");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static Language getLanguage(char c) {
        if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return Language.CHI;
        }
        return Language.ENG;
    }

    public static String extractLanguageCharacters(String str, Pattern pattern) {
        StringBuilder sb = new StringBuilder();

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            sb.append(matcher.group() + " ");
        }
        return sb.toString();
    }

    public static String extractEnglishLanguageCharacters(String initialString, Map<Language, String> otherLanguageStringMap) {
        StringBuilder sb = new StringBuilder();

        otherLanguageStringMap.forEach((language, s) -> {
            sb.append(s + " ");
        });

        String foreignStrings = sb.toString();
        String[] foreignWords = foreignStrings.split(" ");
        for (String word : foreignWords) {
            initialString = initialString.replaceAll(word, "");
        }

        return initialString;
    }
}
