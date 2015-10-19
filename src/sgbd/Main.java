package sgbd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import sgbd.compilador.ManejadorArchivos;
import sgbd.semantico.Util;
import sgbd.compilador.Tabla;

public class Main
{
    private static final int PORT = 6789;

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
        
        //System.out.println(ManejadorArchivos.getNombreArchivoTabla());
        /*
        
        Tabla t = Tabla.cargar("4.pd");
        System.out.println(t.getFilas().get(0));
        */
        
        /**
         * Crear Tabla de Base de Datos
         */
        /*
        Tabla t = new Tabla();
        ArrayList<Object> fil = new ArrayList<>();
        fil.add("BD_PANDOSQL");
        t.addFila(fil);
        
        ArrayList<Integer> tipo = new ArrayList<>();
        tipo.add(2);
        t.setTipos(tipo);
        
        ArrayList<String> col = new ArrayList<>();
        col.add("nombreBD");
        t.setColumnas(col);
        
        Tabla.guardar(t, "1.pd");
               */
        /**
         * Crear tabla de Tablas
         */
        /*
        Tabla t = new Tabla();
        ArrayList<Object> fil = new ArrayList<>();
        fil.add("BD_PANDOSQL");
        fil.add("manager_dbs");
        fil.add("1.pd");
        t.addFila(fil);
        
        ArrayList<Object> fil2 = new ArrayList<>();
        fil2.add("BD_PANDOSQL");
        fil2.add("manager_tbs");
        fil2.add("2.pd");
        t.addFila(fil2);
        
        ArrayList<Integer> tipo = new ArrayList<>();
        tipo.add(2);
        tipo.add(2);
        tipo.add(2);
        t.setTipos(tipo);
        
        ArrayList<String> col = new ArrayList<>();
        col.add("nombreBD");
        col.add("nombreTB");
        col.add("archivo");
        t.setColumnas(col);
        
        Tabla.guardar(t, "2.pd");
        */
    }
}