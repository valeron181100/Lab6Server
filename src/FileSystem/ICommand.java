package FileSystem;

import NetStuff.TransferPackage;

import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;

interface ICommand{
    void start(Command command, TransferPackage transferPackage) throws UnsupportedEncodingException;
}
