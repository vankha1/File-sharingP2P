import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileSharingClient extends Remote {
  public void downloadFile(String fileName, byte[] fileContent) throws RemoteException;
}
