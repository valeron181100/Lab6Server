package FileSystem;

import NetStuff.User;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class GlobalVariables {
    public static Hashtable<User, SocketAddress> users = new Hashtable<>();
}
