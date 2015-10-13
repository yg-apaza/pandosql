package sgbd;

import sgbd.semantico.AST;
import sgbd.semantico.ManejadorArchivos;
import sgbd.semantico.Nodo;
import sgbd.semantico.accion;

public class Compilador
{
    private AST arbol;
    private String actualBD;
    
    public Compilador(AST arbol, String actualBD)
    {
        this.arbol = arbol;
        this.actualBD = actualBD;
    }
    
    public void compilar()
    {
        compilar(arbol.getRaiz());
    }
    
    private void compilar(Nodo nodo)
    {
        if(!nodo.esTerminal())
        {
            switch(nodo.getCodigo())
            {
                case accion.USE_DATABASE:
                    actualBD = nodo.getHijos().get(0).getValor();
                break;
                    
                case accion.CREATE_DATABASE:
                    ManejadorArchivos.crearBD(nodo.getHijos().get(0).getValor());
                break;
            }
            for(int i = 0; i < nodo.getHijos().size(); i++)
                compilar(nodo.getHijos().get(i));
        }
    }
}
