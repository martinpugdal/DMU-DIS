package opgave1.thread;

import opgave1.SharedString;

public class ShowLastestInputThread extends Thread {

    private final SharedString sharedString;

    public ShowLastestInputThread(SharedString sharedString) {
        this.sharedString = sharedString;
    }

    /*
     * Tråd2 udskriver på skærmen hvert 3. sekund den seneste streng, som tråd1 har indlæst.
     */
    @Override
    public void run() {
        while (true) {
            if (!sharedString.getSharedString().isEmpty()) {
                System.out.println(sharedString.getSharedString());
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
