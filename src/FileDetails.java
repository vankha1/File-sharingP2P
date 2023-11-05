
import java.io.Serializable;

// objects of this class can be converted to a byte stream and sent over the network or saved to a file.
public class FileDetails implements Serializable {

        String peerId;
        String FileName;
        String portNumber;
        String SourceDirectoryName;

}
