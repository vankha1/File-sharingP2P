
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nikitha Mahesh
 */

// objects of this class can be converted to a byte stream and sent over the network or saved to a file.
public class FileDetails implements Serializable {

        String peerId;
        String FileName;
        String portNumber;
        String SourceDirectoryName;

}
