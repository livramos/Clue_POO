package jogo;

public class Jogador {
	private String nome;
    private FolhaNotas minhaFolha;
    
    public Jogador(String nome) {
        this.nome = nome;
        this.minhaFolha = null;
    }
    
    public void receberFolha(FolhaNotas folha) {
        this.minhaFolha = folha;
    }
    
    public FolhaNotas getMinhaFolha() {
        return minhaFolha;
    }
    
    public String getNome() {
        return nome;
    }
}