import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Cliente {
    private static final String HOST = "192.168.3.118";
      // private static final String HOST = "10.28.147.87";
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

//                try {
//                    Thread.sleep(800);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                limparTerminal();
                System.out.println(mensagem);

                if(mensagem.equals("Acabou o jogo!")){
                    System.out.println("Acabou o jogo!");
                    break;
                }

                if (mensagem.contains("Vamos negoney estamos prontos para ver você perder:")) {
                    String acao = scanner.nextLine();
                    out.writeObject(acao);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void limparTerminal() {
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
