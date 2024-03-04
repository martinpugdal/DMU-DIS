package Talk23V_Opgave_4B.Talk23V_Opgave_4.example.DNS_TCP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class DNSTread implements Runnable{
    private final Socket clientSocket;
    private IpLookUp ipLookUp;

    public DNSTread(Socket clientSocket, IpLookUp ipLookUp) {
        this.clientSocket = clientSocket;
        this.ipLookUp = ipLookUp;
    }


    @Override
    public void run() {
        try {
            String clientRequst;
            String ipResons;


            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());


            clientRequst = inFromClient.readLine();
            System.out.println(clientRequst);
            ipResons =  this.ipLookUp.ipLookUp(clientRequst);
            outToClient.writeBytes(ipResons);
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

