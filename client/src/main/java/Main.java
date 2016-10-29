import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    public static final int PORT = 6666;
    public static final String urlToStart = "http://mentallandscape.com/C_Catalog.htm";
    public static final String IP = "127.0.0.1";
    public static boolean superflag = false;

    public static void main(String[] args) {
        try {
            InetAddress ipAddress = InetAddress.getByName(IP);
            System.out.println("Any of you heard of a socket with IP address " + IP + " and port " + PORT + "?");
            Socket socket = new Socket(ipAddress, PORT);
            System.out.println("Yes! I just got hold of the program.");

            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = "http://mentallandscape.com/C_Catalog.htm";
            System.out.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
            System.out.println();

            while (true) {
                if (!superflag) {
                    line = keyboard.readLine();
                    System.out.println("Sending this line to the server...");
                    out.writeUTF(line);
                    out.flush();
                    superflag = true;
                }
                line = in.readUTF();
                System.out.println("The server was very polite. It sent me this : " + line);
                System.out.println("Looks like the server is pleased with us. Go ahead and enter more lines.");
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}