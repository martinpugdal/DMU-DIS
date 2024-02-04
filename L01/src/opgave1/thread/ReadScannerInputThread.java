package opgave1.thread;

import opgave1.SharedString;

import java.util.Scanner;

public class ReadScannerInputThread extends Thread {

    private final SharedString sharedString;

    public ReadScannerInputThread(SharedString sharedString) {
        this.sharedString = sharedString;
    }

    /*
     * Tråd1 indlæser strenge fra tastatur.
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            sharedString.setSharedString(scanner.nextLine());
        }
    }
}
