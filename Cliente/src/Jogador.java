import java.io.Serializable;

public class Jogador implements Serializable {
    private String nome;
    private String classe;
    private int vida;
    private boolean defendendo;

    public Jogador(String nome, String classe) {
        this.nome = nome;
        this.classe = classe;
        this.vida = 100; // Vida inicial do jogador
        this.defendendo = false;
    }

    public String getNome() {
        return nome;
    }

    public String getClasse() {
        return classe;
    }

    public int getVida() {
        return vida;
    }

    public void receberDano(int dano) {
        if (defendendo) {
            dano /= 2;
            defendendo = false; // A defesa só dura um turno
        }
        vida -= dano;
        if (vida < 0) vida = 0;
    }

    public void defender() {
        defendendo = true;
    }

    public int tomarPocao() {
        int vidaRecuperada = 20;
        vida += vidaRecuperada;
        if (vida > 100) vida = 100;
        return vidaRecuperada;
    }
}
