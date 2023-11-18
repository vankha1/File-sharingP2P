import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class FileSharing extends UnicastRemoteObject implements FileSharingInterface {
    private ArrayList<FileDetails> Files;
    private ArrayList<String> clients;

    // private ArrayList<FileDetails> FilesMatched;
    public FileSharing() throws RemoteException {
        super();
        Files = new ArrayList<FileDetails>();
        clients = new ArrayList<String>();
    }

    public void addClient(String peerID){
        this.clients.add(peerID);
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
            System.out.println("There are no files to discover");
        } else {
            for (int i = 0; i < Files.size(); i++) {
                FileDetails fd = Files.get(i);
                if (fd.peerID.equals(peerID)) {
                    System.out.println("File name " + fd.FileName + " registered with peerID " + fd.peerID + " on port number " + fd.portNumber + " and the directory is " + fd.SourceDirectoryName);
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

    public void removeClient(String peerID) throws RemoteException {
        this.clients.remove(peerID);
        System.out.println(this.clients);
    }

    public ArrayList<FileDetails> searchFile(String filename) throws RemoteException {
        ArrayList<FileDetails> FilesMatched= new ArrayList<FileDetails>();
        for(int i=0;i<this.Files.size();i++)
        {
            if(filename.equalsIgnoreCase(Files.get(i).FileName))
            {
                FilesMatched.add(Files.get(i));

            }
        }
        return (FilesMatched);
    }
}