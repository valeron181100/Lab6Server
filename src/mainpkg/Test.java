package mainpkg;

import NetStuff.DataBaseWorks.Hash.StringHasher;
import NetStuff.Net.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {
    public static void main(String[] args) throws Exception {
        StringHasher stringHasher = new StringHasher("SHA-384");
        String res = stringHasher.getHash("va181100");
        System.out.println(res);
    }
}


