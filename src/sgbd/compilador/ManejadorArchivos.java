package sgbd.compilador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ManejadorArchivos
{
    public static void createBD(String bd)
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
    
    public static void createTable(String bd, String tabla, ArrayList<Integer> tipos, ArrayList<String> cols)
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

    public static String showDatabases()
    {
        return Tabla.cargar("1.pd").toString();
    }
    
    public static void dropTable(String bd, String tabla)
    {
        ArrayList<Object> fil = Tabla.interseccion( Tabla.seleccion(Tabla.cargar("2.pd"), "nombreBD", bd),
                                Tabla.seleccion(Tabla.cargar("2.pd"), "nombreTB", tabla)).getFilas().get(0);
        Tabla tbs = Tabla.cargar("2.pd");
        
        String name = (String)fil.get(2);
        File file = new File("pd_files/" + name);
        file.delete();
        tbs.getFilas().remove(fil);

        Tabla.guardar(tbs, "2.pd");
    }
    
    public static void deleteTable(String bd, String tabla)
    {
        String file = getFileName(bd, tabla);
        Tabla aux = Tabla.cargar(file);
        aux.getFilas().clear();
        Tabla.guardar(aux, file);
    }

    public static String showTables(String actualBD)
    {
        ArrayList<String> col = new ArrayList<>();
        col.add("nombreTB");
        return Tabla.proyeccion(Tabla.seleccion(Tabla.cargar("2.pd"), "nombreBD", actualBD), col).toString();
    }

    public static void insertRegister(String bd, String tabla, ArrayList<Object> fil)
    {
        String file = getFileName(bd, tabla);
        Tabla aux = Tabla.cargar(file);
        aux.addFila(fil);
        Tabla.guardar(aux, file);
    }
    
    public static String getFileName(String bd, String tabla)
    {
        ArrayList<String> f = new ArrayList<>();
        f.add("archivo");
        return (String)Tabla.proyeccion(
                                        Tabla.interseccion( Tabla.seleccion(Tabla.cargar("2.pd"), "nombreBD", bd),
                                                            Tabla.seleccion(Tabla.cargar("2.pd"), "nombreTB", tabla)),
                                        f
        ).getFilas().get(0).get(0);
    }
    
    /**
     * 
     * @param bd
     * @param tabla
     * @param col1
     * @param d1
     * @param col2
     * @param d2
     * @param op 0 OR, 1 AND
     * @return 
     */
    public static Tabla ejecutarOperacion(String bd, String tabla, String col1, Object d1, String col2, Object d2, int op)
    {
        Tabla aux = Tabla.cargar(getFileName(bd, tabla));
        switch(op)
        {
            case 0:
                return Tabla.union(Tabla.seleccion(aux, col1, d1), Tabla.seleccion(aux, col2, d2));
                
            case 1:
                return Tabla.interseccion(Tabla.seleccion(aux, col1, d1), Tabla.seleccion(aux, col2, d2));
        }
        return null;
    }
    
    public static Tabla ejecutarOperacion(String bd, String tabla, String col1, Object d1)
    {
        Tabla aux = Tabla.cargar(getFileName(bd, tabla));
        return Tabla.seleccion(aux, col1, d1);
    }

    public static String selectAll(String bd, String tabla)
    {
        return Tabla.cargar(getFileName(bd, tabla)).toString();
    }

    static void deleteRegister(String bd, String tabla, String col, Object o1)
    {
        String file = getFileName(bd, tabla);
        Tabla aux = Tabla.cargar(file);
        ArrayList<ArrayList<Object>> filas = Tabla.seleccion(aux, col, o1).getFilas();
        
        for (ArrayList<Object> fila : filas)
            aux.getFilas().remove(fila);
        Tabla.guardar(aux, file);
    }
}