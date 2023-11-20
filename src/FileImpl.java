
// import java.io.BufferedInputStream;
// import java.io.File;
// import java.io.FileInputStream;
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

   public void downloadFile(String fileName, byte[] fileContent) {
      try {
         FileOutputStream os = new FileOutputStream(directoryName + "\\" + fileName
               );
         os.write(fileContent);
         System.out.println("Download successfully!.");
         os.close();
      } catch (IOException e) {
         e.printStackTrace();
      } 
   }
}
