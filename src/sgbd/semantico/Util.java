package sgbd.semantico;

import java.util.ArrayList;
import sgbd.compilador.Tabla;

public class Util
{
    public static boolean existeBD(String bd)
    {
        return Tabla.seleccion(Tabla.cargar("manager_dbs.pd"), "nombreBD", bd).numFilas() != 0;
    }
    
    /**
     * Se asume que la BD existe
     * @param bd
     * @param tabla
     * @return 
     */
    public static boolean existeTabla(String bd, String tabla)
    {
        return Tabla.interseccion(  Tabla.seleccion(Tabla.cargar("manager_tables.pd"), "nombreBD", bd),
                                    Tabla.seleccion(Tabla.cargar("manager_tables.pd"), "nombreTB", tabla)).numFilas() != 0;
    }
    
    /**
     * Se asume que la tabla y la BD existen
     * @param bd
     * @param tabla
     * @param campo
     * @return 
     */
    public static boolean existeCampo(String bd, String tabla, String campo)
    {
        ArrayList<String> f = new ArrayList<>();
        f.add("archivo");
        String tabla_file = (String)Tabla.proyeccion(
                                        Tabla.interseccion( Tabla.seleccion(Tabla.cargar("manager_tables.pd"), "nombreBD", bd),
                                                            Tabla.seleccion(Tabla.cargar("manager_tables.pd"), "nombreTB", tabla)),
                                        f
        ).getFilas().get(0).get(0);
        return Tabla.cargar(tabla_file).getColumnas().indexOf(campo) >= 0;
    }
    
    /**
     * Se asume que la tabla existe
     * @param bd
     * @param tabla
     * @param tipos
     * @return 
     */
    public static boolean comprobarTodosArgumentos(String bd, String tabla, ArrayList<Integer> tipos)
    {
        ArrayList<String> f = new ArrayList<>();
        f.add("archivo");
        String tabla_file = (String)Tabla.proyeccion(
                                        Tabla.interseccion( Tabla.seleccion(Tabla.cargar("manager_tables.pd"), "nombreBD", bd),
                                                            Tabla.seleccion(Tabla.cargar("manager_tables.pd"), "nombreTB", tabla)),
                                        f
        ).getFilas().get(0).get(0);
        
        return Tabla.cargar(tabla_file).getTipos().equals(tipos);
    }

    static boolean comprobarArgumento(String bd, String tabla, String campo, int t)
    {
        ArrayList<String> f = new ArrayList<>();
        f.add("archivo");
        String tabla_file = (String)Tabla.proyeccion(
                                        Tabla.interseccion( Tabla.seleccion(Tabla.cargar("manager_tables.pd"), "nombreBD", bd),
                                                            Tabla.seleccion(Tabla.cargar("manager_tables.pd"), "nombreTB", tabla)),
                                        f
        ).getFilas().get(0).get(0);
        
        Tabla aux = Tabla.cargar(tabla_file);
        int ind = Tabla.getIndiceColumna(aux, campo);
        return ind >= 0?aux.getTipos().get(ind) == t:false;
    }
}