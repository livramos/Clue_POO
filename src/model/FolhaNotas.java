package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FolhaNotas {
    private List<Carta> suspeitos;
    private List<Carta> comodos;
    private List<Carta> armas;
    private Map<String, Boolean> marcacoes;

    FolhaNotas(List<Carta> suspeitos, List<Carta> comodos, List<Carta> armas) {
        this.suspeitos = new ArrayList<Carta>(suspeitos);
        this.comodos = new ArrayList<Carta>(comodos);
        this.armas = new ArrayList<Carta>(armas);
        this.marcacoes = new HashMap<String, Boolean>();

        inicializarMarcacoes(this.suspeitos);
        inicializarMarcacoes(this.comodos);
        inicializarMarcacoes(this.armas);
    }

    private void inicializarMarcacoes(List<Carta> cartas) {
        for (Carta carta : cartas) {
            marcacoes.put(carta.getNome(), false);
        }
    }

    void marcarCarta(String nomeCarta) {
        if (!marcacoes.containsKey(nomeCarta)) {
            throw new IllegalArgumentException("Carta não existe na folha: " + nomeCarta);
        }

        marcacoes.put(nomeCarta, true);
    }

    boolean cartaEstaMarcada(String nomeCarta) {
        if (!marcacoes.containsKey(nomeCarta)) {
            throw new IllegalArgumentException("Carta não existe na folha: " + nomeCarta);
        }

        return marcacoes.get(nomeCarta);
    }
    
    void definirMarcacaoCarta(String nomeCarta, boolean marcada) {
        if (!marcacoes.containsKey(nomeCarta)) {
            throw new IllegalArgumentException("Carta não existe na folha: " + nomeCarta);
        }

        marcacoes.put(nomeCarta, marcada);
    }

    List<Carta> getSuspeitos() {
        return suspeitos;
    }

    List<Carta> getComodos() {
        return comodos;
    }

    List<Carta> getArmas() {
        return armas;
    }
}