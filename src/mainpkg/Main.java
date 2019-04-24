package mainpkg;

import Clothes.Costume;
import Clothes.TopClothes;
import FileSystem.CollectionManager;
import FileSystem.Command;
import FileSystem.EmptyFileException;
import FileSystem.UsersVariables;
import NetStuff.*;
import com.sun.org.apache.bcel.internal.generic.Select;
import javafx.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Main {

    private static final int TIMEOUT = 3000;

    public static final String DEFAULT_CHAR_SET = "UTF-8";

    public static Set<Pair<Costume, String>> objectsHashSet = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {

        //Runtime.getRuntime().addShutdownHook(new Thread(UsersVariables::saveUsers));

        try {
            UsersVariables.restoreUsers();
            if(args.length == 0){
                System.out.println("Введите порт!");
                System.exit(0);
            }

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
        }

    }

}
