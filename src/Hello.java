import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class Hello extends UnicastRemoteObject implements HelloInterface {
    private ArrayList<FileDetails> Files;
    private ArrayList<String> clients;

    // private ArrayList<FileDetails> FilesMatched;
    public Hello() throws RemoteException {
        super();
        Files = new ArrayList<FileDetails>();
        clients = new ArrayList<String>();
    }

    public void addClient(String peerId){
        this.clients.add(peerId);
        System.out.println(this.clients);
    }   

    public synchronized void registerFiles(String peerId, String fileName, String portno, String srcDir)
            throws RemoteException {
        FileDetails fd = new FileDetails();
        fd.peerId = peerId;
        fd.FileName = fileName;
        fd.portNumber = portno;
        fd.SourceDirectoryName = srcDir;
        this.Files.add(fd);
    }


    public void discoverClient(String peerID) throws RemoteException {
        boolean found = false;
        if (Files.isEmpty()) {
            System.out.println("There are no files to discover");
        } else {
            for (int i = 0; i < Files.size(); i++) {
                FileDetails fd = Files.get(i);
                if (fd.peerId.equals(peerID)) {
                    System.out.println("File name " + fd.FileName + " registered with peerID " + fd.peerId + " on port number " + fd.portNumber + " and the directory is " + fd.SourceDirectoryName);
                    found = true;
                }
            }
            if (!found){
                System.out.println("There are no files of peer " + peerID);
            }
        }
    }

    public void pingClient(String peerID) throws RemoteException {
        boolean isAlive = false;
        for (int i = 0; i < this.clients.size(); i++){
            if (this.clients.get(i).equals(peerID)) {
                isAlive = true;
                break;
            }
        }
        if (isAlive) {
            System.out.println("Client " + peerID + "  sitll alive");
        } else {
            System.out.println("Client " + peerID + " died");
        }
    }

    public void removeClient(String peerId) throws RemoteException {
        this.clients.remove(peerId);
        System.out.println(this.clients);
    }

    public FileDetails search(String fileName) throws RemoteException {
        FileDetails FileMatched = new FileDetails();
        for (int i = 0; i < this.Files.size(); i++) {
            if (fileName.equalsIgnoreCase(Files.get(i).FileName)) {
                FileMatched = Files.get(i);
                break;
            }
        }
        return FileMatched;
    }
}