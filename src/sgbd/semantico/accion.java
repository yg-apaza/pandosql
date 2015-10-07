package sgbd.semantico;

public class accion {
    public static final int CREATE_DATABASE = 0;
    public static final int DROP_DATABASE = 1;
    public static final int USE_DATABASE = 2;
    public static final int SHOW_DATABASES = 3;
    
    public static final int CREATE_TABLE = 4;
    public static final int DROP_TABLE = 5; 
    public static final int SHOW_TABLES = 6;
    
    public static final int INSERT_REGISTER = 7;
    public static final int DELETE_REGISTER = 8;
    public static final int UPDATE_REGISTER = 9;
    
    public static final int SELECT = 10;
    public static final int SENTENCE = 11;
    public static final int PARAMETERS = 12;
    public static final int ARGUMENTS = 13;
    public static final int IDENTIFIERS = 14;
    public static final int PARAMETER = 15;
    public static final int SELECT_ALL = 16;
    public static final int CONDITION = 17;
    public static final int START = 18;
    public static final String[] acciones = new String[]
    {
        "CREAR BASE DE DATOS",    
        "ELIMINAR BASE DE DATOS",
        "UTILIZAR BASE DE DATOS",
        "MOSTRAR BASES DE DATOS",        
        "CREAR TABLA",
        "ELIMINAR TABLA",
        "MOSTRAR TABLAS",
                
        "INSERTAR REGISTRO",
        "ELIMINAR REGISTRO",
        "ACTUALIZAR REGISTRO",		
        
        "OBTENER DATOS",
        "SENTENCIA SQL",
        "PARAMETROS DE TABLA",
        "ARGUMENTOS PARA ENTRADA",
        "IDENTIFICADORES",
        "PARAMETRO",
        "SELECCIONAR TODO",
        "CONDICION DE ASIGNACION",
        "INICIO DE TODO"
    };
}
