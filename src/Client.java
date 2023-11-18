
// import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
// import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {
    String portno = null;
    String directoryName = null;
    String fileTobeSearched = null;
    String peerID = null;
    boolean isAlive = false;
    BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

    Client(String portno) {
        this.portno = portno;
    }

    public void run() {

        isAlive = true;
        try {
            // Looking up the registry for the remote object
            FileSharingInterface hello = (FileSharingInterface) Naming.lookup("Hello");

            // input peerId
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the peer ID: ");
            peerID = br.readLine();

            hello.addClient(peerID);

            String inputLine = br.readLine();
            if (inputLine.equals("exit")) {
                hello.removeClient(peerID);
            }
            while (true) {
                if (inputLine.equals("exit")) {
                    break;
                } else {
                    String[] inputArr = inputLine.split(" \"");
                   if (inputArr[0].equals("publish") && inputArr.length == 3) {
                        String directoryName = inputArr[1].replace("\"", "");
                        String fileName = inputArr[2].replace("\"", "");
                        publishFile(directoryName, fileName);
                   } else if (inputArr[0].equals("fetch") && inputArr.length == 2) {
                        String fileTobeSearched = inputArr[1].replace("\"", "");
                        fetchFile( fileTobeSearched );
                   } else {
                        System.out.println("Invalid input! Please try again!");
                   }
                }
                inputLine = br.readLine();
            }
        } catch (Exception e) {
            System.out.println("HelloClient exception: " + e);
        }
    }

    public void publishFile(String directoryName, String fileName) throws IOException {
        this.directoryName = directoryName;
        try {
            FileSharingInterface hello = (FileSharingInterface) Naming.lookup("Hello");
            try {
                // register file in the directoryName with the remote object (server side).
                // This method is done by the server, so thread of server will log the result of
                hello.registerFiles(peerID, fileName, portno, directoryName);
            } catch (RemoteException ex) {
                Logger.getLogger(FileSharingClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            System.out.println("HelloClient exception: " + e);
        }

        try {
            FileSharingClient fi = new FileImpl(directoryName);
            Naming.rebind("rmi://localhost:" + portno + "/FileServer", fi);
        } catch (Exception e) {
            System.err.println("FileServer exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void fetchFile(String fileTobeSearched) {
        System.out.println(fileTobeSearched);
        try {
            FileSharingInterface hello = (FileSharingInterface) Naming.lookup("Hello");

            ArrayList<FileDetails> FilesName = hello.searchFile(fileTobeSearched);

            downloadFile(FilesName);
        } catch (Exception e) {
            System.out.println("HelloClient exception: " + e);
        }
    }

    public void downloadFile(ArrayList<FileDetails> FilesName)
            throws NotBoundException, RemoteException, MalformedURLException, IOException {
        // get port
        for(int i=0;i<FilesName.size();i++)
        {
            System.out.println("This file can be found in peerID " + FilesName.get(i).peerID);
        }
        String portForAnotherClient = FilesName.get(0).portNumber;
        String sourceDir = FilesName.get(0).SourceDirectoryName;
        FileSharingClient peerServer = (FileSharingClient) Naming.lookup("rmi://localhost:" + portForAnotherClient + "/FileServer");

        String source = sourceDir + "\\" + FilesName.get(0).FileName;
        // directory where file will be copied
        String target = directoryName;

        InputStream is = null;
        OutputStream os = null;
        try {
            File srcFile = new File(source);
            File destFile = new File(target);
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            is = new FileInputStream(srcFile);

            os = new FileOutputStream(target + "\\" + srcFile.getName());
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            System.out.println("Download successfully!.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }

    public static void main(String[] args) throws IOException {

        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
        String portno = null;
        System.out.print("Enter port number to be registered: ");
        portno = inp.readLine();
        try {
            LocateRegistry.createRegistry(Integer.parseInt(portno));
        } catch (Exception e) {
            System.err.println("FileServer exception: " + e.getMessage());
            e.printStackTrace();
        }
        new Client(portno).run();
    }
}