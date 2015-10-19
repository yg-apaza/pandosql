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
        /* Suponiendo que los tienen las mismas columnas, es decir el número de columnas y los mismos tipos */
       
        Tabla resultado = new Tabla();
        resultado.setColumnas(t1.columnas);
        resultado.setTipos(t1.tipos);
              
        t1.filas.stream().forEach((aux) -> {
            t2.filas.stream().filter((fila) -> (aux.equals(fila))).forEach((_item) -> {
                resultado.getFilas().add(aux);
            });
        });
        
        return resultado;
    }
    
    public static Tabla union(Tabla t1, Tabla t2)
    {
        Tabla resultado = new Tabla();
        resultado.setColumnas(t1.columnas);
        resultado.setTipos(t1.tipos);
              
        t1.filas.stream().forEach((fila) -> {
            resultado.filas.add(fila);
        });
        
        t2.filas.stream().filter((fila) -> (!resultado.filas.contains(fila))).forEach((fila) -> {
            resultado.filas.add(fila);
        });
        
        return resultado;
    }
    
    public static Tabla proyeccion(Tabla t, ArrayList<String> columnas)
    {
        Tabla resultado = new Tabla();
        resultado.setColumnas(columnas);
        
        ArrayList <Integer> indices = new ArrayList <>();
        
        columnas.stream().map((columna) -> t.columnas.indexOf(columna)).map((indice) -> {
            resultado.tipos.add(t.tipos.get(indice));
            return indice;
        }).forEach((indice) -> {
            indices.add(indice);
        });
        
        t.filas.stream().map((fila) -> {
            ArrayList <Object> nuevo = new ArrayList <>();
            indices.stream().forEach((indice) -> {
                nuevo.add(fila.get(indice));
            });
            return nuevo;
        }).forEach((nuevo) -> {
            resultado.filas.add(nuevo);
        });
        
        return resultado;
    }
    
    public static Tabla seleccion(Tabla t, String columna, Object data)
    {
        /*
            //Implementación Yuliana
            Tabla t2 = t;
            ArrayList<ArrayList<Object>> datos = new ArrayList<>();
            for(int i = 0; i < t.numFilas(); i++)
                if(t.getFilas().get(i).get(getIndiceColumna(t, columna)).equals(data))
                    datos.add(t.getFilas().get(i));
            t2.setFilas(datos);
            return t2; 
        */
        
        /* Implementación Kevin */
        
        Tabla resultado = new Tabla();
        resultado.setColumnas(t.columnas);
        resultado.setTipos(t.tipos);
        
        int indice = t.columnas.indexOf(columna);
                
        t.filas.stream().filter((fila) -> (fila.get(indice).equals(data))).forEach((fila) -> {
            resultado.filas.add(fila);
        });
                
        return resultado;
        
    }
    
    private static int getTipo(Tabla t, String columna)
    {
        return t.getTipos().get(t.getColumnas().indexOf(columna));
    }
    
    public static int getIndiceColumna(Tabla t, String columna)
    {
        return t.getColumnas().indexOf(columna);
    }
    
    @Override
    public String toString()
    {
        /*Bajo muchas condicones comprobadas antes*/
        
        String resultado = "";
        for(int i = 0;i < columnas.size();i++) {
            resultado += columnas.get(i) + " (" + tipos.get(i) + "),\t"; 
        }
        
        resultado += "\n";
        
        for (ArrayList<Object> aux : filas) {
            resultado = aux.stream().map((aux1) -> aux1 + "\t\t").reduce(resultado, String::concat);
            resultado += "\n";
        }

        return resultado;
    }
}
