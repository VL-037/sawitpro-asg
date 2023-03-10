package constant;

public enum Language {
    ENG("ENG", "^[ \\w \\d \\s \\. \\& \\+ \\- \\, \\! \\@ \\# \\$ \\% \\^ \\* \\( \\) \\; \\\\ \\/ \\| \\< \\> \\\" \\' \\? \\= \\: \\[ \\] ]*$"),
    CHI("CHI", "[\u4e00-\u9fa5]");
    private final String label;
    private final String regex;

    Language(String label, String regex) {
        this.label = label;
        this.regex = regex;
    }

    public String getLabel() {
        return label;
    }

    public String getRegex() {
        return regex;
    }
}
