package sgbd.lexico;

import java_cup.runtime.*;
import java.util.ArrayList;
import lema.analizadorSemantico.Nodo;
import sgbd.Mistake;

%%

%public
%class Lexico
%line
%column
%cup

%{
    /** Variable encargada de guardar errores léxicos */
    public Mistake e;
    
    /**
        * Constructor de clase Lexico.
        *
        * @param    in  lectura de entrada.
        * @param    e   errores encontrados por el compilador.    
    */    
    public Lexico(java.io.Reader in, Mistake e)
    {
        this.e = e;
        this.zzReader = in;
    }
    
    /**
        * Crea un nuevo token no terminal encontrado.
        *
        * @param    type    código del token aceptado por LeMa.
        * @return   nuevo   <tt>Symbol</tt> creado para almacenar al token
    */
    private Symbol symbol(int type)
    {
        return new Symbol(type, yyline, yycolumn);
    }
    
    /**
        * Crea un nuevo token terminal encontrado.
        *
        * @param    type    código del token aceptado por LeMa.
        * @param    value   estructura que almacena tokens terminales.
        * @return   nuevo   <tt>Symbol</tt> creado para almacenar al token.
    */
    private Symbol symbol(int type, Object value)
    {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

L = [a-zA-Z]
D = [0-9]
WHITE = [ \t\r\n]

%%

/** Token: Espacio en blanco y salto de línea */
{WHITE}                         {                                       }

/** Token: Comentario en Bloque */
"/*"([^*]|\*+[^/*])*"*"+"/"     {                                       }

/** Token: Comentario en Línea */
"//"([^\n]*)                    {                                       }

/*------------------------------------TIPOS DE DATOS-------------------------------------*/

/** Token: Palabra reservada 'INTEGER' */
"INTEGER"                         { return symbol(sym.integer,   (new Nodo(sym.integer, yytext(), yyline, yycolumn, null, true)));  }

/** Token: Palabra reservada 'DOUBLE' */
"DOUBLE"                        { return symbol(sym.real,  (new Nodo(sym.real, yytext(), yyline, yycolumn, null, true)));  }

/** Token: Palabra reservada 'VARCHAR' */
"VARCHAR"                          { return symbol(sym.varchar,    (new Nodo(sym.varchar, yytext(), yyline, yycolumn, null, true)));     }

/** Token: Palabra reservada 'BOOLEAN' */
"BOOLEAN"                          { return symbol(sym.bool,    (new Nodo(sym.bool, yytext(), yyline, yycolumn, null, true)));     }

/*------------------------------------OPERADORES-------------------------------------*/

/** Token: Operador de Agrupación '(' */
"("                             { return symbol(sym.par_ab);            }

/** Token: Operador de Agrupación ')' */
")"                             { return symbol(sym.par_ce);            }

/** Token: Operador coma ',' */
","                             { return symbol(sym.coma);             }

/** Token: Operador igual '=' */
"="                             { return symbol(sym.igual);             }

/** Token: Operador asterisco '*' */
"*"                             { return symbol(sym.all);             }

/** Token: Operador fin ';' */
";"                             { return symbol(sym.fin);        }

/*--------------------------------PALABRAS RESERVADAS-----------------------------------*/
/** Token: Palabra reservada 'USE' */
"USE"                          { return symbol(sym.use);           }

/** Token: Palabra reservada 'CREATE' */
"CREATE"                          { return symbol(sym.create);           }

/** Token: Palabra reservada 'DROP' */
"DROP"                         { return symbol(sym.drop);          }

/** Token: Palabra reservada 'DATABASE' */
"DATABASE"                      { return symbol(sym.database);           }

/** Token: Palabra reservada 'DATABASES' */
"DATABASES"                   { return symbol(sym.databases);         }

/** Token: Palabra reservada 'SHOW' */
"SHOW"                       { return symbol(sym.show);            }

/** Token: Operador Relacional 'TABLE' */
"TABLE"                            { return symbol(sym.table);             }

/** Token: Operador Relacional 'TABLES' */
"TABLES"                            { return symbol(sym.tables);               }

/** Token: Operador Relacional 'INSERT' */
"INSERT"                            { return symbol(sym.insert);             }

/** Token: Operador Relacional 'INTO' */
"INTO"                            { return symbol(sym.into);               }

/** Token: Operador Relacional 'VALUES' */
"VALUES"                            { return symbol(sym.values);               }

/** Token: Operador Relacional 'DELETE' */
"DELETE"                            { return symbol(sym.delete);               }

/** Token: Operador Relacional 'FROM' */
"FROM"                            { return symbol(sym.from);               }

/** Token: Operador Relacional 'WHERE' */
"WHERE"                            { return symbol(sym.where);               }

/** Token: Operador Relacional 'UPDATE' */
"UPDATE"                            { return symbol(sym.update);               }

/** Token: Operador Relacional 'SET' */
"SET"                            { return symbol(sym.set);               }

/** Token: Operador Relacional 'SELECT' */
"SELECT"                            { return symbol(sym.select);               }

/*--------------------------------NUMEROS - IDENTIFICADORES-----------------------------------*/
/** Token: Cadena */
\"[^\"\n]*\"                    { return symbol(sym.cadena,     (new Nodo(sym.cadena, yytext(), yyline, yycolumn, null, true)));    }

/** Token: Número Entero */
{D}+                            { return symbol(sym.numero,     (new Nodo(sym.numero, yytext(), yyline, yycolumn, null, true)));    }

/** Token: Número Real */
{D}+"."{D}+                     { return symbol(sym.numreal,       (new Nodo(sym.numreal, yytext(), yyline, yycolumn, null, true)));      }

"true"                          { return symbol(sym.tr,         (new Nodo(sym.tr, yytext(), yyline, yycolumn, null, true)) );       }
"false"                         { return symbol(sym.fa,         (new Nodo(sym.fa, yytext(), yyline, yycolumn, null, true)) );       }
/** Token: Identificador */
{L}+({L}|{D}|"_")*              { return symbol(sym.id,         (new Nodo(sym.id, yytext(), yyline, yycolumn, null, true)) );       }

/** Token: Error */
.                               { String[] datos = {yytext(),
                                                    String.valueOf(yyline + 1),
                                                    String.valueOf(yycolumn)};
                                  e.insertarError(0, 0, datos);         }
