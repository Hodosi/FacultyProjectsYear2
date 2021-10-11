import java.util.List;

public class Division extends ComplexExpression {
    public Division(Operation operation, List<ComplexNumber> complex_numbers) {
        super(operation, complex_numbers);
    }

    public ComplexNumber executeOneOperation(ComplexNumber nr1, ComplexNumber nr2){
        return nr1.divide(nr2);
    }
}
