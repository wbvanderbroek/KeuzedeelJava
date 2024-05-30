import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Do you want to multiply(*), divide(/), subtract(-) or add?(+)");
        char char1 = myObj.nextLine().charAt(0);

        System.out.println("Enter first number");
        float number1 = 0f;
        try
        {
            number1 = Float.parseFloat(myObj.nextLine());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        System.out.println("Enter second number");
        float number2 = 0f;
        try
        {
            number2 = Float.parseFloat(myObj.nextLine());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }


        float output = 0f;
        if (char1 == '*')
        {
            output = number1 * number2;
        }
        else if (char1 == '/')
        {
            output = number1 / number2;
        }
        else if (char1 == '-')
        {
            output = number1 - number2;
        }
        else if (char1 == '+')
        {
            output = number1 + number2;
        }
        else
        {
            System.out.println("You gave an incorrect input");
        }
        System.out.println("Output: " + output);
    }
}