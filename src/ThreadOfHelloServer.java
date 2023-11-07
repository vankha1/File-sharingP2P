
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/* 
Server class bao gồm:
- RMI registry: Bộ đăng kí này sẽ đăng kí một Remote object với Naming Registry. Giúp các Remote object được chấp nhận khi gọi các method từ xa.
- Các class thực thi trên server.
 
*/

// The main method for the server is defined in the class ThreadOfHelloServer. The server's main method does the following: Create and export a remote object

// The static method LocateRegistry.getRegistry that takes no arguments returns a stub that implements the remote interface java.rmi.registry.Registry and sends invocations to the registry on server's local host on the default registry port of 1099. The bind method is then invoked on the registry stub in order to bind the remote object's stub to the name "Hello" in the registry.
public class ThreadOfHelloServer implements Runnable {
   public void run() {
      try {
         LocateRegistry.createRegistry(1099);
         HelloInterface hello = new Hello();
         Naming.rebind("Hello", hello);

         System.out.println("Hello Server is ready.");
         while(true){
            hello.getClientFiles();
         }
      } catch (Exception e) {
         System.out.println("Hello Server failed: " + e);
      }
   }

   public static void main(String[] args) {
      new ThreadOfHelloServer().run();
   }
}
