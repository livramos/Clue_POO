package model;
import java.util.ArrayList;
import java.util.List;

public class Distribuicao {
	private List<Jogador> jogadores;
	
	public Distribuicao() {
        this.jogadores = new ArrayList<>();
    }
    
    public void adicionarJogador(String nome) {
        jogadores.add(new Jogador(nome));
    }
    
    public void distribuirFolhas() {
        for (Jogador jogador : jogadores) {
            FolhaNotas folha = new FolhaNotas();
            jogador.receberFolha(folha);
            System.out.println("Bloco de folhas entregue para: " + jogador.getNome());
        }
        System.out.println("Total de folhas distribuídas: " + jogadores.size());
    }

	public static void main(String[] args) {
		Distribuicao jogo = new Distribuicao();
		
		jogo.adicionarJogador("Mauro");
		jogo.adicionarJogador("Laura");
		jogo.adicionarJogador("Teodoro");
		
		jogo.distribuirFolhas();
	}
}
