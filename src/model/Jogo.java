package model;

import java.util.List;

 class Jogo {
    private List<Jogador> jogadoresEmOrdemDaEsquerda;
    private int indiceJogadorAtual;

    public Jogo(List<Jogador> jogadoresEmOrdemDaEsquerda) {
        this.jogadoresEmOrdemDaEsquerda = jogadoresEmOrdemDaEsquerda;
        this.indiceJogadorAtual = encontrarIndiceScarlet();
    }

    private int encontrarIndiceScarlet() {
        for (int i = 0; i < jogadoresEmOrdemDaEsquerda.size(); i++) {
            if (jogadoresEmOrdemDaEsquerda.get(i).getNome().equalsIgnoreCase("Srta. Scarlet")) {
                return i;
            }
        }

        throw new IllegalArgumentException("Srta. Scarlet precisa estar na lista de jogadores.");
    }

    public Jogador getJogadorAtual() {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);
    }

    public void passarTurno() {
        indiceJogadorAtual = indiceJogadorAtual + 1;

        if (indiceJogadorAtual >= jogadoresEmOrdemDaEsquerda.size()) {
            indiceJogadorAtual = 0;
        }
    }
}