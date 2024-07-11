import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor Inicializado, aguardando jogadores...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Jogador 1 conectado.");
            ObjectOutputStream Output1 = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream Input1 = new ObjectInputStream(clientSocket.getInputStream());

            // message de espera
            Output1.writeObject("Aguardando o segundo jogador...");

            Socket clienteSocket2 = serverSocket.accept();
            System.out.println("Jogador 2 conectado.");

            ObjectOutputStream Output2 = new ObjectOutputStream(clienteSocket2.getOutputStream());
            ObjectInputStream Input2 = new ObjectInputStream(clienteSocket2.getInputStream());

            Player PlayerNumber1 = (Player) Input1.readObject();
            Player PlayerNumber2 = (Player) Input2.readObject();

            // Inicia o jogo
            GeneralGame generalGame = new GeneralGame(PlayerNumber1, PlayerNumber2, Output1, Output2, Input1, Input2);
            generalGame.StartGame();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
