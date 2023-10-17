package it.fi.meucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client {
    private String serverName;
    private int serverPort;
    private Socket mioSocket;
    private BufferedReader tastiera;
    private DataOutputStream dataToServer;
    private BufferedReader dataFromServer;

    public Client (String serverName, int serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public Socket connetti(){
        System.out.println("CLIENT is running...");
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            mioSocket = new Socket(serverName, serverPort);
            dataToServer = new DataOutputStream(mioSocket.getOutputStream());
            dataFromServer = new BufferedReader(new InputStreamReader(mioSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println("Errore nella connessione");
        } 
        return mioSocket;
    }

    public void comunica() throws IOException{
        //ricezione lista file disponibili dal server
        String listaDalServer = dataFromServer.readLine();
        System.out.println(listaDalServer);

        //scelta del file
        System.out.println("Inserisci il nome del file da scaricare:"+"\n");
        String stringaUtente = tastiera.readLine();

        System.out.println("invio nome al server");
        dataToServer.writeBytes(stringaUtente + "\n");

        //ricezione del file
        String nomeFile = "src/main/receivedFiles/" + stringaUtente; // file da scaricare dal server

        riceviFile(nomeFile);
        
        mioSocket.close();        
    }

    private void riceviFile(String nomeFile) throws IOException{
        // stream di lettura dal socket
        ObjectInputStream reader = new ObjectInputStream(mioSocket.getInputStream());

        // stream di scrittura su file
        FileOutputStream writer = new FileOutputStream(nomeFile);

        //leggo i byte dal socket e li scrivo sul file
        byte[] buffer = new byte[1024];
        int lengthRead;
        while ((lengthRead = reader.read(buffer)) > 0) {
            writer.write(buffer, 0, lengthRead);
        }

        System.out.println("File ricevuto.");

        //chiusura stream e socket
        writer.close();
        reader.close();
    }
}
