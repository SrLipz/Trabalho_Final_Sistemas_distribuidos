import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private static final String HOST = "192.168.3.118"; // Alterar para o IP correto
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORTA)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            limparTerminal();
            System.out.println("Bem vindo ao jogo General!");
            while (true) {
                System.out.println("\nEscolha sua ação:\n[ 1 ] Entrar\n[ 2 ] Sair");
                String entrada = scanner.nextLine();
                if ("2".equals(entrada)) {
                    System.out.println("Saindo do jogo...");
                    break;
                } else if ("1".equals(entrada)) {
                    break;
                }
            }

            System.out.print("Digite seu nome: ");
            String nome = scanner.nextLine();

            Jogador jogador = new Jogador(nome);
            out.writeObject(jogador);

            while (true) {
                String mensagem = (String) in.readObject();
                limparTerminal();
                System.out.println(mensagem);

                if (mensagem.contains("qual será sua ação")) {
                    String acao = scanner.nextLine();
                    out.writeObject(acao);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void limparTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
