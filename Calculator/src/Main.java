import java.util.Scanner;  // Import the Scanner class

class Main {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter first number");

        String number1 = myObj.nextLine();
        System.out.println("Enter second number");
        String number2 = myObj.nextLine();

        System.out.println("Your numbers are " + number1 + " and " + number2);

    }
}