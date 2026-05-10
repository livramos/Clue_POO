package model;

import java.util.ArrayList;
import java.util.List;

class Casa {
    private String nome;
    private List<Casa> casasAdjacentes;
    private boolean ocupada;

    Casa(String nome) {
        this.nome = nome;
        this.casasAdjacentes = new ArrayList<Casa>();
        this.ocupada = false;
    }

    String getNome() {
        return nome;
    }

    void adicionarCasaAdjacente(Casa casa) {
        casasAdjacentes.add(casa);
    }

    List<Casa> getCasasAdjacentes() {
        return casasAdjacentes;
    }

    boolean estaOcupada() {
        return ocupada;
    }

    void ocupar() {
        ocupada = true;
    }

    void desocupar() {
        ocupada = false;
    }
}