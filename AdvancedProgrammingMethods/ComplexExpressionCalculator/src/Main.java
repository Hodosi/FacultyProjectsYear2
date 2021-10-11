import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        // 2 +3 * i + 5 -6 * i + -2 +i
        // 2+3*i + 5-6*i + -2+i
        // 2+3*i + 5-6*i + -2+i =
        // 2+3*i + 5-6*i + -2+i + i
        // 2+3*i + 5-6*i + -2+i + 3*i
        // 2+3*i * 5-6*i + -2+i / 3*i
        //\left(2+3\cdot \:i\right)\cdot \left(5-6\cdot \:i\right)\:\:+\:\left(-2+i\right)\:/\:\left(3\cdot \:i\right)
        // 2+3*i * 5-6*i * -2+i / 3*i
        //\left(2+3\cdot \:i\right)\:\cdot \left(\:5-6\cdot \:i\right)\:\cdot \left(-2+i\:\right)/\:\left(3\cdot \:i\right)

        String command_line_string = "";
        for(String arg : args){
            command_line_string = command_line_string + arg + " ";
        }
        command_line_string = command_line_string.substring(0, command_line_string.length() - 1);

        Scanner in = new Scanner(System.in);
        String expression_line = in.nextLine();

        System.out.println("Your entered string: " + expression_line);
        // System.out.println("Your command line string: " + command_line_string);

        in.close();

        //ExpressionParser expression_parser = new ExpressionParser();
        ExpressionParserBasic expression_parser = new ExpressionParserBasic();

        //if(expression_parser.parseExpression(command_line_string)){
        if(expression_parser.parseExpression(expression_line)){
            ComplexNumber result_complex_number = expression_parser.solveComplexExpression();
            System.out.println("Real part: " + result_complex_number.getReal());
            System.out.println("Imaginary part: " + result_complex_number.getImaginary());
        }

        else{
            System.out.println("Invalid expression!");
        }



//        // Using Scanner for Getting Input from User
//        Scanner in = new Scanner(System.in);
//
//        String s = in.nextLine();
//        System.out.println("You entered string " + s);
//
//        int a = in.nextInt();
//        System.out.println("You entered integer " + a);
//
//        float b = in.nextFloat();
//        System.out.println("You entered float " + b);
//
//        // closing scanner
//        in.close();
    }
}
