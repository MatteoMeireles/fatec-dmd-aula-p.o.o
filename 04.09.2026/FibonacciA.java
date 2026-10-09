package projeto;

public class FibonacciA {
    public static void main(String[] args) {
   	 
        int n1 = 0;
        int n2 = 1;
        int n3;
        int i = 0;
 
        while (i < 30) {
 
            System.out.println(n1);
 
            n3 = n1 + n2;
            n1 = n2;
            n2 = n3;
 
            i++;
        }
    }
}
