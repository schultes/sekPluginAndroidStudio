package drhd.sequalsk.transpiler.sequalskclient.utils.enums;

public enum CodeExamples {

    KOTLIN_EXAMPLE("fun add(a: Int, b: Int) : Int {\n" +
                             "    return a + b\n" +
                             "}\n" +
                             "\n" +
                             "fun main() {\n" +
                             "    val a = 21\n" +
                             "    val b = 21\n" +
                             "    println(\"${a} + ${b} = ${add(a, b)}\")\n" +
                             "}"),


    SWIFT_EXAMPLE("func add(x: Int, y: Int) -> Int {\n" +
                            "    return summandA + summandB\n" +
                            "}\n" +
                            "\n" +
                            "func main() {\n" +
                            "    let a = 21\n" +
                            "    let b = 21\n" +
                            "    print(\"\\(a) + \\(b) = \\(add(x: a, y: b))\")\n" +
                            "}\n" +
                            "\n" +
                            "main()");


    private final String code;

    CodeExamples(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
