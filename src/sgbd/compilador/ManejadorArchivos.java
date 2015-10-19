package sgbd.compilador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManejadorArchivos
{
    public static void crearBD(String bd)
    {
        Tabla t = Tabla.cargar("1.pd");
        ArrayList<Object> data = new ArrayList<>();
        data.add(bd);
        t.addFila(data);
        Tabla.guardar(t, "1.pd");
    }

    public static void dropBD(String bd)
    {
        Tabla tbs = Tabla.cargar("2.pd");
        Tabla aux = Tabla.seleccion(tbs, "nombreBD", bd);
        for(int i = 0; i < aux.getFilas().size(); i++)
        {
            String name = (String)aux.getFilas().get(i).get(2);
            File file = new File("pd_files/" + name);
            file.delete();
            tbs.getFilas().remove(aux.getFilas().get(i));
        }
        Tabla.guardar(tbs, "2.pd");
        
        Tabla dbs = Tabla.cargar("1.pd");
        ArrayList<Object> bdr = new ArrayList<>();
        bdr.add(bd);
        dbs.getFilas().remove(bdr);
        Tabla.guardar(dbs, "1.pd");
    }
    
    public static void crearTabla(String bd, String tabla, ArrayList<Integer> tipos, ArrayList<String> cols)
    {
        String archivo = getNombreArchivoTabla();
        Tabla tbs = Tabla.cargar("2.pd");
        ArrayList<Object> newFil = new ArrayList<>();
        newFil.add(bd);
        newFil.add(tabla);
        newFil.add(archivo);
        tbs.addFila(newFil);
        Tabla.guardar(tbs, "2.pd");
        
        Tabla newTabla = new Tabla();
        newTabla.setColumnas(cols);
        newTabla.setTipos(tipos);
        newTabla.setFilas(new ArrayList<>());
        try
        {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("pd_files/" + archivo, false));
            out.writeObject(newTabla);
            out.close();
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
    
    public static String getNombreArchivoTabla()
    {
        File folder = new File("pd_files/");
        File[] lista = folder.listFiles();
        
        int max = 2;
        String substring = "";
        
        for (File file : lista)
        {
            try
            {
                if (file.isFile())
                {
                    substring = file.getName().substring(0, file.getName().indexOf("."));
                    if(Integer.valueOf(substring) > max)
                        max = Integer.valueOf(substring);
                }
            }
            catch(NumberFormatException ex)
            {
                continue;
            }
        }
        
        return (max + 1) + ".pd";
    }
}