package sgbd.semantico;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Util
{
    public static boolean existeBD(String bd)
    {
        ObjectInputStream archivo = null;
        try
        {
            String n;
            archivo = new ObjectInputStream(new FileInputStream("pd_files/manager_dbs.pd"));
            while(null != (n = (String) archivo.readObject()))
                if(n.equals(bd))
                    return true;
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
    
    public static boolean existeCampo(String bd, String tabla, String campo)
    {
        return false;
    }
}
