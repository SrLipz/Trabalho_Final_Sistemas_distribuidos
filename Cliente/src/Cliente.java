import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private static final String HOST = "192.168.2.158"; // Alterar para o IP correto
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORTA)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            limparTerminal();
            System.out.println("                 ___====-_  _-====___\n" +
                    "           _--^^^#####//      \\\\#####^^^--_\n" +
                    "        _-^##########// (    ) \\\\##########^-_\n" +
                    "       -############//  |\\^^/|  \\\\############-\n" +
                    "     _/############//   (@::@)   \\\\############\\_\n" +
                    "    /#############((     \\\\//     ))#############\\\n" +
                    "   -###############\\\\    (oo)    //###############-\n" +
                    "  -#################\\\\  / VV \\  //#################-\n" +
                    " -###################\\\\/      \\//###################-\n" +
                    "_#/|##########/\\######(   /\\   )######/\\##########|\\#_\n" +
                    "|/ |#/\\#/\\#/\\/  \\#/\\##\\  |  |  /##/\\#/  \\/\\#/\\#/\\#| \\|\n" +
                    "`  |/  V  V  `   V  \\#\\| |  | |/#/  V   '  V  V  \\|  '\n" +
                    "   `   `  `      `   / | |  | | \\   '      '  '   '\n" +
                    "                    (  | |  | |  )\n" +
                    "                   __\\ | |  | | /__\n" +
                    "                  (vvv(VVV)(VVV)vvv)\n"
                    +"\n        Bem-vindo(a) a batalha contra o dragão!");

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
            System.out.println("\nEscolha sua classe:\n[ 1 ] Guerreiro\n[ 2 ] Mago\n[ 3 ] Arqueiro");
            String classeEscolha = scanner.nextLine();
            String classe;
            switch (classeEscolha) {
                case "1":
                    classe = "Guerreiro";
                    break;
                case "2":
                    classe = "Mago";
                    break;
                case "3":
                    classe = "Arqueiro";
                    break;
                default:
                    classe = "Guerreiro"; // Classe padrão
                    break;
            }

            Jogador jogador = new Jogador(nome, classe);
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
