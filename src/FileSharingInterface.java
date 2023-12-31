
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// Defining the remote interface - A remote object is an instance of a class that implements a remote interface. A remote interface extends the interface java.rmi.Remote and declares a set of remote methods. 
public interface FileSharingInterface extends Remote {

    public void registerFiles(String peerID, String filename, String portno, String srcDir) throws RemoteException;

    public ArrayList<FileDetails> searchFile(String filename)throws RemoteException;

    public void renameFile(String filename, String newFileName) throws RemoteException;

    public void deleteFile(String filename)throws RemoteException;

    public boolean addClient(String peerID) throws RemoteException;

    public void removeClient(String peerId) throws RemoteException;

    public void pingClient(String peerID) throws RemoteException;

    public void discoverClient(String peerID) throws RemoteException;

    public void listAllClients() throws RemoteException;

}
