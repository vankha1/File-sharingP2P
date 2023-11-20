
// import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.*;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.net.InetAddress;

public class Client implements Runnable {
    String portno = null;
    String directoryName = null;
    String fileTobeSearched = null;
    String peerID = null;
    boolean isAlive = false;
    BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    Client(String portno) {
        this.portno = portno;
    }

    public void run() {

        isAlive = true;
        try {
            // Looking up the registry for the remote object
            FileSharingInterface hello = (FileSharingInterface) Naming.lookup("rmi://172.29.16.1/Hello");

            // input peerId
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            InetAddress ip = InetAddress.getLocalHost();
            System.out.println(ANSI_YELLOW + "My IP address : " + ip.getHostAddress() + ANSI_RESET);

            System.out.print(ANSI_BLUE + "Enter the peer ID: " + ANSI_RESET);
            peerID = br.readLine();

            hello.addClient(ip.getHostAddress());

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
                        fetchFile(fileTobeSearched);
                    } else if (inputArr[0].equals("rename") && inputArr.length == 3) {
                        String fileTobeRenamed = inputArr[1].replace("\"", "");
                        String fileNewName = inputArr[2].replace("\"", "");
                        renameFileLocal(fileTobeRenamed, fileNewName);
                    } else if (inputArr[0].equals("delete") && inputArr.length == 2) {
                        String fileTobeRenamed = inputArr[1].replace("\"", "");
                        deleteFileLocal(fileTobeRenamed);
                    } else {
                        System.out.println(ANSI_RED + "Invalid input! Please try again!" + ANSI_RESET);
                    }
                }

                inputLine = br.readLine();
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "HelloClient exception: " + e + ANSI_RESET);
        }
    }

    public void publishFile(String directoryName, String fileName) throws IOException {
        this.directoryName = directoryName;
        try {
            FileSharingInterface hello = (FileSharingInterface) Naming.lookup("rmi://172.29.16.1/Hello");
            try {
                File directoryList = new File(directoryName);
                int counter = 0;
                String[] store = directoryList.list();
                boolean isAlive = false;
                while (counter < store.length) {
                    File currentFile = new File(store[counter]);
                    if (fileName.equalsIgnoreCase(currentFile.getName())) {
                        isAlive = true;
                        break;
                    }
                    counter++;
                }
                if (isAlive) {
                    hello.registerFiles(peerID, fileName, portno, directoryName);
                } else {
                    System.out.println("Can not find file to publish!.");
                }
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
            System.err.println(ANSI_RED + "FileServer exception: " + e.getMessage() + ANSI_RESET);
            e.printStackTrace();
        }
    }

    public void fetchFile(String fileTobeSearched) {
        System.out.println(fileTobeSearched);
        File directoryList = new File(this.directoryName);
        int counter = 0;
        String[] store = directoryList.list();
        boolean isAlive = false;
        while (counter < store.length) {
            File currentFile = new File(store[counter]);
            if (fileTobeSearched.equalsIgnoreCase(currentFile.getName())) {
                isAlive = true;
                break;
            }
            counter++;
        }
        if (isAlive) {
            System.out.println("This file already exists in the directory!");
        }
        else {
            try {
                FileSharingInterface hello = (FileSharingInterface) Naming.lookup("rmi://172.29.16.1/Hello");
    
                ArrayList<FileDetails> FilesName = hello.searchFile(fileTobeSearched);
    
                downloadFile(FilesName);
            } catch (Exception e) {
                System.out.println(ANSI_RED + "HelloClient exception: " + e + ANSI_RESET);
            }
        }
    }

    public void renameFileLocal(String FileName, String newFileName) throws IOException {
        try {
            FileSharingInterface hello = (FileSharingInterface) Naming.lookup("rmi://172.29.16.1/Hello");

            String sourceFile = this.directoryName + "\\" + FileName;
            String sourceNewFile = this.directoryName + "\\" + newFileName;
            File file = new File(sourceFile);
            File newFile = new File(sourceNewFile);

            boolean flag = file.renameTo(newFile);

            hello.renameFile(FileName, newFileName);

            if (flag) {
                System.out.println(ANSI_BLUE + "File Successfully Rename" + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println("Operation Failed");
        }
    }

    public void deleteFileLocal(String fileName) throws IOException, NoSuchFileException {
        File file = new File("C:\\Users\\Admin\\Downloads\\test\\test.txt");
        if (file.delete()) {
            System.out.println(ANSI_BLUE + "File deleted successfully" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Failed to delete the file" + ANSI_RESET);
        }
    }

    public void downloadFile(ArrayList<FileDetails> FilesName)
            throws NotBoundException, RemoteException, MalformedURLException, IOException {
        // get port
        for (int i = 0; i < FilesName.size(); i++) {
            System.out.println(ANSI_BLUE + "This file can be found in peerID " + FilesName.get(i).peerID + ANSI_RESET);
        }
        String portForAnotherClient = FilesName.get(0).portNumber;
        String sourceDir = FilesName.get(0).SourceDirectoryName;
        FileSharingClient peerServer = (FileSharingClient) Naming
                .lookup("rmi://localhost:" + portForAnotherClient + "/FileServer");

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
            System.out.println(ANSI_BLUE + "Download successfully!." + ANSI_RESET);
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
        System.out.print(ANSI_YELLOW + "Enter port number to be registered: " + ANSI_RESET);
        portno = inp.readLine();
        try {
            LocateRegistry.createRegistry(Integer.parseInt(portno));
        } catch (Exception e) {
            System.err.println(ANSI_RED + "FileServer exception: " + e.getMessage() + ANSI_RESET);
            e.printStackTrace();
        }
        new Client(portno).run();
    }
}
