package sgbd.semantico;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Util
{
    public static boolean existeBD(String bd)
    {
        FileInputStream fich = null;
        DataInputStream entrada = null;
        try
        {
            String bdSearch = null;
            String n;
            int i = 0;
            
            fich = new FileInputStream("pd_files/manager_dbs.pd");
            entrada = new DataInputStream(fich);
            
            while(entrada.read()!=-1)
            {
                n = entrada.readLine();
                  
                if(n.equals(bd))
                    return true;
            }
            
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
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
