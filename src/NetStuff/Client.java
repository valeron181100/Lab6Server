package NetStuff;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Client {
    private SocketAddress adress;
    private TransferPackage aPackage;

    public Client(){ }

    public Client(InetSocketAddress adress, TransferPackage aPackage){
        this.adress = adress;
        this.aPackage = aPackage;
    }

    public SocketAddress getAdress() {
        return adress;
    }

    public TransferPackage getaPackage() {
        return aPackage;
    }

    public void setAdress(SocketAddress adress) {
        this.adress = adress;
    }

    public void setaPackage(TransferPackage aPackage) {
        this.aPackage = aPackage;
    }
}
