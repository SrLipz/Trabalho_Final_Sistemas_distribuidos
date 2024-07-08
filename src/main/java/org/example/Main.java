package org.example;

import org.server.GeneralServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GeneralServer generalServer = new GeneralServer();

        try {
            generalServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}