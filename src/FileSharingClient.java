import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileSharingClient extends Remote {
  public byte[] downloadFile(FileDetails file) throws RemoteException;
}
