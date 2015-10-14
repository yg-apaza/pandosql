package sgbd.semantico;

import java.util.ArrayList;
import sgbd.Mistake;
import sgbd.lexico.sym;

public class AST
{
    private Nodo raiz;
    private Mistake errores;
    private String actualBD;
    
    public AST(Nodo raiz, Mistake errores, String actualBD)
    {
        this.raiz = raiz;
        this.errores = errores;
        this.actualBD = actualBD;
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
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else if(Util.existeTabla(actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                break;
                    
                case accion.DROP_TABLE:
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else if(!Util.existeTabla(actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                break;
                    
                case accion.DELETE_TABLE:
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else if(!Util.existeTabla(actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                break;
                    
                case accion.SHOW_TABLES:
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getLinea()+1), String.valueOf(nodo.getColumna())}));
                break;
                    
                case accion.INSERT_REGISTER:
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getLinea()+1), String.valueOf(nodo.getColumna())}));
                    else if(!Util.existeTabla(actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else
                    {
                        ArrayList<Integer> tipos = new ArrayList<>();
                        ArrayList<Nodo> args = nodo.getHijos().get(1).getHijos();
                        for (Nodo arg : args)
                        {
                            switch (arg.getCodigo())
                            {
                                case sym.numero:
                                    tipos.add(0);
                                    break;
                                case sym.numreal:
                                    tipos.add(1);
                                    break;
                                case sym.cadena:
                                    tipos.add(2);
                                    break;
                                case sym.tr:
                                case sym.fa:
                                    tipos.add(3);
                                break;
                            }
                        }
                        if(!Util.comprobarTodosArgumentos(actualBD, nodo.getHijos().get(0).getValor(), tipos))
                            errores.insertarError(Mistake.SEMANTICO, Mistake.ARGUMENTOS_NO_COINCIDEN, (new String[] {nodo.getHijos().get(0).getValor(), String.valueOf(nodo.getHijos().get(1).getLinea()+1),String.valueOf(nodo.getHijos().get(1).getColumna())}));
                    }
                break;
                    
                case accion.DELETE_REGISTER:
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getLinea()+1), String.valueOf(nodo.getColumna())}));
                    else if(!Util.existeTabla(actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else if(!Util.existeCampo(actualBD, nodo.getHijos().get(0).getValor(), nodo.getHijos().get(1).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.CAMPO_NO_EXISTE, (new String[] {nodo.getHijos().get(1).getValor(), nodo.getHijos().get(0).getValor(), String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else
                    {
                        int t = -1;
                        switch(nodo.getHijos().get(2).getCodigo())
                        {
                            case sym.numero:
                                    t = 0;
                                    break;
                                case sym.numreal:
                                    t = 1;
                                    break;
                                case sym.cadena:
                                    t = 2;
                                    break;
                                case sym.tr:
                                case sym.fa:
                                    t = 3;
                                break;
                        }
                        if(!Util.comprobarArgumento(actualBD, nodo.getHijos().get(0).getValor(), nodo.getHijos().get(1).getValor(), t))
                            errores.insertarError(Mistake.SEMANTICO, Mistake.ARGUMENTO_NO_COINCIDE, (new String[] {nodo.getHijos().get(2).getValor(), nodo.getHijos().get(1).getValor(), String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    }
                break;
                    
                case accion.UPDATE_REGISTER:
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getLinea()+1), String.valueOf(nodo.getColumna())}));
                    else if(!Util.existeTabla(actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else if(!Util.existeCampo(actualBD, nodo.getHijos().get(0).getValor(), nodo.getHijos().get(1).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.CAMPO_NO_EXISTE, (new String[] {nodo.getHijos().get(1).getValor(), nodo.getHijos().get(0).getValor(), String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else
                    {
                        int t = -1;
                        switch(nodo.getHijos().get(2).getCodigo())
                        {
                            case sym.numero:
                                    t = 0;
                                    break;
                                case sym.numreal:
                                    t = 1;
                                    break;
                                case sym.cadena:
                                    t = 2;
                                    break;
                                case sym.tr:
                                case sym.fa:
                                    t = 3;
                                break;
                        }
                        if(!Util.comprobarArgumento(actualBD, nodo.getHijos().get(0).getValor(), nodo.getHijos().get(1).getValor(), t))
                            errores.insertarError(Mistake.SEMANTICO, Mistake.ARGUMENTO_NO_COINCIDE, (new String[] {nodo.getHijos().get(2).getValor(), nodo.getHijos().get(1).getValor(), String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                        else
                            verificarCondiciones(nodo.getHijos().get(3), nodo.getHijos().get(0).getValor());
                    }
                break;
                    
                case accion.SELECT:
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getLinea()+1), String.valueOf(nodo.getColumna())}));
                    else if(!Util.existeTabla(actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else
                    {
                        ArrayList<Nodo> cols = nodo.getHijos().get(1).getHijos();
                        for(Nodo c: cols)
                            if(!Util.existeCampo(actualBD, nodo.getHijos().get(0).getValor(), c.getValor()))
                                errores.insertarError(Mistake.SEMANTICO, Mistake.CAMPO_NO_EXISTE, (new String[] {c.getValor(), nodo.getHijos().get(0).getValor(), String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                        verificarCondiciones(nodo.getHijos().get(2), nodo.getHijos().get(0).getValor());
                    }
                break;
                    
                case accion.SELECT_ALL:
                    if(actualBD == null)
                        errores.insertarError(Mistake.SEMANTICO, Mistake.BD_NO_SELECCIONADA, (new String[] {String.valueOf(nodo.getLinea()+1), String.valueOf(nodo.getColumna())}));
                    else if(!Util.existeTabla(actualBD, nodo.getHijos().get(0).getValor()))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.TABLA_NO_EXISTE, (new String[] {nodo.getHijos().get(0).getValor(), actualBD, String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                    else
                        verificarCondiciones(nodo.getHijos().get(1), nodo.getHijos().get(0).getValor());
                break;
            }
            
            for(int i = 0; i < nodo.getHijos().size(); i++)
                    verificar(nodo.getHijos().get(i));
        }
    }
    
    private void verificarCondiciones(Nodo nodo, String tabla)
    {
        if(!nodo.esTerminal())
        {
            switch(nodo.getCodigo())
            {
                case accion.CONDITION:
                    int t = -1;
                    switch(nodo.getHijos().get(1).getCodigo())
                    {
                        case sym.numero:
                                t = 0;
                        break;
                        case sym.numreal:
                            t = 1;
                        break;
                        case sym.cadena:
                            t = 2;
                        break;
                        case sym.tr:
                        case sym.fa:
                            t = 3;
                        break;
                    }
                    if(!Util.comprobarArgumento(actualBD, tabla, nodo.getHijos().get(0).getValor(), t))
                        errores.insertarError(Mistake.SEMANTICO, Mistake.ARGUMENTO_NO_COINCIDE, (new String[] {nodo.getHijos().get(1).getValor(), nodo.getHijos().get(0).getValor(), String.valueOf(nodo.getHijos().get(0).getLinea()+1),String.valueOf(nodo.getHijos().get(0).getColumna())}));
                break;
            }
            for(int i = 0; i < nodo.getHijos().size(); i++)
                    verificarCondiciones(nodo.getHijos().get(i), tabla);
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
