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
    
    public Compilador(AST arbol)
    {
        this.arbol = arbol;
    }
    
    public void compilar(AtomicReference<String> actualBD, ObjectOutputStream out)
    {
        compilar(arbol.getRaiz(), actualBD, out);
    }
    
    private void compilar(Nodo nodo, AtomicReference<String> actualBD, ObjectOutputStream out)
    {
        if(!nodo.esTerminal())
        {
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
                    ManejadorArchivos.showDatabases(out);
                break;
                    
                case accion.DROP_TABLE:
                    ManejadorArchivos.dropTable(actualBD.get(), nodo.getHijos().get(0).getValor());
                break;
                    
                case accion.SHOW_TABLES:
                    ManejadorArchivos.showDatabases(actualBD.get(), out);
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
            }
            for(int i = 0; i < nodo.getHijos().size(); i++)
                compilar(nodo.getHijos().get(i), actualBD, out);
        }
    }
}
