package Broker;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable{
    private InputStream input;
    private BufferedReader reader;
    private Socket socket;

    public Connection(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text;
            while ((text = reader.readLine()) != null) {
                System.out.println("Received: " + text);
                writer.println("Echo: " + text);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
