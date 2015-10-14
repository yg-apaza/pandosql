package sgbd.compilador;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

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
    private static final long serialVersionUID = 8799656478674716638L;
    
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
    
    public void addFila(ArrayList<Object> fila)
    {
        filas.add(fila);
    }
    
    public static void guardar(Tabla t, String file)
    {
        ObjectOutputStream archivo = null;
        try
        {
            archivo = new ObjectOutputStream(new FileOutputStream("pd_files/" + file, false));
            archivo.writeObject(t);
            archivo.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public static Tabla cargar(String file)
    {
        Tabla t = null;
        ObjectInputStream archivo = null;
        
        try
        {
            archivo = new ObjectInputStream(new FileInputStream("pd_files/" + file));
            t = (Tabla) archivo.readObject();
            archivo.close();
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
    
    public static Tabla proyeccion(Tabla t, ArrayList<String> columnas)
    {
        return null;
    }
    
    public static Tabla seleccion(Tabla t, String columna, Object data)
    {
        Tabla t2 = t;
        ArrayList<ArrayList<Object>> datos = new ArrayList<>();
        for(int i = 0; i < t.numFilas(); i++)
            if(t.getFilas().get(i).get(getIndiceColumna(t, columna)).equals(data))
                datos.add(t.getFilas().get(i));
        t2.setFilas(datos);
        return t2;
    }
    
    private static int getTipo(Tabla t, String columna)
    {
        return t.getTipos().get(t.getColumnas().indexOf(columna));
    }
    
    private static int getIndiceColumna(Tabla t, String columna)
    {
        return t.getColumnas().indexOf(columna);
    }
    
    @Override
    public String toString()
    {
        return "";
    }
}
