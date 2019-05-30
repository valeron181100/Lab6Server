package NetStuff.DataBaseWorks;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger {

    private FileWriter writer;

    public FileLogger(){
        try {
            writer = new FileWriter("logs.txt", true);
        } catch (IOException e) {
        }

    }


    public void log(String message){
        try {
            writer.append(message);
            writer.close();
            writer = new FileWriter("logs.txt", true);
        } catch (IOException e) {
        }
    }
}
