package NetStuff;

import FileSystem.CollectionManager;
import FileSystem.Command;
import FileSystem.EmptyFileException;
import mainpkg.Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class ServerThreadHandler implements Runnable {

    /**
     * Буфер для получения и отправки байтов
     */
    private ByteBuffer buffer = ByteBuffer.allocate(Integer.MAX_VALUE/16);

    private SocketAddress templeAdress = null;

    private Command previousCmd = null;

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
            if (recieved.getAdditionalData() != null && recieved.getId() != 110)
                recieved.setData(CollectionManager.getCollectionFromXML(new String(recieved.getAdditionalData(), Main.DEFAULT_CHAR_SET)).stream());
            Command command = Command.parseCmd(recieved.getCmdData().trim());
            if(command == null)
                client.setaPackage(new TransferPackage(-1, "Неверная команда!", null));
            else {
                if (command == Command.I1A8S1D1F0G0H && previousCmd != Command.IMPORT){
                    client.setaPackage(new TransferPackage(-1, "Неверная команда!", null));
                }
                else {
                    command.setAddress(adress);
                    client.setaPackage(command.start(command, recieved));
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
