import java.io.Serializable;

public class Jogador implements Serializable {
    private String nome;
    private int pontuacao = 0;

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void AdicionarPontuacao(int pontuacaoParaAdicionar){
        this.pontuacao += pontuacaoParaAdicionar;
    }
}
