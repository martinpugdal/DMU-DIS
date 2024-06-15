package Skeleton2024;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket connSocket;

    public ServerThread(Socket connSocket) {
        this.connSocket = connSocket;
    }

    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());

            String clientSentence = inFromClient.readLine();

            if (clientSentence != null && clientSentence.startsWith("GET")) {
                String[] requestParts = clientSentence.split(" ");
                if (requestParts.length > 1) {
                    String fileName = requestParts[1].substring(1);
                    File file = new File("C:\\Users\\Martin\\Documents\\GitHub\\DMU-DIS\\L03\\src\\Skeleton2024\\filer", fileName);

                    if (file.exists() && !file.isDirectory() && file.canRead()) {
                        String contentType = ContentType(fileName);
                        byte[] fileContent = read(file.getAbsolutePath());

                        outToClient.writeBytes("HTTP/1.1 200 OK\r\n");
                        outToClient.writeBytes("Content-Type: " + contentType + "\r\n");
                        outToClient.writeBytes("Connection: close\r\n");
                        outToClient.writeBytes("\r\n"); // End of headers

                        outToClient.write(fileContent);
                    } else {
                        String notFoundMessage = "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Connection: close\r\n" +
                            "\r\n" +
                            "<h1>404 Not Found</h1>";
                        outToClient.writeBytes(notFoundMessage);

                    }
                }
            }
            connSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String ContentType(String docuname) {
        if (docuname.endsWith(".html")) {
            return ("Content-Type: text/html\n");
        } else if (docuname.endsWith(".gif")) {
            return ("Content-Type: image/gif\n");
        } else if (docuname.endsWith(".png")) {
            return ("Content-Type: image/png\n");
        } else if (docuname.endsWith(".jpg")) {
            return ("Content-Type: image/jpg\n");
        } else if (docuname.endsWith(".js")) {
            return ("Content-Type: text/javascript\n");
        } else if (docuname.endsWith(".css")) {
            return ("Content-Type: text/css\n");
        } else {
            return ("Content-Type: text/plain\n");
        }
    }

    public byte[] read(String aInputFileName) throws FileNotFoundException {
        File file = new File(aInputFileName);
        byte[] result = new byte[(int) file.length()];
        try {
            InputStream input = null;
            try {
                int totalBytesRead = 0;
                input = new BufferedInputStream(new FileInputStream(file));
                while (totalBytesRead < result.length) {
                    int bytesRemaining = result.length - totalBytesRead;
                    int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
                    // input.read() returns -1, 0, or more :
                    if (bytesRead > 0) {
                        totalBytesRead = totalBytesRead + bytesRead;
                    }
                }
            } finally {
                input.close();
            }
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
