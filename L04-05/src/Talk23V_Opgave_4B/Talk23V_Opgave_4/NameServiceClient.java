package Talk23V_Opgave_4B.Talk23V_Opgave_4;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameServiceClient {

    private Map<Socket, String> users = new HashMap<>();

    public void addUser(String user, Socket socket) {
        users.put(socket, user);
    }

    public void removeUser(String ip) {
        users.remove(ip);
    }

    public List<String> getUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Socket> getSockets() {
        return new ArrayList<>(users.keySet());
    }

    public boolean userExists(String user) {
        return users.containsValue(user);
    }

    public boolean socketExists(Socket socket) {
        return users.containsKey(socket);
    }

    public String getUser(Socket socket) {
        return users.get(socket);
    }
}
