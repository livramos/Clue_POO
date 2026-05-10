package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

 class Tabuleiro {
    private Map<String, Casa> casas;

    public Tabuleiro() {
        this.casas = new HashMap<>();
    }

    public void adicionarCasa(Casa casa) {
        casas.put(casa.getNome(), casa);
    }

    public Casa getCasa(String nome) {
        return casas.get(nome);
    }

    public List<Casa> mapearCasasAlcancaveis(Casa casaInicial, int valorDados) {
        List<Casa> casasAlcancaveis = new ArrayList<>();
        Set<Casa> casasVisitadas = new HashSet<>();
        Queue<CasaComDistancia> fila = new LinkedList<>();

        fila.add(new CasaComDistancia(casaInicial, 0));
        casasVisitadas.add(casaInicial);

        while (!fila.isEmpty()) {
            CasaComDistancia atual = fila.poll();

            Casa casaAtual = atual.getCasa();
            int distanciaAtual = atual.getDistancia();

            if (distanciaAtual > 0) {
                casasAlcancaveis.add(casaAtual);
            }

            if (distanciaAtual < valorDados) {
            	for (Casa adjacente : casaAtual.getCasasAdjacentes()) {
                    if (!casasVisitadas.contains(adjacente)) {
                        casasVisitadas.add(adjacente);
                        fila.add(new CasaComDistancia(adjacente, distanciaAtual + 1));
                    }
                }
            }
        }

        return casasAlcancaveis;
    }
}