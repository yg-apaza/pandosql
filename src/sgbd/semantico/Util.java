package sgbd.semantico;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util
{
    public static boolean existeBD(String bd)
    {
        ObjectInputStream archivo = null;
        try
        {
            String n;
            int i = 0;
            
            archivo = new ObjectInputStream(new FileInputStream("pd_files/manager_dbs.pd"));
            
            while(null != (n = (String) archivo.readObject()))
            {
                if(n.equals(bd))
                    return true;
            }
            
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    public static boolean existeTabla(String bd, String tabla)
    {
        return false;
    }
}
