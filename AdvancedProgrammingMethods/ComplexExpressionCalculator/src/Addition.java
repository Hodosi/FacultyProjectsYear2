import java.util.List;

public class Addition extends ComplexExpression {
    public Addition(Operation operation, List<ComplexNumber> complex_numbers) {
        super(operation, complex_numbers);
    }

    @Override
    public ComplexNumber executeOneOperation(ComplexNumber nr1, ComplexNumber nr2){
        return nr1.add(nr2);
    }
}
