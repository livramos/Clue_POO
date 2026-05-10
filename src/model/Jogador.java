package model;

import java.util.ArrayList;
import java.util.List;

class Jogador {
    private String nome;
    private List<Carta> cartas;
    private Casa casaAtual;
    private FolhaNotas minhaFolha;
    
    Jogador(String nome) {
        this.nome = nome;
        this.cartas = new ArrayList<Carta>();
        this.casaAtual = null;
        this.minhaFolha = null;
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
    
    void receberFolha(FolhaNotas folhaNotas) {
        this.minhaFolha = folhaNotas;
    }

    FolhaNotas getFolhaNotas() {
        return minhaFolha;
    }

    Casa getCasaAtual() {
        return casaAtual;
    }

    void setCasaAtual(Casa casaAtual) {
        this.casaAtual = casaAtual;
    }
}