package drhd.sequalsk.transpiler.sequalskclient.utils.enums;

public enum TranspilerLanguage {

    KOTLIN("Kotlin"), SWIFT("Swift");

    TranspilerLanguage(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }

    public static TranspilerLanguage other(TranspilerLanguage language) {
        if(language == KOTLIN) return SWIFT;
        return KOTLIN;
    }

}
