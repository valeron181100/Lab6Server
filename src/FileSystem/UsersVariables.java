package FileSystem;

import NetStuff.User;

import java.io.*;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UsersVariables {
    public static ConcurrentHashMap<User, SocketAddress> users = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<User,SocketAddress> onlineUsers = new ConcurrentHashMap<>();
    public static void saveUsers(){
        try (FileOutputStream writer = new FileOutputStream("users.base");
             ObjectOutputStream oos = new ObjectOutputStream(writer)){
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Что-то пошло не так при сохраненнии пользователей!");
        }
    }

    @SuppressWarnings("unchecked")
    public static void restoreUsers(){
        try (FileInputStream reader = new FileInputStream("users.base");
             ObjectInputStream ois = new ObjectInputStream(reader)){

            users = (ConcurrentHashMap<User, SocketAddress>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Файл с пользователями не найден!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Что-то пошло не так при восстановлении пользователей!");
        }

    }
}
