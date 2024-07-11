import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Jogo {
    private Jogador jogador1;
    private Jogador jogador2;
    private ObjectOutputStream out1;
    private ObjectOutputStream out2;
    private ObjectInputStream in1;
    private ObjectInputStream in2;
    private int rodada = 1;
    private int totalDeRodadas = 5;

    private static final Random random = new Random();

    public Jogo(Jogador jogador1, Jogador jogador2, ObjectOutputStream out1, ObjectOutputStream out2, ObjectInputStream in1, ObjectInputStream in2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.out1 = out1;
        this.out2 = out2;
        this.in1 = in1;
        this.in2 = in2;
    }

    public void iniciar() throws IOException, ClassNotFoundException {
        // Incialização do jogo
        enviarMensagem("Bem vindo ao General, esperamos que se divirta mais que um gordo na praça de alimentação ou que um drogado na cracolândia!\nBom jogo!");
        enviarMensagem(jogador1.getNome() + " - pontos " + jogador1.getPontuacao());
        enviarMensagem(jogador2.getNome() + " - pontos" + jogador2.getPontuacao());

        while (rodada <= totalDeRodadas) {
            // Jogador 1 executa seu turno
            enviarStatus();
            executarTurno(jogador1, out1, in1, jogador2, out2);

            // Jogador 2 executa seu turno
            enviarStatus();
            executarTurno(jogador2, out2, in2, jogador1, out1);

            // Avança para a próxima rodada.
            rodada++;
        }

        System.out.println("Antes do Acabou o jogo");
        // Verifica o vencedor
        enviarMensagem("Acabou o jogo!");
    }

    private void enviarStatus() throws IOException {
        String status = String.format("Rodada %d:\n%s com %d pontos\n%s com %d pontos\n",
                rodada, jogador1.getNome(), jogador1.getPontuacao(), jogador2.getNome(), jogador2.getPontuacao());
        out1.writeObject(status);
        out2.writeObject(status);
    }

    private void executarTurno(Jogador jogadorAtual, ObjectOutputStream outAtual, ObjectInputStream inAtual, Jogador outroJogador, ObjectOutputStream outOutro) throws IOException, ClassNotFoundException {
        // Informa ao jogador atual que é sua vez
        outAtual.writeObject("Vamos negoney estamos prontos para ver você perder:\n[ 1 ] Jogar os dados");

        // Aguarda ação do jogador atual
        String acao = (String) inAtual.readObject();
        processarAcao(jogadorAtual, acao);

        // Informa ao outro jogador a ação do jogador atual
        outOutro.writeObject("O jogador " + jogadorAtual.getNome() + " Jogou os dados!");
    }

    private void processarAcao(Jogador jogador, String acao) throws IOException {
        // Vai ser para validar os pontos
        // Qual pontuação que ele atingiu na rodada
        System.out.println(acao);
        switch (acao) {
            case "1":
                int[] valorDosDados = rollDice();
                enviarMensagem("Valor dos dados\n Dado 1: " +valorDosDados[0] + "\n Dado 2: " + valorDosDados[1] + "\nDado 3: "+ valorDosDados[2]+"\n");
                int score = calculateScore(valorDosDados);
                System.out.println("Bateu score");
                jogador.AdicionarPontuacao(score);
                System.out.println("Bateu depois score");
                enviarMensagem("Pontuação da rodada: "+ score);
                break;
            default:
                enviarMensagem(jogador.getNome() + " perdeu a vez por jogada inválida, chora agora vacilão!");
                break;
        }
    }

    private void enviarMensagem(String mensagem) throws IOException {
        out1.writeObject(mensagem);
        out2.writeObject(mensagem);
    }

    private static int[] rollDice() {
        return new int[]{random.nextInt(6) + 1, random.nextInt(6) + 1, random.nextInt(6) + 1};
    }

    private static int calculateScore(int[] dice) {
        Arrays.sort(dice);
        if (isSequence(dice, 1, 2, 3)) {
            return 12; // Menores Dados
        } else if (isSequence(dice, 4, 5, 6)) {
            return 18; // Maiores Dados
        } else if (isTrio(dice)) {
            return 20; // Trio
        } else if (isDupla(dice)) {
            return 18; // Dupla
        } else {
            return Arrays.stream(dice).sum(); // Simples
        }
    }

    private static boolean isSequence(int[] dice, int... sequence) {
        return Arrays.equals(dice, sequence);
    }

    private static boolean isTrio(int[] dice) {
        return dice[0] == dice[1] && dice[1] == dice[2];
    }

    private static boolean isDupla(int[] dice) {
        return (dice[0] == dice[1] || dice[1] == dice[2] || dice[0] == dice[2]);
    }
}
