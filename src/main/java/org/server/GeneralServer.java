package org.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;



public class GeneralServer {

    public static final int PORT = 12345;

    private ServerSocket serverSocket;

    private List<ClientSocket> clientSockets = new ArrayList<ClientSocket>();

    public void start() throws IOException{
        this.serverSocket  = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta: "+ PORT);
    }

}
