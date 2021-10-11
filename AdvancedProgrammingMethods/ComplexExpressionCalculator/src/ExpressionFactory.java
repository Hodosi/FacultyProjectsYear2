import java.util.List;

public class ExpressionFactory {
    public static ExpressionFactory instance = new ExpressionFactory();

    private ExpressionFactory(){}

    public static ExpressionFactory getInstance(){
        return instance;
    }

    public ComplexExpression createExpression(Operation operation, List<ComplexNumber> args){
        if (operation == Operation.ADDITION){
            return new Addition(operation, args);
        }

        if (operation == Operation.SUBSTRACTION){
            return new Subtraction(operation, args);
        }

        if (operation == Operation.MULTIPLICATION){
            return new Multiplication(operation, args);
        }

        if (operation == Operation.DIVISON){
            return new Division(operation, args);
        }

        return null;
    }
}
