import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado e aguardando conexões...");

            Socket clienteSocket1 = serverSocket.accept();
            System.out.println("Jogador 1 conectado.");
            ObjectOutputStream out1 = new ObjectOutputStream(clienteSocket1.getOutputStream());
            ObjectInputStream in1 = new ObjectInputStream(clienteSocket1.getInputStream());

            // Envia mensagem para o jogador 1 aguardando o jogador 2
            out1.writeObject("Aguardando o segundo jogador...");

            Socket clienteSocket2 = serverSocket.accept();
            System.out.println("Jogador 2 conectado.");
            ObjectOutputStream out2 = new ObjectOutputStream(clienteSocket2.getOutputStream());
            ObjectInputStream in2 = new ObjectInputStream(clienteSocket2.getInputStream());

            Jogador jogador1 = (Jogador) in1.readObject();
            Jogador jogador2 = (Jogador) in2.readObject();

            // Inicia o jogo
            Jogo jogo = new Jogo(jogador1, jogador2, out1, out2, in1, in2);
            jogo.iniciar();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
