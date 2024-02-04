package opgave1;

import opgave1.thread.ReadScannerInputThread;
import opgave1.thread.ShowLastestInputThread;

public class Main {

    public static void main(String[] args) {

        SharedString sharedString = new SharedString();
        Thread readScannerInputThread = new ReadScannerInputThread(sharedString);
        Thread showLastestInputThread = new ShowLastestInputThread(sharedString);

        readScannerInputThread.start();
        showLastestInputThread.start();

        try {
            readScannerInputThread.join();
            showLastestInputThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
