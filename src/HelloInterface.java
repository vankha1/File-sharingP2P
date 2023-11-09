
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// Remote interface: Nên extend từ java.rmi.remote. Nó khai báo tất cả các phương thức mà Client có thể gọi. Tất cả các method trong interface này nên throw RemoteException
// Defining the remote interface - A remote object is an instance of a class that implements a remote interface. A remote interface extends the interface java.rmi.Remote and declares a set of remote methods. 
public interface HelloInterface extends Remote {
    // two sets of remote methods. Each remote method must declare
    // java.rmi.RemoteException (or a superclass of RemoteException) in its throws
    // clause, in addition to any application-specific exceptions. Since there is a
    // chance of network issues during remote calls, an exception named
    // RemoteException may occur; throw it.
    public void registerFiles(String peerId, String filename, String portno, String srcDir) throws RemoteException;

    public FileDetails search(String fileName) throws RemoteException;

    public void addClient(String peerId) throws RemoteException;

    public void getClientFiles() throws RemoteException;

    public void removeClient(String peerId) throws RemoteException;

    public String getLiveClients(String text) throws RemoteException;
}
