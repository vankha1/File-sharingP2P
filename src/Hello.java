
// import java.io.File;
// import java.io.IOException;
// import java.net.MalformedURLException;
// import java.rmi.Naming;
// import java.rmi.NotBoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
// import java.util.logging.Level;
// import java.util.logging.Logger;
// import java.io.BufferedReader;
// import java.io.IOException;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nikitha Mahesh
 */
public class Hello extends UnicastRemoteObject implements HelloInterface {
    private ArrayList<FileDetails> Files;

    // private ArrayList<FileDetails> FilesMatched;
    public Hello() throws RemoteException {
        super();
        Files = new ArrayList<FileDetails>();
    }

    public synchronized void registerFiles(String peerId, String fileName, String portno, String srcDir)
            throws RemoteException {
        FileDetails fd = new FileDetails();
        fd.peerId = peerId;
        fd.FileName = fileName;
        fd.portNumber = portno;
        fd.SourceDirectoryName = srcDir;
        this.Files.add(fd);

        // System.out.println("File name" + " " + fd.FileName + "registered with peerID"
        // + " " + fd.peerId
        // + "on port number" + fd.portNumber + "and the directory is" +
        // fd.SourceDirectoryName);
        // getClientDetail(this.chatClients);

    }

    public void getClientFiles() throws RemoteException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter the peer ID to discover : ");
            String inputCommand = br.readLine();
            String peerID = inputCommand.split(" ")[1];
            if (inputCommand.split(" ")[0].equals("discover")) {
                boolean found = false;
                if (Files.isEmpty()) {
                    System.out.println("There are no files to discover");
                } else {
                    for (int i = 0; i < Files.size(); i++) {
                        FileDetails fd = Files.get(i);
                        // System.out.println(fd.peerId == peerID);
                        if (fd.peerId.equals(peerID)) {
                            System.out.println(
                                    "File name" + " " + fd.FileName + "registered with peerID" + " " + fd.peerId
                                            + "on port number" + fd.portNumber + "and the directory is"
                                            + fd.SourceDirectoryName);
                            found = true;
                        }
                    }
                    if (!found){
                        System.out.println("There are no files of peer" + peerID);
                    }
                }
            } else if (inputCommand.split(" ")[0].equals("ping")) {
                // Get hostname by textual representation of IP address
                InetAddress addr = InetAddress.getByName("127.0.0.1");
                // Get hostname by a byte array containing the IP address
                byte[] ipAddr = new byte[] { 127, 0, 0, 1 };
                addr = InetAddress.getByAddress(ipAddr);
                // Get the host name
                String hostname = addr.getHostName();
                System.out.println(hostname);
                // Get canonical host name
                String hostnameCanonical = addr.getCanonicalHostName();
                System.out.println(hostnameCanonical);
            }
        } catch (Exception e) {
            System.out.println("HelloServer exception: " + e);
        }
    }

    public boolean isAliveChecking(Thread checked) throws RemoteException {
        System.out.println(checked.getState());
        return false;
    }

    public ArrayList<FileDetails> search(String filename) throws RemoteException {
        ArrayList<FileDetails> FilesMatched = new ArrayList<FileDetails>();
        for (int i = 0; i < this.Files.size(); i++) {
            if (filename.equalsIgnoreCase(Files.get(i).FileName)) {
                // System.out.println("The peer ID's having the same filename are
                // "+Files.get(i).peerId);
                FilesMatched.add(Files.get(i));

            }
        }
        return (FilesMatched);
    }
}