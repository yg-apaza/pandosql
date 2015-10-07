package sgbd;

import java.util.ArrayList;

public class Mistake
{
    private final ArrayList<String> errorLexico;
    private final ArrayList<String> errorSintactico;
    private final ArrayList<String> errorSemantico;
    private final ArrayList<String> warnings;
    
    /* Errores Generales */
    public static final int LEXICO = 0;
    public static final int SINTACTICO = 1;
    public static final int SEMANTICO = 2;
    
    /* Errores del Análisis Léxico */
    public static final int TOKEN_INVALIDO = 0;
    
    /* Errores del Análisis Sintáctico */
    public static final int ERROR_SINTACTICO = 0;
    
    /* Errores del Análisis Semántico */
    public static final int BD_NO_EXISTE = 0;
    
    /* Warnings */
    public static final int NUM_ELEMENTOS_INCORRECTOS = 0;
    public static final int FILAS_NO_COINCIDE = 1;
    public static final int COLUMNAS_INCORRECTAS = 2;
    
    private final String [] listaLexico =
    {
        "Error Lexico: Token '$' no reconocido. Lin: $ Col: $"
    };
    
    private final String [] listaSintactico =
    {
        "Error Sintactico: $. Lin: $ Col: $"
    };
    
    private final String [] listaSemantico =
    {
        "Error Semántico: Base de datos '$' no existe. Lin: $ Col: $"
    };
    
    private final String[] listaWarnings =
    {
        "Warning Semántico: Número de elementos introducidos a la matriz/vector '$' incorrecto. Lin: $ Col: $",
        "Warning Semántico: Número de filas de la matriz/vector '$' no coincide con las inicializadas. Lin: $ Col: $",
        "Warning Semántico: Número de columnas de la fila $ de la matriz/vector '$' incorrecto. Lin: $ Col: $"
    };
    
    public Mistake()
    {
        errorLexico     = new ArrayList <>();
        errorSintactico = new ArrayList <>();
        errorSemantico  = new ArrayList <>();
        warnings        = new ArrayList <>();
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
    
    public void insertarWarning(int codigo, String[] datos)
    {
        warnings.add(unir(listaWarnings[codigo], datos));
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
            case 3:
                return warnings;
        }
        return (new ArrayList <>());
    }    
}
