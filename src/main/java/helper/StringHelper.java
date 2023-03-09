package helper;

public class StringHelper {

    public static String applyColor(String result) {
        StringBuilder contentBuilder = new StringBuilder();
        String[] words = result.split(" ");
        for (String word : words) {
            if (word.toLowerCase().contains("o")) {
                String coloredWord = "<span style='color:blue;'>" + word + "</span>";
                contentBuilder.append(coloredWord);
            } else {
                contentBuilder.append(word);
            }
            contentBuilder.append(" ");
        }
        return contentBuilder.toString();
    }
}
