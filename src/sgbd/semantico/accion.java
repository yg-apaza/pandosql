package sgbd.semantico;

public class accion {
    public static final int SENTENCES = 0;
    
    public static final int CREATE_DATABASE = 1;
    public static final int DROP_DATABASE = 2;
    public static final int USE_DATABASE = 3;
    public static final int SHOW_DATABASES = 4;
    
    public static final int CREATE_TABLE = 5;
    public static final int DROP_TABLE = 6; 
    public static final int SHOW_TABLES = 7;
    
    public static final int INSERT_REGISTER = 8;
    public static final int DELETE_REGISTER = 9;
    public static final int UPDATE_REGISTER = 10;
    
    public static final int SELECT = 11;
    public static final int SELECT_ALL = 12;
    
    public static final int PARAMETERS = 13;
    public static final int ARGUMENTS = 14;
    public static final int IDENTIFIERS = 15;
    public static final int PARAMETER = 16;
    
    public static final int CONDITION = 17;
    
    public static final String[] acciones = new String[]
    {
        "SENTENCIAS SQL",
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
        "SELECCIONAR TODO",       
        "PARAMETROS DE TABLA",
        "ARGUMENTOS PARA ENTRADA",
        "IDENTIFICADORES",
        "PARAMETRO",       
        "CONDICION DE ASIGNACION"
    };
}
