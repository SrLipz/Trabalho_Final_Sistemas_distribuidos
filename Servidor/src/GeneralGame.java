import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class GeneralGame {
    private Player PlayerNumber1;
    private Player PlayerNumber2;
    private ObjectOutputStream Output1;
    private ObjectOutputStream Output2;
    private ObjectInputStream Input1;
    private ObjectInputStream Input2;
    private int Round = 1;
    private int TotalRounds = 5;

    private static final Random random = new Random();

    public GeneralGame(Player PlayerNumber1, Player PlayerNumber2, ObjectOutputStream Output1, ObjectOutputStream Output2, ObjectInputStream Input1, ObjectInputStream Input2) {
        this.PlayerNumber1 = PlayerNumber1;
        this.PlayerNumber2 = PlayerNumber2;
        this.Output1 = Output1;
        this.Output2 = Output2;
        this.Input1 = Input1;
        this.Input2 = Input2;
    }

    public void StartGame() throws IOException, ClassNotFoundException {

        // Incialização do jogo
        SendMessageToServer("Bem vindo ao General, esperamos que se divirta mais que um gordo na praça de alimentação ou que um drogado na cracolândia!\nBom jogo!");
        SendMessageToServer(PlayerNumber1.getName() + " - pontos " + PlayerNumber1.getScore());
        SendMessageToServer(PlayerNumber2.getName() + " - pontos " + PlayerNumber2.getScore());

        for (; Round <= TotalRounds ; Round++) {
            SendMessageToServer(ReturnStatusPointsPlayer());
            RunRound(PlayerNumber1, Output1, Input1, Output2);

            // Player 2 executa seu turno
            SendMessageToServer(ReturnStatusPointsPlayer());
            RunRound(PlayerNumber2, Output2, Input2, Output1);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        if(PlayerNumber1.getScore() == PlayerNumber2.getScore()){
            SendMessageToServer("Deu empate, nem pra vencer vocês servem!");
        }else if(PlayerNumber1.getScore() > PlayerNumber2.getScore()){
            SendMessageToServer(String.format("O %s ganhou o jogo!!!!!\nTome seu biscoito!", PlayerNumber1.getName()));
        }else {
            SendMessageToServer(String.format("O %s ganhou o jogo!!!!!\nTome seu biscoito!", PlayerNumber2.getName()));
        }

    }

    // Retorna mensagem constumizada sobre a pontuação dos players atual.
    private String ReturnStatusPointsPlayer(){
        String status = String.format("Round %d:\n%s com %d pontos\n%s com %d pontos\n",
                Round, PlayerNumber1.getName(), PlayerNumber1.getScore(), PlayerNumber2.getName(), PlayerNumber2.getScore());
        return status;
    }

    private void RunRound(Player currentPlayer, ObjectOutputStream currentOutput, ObjectInputStream currentInput, ObjectOutputStream OtherOutput) throws IOException, ClassNotFoundException {
        // Informa ao jogador atual que é sua vez
        currentOutput.writeObject("Vamos negoney estamos prontos para ver você perder:\n[ 1 ] Jogar os dados");

        // Aguarda ação do jogador atual
        String PlayerAction = (String) currentInput.readObject();
        ProcessPlayerAction(currentPlayer, PlayerAction);

        // Informa ao outro jogador a ação do jogador atual
        OtherOutput.writeObject("O jogador " + currentPlayer.getName() + " Jogou os dados!");
    }

    private void ProcessPlayerAction(Player player, String PlayerAction) throws IOException {

        System.out.println(PlayerAction);
        if (PlayerAction.equals("1")) {
            int[] DiceValueRolls = rollDice();
            SendMessageToServer("Valor dos dados\n Dado 1: " + DiceValueRolls[0] + "\n Dado 2: " + DiceValueRolls[1] + "\n Dado 3 : " + DiceValueRolls[2] + "\n");

            int score = calculateScore(DiceValueRolls);
            player.AddScore(score);

            SendMessageToServer("Pontuação da Round: " + score);
        } else {
            SendMessageToServer(player.getName() + " perdeu a vez por jogada inválida, chora agora vacilão!");
        }
    }

    // Realiza o envio de Objetos para o servidor.
    private void SendMessageToServer(String message) throws IOException {
        Output1.writeObject(message);
        Output2.writeObject(message);
    }

    // Realiza a jogada dos dados.
    private static int[] rollDice() {
        return new int[]{random.nextInt(6) + 1, random.nextInt(6) + 1, random.nextInt(6) + 1};
    }

    // Realiza o cálculo da pontuação, a partir do resultado dos dados.
    private static int calculateScore(int[] dice) {
        // realiza a ordenação dos dados.
        Arrays.sort(dice);

        if (isSequence(dice, 1, 2, 3)) {
            return 12;
        } else if (isSequence(dice, 4, 5, 6)) {
            return 18;
        } else if (isTrio(dice)) {
            return 20;
        } else if (isDupla(dice)) {
            return 18;
        } else {
            return Arrays.stream(dice).sum();
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
