package model;

import java.util.List;

public class PiaoDeslocamento {
	private Tabuleiro tabuleiro;
    private Dado dado;
    
    public PiaoDeslocamento(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        this.dado = new Dado();
    }
    
    public int rolarDado() {
        return dado.rolar();
    }
    
    public List<Casa> getCasasAlcancaveis(Jogador jogador, int valorDados) {
        return tabuleiro.mapearCasasAlcancaveis(jogador.getCasaAtual(), valorDados);
    }
    
    public boolean moverJogador(Jogador jogador, Casa destino, int valorDados) {
        List<Casa> alcancaveis = getCasasAlcancaveis(jogador, valorDados);
        
        if (alcancaveis.contains(destino)) {
            jogador.setCasaAtual(destino);
            return true;
        }
        return false;
    }
}
