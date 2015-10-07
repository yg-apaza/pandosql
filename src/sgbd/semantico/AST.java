package sgbd.semantico;

import sgbd.Mistake;

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
