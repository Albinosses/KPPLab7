package backend;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Bank {
    private volatile int TotalCash;

    public Bank(int totalCash){
        TotalCash = totalCash;
    }

    public synchronized void withdrawCash(int amount){
        try {
            if (!Files.exists(Path.of("log.txt"))) {
                Files.createFile(Path.of("log.txt"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (TotalCash >= amount) {
            TotalCash -= amount;
            String message = "Зняття готівки на суму " + amount + " грн. Залишок: " + TotalCash + " грн.";

            writeToFile("log.txt", message);
        } else {
            writeToFile("log.txt", "Недостатньо коштів на рахунку");
        }
    }

    public synchronized void depositCash(int amount) {
        try {
            if (!Files.exists(Path.of("log.txt"))) {
                Files.createFile(Path.of("log.txt"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        TotalCash += amount;
        String message = "Внесення депозиту на суму " + amount + " грн. Залишок: " + TotalCash + " грн.";

        writeToFile("log.txt", message);
    }

    private void writeToFile(String filename, String message) {
        try {
            String existingContent = Files.readString(Path.of(filename));

            String newContent = message + System.lineSeparator() + existingContent;

            Files.write(Path.of(filename), newContent.getBytes(), StandardOpenOption.WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalCash() {
        return TotalCash;
    }

    public void setTotalCash(int totalCash) {
        TotalCash = totalCash;
    }
}
