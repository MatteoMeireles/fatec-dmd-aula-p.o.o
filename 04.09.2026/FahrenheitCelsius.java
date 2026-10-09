package projeto;
import java.util.Scanner;

public class FahrenheitCelsius {
    public static void main(String[] args) {
 
        Scanner entrada = new Scanner(System.in);
 
        double f;
        double c;
 
        System.out.print("Digite a temperatura em Fahrenheit: ");
        f = entrada.nextDouble();
 
        c = (f - 32.0) * (5.0 / 9.0);
 
        System.out.println("Temperatura em Celsius: " + c);
    }
}

