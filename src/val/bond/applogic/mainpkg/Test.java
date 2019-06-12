package val.bond.applogic.mainpkg;

import val.bond.applogic.NetStuff.DataBaseWorks.Hash.StringHasher;

public class Test {
    public static void main(String[] args) throws Exception {
        StringHasher stringHasher = new StringHasher("SHA-384");
        String res = stringHasher.getHash("va181100");
        System.out.println(res);
    }
}


