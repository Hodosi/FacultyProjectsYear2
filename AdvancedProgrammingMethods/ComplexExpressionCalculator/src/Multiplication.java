import java.util.List;

public class Multiplication extends ComplexExpression {
    public Multiplication(Operation operation, List<ComplexNumber> complex_numbers) {
        super(operation, complex_numbers);
    }

    @Override
    public ComplexNumber executeOneOperation(ComplexNumber nr1, ComplexNumber nr2){
        return nr1.multiply(nr2);
    }
}
