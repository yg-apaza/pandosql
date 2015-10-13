package sgbd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java_cup.runtime.Symbol;
import sgbd.lexico.Lexico;
import sgbd.lexico.sym;
import sgbd.semantico.AST;
import sgbd.semantico.Nodo;
import sgbd.sintactico.parser;

public class Servidor extends Thread
{
    protected Socket socket;
    private String message = "";
    private static Mistake errores = new Mistake();
    
    public Servidor(Socket clientSocket)
    {
        this.socket = clientSocket;
    }

    @Override
    public void run()
    {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        
        try
        {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println("Servidor PandoSQL no pudo iniciar.");
            return;
        }
        
        String data;
        String actualBD = null;
        
        while (true)
        {
            try
            {
                data = in.readUTF();
                /*
                String archivo = "";
                String opcion = "";
                */
                if (data.isEmpty())
                {
                    socket.close();
                    System.out.println("No se pudo establecer comunicación con el servidor PandoSQL.");
                    return;
                }
                else
                {
                    Compilar(data, out, actualBD);
                    /*
                    archivo = data.substring(0, data.indexOf(" "));
                    opcion = data.substring(data.indexOf(" ") + 1, data.length());
                    if(opcion.isEmpty())
                        Compilar(archivo, out);
                    else
                    {
                        message += ("Archivo: " + archivo + "\n\n");
                        switch(Integer.parseInt(opcion))
                        {
                            case 0:
                                ALexico(archivo, out);
                                break;
                            case 1:
                                ASintactico(archivo, out);
                                break;
                            case 2:
                                ASemantico(archivo, out);
                                break;
                            case 3:
                                Compilar(archivo, out);
                                break;
                        }
                    }
                    */
                }
            }
            catch (IOException e)
            {
                System.out.println("Cliente PandoSQL desconectado.");
                return;
            }
        }
    }
    
    public void ALexico(String file, ObjectOutputStream out) throws IOException, SocketException
    {
        Reader reader = null;
        try
        {
            errores = new Mistake();
            message += "ANALIZADOR LEXICO\n";
            message += "-----------------------------------------------------------------------------\n";
            
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
            
            message += resultado + "\n";
            ArrayList<String> eLexico = errores.getError(0);
            for (String eLexico1 : eLexico)
                message += eLexico1 + "\n";

            message += "\n";
            if(eLexico.isEmpty())
                message += "Finalizado: Análisis Léxico realizado con éxito\n";
            else
                message += "Finalizado: Se encontraron " + eLexico.size() + " error(es)\n";
            
            out.writeUTF(message);
            out.flush();
            message = "";
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
    
    public void ASintactico(String file, ObjectOutputStream out) throws IOException, SocketException
    {
        errores = new Mistake();
        message += "ANALIZADOR SINTACTICO\n";
        message += "-----------------------------------------------------------------------------\n";
        try
        {
            parser p = new parser(new Lexico(new FileReader(file), errores), errores);
            Object result = p.parse();

            ArrayList<String> eLexico = errores.getError(0);
            ArrayList<String> eSintactico = errores.getError(1);

            if(eLexico.isEmpty())
            {
                for (String eSintactico1 : eSintactico)
                   message += eSintactico1 + "\n";

                message += "\n";
                if(eSintactico.isEmpty())
                    message += "Finalizado: Análisis Sintactico realizado con éxito\n";
                else
                    message += "Finalizado: Se encontraron " + eSintactico.size() + " error(es)\n";
            }
            else
            {
                message += "Error Lexico: Se encontraron errores durante el análisis léxico\n";
                message += "Finalizado\n";
            }
            out.writeUTF(message);
            out.flush();
        }
        catch (FileNotFoundException ex)
        {
            message += "Error: Archivo incorrecto\n";
            message += ex.getMessage() + "\n";
            message += "Finalizado\n";
            out.writeUTF(message);
            out.flush();
        }
        catch (Exception ex)
        {
            message += "Error:\n";
            message += ex.getMessage() + "\n";
            message += "Finalizado\n";
            out.writeUTF(message);
            out.flush();
        }
    }
   
    public  void ASemantico(String file, ObjectOutputStream out, String actualBD) throws IOException, SocketException
    {
        errores = new Mistake();
        message += "ANALIZADOR SEMANTICO\n";
        message += "-----------------------------------------------------------------------------\n";
        
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
                    AST ast = new AST(raiz, errores, actualBD);
                    
                    ast.verificar();
                    
                    ArrayList<String> eSemantico = errores.getError(2);
                    ArrayList<String> wSemantico = errores.getError(3);
                    for (String eSemantico1 : eSemantico)
                        message += eSemantico1 + "\n";
                    
                    for (String w : wSemantico)
                        message += w + "\n";

                    if(eSemantico.isEmpty())
                        message += "Finalizado: Análisis Semántico realizado con éxito\n";
                    else
                        message += "Finalizado: Se encontraron " + eSemantico.size() + " error(es)\n";
                    message += ast.toString();
                }
                else
                {
                    message += "Error Sintactico: Se encontraron errores durante el análisis sintáctico\n";
                    message += "Finalizado\n";
                }
            }
            else
            {
                message += "Error Lexico: Se encontraron errores durante el análisis léxico\n";
                message += "Finalizado\n";
            }
            out.writeUTF(message);
            out.flush();
        }
        catch (FileNotFoundException ex)
        {
            message += "Error: Archivo incorrecto\n";
            message += ex.getMessage() + "\n";
            message += "Finalizado\n";
            out.writeUTF(message);
            out.flush();
        }
        catch (Exception ex)
        {
            message += "Error:\n";
            message += ex.getMessage() + "\n";
            message += "Finalizado\n";
            out.writeUTF(message);
            out.flush();
        }
    }
   
    public void Compilar(String linea, ObjectOutputStream out, String actualBD) throws IOException, SocketException
    {
        errores = new Mistake();
        
        try
        {
            parser p = new parser(new Lexico(new StringReader(linea), errores), errores);
            Object result = p.parse();
            ArrayList<String> eLexico = errores.getError(0);
            ArrayList<String> eSintactico = errores.getError(1);
            if(eLexico.isEmpty())
            {
                if(eSintactico.isEmpty())
                {
                    Nodo raiz = p.getRaiz();
                    AST ast = new AST(raiz, errores, actualBD);
                    ast.verificar();
                    ArrayList<String> eSemantico = errores.getError(2);
                    ArrayList<String> wSemantico = errores.getError(3);
                    if(eSemantico.isEmpty())
                    {
                        /* COMPILADOR */
                        Compilador comp = new Compilador(ast, actualBD);
                        comp.compilar();
                    }
                    else
                    {
                        for (String eSemantico1 : eSemantico)
                            message += eSemantico1 + "\n";
                        message += "Finalizado: Se encontraron " + eSemantico.size() + " error(es) semánticos\n";
                    }
                }
                else
                {
                    for (String eSintactico1 : eSintactico)
                        message += eSintactico1 + "\n";
                    message += "Finalizado: Se encontraron " + eSintactico.size() + " error(es) sintácticos\n";
                }
            }
            else
            {
                for (String eLexico1 : eLexico)
                    message += eLexico1 + "\n";
                message += "Finalizado: Se encontraron " + eLexico.size() + " error(es) léxicos\n";
            }
            out.writeUTF(message);
            out.flush();
        }
        catch (Exception ex)
        {
            message += "Error:\n";
            message += ex.getMessage() + "\n";
            message += "Finalizado\n";
            out.writeUTF(message);
            out.flush();
        }
    }
}
