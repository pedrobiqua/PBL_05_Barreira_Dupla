import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("PBL 05 BARREIRA DUPLA");

        Semaphore barreiraEntrada = new Semaphore(0); // fechada 
        Semaphore barreiraSaida = new Semaphore(1); // aberta 
        Semaphore mutex = new Semaphore(1);
        int n = 4;

        Trabalhadora t1 = new Trabalhadora(mutex, barreiraEntrada, barreiraSaida, n);
        Trabalhadora t3 = new Trabalhadora(mutex, barreiraEntrada, barreiraSaida, n);
        Trabalhadora t4 = new Trabalhadora(mutex, barreiraEntrada, barreiraSaida, n);
        Trabalhadora t2 = new Trabalhadora(mutex, barreiraEntrada, barreiraSaida, n);
        Combinadora c = new Combinadora();

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        c.start();
    }
}
