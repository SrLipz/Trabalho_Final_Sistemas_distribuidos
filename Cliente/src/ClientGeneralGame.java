import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientGeneralGame {
    private static final String HOST = "192.168.3.118";
      // private static final String HOST = "10.28.147.87";
    private static final int PORT = 12345;


    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT)) {

            // Objeto de saída.
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            // Objeto de entrada.
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Inicializando o Scanner.
            Scanner scanner = new Scanner(System.in);

            CleanTerminal();

            // Mensagem Inicial do jogo.
            System.out.println("Bem vindo ao jogo General!");

            //
            while (true) {
                System.out.println("\nEscolha sua ação:\n[ 1 ] Entrar\n[ 2 ] Sair");
                String entrada = scanner.nextLine();
                if ("2".equals(entrada)) {
                    System.out.println("Saindo do jogo...");
                    socket.close();
                    break;
                } else if ("1".equals(entrada)) {
                    break;
                }
            }

            if (!socket.isClosed()) {
                System.out.print("Digite seu nome: ");
                String nome = scanner.nextLine();

                Jogador jogador = new Jogador(nome);
                out.writeObject(jogador);
            }

            while (true) {
                String mensagem = (String) in.readObject();

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                CleanTerminal();
                System.out.println(mensagem);


                if (mensagem.contains("Vamos negoney estamos prontos para ver você perder:")) {
                    String acao = scanner.nextLine();
                    out.writeObject(acao);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Realiza a limpeza do terminal tanto no OS Linux quanto windows.
    private static void CleanTerminal() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
