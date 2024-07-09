import java.io.*;
import java.util.Random;

public class Jogo {
    private Jogador jogador1;
    private Jogador jogador2;
    private ObjectOutputStream out1;
    private ObjectOutputStream out2;
    private ObjectInputStream in1;
    private ObjectInputStream in2;
    private int vidaDragao;
    private int turno;

    public Jogo(Jogador jogador1, Jogador jogador2, ObjectOutputStream out1, ObjectOutputStream out2, ObjectInputStream in1, ObjectInputStream in2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        this.out1 = out1;
        this.out2 = out2;
        this.in1 = in1;
        this.in2 = in2;
        this.vidaDragao = 300; // Vida inicial do dragão
        this.turno = 1; // Inicializa o turno como 1
    }

    public void iniciar() throws IOException, ClassNotFoundException {
        // Mensagem inicial
        enviarMensagem("A batalha final se dá início... de um lado temos um poderoso dragão com 300 de vida e do outro lado nossos heróis.");
        enviarMensagem(jogador1.getNome() + " - " + jogador1.getClasse());
        enviarMensagem(jogador2.getNome() + " - " + jogador2.getClasse());

        while (vidaDragao > 0 && (jogador1.getVida() > 0 || jogador2.getVida() > 0)) {
            // Jogador 1 executa seu turno
            enviarStatus();
            executarTurno(jogador1, out1, in1, jogador2, out2);
            if (vidaDragao <= 0 || (jogador1.getVida() <= 0 && jogador2.getVida() <= 0)) break;

            // Jogador 2 executa seu turno
            enviarStatus();
            executarTurno(jogador2, out2, in2, jogador1, out1);
            if (vidaDragao <= 0 || (jogador1.getVida() <= 0 && jogador2.getVida() <= 0)) break;

            // Ação do dragão
            enviarStatus();
            acaoDragao();

            // Incrementa o turno
            turno++;
        }

        // Verifica o vencedor
        if (vidaDragao <= 0) {
            enviarMensagem("Parabéns! Vocês derrotaram o dragão!");
        } else {
            enviarMensagem("O dragão derrotou os jogadores. Fim de jogo.");
        }
    }

    private void enviarStatus() throws IOException {
        String status = String.format("Turno %d:\nDragão com %d de vida\n%s com %d de vida\n%s com %d de vida\n",
                turno, vidaDragao, jogador1.getNome(), jogador1.getVida(), jogador2.getNome(), jogador2.getVida());
        out1.writeObject(status);
        out2.writeObject(status);
    }

    private void executarTurno(Jogador jogadorAtual, ObjectOutputStream outAtual, ObjectInputStream inAtual, Jogador outroJogador, ObjectOutputStream outOutro) throws IOException, ClassNotFoundException {
        // Informa ao jogador atual que é sua vez
        outAtual.writeObject("Você está de frente com o poderoso Dragão, qual será sua ação:\n[ 1 ] Atacar\n[ 2 ] Defender\n[ 3 ] Tomar poção de cura");

        // Aguarda ação do jogador atual
        String acao = (String) inAtual.readObject();
        processarAcao(jogadorAtual, acao);

        // Informa ao outro jogador a ação do jogador atual
        outOutro.writeObject("O jogador " + jogadorAtual.getNome() + " escolheu sua ação: " + acao);
    }

    private void processarAcao(Jogador jogador, String acao) throws IOException {
        switch (acao) {
            case "1":
                int dano = calcularDano(jogador);
                vidaDragao -= dano;
                enviarMensagem(jogador.getNome() + " atacou o dragão e causou " + dano + " de dano!");
                break;
            case "2":
                jogador.defender();
                enviarMensagem(jogador.getNome() + " está em posição de defesa!");
                break;
            case "3":
                int vidaRecuperada = jogador.tomarPocao();
                enviarMensagem(jogador.getNome() + " tomou uma poção de vida e recuperou " + vidaRecuperada + " de vida!");
                break;
            default:
                enviarMensagem(jogador.getNome() + " não fez uma ação válida!");
                break;
        }
    }

    private void acaoDragao() throws IOException {
        Random random = new Random();
        int alvo = random.nextInt(2);
        Jogador jogadorAlvo = (alvo == 0) ? jogador1 : jogador2;

        if (jogadorAlvo.getVida() > 0) {
            int danoDragao = random.nextInt(30) + 1;
            jogadorAlvo.receberDano(danoDragao);
            enviarMensagem("O dragão atacou " + jogadorAlvo.getNome() + " e causou " + danoDragao + " de dano!");
        }
    }

    private int calcularDano(Jogador jogador) {
        Random random = new Random();
        return random.nextInt(20) + 1;
    }

    private void enviarMensagem(String mensagem) throws IOException {
        out1.writeObject(mensagem);
        out2.writeObject(mensagem);
    }
}
