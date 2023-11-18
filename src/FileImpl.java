
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileImpl extends UnicastRemoteObject
      implements FileSharingClient {

   private String directoryName;

   public FileImpl(String s) throws RemoteException {
      super();
      directoryName = s;
   }

   public byte[] downloadFile(String fileName) {
      try {
         File file = new File(directoryName + "/" + fileName);
         byte buffer[] = new byte[(int) file.length()];
         BufferedInputStream input = new BufferedInputStream(new FileInputStream(directoryName + "//" + fileName));
         input.read(buffer, 0, buffer.length);
         input.close();
         return (buffer);
      } catch (Exception e) {
         System.out.println("FileImpl: " + e.getMessage());
         e.printStackTrace();
         return (null);
      }
   }
}
