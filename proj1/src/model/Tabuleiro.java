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
    private Map<String, String> passagensSecretas;

    Tabuleiro() {
        this.casas = new HashMap<String, Casa>();
        this.passagensSecretas = new HashMap<String, String>();
    }

    void adicionarCasa(Casa casa) {
        casas.put(casa.getNome(), casa);
    }

    Casa getCasa(String nome) {
        return casas.get(nome);
    }

    void conectarCasas(String nomeCasa1, String nomeCasa2) {
        Casa casa1 = casas.get(nomeCasa1);
        Casa casa2 = casas.get(nomeCasa2);

        if (casa1 == null || casa2 == null) {
            throw new IllegalArgumentException(
                "Erro ao conectar casas: " +
                nomeCasa1 + " existe? " + (casa1 != null) +
                " | " +
                nomeCasa2 + " existe? " + (casa2 != null)
            );
        }

        casa1.adicionarCasaAdjacente(casa2);
        casa2.adicionarCasaAdjacente(casa1);
    }

    void adicionarPassagemSecreta(String origem, String destino) {
        if (!casas.containsKey(origem) || !casas.containsKey(destino)) {
            throw new IllegalArgumentException("Os cômodos da passagem secreta precisam existir no tabuleiro.");
        }

        passagensSecretas.put(origem, destino);
        passagensSecretas.put(destino, origem);
    }

    void limparOcupacao() {
        for (Casa casa : casas.values()) {
            casa.desocupar();
        }
    }

    boolean temPassagemSecreta(String nomeCasa) {
        return passagensSecretas.containsKey(nomeCasa);
    }

    String getDestinoPassagemSecreta(String nomeCasa) {
        return passagensSecretas.get(nomeCasa);
    }

    private boolean ehComodo(Casa casa) {
        return casa != null && casa.getNome().startsWith("COMODO_");
    }

    List<Casa> mapearCasasAlcancaveis(Casa casaInicial, int valorDados) {
        List<Casa> casasAlcancaveis = new ArrayList<Casa>();
        Set<Casa> casasVisitadas = new HashSet<Casa>();
        Queue<CasaComDistancia> fila = new LinkedList<CasaComDistancia>();

        fila.add(new CasaComDistancia(casaInicial, 0));
        casasVisitadas.add(casaInicial);

        while (!fila.isEmpty()) {
            CasaComDistancia atual = fila.poll();

            Casa casaAtual = atual.getCasa();
            int distanciaAtual = atual.getDistancia();

            if (distanciaAtual > 0 && distanciaAtual <= valorDados) {
                casasAlcancaveis.add(casaAtual);
            }

            if (ehComodo(casaAtual) && distanciaAtual > 0) {
                continue;
            }

            if (distanciaAtual < valorDados) {
                for (Casa adjacente : casaAtual.getCasasAdjacentes()) {
                    if (adjacente.getNome().startsWith("INICIO_")) {
                        continue;
                    }

                    boolean destinoEhComodo = ehComodo(adjacente);
                    boolean destinoBloqueado = adjacente.estaOcupada() && !destinoEhComodo;

                    if (!casasVisitadas.contains(adjacente) && !destinoBloqueado) {
                        casasVisitadas.add(adjacente);
                        fila.add(new CasaComDistancia(adjacente, distanciaAtual + 1));
                    }
                }
            }
        }

        return casasAlcancaveis;
    }
}
	
	
