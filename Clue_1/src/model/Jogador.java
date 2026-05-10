package model;

import java.util.ArrayList;
import java.util.List;

class Jogador {
	private String nome;
	private List<Carta> cartas;
	
	Jogador(String nome){
		this.nome = nome;
		this.cartas = new ArrayList<>();
	}
	
	void receberCarta(Carta carta) {
        cartas.add(carta);
    }

    String getNome() {
        return nome;
    }

    List<Carta> getCartas() {
        return cartas;
    }
}