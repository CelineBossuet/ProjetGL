package calc;

public class IntLiteral extends AbstractExpr {
    private final int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

}
