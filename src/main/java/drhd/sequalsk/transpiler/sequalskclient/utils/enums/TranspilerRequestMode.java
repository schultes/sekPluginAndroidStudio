package drhd.sequalsk.transpiler.sequalskclient.utils.enums;

public enum TranspilerRequestMode {
    ONE_WAY("One-way"), TWO_WAY("Two-way");

    private final String name;

    TranspilerRequestMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
