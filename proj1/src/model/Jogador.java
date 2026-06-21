package model;

import java.util.ArrayList;
import java.util.List;

class Jogador {
    private String nome;
    private List<Carta> cartas;
    private Casa casaAtual;
    private FolhaNotas minhaFolha;
    private boolean eliminado;
    
    Jogador(String nome) {
        this.nome = nome;
        this.cartas = new ArrayList<Carta>();
        this.casaAtual = null;
        this.minhaFolha = null;
        this.eliminado = false;
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
    boolean possuiCarta(String nomeCarta) {
        for (Carta carta : cartas) {
            if (carta.getNome().equals(nomeCarta)) {
                return true;
            }
        }

        return false;
    }

    List<String> getNomesCartas() {
        List<String> nomes = new ArrayList<String>();

        for (Carta carta : cartas) {
            nomes.add(carta.getNome());
        }

        return nomes;
    }

    boolean estaEliminado() {
        return eliminado;
    }

    void eliminarPorAcusacaoErrada() {
        eliminado = true;
    }

    void definirEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}