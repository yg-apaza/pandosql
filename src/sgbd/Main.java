package sgbd;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Servidor s = new Servidor();
        s.lanzar();
        /*
        FileOutputStream fos = null;
        DataOutputStream archivo = null;
        
        fos = new FileOutputStream("pd_files/manager_dbs.pd", true);
        archivo = new DataOutputStream(fos);
        
        archivo.writeChars("BD_PONY");
        
        archivo.close();
        */
        /*
        if(args.length == 0)
            System.out.println("Debe ingresar un archivo");
        else
        {
            if(args.length == 1)
                Compilar(args[0]);
            else
            {
                System.out.println("Archivo: " + args[0] + "\n");
                switch(Integer.parseInt(args[1]))
                {
                    case 0:
                        ALexico(args[0]);
                        break;
                    case 1:
                        ASintactico(args[0]);
                        break;
                    case 2:
                        ASemantico(args[0]);
                        break;
                    case 3:
                        Compilar(args[0]);
                        break;
                }
            }
        }
        */
    }
}