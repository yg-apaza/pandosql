package sgbd.compilador;

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
    
    public void compilar(AtomicReference<String> actualBD)
    {
        compilar(arbol.getRaiz(), actualBD);
    }
    
    private void compilar(Nodo nodo, AtomicReference<String> actualBD)
    {
        if(!nodo.esTerminal())
        {
            switch(nodo.getCodigo())
            {
                case accion.USE_DATABASE:
                    actualBD.set(nodo.getHijos().get(0).getValor());
                break;
                    
                case accion.CREATE_DATABASE:
                    ManejadorArchivos.crearBD(nodo.getHijos().get(0).getValor());
                break;
                    
                case accion.DROP_DATABASE:
                    ManejadorArchivos.dropBD(nodo.getHijos().get(0).getValor());
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
                    ManejadorArchivos.crearTabla(actualBD.get(), nodo.getHijos().get(0).getValor(), tipos, cols);
                break;
            }
            for(int i = 0; i < nodo.getHijos().size(); i++)
                compilar(nodo.getHijos().get(i), actualBD);
        }
    }
}
