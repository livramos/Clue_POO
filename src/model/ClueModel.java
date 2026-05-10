package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClueModel {
	private List<Carta> envelopeConfidencial;
	private List<Jogador> jogadores;
	private Dado dado;
	
	public ClueModel() {
	this.envelopeConfidencial = new ArrayList<>();
    this.jogadores = new ArrayList<>();
    this.dado = new Dado();
	}
	
	private List<Carta> criarSuspeitos() {

		List<Carta> suspeitos = new ArrayList<>();

	    suspeitos.add(new Carta("Srta. Scarlet", TipoCarta.SUSPEITO));
	    suspeitos.add(new Carta("Coronel Mostarda", TipoCarta.SUSPEITO));
	    suspeitos.add(new Carta("Sra. White", TipoCarta.SUSPEITO));
	    suspeitos.add(new Carta("Sr. Green", TipoCarta.SUSPEITO));
	    suspeitos.add(new Carta("Sra. Peacock", TipoCarta.SUSPEITO));
	    suspeitos.add(new Carta("Professor Plum", TipoCarta.SUSPEITO));

	    return suspeitos;
	}
	
	private List<Carta> criarComodos() {

	    List<Carta> comodos = new ArrayList<>();

	    comodos.add(new Carta("Cozinha", TipoCarta.COMODO));
	    comodos.add(new Carta("Salão de Baile", TipoCarta.COMODO));
	    comodos.add(new Carta("Jardim de Inverno", TipoCarta.COMODO));
	    comodos.add(new Carta("Sala de Jantar", TipoCarta.COMODO));
	    comodos.add(new Carta("Sala de Bilhar", TipoCarta.COMODO));
	    comodos.add(new Carta("Adega", TipoCarta.COMODO));
	    comodos.add(new Carta("Sala de Estar", TipoCarta.COMODO));
	    comodos.add(new Carta("Hall de Entrada", TipoCarta.COMODO));
	    comodos.add(new Carta("Biblioteca", TipoCarta.COMODO));

	    return comodos;
	}

	private List<Carta> criarArmas() {

	    List<Carta> armas = new ArrayList<>();

	    armas.add(new Carta("Corda", TipoCarta.ARMA));
	    armas.add(new Carta("Cano de Chumbo", TipoCarta.ARMA));
	    armas.add(new Carta("Faca", TipoCarta.ARMA));
	    armas.add(new Carta("Chave Inglesa", TipoCarta.ARMA));
	    armas.add(new Carta("Castiçal", TipoCarta.ARMA));
	    armas.add(new Carta("Revólver", TipoCarta.ARMA));

	    return armas;
	}
	
	public void prepararJogo(int quantidadeJogadores) {
		List<Carta> suspeitos = criarSuspeitos();
	    List<Carta> comodos = criarComodos();
	    List<Carta> armas = criarArmas();

	    Collections.shuffle(suspeitos);
	    Collections.shuffle(comodos);
	    Collections.shuffle(armas);
	    
	    envelopeConfidencial.clear();

	    envelopeConfidencial.add(suspeitos.remove(0));
	    envelopeConfidencial.add(comodos.remove(0));
	    envelopeConfidencial.add(armas.remove(0));
		
	    List<Carta> cartasRestantes = new ArrayList<>();

        cartasRestantes.addAll(suspeitos);
        cartasRestantes.addAll(comodos);
        cartasRestantes.addAll(armas);

        Collections.shuffle(cartasRestantes);

        jogadores.clear();

        for (int i = 0; i < quantidadeJogadores; i++) {
            jogadores.add(new Jogador("Jogador " + (i + 1)));
        }

        for (int i = 0; i < cartasRestantes.size(); i++) {
            int jogadorAtual = i % quantidadeJogadores;
            jogadores.get(jogadorAtual).receberCarta(cartasRestantes.get(i));
        }
	}
	
	public int[] lancarDados() {
		int dado1 = dado.rolar();
		int dado2 = dado.rolar();
		
		return new int[] { dado1, dado2 };
	}
	
	public int getQuantidadeCartasEnvelope() {
	    return envelopeConfidencial.size();
	}

	public int getQuantidadeJogadores() {
	    return jogadores.size();
	}

	public int getQuantidadeCartasDoJogador(int indiceJogador) {
	    return jogadores.get(indiceJogador).getCartas().size();
	}
	
}
