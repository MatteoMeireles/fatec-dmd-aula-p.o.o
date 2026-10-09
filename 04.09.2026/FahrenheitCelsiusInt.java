package projeto;
import java.util.Scanner;

public class FahrenheitCelsiusInt {
    public static void main(String[] args) {
 
        Scanner entrada = new Scanner(System.in);
 
        int f;
        int c;
 
        System.out.print("Digite a temperatura em Fahrenheit: ");
        f = entrada.nextInt();
 
        c = (f - 32) * (5 / 9);
 
        System.out.println("Temperatura em Celsius: " + c);
    }
}