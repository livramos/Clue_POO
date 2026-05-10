package model;

import java.util.ArrayList;
import java.util.List;

class FolhaNotas {
    private List<Carta> suspeitos;
    private List<Carta> comodos;
    private List<Carta> armas;

    FolhaNotas(List<Carta> suspeitos, List<Carta> comodos, List<Carta> armas) {
        this.suspeitos = new ArrayList<Carta>(suspeitos);
        this.comodos = new ArrayList<Carta>(comodos);
        this.armas = new ArrayList<Carta>(armas);
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
