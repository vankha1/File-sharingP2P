
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


// Remote interface: Nên extend từ java.rmi.remote. Nó khai báo tất cả các phương thức mà Client có thể gọi. Tất cả các method trong interface này nên throw RemoteException
// Defining the remote interface - A remote object is an instance of a class that implements a remote interface. A remote interface extends the interface java.rmi.Remote and declares a set of remote methods. 
public interface FileSharingInterface extends Remote {

    public void registerFiles(String peerID, String filename, String portno, String srcDir) throws RemoteException;

    public ArrayList<FileDetails> searchFile(String filename)throws RemoteException;

    public void renameFile(String filename, String newFileName)throws RemoteException;

    public void deleteFile(String filename)throws RemoteException;

    public void addClient(String peerID) throws RemoteException;


    public void removeClient(String peerId) throws RemoteException;


    public void pingClient(String peerID) throws RemoteException;

    public void discoverClient(String peerID) throws RemoteException;
}
