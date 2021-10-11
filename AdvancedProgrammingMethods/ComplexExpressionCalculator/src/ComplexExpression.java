import java.util.List;

public abstract class ComplexExpression {
    Operation operation;
    List<ComplexNumber> complex_numbers;

    public ComplexExpression(Operation operation, List<ComplexNumber> complex_numbers) {
        this.operation = operation;
        this.complex_numbers = complex_numbers;
    }

    public abstract ComplexNumber executeOneOperation(ComplexNumber nr1, ComplexNumber nr);
}
