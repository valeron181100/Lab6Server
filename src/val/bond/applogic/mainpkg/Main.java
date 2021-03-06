package val.bond.applogic.mainpkg;

import val.bond.applogic.Clothes.Costume;
import val.bond.applogic.FileSystem.UsersVariables;
import val.bond.applogic.NetStuff.DataBaseWorks.DBController;
import val.bond.applogic.NetStuff.DataBaseWorks.JDBCConnector;
import val.bond.applogic.NetStuff.Mail.MailSender;
import val.bond.applogic.NetStuff.Mail.MailService;
import val.bond.applogic.NetStuff.Net.Client;
import val.bond.applogic.NetStuff.Net.ServerThreadHandler;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private static final int TIMEOUT = 3000;

    public static final String DEFAULT_CHAR_SET = "UTF-8";

    public static Set<Pair<Costume, String>> objectsHashSet = ConcurrentHashMap.newKeySet();

    public static MailSender mailSender = new MailSender(MailService.GMAIL, "valeron.bondar181100@gmail.com", "Evgeniya1973");

    public static boolean writeCollection(Set<Pair<Costume, String>> collection){
        try (FileOutputStream writer = new FileOutputStream("collection.xml");
             ObjectOutputStream oos = new ObjectOutputStream(writer)){
            oos.writeObject(collection);
        } catch (IOException e) {
            System.err.println("Что-то пошло не так при сохраненнии коллекции!");
            return false;
        }
        System.out.println("Writing was ended!");
        return true;
    }

    public static JDBCConnector jdbcConnector;
    public static DBController controller;

    static {
        jdbcConnector = new JDBCConnector();
        controller = new DBController(jdbcConnector);
    }

    @SuppressWarnings("unchecked")
    public static Set<Pair<Costume, String>> getCollectionFromFile(){
        try (FileInputStream reader = new FileInputStream("collection.xml");
             ObjectInputStream ois = new ObjectInputStream(reader)){

            return (Set<Pair<Costume, String>>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Файл с коллекцией не найден!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Что-то пошло не так при восстановлении коллекции!");
        }
        return null;
    }

    public static Set<Pair<Costume, String>> getObjectsHashSet(){
        return objectsHashSet;
    }

    public static void main(String[] args) {
        /*
        Set<Pair<Costume, String>> collectionFromFile = getCollectionFromFile();
        if(collectionFromFile != null)
            objectsHashSet.addAll(collectionFromFile);

        Executors.newScheduledThreadPool(1).schedule(() -> {
            HashSet<Pair<Costume, String>> pairs = new HashSet<>();
            for (Map.Entry<User, SocketAddress> entry : UsersVariables.onlineUsers.entrySet()) {
                Set<Pair<Costume, String>> pairSet = objectsHashSet.stream().filter(p -> p.getValue().equals(entry.getKey().getLogin())).collect(Collectors.toSet());
                pairs.addAll(pairSet);
            }
            objectsHashSet.clear();
            objectsHashSet.addAll(pairs);
        }, 10, TimeUnit.SECONDS);*/

        try {

            Runtime.getRuntime().addShutdownHook(new Thread(UsersVariables::shutdownServerSendPackage));

            //UsersVariables.restoreUsers();
            if(args.length == 0){
                System.out.println("Введите порт!");
                System.exit(0);
            }

            objectsHashSet.clear();
            objectsHashSet.addAll(controller.getCostumesFromDB());

            int port = Integer.parseInt(args[0]);

            Selector selector = Selector.open();
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            datagramChannel.socket().bind(new InetSocketAddress(port));
            datagramChannel.register(selector, SelectionKey.OP_READ, new Client());

            boolean done = false;

            while(true){
                if (selector.select(TIMEOUT) == 0) {
                    System.out.print(".");
                    continue;
                }

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();

                    ServerThreadHandler handler = new ServerThreadHandler(key);
                    handler.run();

                    keyIterator.remove();
                }
            }


        }
        catch (BindException e){
            System.err.println("Ошибка: Порт уже занят!");
        }
        catch (IOException e) {
            System.out.println("Упс, что-то пошло не так!");
        } catch (SQLException e) {
            System.err.println("Что-то пошло не так при восстановлении коллекции");
        }

    }

    public static int strHashCode(String str){
        int result = 13;
        int prime = 26;
        for (int i = 0; i < str.length(); i++) {
            result = result * prime + (int)str.charAt(i) * (int)Math.pow(prime, i + 1);
        }
        return result;
    }

}
