import java.util.List;

public class Subtraction extends ComplexExpression {
    public Subtraction(Operation operation, List<ComplexNumber> complex_numbers) {
        super(operation, complex_numbers);
    }

    @Override
    public ComplexNumber executeOneOperation(ComplexNumber nr1, ComplexNumber nr2){
        return nr1.subtract(nr2);
    }
}
