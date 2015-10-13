package sgbd.semantico;

import sgbd.Mistake;
import sgbd.Servidor;

public class AST
{
    private Nodo raiz;
    private Mistake errores;
    
    public AST()
    {
        this(new Nodo(),null);
    }
    
    public AST(Nodo raiz, Mistake errores)
    {
        this.raiz = raiz;
        this.errores = errores;
    }
    
    public void verificar()
    {
        verificar(raiz);
    }
    
    /**
     * Analizador Semantico
    */
    private void verificar(Nodo nodo)
    {
        if(!nodo.esTerminal())
        {
            switch(nodo.getCodigo())
            {
                case accion.USE_DATABASE:
                    if(!Util.existeBD(nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                break;
                    
                case accion.DROP_DATABASE:
                    if(!Util.existeBD(nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                break;
                    
                case accion.CREATE_TABLE:
                    if(Servidor.actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else if(Util.existeTabla(Servidor.actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), Servidor.actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                break;
                    
                case accion.DROP_TABLE:
                    if(Servidor.actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else if(!Util.existeTabla(Servidor.actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), Servidor.actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                break;
            }
            
            for(int i = 0; i < nodo.getHijos().size(); i++)
                    verificar(nodo.getHijos().get(i));
        }
    }
    
    public Nodo getRaiz()
    {
        return raiz;
    }
    
    
    @Override
    public String toString()
    {
        return "" + raiz;
    }
}
