package it.fi.meucci;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Client cliente = new Client("localhost", 6789);
        
        cliente.connetti();
        
        try {
            cliente.comunica();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
