import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {

            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());

            String laestFraKlient = in.readLine();

            String[] a = null;
            if (laestFraKlient != null) {
                a = laestFraKlient.split("\\s+");
            }

            if (a != null && !a[1].endsWith(".ico")) {
                out.writeBytes("HTTP 200 All is OK\n");
                out.writeBytes("Content-Type: " + getContentType(a[1]) + "\n");
                out.writeBytes("Connection: close\n");
                out.writeBytes("\n");

                byte[] b = readFile("/var/www/java" + a[1]);
                if (b != null) {
                    out.write(b);
                } else {
                    out.writeBytes("HTTP 404 Not Found\n");
                    out.writeBytes("Connection: close\n");
                    out.writeBytes("\n");
                }
            } else {
                out.writeBytes("HTTP 404 Not Found\n");
                out.writeBytes("Connection: close\n");
                out.writeBytes("\n");
            }
            connectionSocket.close();
        }
    }

    static String getContentType(String filnavn) {
        if (filnavn.endsWith(".html") || filnavn.endsWith(".htm")) {
            return "text/html";
        } else if (filnavn.endsWith(".jpg") || filnavn.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filnavn.endsWith(".gif")) {
            return "image/gif";
        } else if (filnavn.endsWith(".png")) {
            return "image/png";
        } else if (filnavn.endsWith(".pdf")) {
            return "application/pdf";
        } else if (filnavn.endsWith(".txt")) {
            return "text/plain";
        } else if (filnavn.endsWith(".css")) {
            return "text/css";
        } else if (filnavn.endsWith(".js")) {

            return "application/javascript";
        } else if (filnavn.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }

    static byte[] readFile(String filnavn) {
        try {
            long length = java.nio.file.Files.size(java.nio.file.Paths.get(filnavn));
            return Arrays.copyOfRange(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filnavn)), 0, Math.toIntExact(length));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}