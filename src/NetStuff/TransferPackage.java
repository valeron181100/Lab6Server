package NetStuff;

import Clothes.Costume;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransferPackage implements Serializable {

    private int id;

    private String cmdData;

    private transient Stream<Costume> data;

    private byte[] additionalData;

    public TransferPackage(){
        this.id = 666;
    }

    public boolean isEmpty(){
        return id == 666;
    }

    public TransferPackage(int id, String cmdData, Stream<Costume> data){
        this.id = id;
        this.cmdData = cmdData;
        this.data = data;
    }

    public TransferPackage(int id, String cmdData, Stream<Costume> data, byte[] additionalData){
        this.id = id;
        this.cmdData = cmdData;
        this.data = data;
        this.additionalData = additionalData;
    }

    public byte[] getAdditionalData() {
        return additionalData;
    }

    public String getCmdData() {
        return cmdData;
    }

    public Stream<Costume> getData() {
        return data;
    }

    public void setData(Stream<Costume> data) {
        this.data = data;
    }

    public void setAdditionalData(byte[] additionalData) {
        this.additionalData = additionalData;
    }

    public int getId() {
        return id;
    }

    public static TransferPackage restoreObject(InputStream inputStream){
        try(ObjectInputStream ois = new ObjectInputStream(inputStream)){
            Object obj = ois.readObject();
            return (TransferPackage)obj ;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private void writeObject(ObjectOutputStream out) throws  IOException{
        out.defaultWriteObject();
        if (getData() != null){
            ArrayList<Costume> list = new ArrayList<>();
            getData().sequential().collect(Collectors.toCollection(() -> list));
            out.writeObject(list);
        }
        else
            out.writeObject(null);
    }

    private void readObject(ObjectInputStream in) throws  IOException, ClassNotFoundException{
        in.defaultReadObject();
        Object obj = in.readObject();
        ArrayList<Costume> list = new ArrayList<>();
        if(obj != null){
            list = (ArrayList<Costume>)obj;
            this.data = list.stream();
        }
        else {
            this.data = null;
        }
    }

    public byte[] getBytes(){

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
