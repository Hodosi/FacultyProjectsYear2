import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpressionParser {

    private ExpressionFactory expression_factory;
    private List<ComplexNumber> complex_numbers;
    private List<Operation> operations;

    private float real_part;
    private float imaginary_part;
    private Operation complex_expression_operation;
    private String[] expression_parts;
    private int current_expression_index;

    public ExpressionParser() {
        this.expression_factory = ExpressionFactory.getInstance();
        this.complex_numbers = new ArrayList<ComplexNumber>();
        this.operations = new ArrayList<Operation>();
    }

    private boolean checkRealPart(){
        if(this.current_expression_index >= this.expression_parts.length) {
            return false;
        }
        String candidate_real_part = this.expression_parts[this.current_expression_index];
        try{
            this.real_part = Float.parseFloat(candidate_real_part);
        }
        catch(NumberFormatException e) {
            return false;
//            if(candidate_real_part.startsWith("-")){
//                try{
//                    this.real_part = (-1) * Float.parseFloat(candidate_real_part.substring(1, candidate_real_part.length() - 1));
//                }
//                catch (NumberFormatException ee){
//                    return false;
//                }
//            }
//            else{
//                return false;
//            }
        }
        current_expression_index += 1;
        return true;
    }

    private boolean checkImaginaryPart(){
        if(this.current_expression_index >= this.expression_parts.length) {
            return false;
        }
        if(expression_parts[this.current_expression_index].equals("+i")){
            this.imaginary_part = 1;
            current_expression_index += 1;
            return true;
        }
        if(expression_parts[this.current_expression_index].equals("-i")){
            this.imaginary_part = -1;
            current_expression_index += 1;
            return true;
        }
        String candidate_imaginary_part = this.expression_parts[this.current_expression_index];
        try{
            this.imaginary_part = Float.parseFloat(candidate_imaginary_part);
        }
        catch(NumberFormatException e) {
            return false;
//            if(candidate_imaginary_part.startsWith("-")){
//                try{
//                    this.imaginary_part = (-1) * Float.parseFloat(candidate_imaginary_part.substring(1, candidate_imaginary_part.length() - 1));
//                }
//                catch (NumberFormatException ee){
//                    return false;
//                }
//            }
//            else{
//                return false;
//            }
        }

        this.current_expression_index += 1;
        if(!expression_parts[this.current_expression_index].equals("*")){
            return false;
        }
        this.current_expression_index += 1;
        if(!expression_parts[this.current_expression_index].equals("i")){
            return false;
        }
        current_expression_index += 1;
        return true;
    }

    private boolean checkComplexExpressionOperation(){
        if(this.current_expression_index >= this.expression_parts.length) {
            return false;
        }
        if(this.expression_parts[this.current_expression_index].equals("+")){
            complex_expression_operation = Operation.ADDITION;
            current_expression_index += 1;
            return true;
        }
        if(expression_parts[this.current_expression_index].equals("-")){
            complex_expression_operation = Operation.SUBSTRACTION;
            current_expression_index += 1;
            return true;
        }
        if(this.expression_parts[this.current_expression_index].equals("*")){
            complex_expression_operation = Operation.MULTIPLICATION;
            current_expression_index += 1;
            return true;
        }
        if(expression_parts[this.current_expression_index].equals("/")){
            complex_expression_operation = Operation.DIVISON;
            current_expression_index += 1;
            return true;
        }
        return false;
    }

    private void createComplexNumber(){
        ComplexNumber complex_number = new ComplexNumber(this.real_part, this.imaginary_part);
        this.complex_numbers.add(complex_number);
    }

    public boolean parseExpression(String expression_string) {
        expression_parts = expression_string.split(" ");
        System.out.println(Arrays.toString(expression_parts));

        boolean valid_expression = true;
        current_expression_index = 0;
        while (true) {
            if (!checkRealPart()) {
                valid_expression = false;
                break;
            }
            if (!checkImaginaryPart()) {
                valid_expression = false;
                break;
            }
            createComplexNumber();

            if (this.current_expression_index >= this.expression_parts.length) {
                break;
            }

            if (!checkComplexExpressionOperation()) {
                valid_expression = false;
                break;
            }
            operations.add(this.complex_expression_operation);
        }

        if (this.operations.size() == this.complex_numbers.size()) {
            this.operations.remove(this.operations.size() - 1);
        }

        return valid_expression;
    }

    public ComplexNumber solveComplexExpression(){

        boolean multiplication_division = true;
        while (multiplication_division){

            multiplication_division = false;
            for(int i = 0; i < this.operations.size(); i++){

                ComplexExpression complex_expression = null;
                if(this.operations.get(i) == Operation.MULTIPLICATION){
                    complex_expression = this.expression_factory.createExpression(Operation.MULTIPLICATION, this.complex_numbers);
                    multiplication_division = true;
                }

                else if(this.operations.get(i) == Operation.DIVISON){

                    complex_expression = this.expression_factory.createExpression(Operation.DIVISON, this.complex_numbers);
                    multiplication_division = true;
                }

                if(multiplication_division){
                    ComplexNumber first_number = this.complex_numbers.get(i);
                    ComplexNumber second_number = this.complex_numbers.get(i + 1);
                    ComplexNumber result = complex_expression.executeOneOperation(first_number, second_number);
                    this.complex_numbers.set(i, result);
                    this.complex_numbers.remove(i + 1);
                    this.operations.remove(i);
                    break;
                }
            }
        }

        boolean addition_substraction = true;
        while (addition_substraction){

            addition_substraction = false;
            for(int i = 0; i < this.operations.size(); i++){

                ComplexExpression complex_expression = null;
                if(this.operations.get(i) == Operation.ADDITION){
                    complex_expression = this.expression_factory.createExpression(Operation.ADDITION, this.complex_numbers);
                    addition_substraction = true;
                }

                else if(this.operations.get(i) == Operation.SUBSTRACTION){

                    complex_expression = this.expression_factory.createExpression(Operation.SUBSTRACTION, this.complex_numbers);
                    addition_substraction = true;
                }

                if(addition_substraction){
                    ComplexNumber first_number = this.complex_numbers.get(i);
                    ComplexNumber second_number = this.complex_numbers.get(i + 1);
                    ComplexNumber result = complex_expression.executeOneOperation(first_number, second_number);
                    this.complex_numbers.set(i, result);
                    this.complex_numbers.remove(i + 1);
                    this.operations.remove(i);
                    break;
                }
            }
        }

        return complex_numbers.get(0);
    }
}
