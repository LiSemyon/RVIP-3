import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {

    public static final int PORT = 6666;
    public static String mainUrl = "http://mentallandscape.com/";
    public static final String IP = "127.0.0.1";

    public static void main(String[] args) {

        try {
            InetAddress ipAddress = InetAddress.getByName(IP);
            ServerSocket ss = new ServerSocket(PORT, 0, ipAddress);
            System.out.println("Waiting for a client...");

            Socket socket = ss.accept();
            System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
            System.out.println();

            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line = null;
            while (true) {
                line = in.readUTF();
                mainUrl = line;
                new ConnectionHandler(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class ConnectionHandler implements Runnable {

    private DataOutputStream dataOutputStream;

    public ConnectionHandler(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            dataOutputStream.writeUTF("before");
        } catch (IOException e) {
            e.printStackTrace();
        }
        startParseNewPage(Main.mainUrl);
    }

    private void startParseNewPage(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            image_search(doc);
            links_search(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void image_search(Document document) {
        Elements img = document.getElementsByTag("img");
        for (Element el : img){
            try {
                dataOutputStream.writeUTF(Main.mainUrl + el.attr("src"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void links_search(Document document) throws InterruptedException {
        Elements href = document.getElementsByTag("a");
        for (Element el : href) {
            if (!el.attr("href").contains(".jpg")) {
                startParseNewPage(Main.mainUrl + el.attr("href"));
            }
        }
    }
}
