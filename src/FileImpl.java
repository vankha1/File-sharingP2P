import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileImpl extends UnicastRemoteObject
      implements FileSharingClient {

   private String directoryName;

   public FileImpl(String s) throws RemoteException {
      super();
      directoryName = s;
   }

   public byte[] downloadFile(FileDetails file) {
      byte[] data;
      String filePath = file.SourceDirectoryName + "\\" + file.FileName;
      File fileToDown = new File(filePath);
      data = new byte[(int) fileToDown.length()];
      FileInputStream in;
      try {
         in = new FileInputStream(filePath);
         try {
            in.read(data, 0, data.length);
         } catch (IOException e) {

            e.printStackTrace();
         }
         try {
            in.close();
         } catch (IOException e) {

            e.printStackTrace();
         }

      } catch (FileNotFoundException e) {

         e.printStackTrace();
      }
      return data;
   }
}
