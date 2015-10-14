package sgbd;

import java.util.ArrayList;

public class Mistake
{
    private final ArrayList<String> errorLexico;
    private final ArrayList<String> errorSintactico;
    private final ArrayList<String> errorSemantico;
    
    /** Identificador de errores */
    public static final int LEXICO = 0;
    public static final int SINTACTICO = 1;
    public static final int SEMANTICO = 2;
    
    /** Errores del Análisis Léxico */
    public static final int TOKEN_INVALIDO = 0;
    
    /** Errores del Análisis Sintáctico */
    public static final int ERROR_SINTACTICO = 0;
    
    /** Errores del Análisis Semántico */
    public static final int BD_NO_EXISTE = 0;
    public static final int BD_NO_SELECCIONADA = 1;
    public static final int TABLA_NO_EXISTE = 2;
    public static final int BD_EXISTE = 3;
    public static final int TABLA_EXISTE = 4;
    public static final int ARGUMENTOS_NO_COINCIDEN = 5;
    public static final int CAMPO_NO_EXISTE = 6;
    public static final int ARGUMENTO_NO_COINCIDE = 7;
    
    private final String [] listaLexico =
    {
        "Error: Token '$' no reconocido. Lin: $ Col: $"
    };
    
    private final String [] listaSintactico =
    {
        "Error: $. Lin: $ Col: $"
    };
    
    private final String [] listaSemantico =
    {
        "Error: Base de datos '$' no existe. Lin: $ Col: $",
        "Error: No se ha seleccionado ninguna base de datos. Lin: $ Col: $",
        "Error: Tabla '$' no existe en la base de datos '$'. Lin: $ Col: $",
        "Error: La base de datos '$' ya existe. Lin: $ Col: $",
        "Error: Tabla '$' ya existe en la base de datos '$'. Lin: $ Col: $",
        "Error: Argumentos no coinciden con los de la tabla '$'. Lin: $ Col: $",
        "Error: Campo '$' no existe en la tabla '$'. Lin: $ Col: $",
        "Error: Argumento '$' no coincide con el campo '$'. Lin: $ Col: $"
    };
    
    public Mistake()
    {
        errorLexico     = new ArrayList <>();
        errorSintactico = new ArrayList <>();
        errorSemantico  = new ArrayList <>();
    }
    
    public void insertarError(int tipo, int codigo, String[] datos)
    {
        switch(tipo)
        {
            case 0:
                errorLexico.add(unir(listaLexico[codigo], datos));
            break;
                
            case 1:
                errorSintactico.add(unir(listaSintactico[codigo], datos));
            break;
            
            case 2:
                errorSemantico.add(unir(listaSemantico[codigo], datos));
            break;
        }
    }
        
    public String unir(String error, String[] datos)
    {
        int index = -1;
        
        for (String dato : datos) 
        {
            index = error.indexOf("$", index + 1);
            error = error.substring(0, index) + dato + error.substring(index+1);
        }
        
        return error;
    }
    
    public ArrayList<String> getError(int tipo)
    {
        switch(tipo)
        {
            case 0:
                return errorLexico;
            case 1:
                return errorSintactico;
            case 2:
                return errorSemantico;
        }
        return (new ArrayList <>());
    }
    
    public void clear()
    {
        this.errorLexico.clear();
        this.errorSintactico.clear();
        this.errorSemantico.clear();
    }
}
