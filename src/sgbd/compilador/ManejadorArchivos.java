package sgbd.compilador;

import java.io.File;
import java.util.ArrayList;

public class ManejadorArchivos
{
    public static void crearBD(String bd)
    {
        Tabla t = Tabla.cargar("manager_dbs.pd");
        ArrayList<Object> data = new ArrayList<>();
        data.add(bd);
        t.addFila(data);
        Tabla.guardar(t, "manager_dbs.pd");
    }

    public static void dropBD(String bd)
    {
        Tabla dbs = Tabla.cargar("manager_dbs.pd");
        ArrayList<Object> bdr = new ArrayList<>();
        bdr.add(bd);
        dbs.getFilas().remove(bdr);
        Tabla.guardar(dbs, "manager_dbs.pd");
        
        Tabla tbs = Tabla.cargar("manager_tables.pd");
        Tabla aux = Tabla.seleccion(tbs, "nombreBD", bd);
        for(int i = 0; i < aux.getFilas().size(); i++)
        {
            String name = (String)aux.getFilas().get(i).get(2);
            File file = new File("pd_files/" + name);
            file.delete();
            tbs.getFilas().remove(aux.getFilas().get(i));
        }
        Tabla.guardar(tbs, "manager_dbs.pd");
    }
}
