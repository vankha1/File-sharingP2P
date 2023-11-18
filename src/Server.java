
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
   public void run() {
      try {
         LocateRegistry.createRegistry(1099);
         FileSharingInterface hello = new FileSharing();
         Naming.rebind("Hello", hello);

         System.out.println("Hello Server is ready.");

         InetAddress ip = InetAddress.getLocalHost();
         System.out.println("The IP address of server : " + ip.getHostAddress());

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
               System.out.println("ping <hostname>: ");
               System.out.println("live check a client");
               System.out.println("discover <hostname> <port>: ");
               System.out.println("get all filenames from client's repo");
               System.out.println("list:");
               System.out.println("list all online clients");
            } else {
               System.out.println("Invalid input! Please try again!");
            }

            inputLine = br.readLine();
            if (inputLine.equals("exit")) {
               Naming.unbind("Hello");
               System.exit(0);
            }
         }
      } catch (Exception e) {
         System.out.println("Hello Server failed: " + e);
      }
   }

   public static void main(String[] args) {
      new Server().run();
   }
}
