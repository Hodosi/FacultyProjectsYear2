import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpressionParserBasic {

    private ExpressionFactory expression_factory;
    private List<ComplexNumber> complex_numbers;
    private List<Operation> operations;

    private float real_part;
    private float imaginary_part;
    private Operation complex_expression_operation;
    private String[] expression_parts;
    private int current_expression_index;

    public ExpressionParserBasic() {
        this.expression_factory = ExpressionFactory.getInstance();
        this.complex_numbers = new ArrayList<ComplexNumber>();
        this.operations = new ArrayList<Operation>();
    }

    private boolean checkComplexNumber(){
        if(this.current_expression_index >= this.expression_parts.length) {
            return false;
        }

        String candidate_complex_number = this.expression_parts[current_expression_index];

        int real_part_sign = 1;
        if(candidate_complex_number.startsWith("+")){
            candidate_complex_number = candidate_complex_number.substring(1);
        }
        else if(candidate_complex_number.startsWith("-")){
            real_part_sign = -1;
            candidate_complex_number = candidate_complex_number.substring(1);
        }

        if(candidate_complex_number.endsWith("i")){
            int imaginary_part_sign;
            String sign;
            if(candidate_complex_number.contains("+")){
                sign = "\\+";
                imaginary_part_sign = 1;
            }
            else if(candidate_complex_number.contains("-")){
                sign = "-";
                imaginary_part_sign = -1;
            }
            else{
                try {
                    this.real_part = 0;
                    if(candidate_complex_number.equals("i")){
                        this.imaginary_part = real_part_sign;
                    }
                    else {
//                        String candidate_imaginary_part = candidate_complex_number.split("\\*")[0];
//                        this.imaginary_part = real_part_sign * Float.parseFloat(candidate_imaginary_part);
                        String[] candidate_imaginary_parts = candidate_complex_number.split("\\*");
                        String candidate_imaginary_part_value = candidate_imaginary_parts[0];
                        String last_i = candidate_imaginary_parts[1];
                        if(!last_i.equals("i")){
                            return false;
                        }
                        this.imaginary_part = real_part_sign * Float.parseFloat(candidate_imaginary_part_value);
                    }
                }
                catch (Exception e){
                    return false;
                }

                current_expression_index += 1;
                return true;
            }
            String[] complex_number_parts = candidate_complex_number.split(sign);
            String candidate_real_part = complex_number_parts[0];
            String candidate_imaginary_part = complex_number_parts[1];
            try{
                this.real_part = real_part_sign * Float.parseFloat(candidate_real_part);
            }
            catch(NumberFormatException e) {
                return false;
            }

            if(candidate_imaginary_part.equals("i")){
                this.imaginary_part = imaginary_part_sign;
            }
            else{
                try{
//                    candidate_imaginary_part = candidate_imaginary_part.split("\\*")[0];
//                    this.imaginary_part = imaginary_part_sign * Float.parseFloat(candidate_imaginary_part);
                    String[] candidate_imaginary_parts = candidate_imaginary_part.split("\\*");
                    String candidate_imaginary_part_value = candidate_imaginary_parts[0];
                    String last_i = candidate_imaginary_parts[1];
                    if(!last_i.equals("i")){
                        return false;
                    }
                    this.imaginary_part = imaginary_part_sign * Float.parseFloat(candidate_imaginary_part_value);
                }
                catch(Exception e) {
                    return false;
                }
            }
        }
        else{
            try{
                this.real_part = real_part_sign * Float.parseFloat(candidate_complex_number);
                this.imaginary_part = 0;
            }
            catch(NumberFormatException e) {
                return false;
            }
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
            if(!checkComplexNumber()){
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

//        if (this.operations.size() == this.complex_numbers.size()) {
//            this.operations.remove(this.operations.size() - 1);
//        }

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

        boolean addition_subtraction = true;
        while (addition_subtraction){

            addition_subtraction = false;
            for(int i = 0; i < this.operations.size(); i++){

                ComplexExpression complex_expression = null;
                if(this.operations.get(i) == Operation.ADDITION){
                    complex_expression = this.expression_factory.createExpression(Operation.ADDITION, this.complex_numbers);
                    addition_subtraction = true;
                }

                else if(this.operations.get(i) == Operation.SUBSTRACTION){

                    complex_expression = this.expression_factory.createExpression(Operation.SUBSTRACTION, this.complex_numbers);
                    addition_subtraction = true;
                }

                if(addition_subtraction){
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
