package sgbd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java_cup.runtime.Symbol;
import sgbd.lexico.Lexico;
import sgbd.lexico.sym;
import sgbd.semantico.AST;
import sgbd.semantico.Nodo;
import sgbd.sintactico.parser;

public class Servidor
{
    private static Mistake errores = new Mistake();
    private ServerSocket servidorSocket;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public Servidor()
    {
        try
        {
            System.out.println("Esperando un cliente PandoSQL ...");
            servidorSocket = new ServerSocket(6789);
            socket = servidorSocket.accept();
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Cliente listo ...");
        }
        catch (IOException ex)
        {
            System.out.println("Error de conexión. Reinicie el servidor.");
        }
    }
    
    public void lanzar() throws IOException
    {
        while(true)
        {
            String data = in.readUTF();

            String archivo = "";
            String opcion = "";

            if(!data.isEmpty())
            {
                archivo = data.substring(0, data.indexOf(" "));
                opcion = data.substring(data.indexOf(" ") + 1, data.length());
                if(opcion.isEmpty())
                    Compilar(archivo);
                else
                {
                    out.writeUTF("Archivo: " + archivo + "\n");
                    out.flush();
                    switch(Integer.parseInt(opcion))
                    {
                        case 0:
                            ALexico(archivo);
                            break;
                        case 1:
                            ASintactico(archivo);
                            break;
                        case 2:
                            ASemantico(archivo);
                            break;
                        case 3:
                            Compilar(archivo);
                            break;
                    }
                }
            }
        }
    }
    
    public void ALexico(String file) throws IOException
    {
        Reader reader = null;
        try
        {
            errores = new Mistake();
            out.writeUTF("ANALIZADOR LEXICO");
            out.writeUTF("------------------------------------------------------------");
            out.flush();
            
            reader = new BufferedReader(new FileReader(file));
            Lexico lexico;
            lexico = new Lexico(reader, errores);
            String resultado = "";
            Symbol token = lexico.next_token();
            
            while(token.sym != 0)
            {
                switch(token.sym)
                {
                    case sym.integer:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Tipo de dato INTEGER\n";
                        break;
                    case sym.real:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Tipo de dato DOUBLE\n";
                        break;
                    case sym.varchar:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Tipo de dato VARCHAR\n";
                        break;
                    case sym.bool:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Tipo de dato BOOLEAN\n";
                        break;
                    case sym.par_ab:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Paréntesis abierto\n";
                        break;
                    case sym.par_ce:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Paréntesis cerrado\n";
                        break;
                    case sym.coma:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Coma\n";
                        break;
                    case sym.igual:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Igual\n";
                        break;
                    case sym.all:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Asterisco\n";
                        break;
                    case sym.fin:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Punto y coma\n";
                        break;
                    case sym.use:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Use\n";
                        break;
                    case sym.create:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Create\n";
                        break;
                    case sym.drop:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Drop\n";
                        break;
                    case sym.database:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Database\n";
                        break;
                    case sym.databases:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Databases\n";
                        break;
                    case sym.show:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Show\n";
                        break;
                    case sym.table:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Table\n";
                        break;
                    case sym.tables:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Tables\n";
                        break;
                    case sym.insert:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Insert\n";
                        break;
                    case sym.into:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Into\n";
                        break;
                    case sym.values:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Values\n";
                        break;
                    case sym.delete:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Delete\n";
                        break;
                    case sym.from:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: From\n";
                        break;
                    case sym.where:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Where\n";
                        break;
                    case sym.update:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Update\n";
                        break;
                    case sym.set:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Set\n";
                        break;
                    case sym.select:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Select\n";
                        break;
                    case sym.cadena:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Cadena de caracteres " + ((Nodo)(token.value)).getValor() + "\n";
                    break;
                    case sym.numero:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Número entero " + ((Nodo)(token.value)).getValor() + "\n";
                    break;
                    case sym.numreal:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Número real " + ((Nodo)(token.value)).getValor() + "\n";
                    break;
                    case sym.tr:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: True\n";
                    break;
                    case sym.fa:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: False\n";
                    break;
                    case sym.id:
                        resultado = resultado + "LINEA: " + (token.left + 1) + " -> Token: Identificador " + ((Nodo)(token.value)).getValor() + "\n";
                    break;
                }
                token = lexico.next_token();
            }
            
            out.writeUTF(resultado);
            out.flush();
            ArrayList<String> eLexico = errores.getError(0);
            for (String eLexico1 : eLexico)
            {
                out.writeUTF(eLexico1);
                out.flush();
            }
            out.writeUTF("\n");
            if(eLexico.isEmpty())
                out.writeUTF("Finalizado: Análisis Léxico realizado con éxito");
            else
                out.writeUTF("Finalizado: Se encontraron " + eLexico.size() + " error(es)");
            out.flush();
        }
        catch (FileNotFoundException ex)
        {
            out.writeUTF("Error: Archivo incorrecto");
            ex.printStackTrace();
            out.writeUTF("Finalizado");
            out.flush();
        }
        finally
        {
            reader.close();
        }
    }
    
    public void ASintactico(String file) throws IOException
    {
        errores = new Mistake();
        out.writeUTF("ANALIZADOR SINTACTICO");
        out.flush();
        out.writeUTF("------------------------------------------------------------");
        out.flush();
        try
        {
            parser p = new parser(new Lexico(new FileReader(file), errores), errores);
            Object result = p.parse();

            ArrayList<String> eLexico = errores.getError(0);
            ArrayList<String> eSintactico = errores.getError(1);

            if(eLexico.isEmpty())
            {
                for (String eSintactico1 : eSintactico)
                {
                    out.writeUTF(eSintactico1);
                    out.flush();
                }
                out.writeUTF("\n");
                if(eSintactico.isEmpty())
                    out.writeUTF("Finalizado: Análisis Sintactico realizado con éxito");
                else
                    out.writeUTF("Finalizado: Se encontraron " + eSintactico.size() + " error(es)");
                out.flush();
            }
            else
            {
                out.writeUTF("Error Lexico: Se encontraron errores durante el análisis léxico");
                out.writeUTF("Finalizado");
                out.flush();
            }
        }
        catch (FileNotFoundException ex)
        {
            out.writeUTF("Error: Archivo incorrecto");
            out.writeUTF(ex.getMessage());
            out.writeUTF("Finalizado");
            out.flush();
        }
        catch (Exception ex)
        {
            out.writeUTF("Error:");
            out.writeUTF(ex.getMessage());
            out.writeUTF("Finalizado");
            out.flush();
        }
    }
   
    public  void ASemantico(String file) throws IOException
    {
        errores = new Mistake();
        out.writeUTF("ANALIZADOR SEMANTICO");
        out.flush();
        out.writeUTF("------------------------------------------------------------");
        out.flush();
        
        try
        {
            parser p = new parser(new Lexico(new FileReader(file), errores), errores);
            Object result = p.parse();
            ArrayList<String> eLexico = errores.getError(0);
            ArrayList<String> eSintactico = errores.getError(1);
            if(eLexico.isEmpty())
            {
                if(eSintactico.isEmpty())
                {
                    Nodo raiz = p.getRaiz();
                    AST ast = new AST(raiz, errores);
                    
                    ast.verificar();
                    
                    ArrayList<String> eSemantico = errores.getError(2);
                    ArrayList<String> wSemantico = errores.getError(3);
                    for (String eSemantico1 : eSemantico)
                    {
                        out.writeUTF(eSemantico1);
                        out.flush();
                    }
                    for (String w : wSemantico)
                    {
                        out.writeUTF(w);
                        out.flush();
                    }
                    if(eSemantico.isEmpty())
                        out.writeUTF("Finalizado: Análisis Semántico realizado con éxito");
                    else
                        out.writeUTF("Finalizado: Se encontraron " + eSemantico.size() + " error(es)");
                    
                    out.writeUTF(ast.toString());
                    out.flush();
                }
                else
                {
                    out.writeUTF("Error Sintactico: Se encontraron errores durante el análisis sintáctico");
                    out.writeUTF("Finalizado");
                    out.flush();
                }
            }
            else
            {
                out.writeUTF("Error Lexico: Se encontraron errores durante el análisis léxico");
                out.writeUTF("Finalizado");
                out.flush();
            }
        }
        catch (FileNotFoundException ex)
        {
            out.writeUTF("Error: Archivo incorrecto");
            out.writeUTF(ex.getMessage());
            out.writeUTF("Finalizado");
            out.flush();
        }
        catch (Exception ex)
        {
            out.writeUTF("Error:");
            out.writeUTF(ex.getMessage());
            out.writeUTF("Finalizado");
            out.flush();
        }
    }
   
    public static void Compilar(String file)
    {
        /*
        errores = new Mistake();
        System.out.println("COMPILACIÓN");
        System.out.flush();
        System.out.println("------------------------------------------------------------");
        System.out.flush();
        
        try
        {
            parser p = new parser(new Lexico(new FileReader(file), errores), errores);
            Object result = p.parse();
            ArrayList<String> eLexico = errores.getError(0);
            ArrayList<String> eSintactico = errores.getError(1);
            if(eLexico.isEmpty())
            {
                if(eSintactico.isEmpty())
                {
                    Nodo raiz = p.getRaiz();
                    AST ast = new AST(raiz, errores);
                    
                    ast.verificar();
                    ArrayList<String> eSemantico = errores.getError(2);
                    ArrayList<String> wSemantico = errores.getError(3);
                    if(eSemantico.isEmpty())
                    {
                        /* COMPILADOR */
        /*
                        String nombre = (file.substring(0, (file.indexOf(".") > -1)?file.indexOf("."):file.length()));                        
                        Compilador comp = new Compilador(ast, nombre);
                        comp.compilar();
                        System.out.println("AST - CODIGO INTERMEDIO");
                        System.out.println(ast);
                    }
                    else
                    {
                        for (String eSemantico1 : eSemantico)
                        {
                            System.out.println(eSemantico1);
                            System.out.flush();
                        }
                        System.out.println("Finalizado: Se encontraron " + eSemantico.size() + " error(es) semánticos");
                    }
                }
                else
                {
                    for (String eSintactico1 : eSintactico)
                    {
                        System.out.println(eSintactico1);
                        System.out.flush();
                    }
                    System.out.println("Finalizado: Se encontraron " + eSintactico.size() + " error(es) sintácticos");
                }
            }
            else
            {
                for (String eLexico1 : eLexico)
                {
                    System.out.println(eLexico1);
                    System.out.flush();
                }
                System.out.println("Finalizado: Se encontraron " + eLexico.size() + " error(es) léxicos");
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Error: Archivo incorrecto");
            ex.printStackTrace();
            System.out.println("Finalizado");
        }
        catch (Exception ex)
        {
            System.out.println("Error:");
            ex.printStackTrace();
            System.out.println("Finalizado");
        }*/
    }
}
