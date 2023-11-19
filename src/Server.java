
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/* 
Server class bao gồm:
- RMI registry: Bộ đăng kí này sẽ đăng ``````````````````` kí một Remote object với Naming Registry. Giúp các Remote object được chấp nhận khi gọi các method từ xa.
- Các class thực thi trên server.
 
*/

// The main method for the server is defined in the class ThreadOfHelloServer. The server's main method does the following: Create and export a remote object

// The static method LocateRegistry.getRegistry that takes no arguments returns a stub that implements the remote interface java.rmi.registry.Registry and sends invocations to the registry on server's local host on the default registry port of 1099. The bind method is then invoked on the registry stub in order to bind the remote object's stub to the name "Hello" in the registry.
public class Server implements Runnable {

   
   public static final String ANSI_RESET = "\u001B[0m";
   public static final String ANSI_BLACK = "\u001B[30m";
   public static final String ANSI_RED = "\u001B[31m";
   public static final String ANSI_YELLOW = "\u001B[33m";
   public static final String ANSI_BLUE = "\u001B[34m";

   public void run() {
      try {
         LocateRegistry.createRegistry(1099);
         FileSharingInterface hello = new FileSharing();
         Naming.rebind("Hello", hello);

         System.out.println(ANSI_BLUE + "Hello Server is ready." + ANSI_RESET);

         InetAddress ip = InetAddress.getLocalHost();
         System.out.println(ANSI_BLUE + "The IP address of server : " + ip.getHostAddress() + ANSI_RESET);

         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         String inputLine = br.readLine();

         if (inputLine.equals("exit")) {
            Naming.unbind("Hello");
            System.exit(0);
         }

         while (true) {
            String[] inputArr = inputLine.split(" \"");
            if (inputArr[0].equals("discover") && inputArr.length == 2) {
               String peerID = inputArr[1].replace("\"", "");
               hello.discoverClient(peerID);
            } else if (inputArr[0].equals("ping") && inputArr.length == 2) {
               String peerID = inputArr[1].replace("\"", "");
               hello.pingClient(peerID);
            } else if (inputArr[0].equals("help") && inputArr.length == 1) {
               System.out.println(ANSI_YELLOW + "ping <hostname>: " + ANSI_RESET);
               System.out.println(ANSI_YELLOW + "live check a client" + ANSI_RESET);
               System.out.println(ANSI_YELLOW + "discover <hostname> <port>: " + ANSI_RESET);
               System.out.println(ANSI_YELLOW + "get all filenames from client's repo" + ANSI_RESET);
               System.out.println(ANSI_YELLOW + "list:" + ANSI_RESET);
               System.out.println(ANSI_YELLOW + "list all online clients" + ANSI_RESET);
            } else {
               System.out.println(ANSI_YELLOW + "Invalid input! Please try again!" + ANSI_RESET);
            }
            inputLine = br.readLine();
            if (inputLine.equals("exit")) {
               Naming.unbind("Hello");
               System.exit(0);
            }
         }
      } catch (Exception e) {
         System.out.println(ANSI_RED + "Hello Server failed: " + e + ANSI_RESET);
      }
   }

   public static void main(String[] args) {
      new Server().run();
   }
}
