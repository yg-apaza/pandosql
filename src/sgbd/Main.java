package sgbd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main
{
    static final int PORT = 6789;

    public static void main(String args[])
    {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try
        {
            serverSocket = new ServerSocket(PORT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        while (true)
        {
            try
            {
                socket = serverSocket.accept();
                System.out.println("Cliente PandoSQL conectado.");
            }
            catch (IOException e)
            {
                System.out.println("I/O error: " + e);
            }
            new Servidor(socket).start();
        }
    }
}