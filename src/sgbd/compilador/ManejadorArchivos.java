package sgbd.compilador;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ManejadorArchivos
{
    public static void crearBD(String bd)
    {
        ObjectOutputStream archivo = null;
        
        try
        {
            archivo = new ObjectOutputStream(new FileOutputStream("pd_files/manager_dbs.pd", true));
            archivo.writeObject(bd);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
