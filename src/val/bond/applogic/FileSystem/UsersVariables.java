package val.bond.applogic.FileSystem;

import org.json.JSONArray;
import org.json.JSONObject;
import val.bond.applogic.Clothes.Costume;
import val.bond.applogic.NetStuff.Net.TransferPackage;
import val.bond.applogic.NetStuff.Net.User;
import val.bond.applogic.mainpkg.Main;
import val.bond.applogic.mainpkg.Pair;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

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

    public static void showSendPackage(){
        try {
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(null);

            JSONArray response = new JSONArray();
            try {
                ArrayList<Pair<Costume, String>> costumes = Main.controller.showCostumesFromDB();

                costumes.forEach(p->{
                    JSONObject costumeData = new JSONObject();
                    costumeData.put("costumeId", Integer.parseInt(p.getValue().split(":")[0]));
                    costumeData.put("costumeName", p.getKey().toString());
                    costumeData.put("costumeLogin", p.getValue().split(":")[1]);
                    response.put(costumeData);
                });

            } catch (SQLException e) {
                System.err.println(e.getSQLState() + e.getMessage());
            }

            JSONObject sending = new JSONObject();
            sending.put("array",response);
            TransferPackage tpkg = new TransferPackage(2, "Команда выполнена.",null, sending.toString().getBytes(Main.DEFAULT_CHAR_SET));
            byte[] bytes = tpkg.getBytes();
            onlineUsers.forEach((k,v)->{
                try {
                    channel.send(ByteBuffer.wrap(bytes), v);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void shutdownServerSendPackage(){
        try {
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(null);
            TransferPackage tpkg = new TransferPackage(9999, "Сервер отключился!", null);
            byte[] bytes = tpkg.getBytes();
            onlineUsers.forEach((k, v) -> {
                try {
                    channel.send(ByteBuffer.wrap(bytes), v);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void restoreUsers(){
        /*try (FileInputStream reader = new FileInputStream("users.base");
             ObjectInputStream ois = new ObjectInputStream(reader)){

            users = (ConcurrentHashMap<User, SocketAddress>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Файл с пользователями не найден!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Что-то пошло не так при восстановлении пользователей!");
        }
*/


    }
}
