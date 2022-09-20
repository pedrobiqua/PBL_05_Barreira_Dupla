import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Trabalhadora extends Thread {
    Semaphore mutex, barreiraEntrada, barreiraSaida;
    int numeroThreads;
    int contador = 0;
    ArrayList<String> generatedFiles = new ArrayList<>();

    public Trabalhadora(Semaphore mutex, Semaphore barreiraEntrada, Semaphore barreiraSaida, int numeroThreads) {
        this.mutex = mutex;
        this.barreiraEntrada = barreiraEntrada;
        this.barreiraSaida = barreiraSaida;
        this.numeroThreads = numeroThreads;
    }

    @Override
    public void run() {
        try {
            mutex.acquire();
                File file = createInitialFile();
                addNumbersToFile(file);
                sortNumbersIntoNewFile(file);
                contador += 1;

                if (contador == numeroThreads) {
                    barreiraSaida.acquire();
                    barreiraEntrada.release();
                }

            mutex.release();
            barreiraEntrada.acquire();
            barreiraEntrada.release();

            generatedFiles.add(file.getName());
            // Sinaliza a thread Combinadora que o novo arquivo está disponível. 

            mutex.acquire();
                contador -= 1;
                if (contador == 0) {
                    barreiraEntrada.acquire();
                    barreiraSaida.release();
                }
            mutex.release();
            barreiraSaida.acquire();
            barreiraSaida.release();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public File createInitialFile() throws IOException {
        Random random = new Random();
        int randomSeed = random.nextInt(10);
        File myObj = new File("ArquivosTrabalhadora/trabalhadora" + randomSeed + ".txt");
        myObj.createNewFile();

        return myObj;
    }

    public void addNumbersToFile(File file) throws IOException {
        Random random = new Random();
        FileWriter myWriter = new FileWriter(file.getName());
        int numberCount = 0;
        while (numberCount < 1000000) {
            int randomNumber = random.nextInt(10000001);
            myWriter.write(randomNumber);
        }
        myWriter.close();
    }

    public void sortNumbersIntoNewFile(File file) throws Exception {
        Random random = new Random();
        int randomSeed = random.nextInt(10);
        File newFile = new File("ArquivosTrabalhadoraOrdenado/trabalhadora" + randomSeed + ".txt");
        FileWriter myWriter = new FileWriter(newFile.getName());
        Scanner fileInput;
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        fileInput = new Scanner(file);

        while (fileInput.hasNext()){
            numbers.add(fileInput.nextInt());
        }
        fileInput.close();
        Collections.sort(numbers);
        for (int number : numbers) {
            myWriter.write(number);
        }
        myWriter.close();
    }
}
