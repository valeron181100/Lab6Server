package FileSystem;

import NetStuff.User;

import java.io.*;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class UsersVariables {
    public static Hashtable<User, SocketAddress> users = new Hashtable<>();
    public static Hashtable<User,SocketAddress> onlineUsers = new Hashtable<>();
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

            users = (Hashtable<User, SocketAddress>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Файл с пользователями не найден!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Что-то пошло не так при восстановлении пользователей!");
        }

    }
}
