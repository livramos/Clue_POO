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
            throw new IllegalArgumentException("Uma das casas não existe no tabuleiro.");
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

    boolean temPassagemSecreta(String nomeCasa) {
        return passagensSecretas.containsKey(nomeCasa);
    }

    String getDestinoPassagemSecreta(String nomeCasa) {
        return passagensSecretas.get(nomeCasa);
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

            boolean ehComodo = casaAtual.getNome().startsWith("COMODO_");

            if (distanciaAtual == valorDados || (distanciaAtual > 0 && ehComodo)) {
                casasAlcancaveis.add(casaAtual);
            }

            if (distanciaAtual < valorDados && !(distanciaAtual > 0 && ehComodo)) {
                for (Casa adjacente : casaAtual.getCasasAdjacentes()) {
                    if (!casasVisitadas.contains(adjacente) && !adjacente.estaOcupada()) {
                        casasVisitadas.add(adjacente);
                        fila.add(new CasaComDistancia(adjacente, distanciaAtual + 1));
                    }
                }
            }
        }

        return casasAlcancaveis;
    }
}