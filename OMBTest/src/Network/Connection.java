package Network;

import Consume.Consumer;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable{
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;

    public Connection(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {


//            String text;
//            while (true) {
//                text = reader.readLine();
//                if(text.equals("exit"))
//                    break;
//                System.out.println("Received: " + text);
//                writer.println("Echo: " + text);
//                Consumer consumer = new Consumer("dummy1");
//            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
