package org.server;
import java.io.*;
import java.net.*;
import java.util.*;
public class ClientSocket {

        private static final String SERVER_ADDRESS = "localhost";
        private static final int SERVER_PORT = 12345;

        public static void main(String[] args) throws IOException {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            new Thread(new ReadThread(socket)).start();
            new Thread(new WriteThread(socket)).start();
        }

        static class ReadThread implements Runnable {
            private BufferedReader in;

            public ReadThread(Socket socket) throws IOException {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }

            @Override
            public void run() {
                try {
                    while (true) {
                        String message = in.readLine();
                        if (message == null) {
                            break;
                        }
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            }
        }

        static class WriteThread implements Runnable {
            private PrintWriter out;
            private Scanner scanner;

            public WriteThread(Socket socket) throws IOException {
                this.out = new PrintWriter(socket.getOutputStream(), true);
                this.scanner = new Scanner(System.in);
            }

            @Override
            public void run() {
                try {
                    while (true) {
                        String message = scanner.nextLine();
                        out.println(message);
                    }
                } catch (Exception e) {
                    System.err.println("Error writing to server: " + e.getMessage());
                }
            }
        }


}
