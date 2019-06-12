package val.bond.applogic.FileSystem;

import val.bond.applogic.NetStuff.Net.TransferPackage;

import java.io.UnsupportedEncodingException;

interface ICommand{
    void start(Command command, TransferPackage transferPackage) throws UnsupportedEncodingException;
}
