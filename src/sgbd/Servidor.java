package sgbd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor
{
    private ServerSocket servidorSocket;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public Servidor()
    {
        try
        {
            System.out.println("Esperando un cliente PandoSQL ...");
            servidorSocket = new ServerSocket(6789);
            socket = servidorSocket.accept();
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Cliente listo ...");
        }
        catch (IOException ex)
        {
            System.out.println("Error de conexión. Reinicie el servidor.");
        }
    }
}
