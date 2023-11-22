
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class FileSharing extends UnicastRemoteObject implements FileSharingInterface {
    private ArrayList<FileDetails> Files;
    private ArrayList<String> clients;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    // private ArrayList<FileDetails> FilesMatched;
    public FileSharing() throws RemoteException {
        super();
        Files = new ArrayList<FileDetails>();
        clients = new ArrayList<String>();
    }

    public boolean addClient(String peerId) {
        if (this.clients.contains(peerId) && !this.clients.isEmpty()){
            return false;
        }
        this.clients.add(peerId);
        System.out.println(ANSI_YELLOW + "Clients are connected in peerID: " + clients + ANSI_RESET);
        
        return true;
    }

    public void removeClient(String peerID) throws RemoteException {
        this.clients.remove(peerID);
    }

    public synchronized void registerFiles(String peerID, String fileName, String portno, String srcDir)
            throws RemoteException {
        FileDetails fd = new FileDetails();
        fd.peerID = peerID;
        fd.FileName = fileName;
        fd.portNumber = portno;
        fd.SourceDirectoryName = srcDir;
        this.Files.add(fd);
    }

    public void discoverClient(String peerID) throws RemoteException {
        boolean found = false;
        if (Files.isEmpty()) {
            System.out.println(ANSI_YELLOW + "There are no files to discover" + ANSI_RESET);
        } else {
            for (int i = 0; i < Files.size(); i++) {
                FileDetails fd = Files.get(i);
                if (fd.peerID.equals(peerID)) {
                    System.out.println(ANSI_YELLOW + "File name " + fd.FileName + " registered with peerID " + fd.peerID
                            + " on port number " + fd.portNumber + " and the directory is " + fd.SourceDirectoryName + ANSI_RESET);
                    found = true;
                }
            }
            if (!found) {
                System.out.println(ANSI_YELLOW + "There are no files of peer " + peerID + ANSI_RESET);
            }
        }
    }

    public void pingClient(String peerID) throws RemoteException {
        boolean isAlive = false;
        for (int i = 0; i < this.clients.size(); i++) {
            if (this.clients.get(i).equals(peerID)) {
                isAlive = true;
                break;
            }
        }
        if (isAlive) {
            System.out.println(ANSI_YELLOW + "Client " + peerID + "  sitll alive" + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Client " + peerID + " died" + ANSI_RESET);
        }
    }

    public void listAllClients() throws RemoteException {
        System.out.println(ANSI_YELLOW + "All clients are available: " + ANSI_RESET);
        System.out.println(ANSI_YELLOW + this.clients + ANSI_RESET);
    }

    public ArrayList<FileDetails> searchFile(String filename) throws RemoteException{
        ArrayList<FileDetails> FilesMatched = new ArrayList<FileDetails>();
        for (int i = 0; i < this.Files.size(); i++) {
            if (filename.equalsIgnoreCase(Files.get(i).FileName)) {
                FilesMatched.add(Files.get(i));

            }
        }
        return (FilesMatched);
    }

    public void renameFile(String filename, String newFileName) throws RemoteException {
        for (int i = 0; i < this.Files.size(); i++) {
            if (Files.get(i).FileName.equals(filename)) {
                Files.get(i).FileName = newFileName;
            }
        }
    }

    public void deleteFile(String filename) throws RemoteException {
        for (int i = 0; i < this.Files.size(); i++) {
            if (Files.get(i).FileName.equals(filename)) {
                Files.remove(i);
            }
        }
    }
}