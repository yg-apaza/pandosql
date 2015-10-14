package sgbd.compilador;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tabla implements Serializable
{
    /**
     * Tipo
     * 0: INTEGER
     * 1: DOUBLE
     * 2: VARCHAR
     * 3: BOOLEAN
     */
    private ArrayList<Integer> tipos;
    private ArrayList<String> columnas;
    private ArrayList<ArrayList<Object>> filas;
    
    public Tabla()
    {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
    
    public Tabla(ArrayList<Integer> tipos, ArrayList<String> columnas, ArrayList<ArrayList<Object>> filas)
    {
        this.tipos = tipos;
        this.columnas = columnas;
        this.filas = filas;
    }

    public ArrayList<Integer> getTipos()
    {
        return tipos;
    }

    public void setTipos(ArrayList<Integer> tipos)
    {
        this.tipos = tipos;
    }

    public ArrayList<String> getColumnas()
    {
        return columnas;
    }

    public void setColumnas(ArrayList<String> columnas)
    {
        this.columnas = columnas;
    }

    public ArrayList<ArrayList<Object>> getFilas()
    {
        return filas;
    }

    public void setFilas(ArrayList<ArrayList<Object>> filas)
    {
        this.filas = filas;
    }
    
    public int numColumnas()
    {
        return columnas.size();
    }
    
    public int numFilas()
    {
        return filas.size();
    }
    
    public static void guardar(Tabla t, String file)
    {
        ObjectOutputStream archivo = null;
        try
        {
            archivo = new ObjectOutputStream(new FileOutputStream("pd_files/" + file, false));
            archivo.writeObject(t);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public static Tabla cargar(String bd, String nombre, String file)
    {
        Tabla t = new Tabla();
        ObjectInputStream archivo = null;
        
        try
        {
            archivo = new ObjectInputStream(new FileInputStream("pd_files/" + file));
            t = (Tabla) archivo.readObject();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        
        return t;
    }
    
    public static Tabla interseccion(Tabla t1, Tabla t2)
    {
        return null;
    }
    
    public static Tabla union(Tabla t1, Tabla t2)
    {
        return null;
    }
    
    public static Tabla proyeccion(ArrayList<String> columnas)
    {
        return null;
    }
    
    public static Tabla seleccion(String columna, Object data)
    {
        return null;
    }
    
    private static int getTipo(Tabla t, String columna)
    {
        return t.getTipos().get(t.getColumnas().indexOf(columna));
    }
    
    @Override
    public String toString()
    {
        return "";
    }
}
