package NetStuff.Server;

import Clothes.Costume;
import FileSystem.CollectionManager;
import FileSystem.Command;
import FileSystem.EmptyFileException;
import FileSystem.UsersVariables;
import mainpkg.Pair;
import mainpkg.Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerThreadHandler implements Runnable {

    /**
     * Буфер для получения и отправки байтов
     */
    private ByteBuffer buffer = ByteBuffer.allocate(Integer.MAX_VALUE/16);

    private SocketAddress templeAdress = null;

    private static Command previousCmd = null;

    private SelectionKey key;

    public ServerThreadHandler(SelectionKey key){
        this.key = key;
    }

    @Override
    public void run() {
        if (key.isReadable()){
            read(key);
        }

        if (key.isWritable()){
            write(key);
        }
    }

    private void read(SelectionKey key){
        DatagramChannel channel = (DatagramChannel) key.channel();
        Client client = (Client) key.attachment();
        buffer.clear();

        try {
            SocketAddress adress = channel.receive(buffer);
            client.setAdress(adress);
            TransferPackage recieved = TransferPackage.restoreObject(new ByteArrayInputStream(buffer.array()));
            if(recieved.getId() == TransferCommandID.CheckingConnectionTP.getId()){
                client.setaPackage(recieved);
                key.interestOps(SelectionKey.OP_WRITE);
                return;
            }
            if(recieved.getId() == 111){
                try(ByteArrayInputStream bais = new ByteArrayInputStream(recieved.getAdditionalData());
                    ObjectInputStream ois = new ObjectInputStream(bais)){
                    User user = (User) ois.readObject();
                    user.setLoggedIn(true);
                    UsersVariables.onlineUsers.remove(user);
                    List<Pair<Costume, String>> colRmItems = Main.getObjectsHashSet().stream().filter(p -> !p.getValue().equals(user.getLogin())).collect(Collectors.toList());
                    Main.getObjectsHashSet().clear();
                    Main.getObjectsHashSet().addAll(colRmItems);
                    System.out.println("Пользователь " + user.getLogin() + " был отключён!");
                } catch (ClassNotFoundException e) {
                    System.err.println("Ошибка при дессериализации пользователя");
                }
                return;
            }
            if (recieved.getAdditionalData() != null && recieved.getCmdData().equals("load"))
                recieved.setData(CollectionManager.getCollectionFromXML(new String(recieved.getAdditionalData(), Main.DEFAULT_CHAR_SET)).stream());
            final User[] user = new User[1];
            UsersVariables.onlineUsers.forEach((k,v)->{
                if(v.equals(adress))
                    user[0] = k;
            });
            if(user[0] != null && !recieved.getCmdData().equals("load")){
                Stream<Pair<Costume, String>> userStream = Main.getObjectsHashSet().stream().filter(p -> p.getValue().equals(user[0].getLogin()));
                if(userStream.count() == 0){
                    client.setaPackage(new TransferPackage(-1, "Прежде чем работать с вашей коллекцией, загрузите её с помощью комманды 'load' ", null));
                    key.interestOps(SelectionKey.OP_WRITE);
                    return;
                }
            }

            Command command = Command.parseCmd(recieved.getCmdData().trim());
            if(previousCmd!=null)
            System.out.println("Previous CMD : " + previousCmd.toString());
            if(command == null)
                client.setaPackage(new TransferPackage(-1, "Неверная команда!", null));
            else {
                System.out.println("Current CMD : " + command.toString());
                if (command == Command.I1A8S1D1F0G0H && previousCmd != Command.IMPORT){
                    client.setaPackage(new TransferPackage(-1, "Неверная команда!", null));
                }
                else {
                    command.setAddress(adress);
                    client.setaPackage(command.start(command, recieved));
                    System.out.println("Collection size: " + Main.getObjectsHashSet().size());
                }
            }

            if(client.getAdress() != null){
                key.interestOps(SelectionKey.OP_WRITE);
                previousCmd = command;
            }
        }
        catch (IOException e) {
            System.err.println("Ошибка при получении!");
        }
        catch (IllegalArgumentException e){
            try {
                client.setaPackage(new TransferPackage(-1, "Команда не выполнена.", null,
                        ("Ошибка: Неверная команда.").getBytes(Main.DEFAULT_CHAR_SET)));
            } catch (UnsupportedEncodingException e1) {
                System.err.println(e1.getMessage());
            }
        }
        catch (EmptyFileException e) {
            try {
                client.setaPackage(new TransferPackage(-1, "Команда не выполнена.", null,
                        ("Файл с коллекцией пуст!").getBytes(Main.DEFAULT_CHAR_SET)));
            } catch (UnsupportedEncodingException e1) {
                System.err.println(e1.getMessage());
            }
        }
        catch (NullPointerException e){
            client.setaPackage(new TransferPackage(-1, "Команда не выполнена. Войдите на сервер еще раз с помощью комманды login", null));
            key.interestOps(SelectionKey.OP_WRITE);
            return;
        }
    }

    private void write(SelectionKey key){
        try {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.bind(null);
        Client client = (Client) key.attachment();

        buffer.flip();


            if(client.getaPackage() != null)
                channel.send(ByteBuffer.wrap(client.getaPackage().getBytes()), client.getAdress());
            else
                channel.send(ByteBuffer.wrap(new TransferPackage(TransferCommandID.ERROR.getId(),"Ошибка: команда не найдена",
                        null).getBytes()), client.getAdress());
            key.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            System.err.println("Ошибка при отправке!");
        }
    }
}
