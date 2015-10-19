package sgbd.compilador;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import sgbd.lexico.sym;
import sgbd.semantico.AST;
import sgbd.semantico.Nodo;
import sgbd.semantico.accion;

public class Compilador
{
    private AST arbol;
    private String message;
    
    public Compilador(AST arbol)
    {
        this.arbol = arbol;
        message = "";
    }

    public String getMessage()
    {
        return message;
    }

    public void compilar(AtomicReference<String> actualBD)
    {
        compilar(arbol.getRaiz(), actualBD);
    }
    
    private void compilar(Nodo nodo, AtomicReference<String> actualBD)
    {
        if(!nodo.esTerminal())
        {
            Object o1 = null;
            Nodo n1 = null;
            ArrayList<String> col = null;
            switch(nodo.getCodigo())
            {
                case accion.USE_DATABASE:
                    actualBD.set(nodo.getHijos().get(0).getValor());
                break;
                    
                case accion.CREATE_DATABASE:
                    ManejadorArchivos.createBD(nodo.getHijos().get(0).getValor());
                break;
                    
                case accion.DROP_DATABASE:
                    ManejadorArchivos.dropBD(nodo.getHijos().get(0).getValor());
                    if(actualBD.get().equals(nodo.getHijos().get(0).getValor()))
                        actualBD.set(null);
                break;
                
                case accion.CREATE_TABLE:
                    ArrayList<String> cols = new ArrayList<>();
                    ArrayList<Integer> tipos = new ArrayList<>();
                    
                    ArrayList<Nodo> parametros = nodo.getHijos().get(1).getHijos();
                    
                    for(Nodo n: parametros)
                    {
                        switch(n.getHijos().get(0).getCodigo())
                        {
                            case sym.integer:
                                tipos.add(0);
                            break;
                            
                            case sym.real:
                                tipos.add(1);
                            break;
                                
                            case sym.varchar:
                                tipos.add(2);
                            break;
                                
                            case sym.bool:
                                tipos.add(3);
                            break;
                        }
                        cols.add(n.getHijos().get(1).getValor());
                    }
                    ManejadorArchivos.createTable(actualBD.get(), nodo.getHijos().get(0).getValor(), tipos, cols);
                break;
                    
                case accion.SHOW_DATABASES:
                    message += ManejadorArchivos.showDatabases();
                break;
                    
                case accion.DROP_TABLE:
                    ManejadorArchivos.dropTable(actualBD.get(), nodo.getHijos().get(0).getValor());
                break;
                    
                case accion.DELETE_TABLE:
                    ManejadorArchivos.deleteTable(actualBD.get(), nodo.getHijos().get(0).getValor());
                break;
                    
                case accion.SHOW_TABLES:
                    message += ManejadorArchivos.showTables(actualBD.get());
                break;
                    
                case accion.INSERT_REGISTER:
                    ArrayList<Object> fil = new ArrayList<>();
                    ArrayList<Nodo> args = nodo.getHijos().get(1).getHijos();
                    for (Nodo arg : args)
                    {
                        switch (arg.getCodigo())
                        {
                            case sym.numero:
                                fil.add(Integer.valueOf(arg.getValor()));
                                break;
                            case sym.numreal:
                                fil.add(Double.valueOf(arg.getValor()));
                                break;
                            case sym.cadena:
                                fil.add(arg.getValor().substring(1, arg.getValor().length() - 1));
                                break;
                            case sym.tr:
                            case sym.fa:
                                fil.add(Boolean.valueOf(arg.getValor()));
                            break;
                        }
                    }
                    ManejadorArchivos.insertRegister(actualBD.get(), nodo.getHijos().get(0).getValor(), fil);
                break;
                    
                case accion.DELETE_REGISTER:
                    n1 = nodo.getHijos().get(2);
                    switch(n1.getCodigo())
                    {
                        case sym.numero:
                            o1 = Integer.valueOf(n1.getValor());
                        break;
                        case sym.numreal:
                            o1 = Double.valueOf(n1.getValor());
                        break;
                        case sym.cadena:
                            o1 = n1.getValor().substring(1, n1.getValor().length() - 1);
                        break;
                        case sym.tr:
                        case sym.fa:
                            o1 = Boolean.valueOf(n1.getValor());
                        break;
                    }
                    ManejadorArchivos.deleteRegister(   actualBD.get(), nodo.getHijos().get(0).getValor(),
                                                        nodo.getHijos().get(1).getValor(),
                                                        o1
                                                    );
                break;
                
                case accion.UPDATE_REGISTER:
                    n1 = nodo.getHijos().get(2);
                    switch(n1.getCodigo())
                    {
                        case sym.numero:
                            o1 = Integer.valueOf(n1.getValor());
                        break;
                        case sym.numreal:
                            o1 = Double.valueOf(n1.getValor());
                        break;
                        case sym.cadena:
                            o1 = n1.getValor().substring(1, n1.getValor().length() - 1);
                        break;
                        case sym.tr:
                        case sym.fa:
                            o1 = Boolean.valueOf(n1.getValor());
                        break;
                    }
                    
                    if(nodo.getHijos().get(3).getHijos().isEmpty())
                        ManejadorArchivos.updateRegister(actualBD.get(), nodo.getHijos().get(0).getValor(),
                                                    nodo.getHijos().get(1).getValor(), o1);
                    else if(nodo.getHijos().get(3).getHijos().size() == 1)
                    {
                        Nodo nc = nodo.getHijos().get(3).getHijos().get(0).getHijos().get(1);
                        Object oc = null;
                        switch(nc.getCodigo())
                        {
                            case sym.numero:
                                oc = Integer.valueOf(nc.getValor());
                            break;
                            case sym.numreal:
                                oc = Double.valueOf(nc.getValor());
                            break;
                            case sym.cadena:
                                oc = nc.getValor().substring(1, nc.getValor().length() - 1);
                            break;
                            case sym.tr:
                            case sym.fa:
                                oc = Boolean.valueOf(nc.getValor());
                            break;
                        }
                        ManejadorArchivos.updateRegister(   actualBD.get(), nodo.getHijos().get(0).getValor(),
                                                            nodo.getHijos().get(1).getValor(), o1,
                                                            ManejadorArchivos.ejecutarOperacion(actualBD.get(), nodo.getHijos().get(0).getValor(), nodo.getHijos().get(3).getHijos().get(0).getHijos().get(0).getValor(), oc)
                                                        );
                    }
                    else
                    {
                        ManejadorArchivos.updateRegister(   actualBD.get(), nodo.getHijos().get(0).getValor(),
                                                            nodo.getHijos().get(1).getValor(), o1,
                                                            resolver(actualBD.get(), nodo.getHijos().get(0).getValor(), nodo.getHijos().get(3).getHijos().get(0))
                                                        );
                    }
                break;
                    
                case accion.SELECT:
                    col = new ArrayList<>();
                    for(int i = 0; i < nodo.getHijos().get(1).getHijos().size(); i++)
                        col.add(nodo.getHijos().get(1).getHijos().get(i).getValor());
                    
                    if(nodo.getHijos().get(2).getHijos().isEmpty())
                        message += Tabla.proyeccion(ManejadorArchivos.selectAll(actualBD.get(), nodo.getHijos().get(0).getValor()), col);
                    else if(nodo.getHijos().get(2).getHijos().size() == 1)
                    {
                        n1 = nodo.getHijos().get(2).getHijos().get(0).getHijos().get(1);
                        switch(n1.getCodigo())
                        {
                            case sym.numero:
                                o1 = Integer.valueOf(n1.getValor());
                            break;
                            case sym.numreal:
                                o1 = Double.valueOf(n1.getValor());
                            break;
                            case sym.cadena:
                                o1 = n1.getValor().substring(1, n1.getValor().length() - 1);
                            break;
                            case sym.tr:
                            case sym.fa:
                                o1 = Boolean.valueOf(n1.getValor());
                            break;
                        }
                        message += Tabla.proyeccion(ManejadorArchivos.ejecutarOperacion(actualBD.get(), nodo.getHijos().get(0).getValor(), nodo.getHijos().get(2).getHijos().get(0).getHijos().get(0).getValor(), o1), col);
                    }
                    else
                        message += Tabla.proyeccion(resolver(actualBD.get(), nodo.getHijos().get(0).getValor(), nodo.getHijos().get(2).getHijos().get(0)), col);
                break;
                    
                case accion.SELECT_ALL:
                    if(nodo.getHijos().get(1).getHijos().isEmpty())
                        message += ManejadorArchivos.selectAll(actualBD.get(), nodo.getHijos().get(0).getValor());
                    else if(nodo.getHijos().get(1).getHijos().size() == 1)
                    {
                        
                        n1 = nodo.getHijos().get(1).getHijos().get(0).getHijos().get(1);
                        switch(n1.getCodigo())
                        {
                            case sym.numero:
                                o1 = Integer.valueOf(n1.getValor());
                            break;
                            case sym.numreal:
                                o1 = Double.valueOf(n1.getValor());
                            break;
                            case sym.cadena:
                                o1 = n1.getValor().substring(1, n1.getValor().length() - 1);
                            break;
                            case sym.tr:
                            case sym.fa:
                                o1 = Boolean.valueOf(n1.getValor());
                            break;
                        }
                        message += ManejadorArchivos.ejecutarOperacion(actualBD.get(), nodo.getHijos().get(0).getValor(), nodo.getHijos().get(1).getHijos().get(0).getHijos().get(0).getValor(), o1);
                    }
                    else
                        message += resolver(actualBD.get(), nodo.getHijos().get(0).getValor(), nodo.getHijos().get(1).getHijos().get(0));
                break;
            }
            for(int i = 0; i < nodo.getHijos().size(); i++)
                compilar(nodo.getHijos().get(i), actualBD);
        }
    }
    /**
     * 
     * @param tabla
     * @param campo
     * @param data
     * @param op 0 OR, 1 AND
     * @return 
     */
    private Tabla resolver(String bd, String tabla, Nodo n)
    {
        if((!n.getHijos().get(0).esTerminal() && n.getHijos().get(0).getCodigo() == accion.CONDITION) &&
           (!n.getHijos().get(1).esTerminal() && n.getHijos().get(1).getCodigo() == accion.CONDITION))
        {
            Object o1 = null;
            Nodo n1 = n.getHijos().get(0).getHijos().get(1);
            switch(n1.getCodigo())
            {
                case sym.numero:
                    o1 = Integer.valueOf(n1.getValor());
                break;
                case sym.numreal:
                    o1 = Double.valueOf(n1.getValor());
                break;
                case sym.cadena:
                    o1 = n1.getValor().substring(1, n1.getValor().length() - 1);
                break;
                case sym.tr:
                case sym.fa:
                    o1 = Boolean.valueOf(n1.getValor());
                break;
            }
            
            Object o2 = null;
            Nodo n2 = n.getHijos().get(1).getHijos().get(1);
            switch(n2.getCodigo())
            {
                case sym.numero:
                    o2 = Integer.valueOf(n2.getValor());
                break;
                case sym.numreal:
                    o2 = Double.valueOf(n2.getValor());
                break;
                case sym.cadena:
                    o2 = n2.getValor().substring(1, n2.getValor().length() - 1);
                break;
                case sym.tr:
                case sym.fa:
                    o2 = Boolean.valueOf(n2.getValor());
                break;
            }
            
            return ManejadorArchivos.ejecutarOperacion(bd, tabla,   n.getHijos().get(0).getHijos().get(0).getValor(), o1,
                                                                    n.getHijos().get(1).getHijos().get(0).getValor(), o2, n.getCodigo() == accion.AND?1:0);
        }
        else if(!n.getHijos().get(0).esTerminal() && n.getHijos().get(0).getCodigo() == accion.CONDITION)
        {
            Object o1 = null;
            Nodo n1 = n.getHijos().get(0).getHijos().get(1);
            switch(n1.getCodigo())
            {
                case sym.numero:
                    o1 = Integer.valueOf(n1.getValor());
                break;
                case sym.numreal:
                    o1 = Double.valueOf(n1.getValor());
                break;
                case sym.cadena:
                    o1 = n1.getValor().substring(1, n1.getValor().length() - 1);
                break;
                case sym.tr:
                case sym.fa:
                    o1 = Boolean.valueOf(n1.getValor());
                break;
            }
            
            switch(n.getCodigo())
            {
                case accion.AND:
                    return Tabla.interseccion
                            (
                                ManejadorArchivos.ejecutarOperacion(bd, tabla, n.getHijos().get(0).getHijos().get(0).getValor(), o1),
                                resolver(bd, tabla, n.getHijos().get(1))
                            );
                    
                case accion.OR:
                    return Tabla.union
                            (
                                ManejadorArchivos.ejecutarOperacion(bd, tabla, n.getHijos().get(0).getHijos().get(0).getValor(), o1),
                                resolver(bd, tabla, n.getHijos().get(1))
                            );
            }
        }
        else if(!n.getHijos().get(1).esTerminal() && n.getHijos().get(1).getCodigo() == accion.CONDITION)
        {
            Object o2 = null;
            Nodo n2 = n.getHijos().get(1).getHijos().get(1);
            switch(n2.getCodigo())
            {
                case sym.numero:
                    o2 = Integer.valueOf(n2.getValor());
                break;
                case sym.numreal:
                    o2 = Double.valueOf(n2.getValor());
                break;
                case sym.cadena:
                    o2 = n2.getValor().substring(1, n2.getValor().length() - 1);
                break;
                case sym.tr:
                case sym.fa:
                    o2 = Boolean.valueOf(n2.getValor());
                break;
            }
            
            switch(n.getCodigo())
            {
                case accion.AND:
                    return Tabla.interseccion
                            (
                                ManejadorArchivos.ejecutarOperacion(bd, tabla, n.getHijos().get(1).getHijos().get(0).getValor(), o2),
                                resolver(bd, tabla, n.getHijos().get(0))
                            );
                    
                case accion.OR:
                    return Tabla.union
                            (
                                ManejadorArchivos.ejecutarOperacion(bd, tabla, n.getHijos().get(1).getHijos().get(0).getValor(), o2),
                                resolver(bd, tabla, n.getHijos().get(0))
                            );
            }
        }
        else
        {
            switch(n.getCodigo())
            {
                case accion.AND:
                    return Tabla.interseccion
                            (
                                    resolver(bd, tabla, n.getHijos().get(0)),
                                    resolver(bd, tabla, n.getHijos().get(1))
                            );
                    
                case accion.OR:
                    return Tabla.union
                            (
                                resolver(bd, tabla, n.getHijos().get(0)),
                                resolver(bd, tabla, n.getHijos().get(1))
                            );
            }
        }
        return null;
    }
}
