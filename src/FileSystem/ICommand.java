package FileSystem;

import NetStuff.Net.TransferPackage;

import java.io.UnsupportedEncodingException;

interface ICommand{
    void start(Command command, TransferPackage transferPackage) throws UnsupportedEncodingException;
}
