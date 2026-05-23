package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueModel {
    private List<Carta> envelopeConfidencial;
    private List<Jogador> jogadoresEmOrdemDaEsquerda;
    private Map<String, Jogador> jogadoresPorNome;
    private Dado dado;
    private Tabuleiro tabuleiro;
    private int indiceJogadorAtual;
    private List<String> ultimasCasasMapeadas;

    public ClueModel() {
        this.envelopeConfidencial = new ArrayList<Carta>();
        this.jogadoresEmOrdemDaEsquerda = new ArrayList<Jogador>();
        this.jogadoresPorNome = new HashMap<String, Jogador>();
        this.dado = new Dado();
        this.tabuleiro = new Tabuleiro();
        this.indiceJogadorAtual = 0;
        this.ultimasCasasMapeadas = new ArrayList<String>();
    }

    private List<Carta> criarSuspeitos() {
        List<Carta> suspeitos = new ArrayList<Carta>();

        suspeitos.add(new Carta("Srta. Scarlet", TipoCarta.SUSPEITO));
        suspeitos.add(new Carta("Coronel Mostarda", TipoCarta.SUSPEITO));
        suspeitos.add(new Carta("Sra. White", TipoCarta.SUSPEITO));
        suspeitos.add(new Carta("Sr. Green", TipoCarta.SUSPEITO));
        suspeitos.add(new Carta("Sra. Peacock", TipoCarta.SUSPEITO));
        suspeitos.add(new Carta("Professor Plum", TipoCarta.SUSPEITO));

        return suspeitos;
    }

    private List<Carta> criarComodos() {
        List<Carta> comodos = new ArrayList<Carta>();

        comodos.add(new Carta("Cozinha", TipoCarta.COMODO));
        comodos.add(new Carta("Salão de Baile", TipoCarta.COMODO));
        comodos.add(new Carta("Jardim de Inverno", TipoCarta.COMODO));
        comodos.add(new Carta("Sala de Jantar", TipoCarta.COMODO));
        comodos.add(new Carta("Sala de Bilhar", TipoCarta.COMODO));
        comodos.add(new Carta("Adega", TipoCarta.COMODO));
        comodos.add(new Carta("Sala de Estar", TipoCarta.COMODO));
        comodos.add(new Carta("Hall de Entrada", TipoCarta.COMODO));
        comodos.add(new Carta("Biblioteca", TipoCarta.COMODO));

        return comodos;
    }

    private List<Carta> criarArmas() {
        List<Carta> armas = new ArrayList<Carta>();

        armas.add(new Carta("Corda", TipoCarta.ARMA));
        armas.add(new Carta("Cano de Chumbo", TipoCarta.ARMA));
        armas.add(new Carta("Faca", TipoCarta.ARMA));
        armas.add(new Carta("Chave Inglesa", TipoCarta.ARMA));
        armas.add(new Carta("Castiçal", TipoCarta.ARMA));
        armas.add(new Carta("Revólver", TipoCarta.ARMA));

        return armas;
    }

    public void prepararJogo(List<String> nomesJogadoresEmOrdemDaEsquerda) {
        if (nomesJogadoresEmOrdemDaEsquerda == null || nomesJogadoresEmOrdemDaEsquerda.size() < 3) {
            throw new IllegalArgumentException("O jogo precisa de pelo menos 3 jogadores.");
        }

        List<Carta> suspeitos = criarSuspeitos();
        List<Carta> comodos = criarComodos();
        List<Carta> armas = criarArmas();

        Collections.shuffle(suspeitos);
        Collections.shuffle(comodos);
        Collections.shuffle(armas);
        
        List<Carta> todosSuspeitos = new ArrayList<Carta>(suspeitos);
        List<Carta> todosComodos = new ArrayList<Carta>(comodos);
        List<Carta> todasArmas = new ArrayList<Carta>(armas);
        
        envelopeConfidencial.clear();

        envelopeConfidencial.add(suspeitos.remove(0));
        envelopeConfidencial.add(comodos.remove(0));
        envelopeConfidencial.add(armas.remove(0));
        

        List<Carta> cartasRestantes = new ArrayList<Carta>();

        cartasRestantes.addAll(suspeitos);
        cartasRestantes.addAll(comodos);
        cartasRestantes.addAll(armas);

        Collections.shuffle(cartasRestantes);

        jogadoresEmOrdemDaEsquerda.clear();
        jogadoresPorNome.clear();

        for (String nome : nomesJogadoresEmOrdemDaEsquerda) {
            Jogador jogador = new Jogador(nome);
            jogador.receberFolha(new FolhaNotas(todosSuspeitos, todosComodos, todasArmas));

            jogadoresEmOrdemDaEsquerda.add(jogador);
            jogadoresPorNome.put(nome, jogador);
        }

        for (int i = 0; i < cartasRestantes.size(); i++) {
            int jogadorAtual = i % jogadoresEmOrdemDaEsquerda.size();
            jogadoresEmOrdemDaEsquerda.get(jogadorAtual).receberCarta(cartasRestantes.get(i));
        }

        indiceJogadorAtual = encontrarIndiceScarlet();
    }

    private int encontrarIndiceScarlet() {
        for (int i = 0; i < jogadoresEmOrdemDaEsquerda.size(); i++) {
            if (jogadoresEmOrdemDaEsquerda.get(i).getNome().equalsIgnoreCase("Srta. Scarlet")) {
                return i;
            }
        }

        throw new IllegalArgumentException("Srta. Scarlet precisa estar na lista de jogadores.");
    }

    public String getNomeJogadorAtual() {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual).getNome();
    }

    public void passarTurno() {
        indiceJogadorAtual = indiceJogadorAtual + 1;

        if (indiceJogadorAtual >= jogadoresEmOrdemDaEsquerda.size()) {
            indiceJogadorAtual = 0;
        }
    }

    public int[] lancarDados() {
        int dado1 = dado.rolar();
        int dado2 = dado.rolar();

        return new int[] { dado1, dado2 };
    }

    public void adicionarCasaAoTabuleiro(String nomeCasa) {
        tabuleiro.adicionarCasa(new Casa(nomeCasa));
    }

    public void conectarCasas(String nomeCasa1, String nomeCasa2) {
        tabuleiro.conectarCasas(nomeCasa1, nomeCasa2);
    }

    public List<String> mapearCasas(String nomeCasaInicial, int[] valoresDados) {
        int totalDados = valoresDados[0] + valoresDados[1];

        Casa casaInicial = tabuleiro.getCasa(nomeCasaInicial);

        if (casaInicial == null) {
            throw new IllegalArgumentException("Casa inicial não encontrada: " + nomeCasaInicial);
        }

        List<Casa> casasAlcancaveis = tabuleiro.mapearCasasAlcancaveis(casaInicial, totalDados);
        List<String> nomesCasas = new ArrayList<String>();

        for (Casa casa : casasAlcancaveis) {
            nomesCasas.add(casa.getNome());
        }
        
        this.ultimasCasasMapeadas = nomesCasas;

        return nomesCasas;
    }

    public void deslocarPiao(String nomeJogador, String nomeCasaDestino) {
        Jogador jogador = jogadoresPorNome.get(nomeJogador);
        Casa destino = tabuleiro.getCasa(nomeCasaDestino);

        if (jogador == null) {
            throw new IllegalArgumentException("Jogador não encontrado: " + nomeJogador);
        }

        if (destino == null) {
            throw new IllegalArgumentException("Casa não encontrada: " + nomeCasaDestino);
        }

        Casa casaAtual = jogador.getCasaAtual();

        if (casaAtual != null) {
            casaAtual.desocupar();
        }

        destino.ocupar();
        jogador.setCasaAtual(destino);
    }

    public int getQuantidadeCartasEnvelope() {
        return envelopeConfidencial.size();
    }

    public int getQuantidadeJogadores() {
        return jogadoresEmOrdemDaEsquerda.size();
    }
    
    public boolean jogadorTemFolhaNotas(int indiceJogador) {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogador).getFolhaNotas() != null;
    }

    public int getQuantidadeCartasDoJogador(int indiceJogador) {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogador).getCartas().size();
    }
    
    public int getQuantidadeSuspeitosNaFolhaDoJogador(int indiceJogador) {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogador).getFolhaNotas().getSuspeitos().size();
    }

    public int getQuantidadeComodosNaFolhaDoJogador(int indiceJogador) {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogador).getFolhaNotas().getComodos().size();
    }

    public int getQuantidadeArmasNaFolhaDoJogador(int indiceJogador) {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogador).getFolhaNotas().getArmas().size();
    }

    public void marcarCartaNaFolhaDoJogador(int indiceJogador, String nomeCarta) {
        jogadoresEmOrdemDaEsquerda.get(indiceJogador).getFolhaNotas().marcarCarta(nomeCarta);
    }

    public boolean cartaEstaMarcadaNaFolhaDoJogador(int indiceJogador, String nomeCarta) {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogador).getFolhaNotas().cartaEstaMarcada(nomeCarta);
    }

    public String getNomeCartaEnvelope(int indice) {
        return envelopeConfidencial.get(indice).getNome();
    }

    public String getNomeCartaDoJogador(int indiceJogador, int indiceCarta) {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogador).getCartas().get(indiceCarta).getNome();
    }
    public void posicionarPiaoDaVez(String nomeCasaInicial) {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);
        Casa casaInicial = tabuleiro.getCasa(nomeCasaInicial);

        if (casaInicial == null) {
            throw new IllegalArgumentException("Casa não encontrada: " + nomeCasaInicial);
        }

        Casa casaAtual = jogadorAtual.getCasaAtual();

        if (casaAtual != null) {
            casaAtual.desocupar();
        }

        casaInicial.ocupar();
        jogadorAtual.setCasaAtual(casaInicial);
    }

    public void deslocarPiaoDaVez(String nomeCasaDestino) {
        if (!ultimasCasasMapeadas.contains(nomeCasaDestino)) {
            throw new IllegalArgumentException("A casa escolhida não está entre as casas alcançáveis.");
        }

        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);
        Casa destino = tabuleiro.getCasa(nomeCasaDestino);

        if (destino == null) {
            throw new IllegalArgumentException("Casa não encontrada: " + nomeCasaDestino);
        }

        Casa casaAtual = jogadorAtual.getCasaAtual();

        if (casaAtual != null) {
            casaAtual.desocupar();
        }

        destino.ocupar();
        jogadorAtual.setCasaAtual(destino);
    }

    public String getNomeCasaAtualDoJogadorDaVez() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        if (jogadorAtual.getCasaAtual() == null) {
            return null;
        }

        return jogadorAtual.getCasaAtual().getNome();
    }
    
    public void adicionarPassagemSecreta(String origem, String destino) {
        tabuleiro.adicionarPassagemSecreta(origem, destino);
    }

    public boolean jogadorDaVezTemPassagemSecreta() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        if (jogadorAtual.getCasaAtual() == null) {
            return false;
        }

        return tabuleiro.temPassagemSecreta(jogadorAtual.getCasaAtual().getNome());
    }

    public String getDestinoPassagemSecretaDoJogadorDaVez() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        if (jogadorAtual.getCasaAtual() == null) {
            return null;
        }

        return tabuleiro.getDestinoPassagemSecreta(jogadorAtual.getCasaAtual().getNome());
    }

    public void usarPassagemSecretaJogadorDaVez() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        if (jogadorAtual.getCasaAtual() == null) {
            throw new IllegalArgumentException("O jogador da vez não está em nenhuma casa.");
        }

        String nomeCasaAtual = jogadorAtual.getCasaAtual().getNome();

        if (!tabuleiro.temPassagemSecreta(nomeCasaAtual)) {
            throw new IllegalArgumentException("A casa atual não possui passagem secreta.");
        }

        String nomeDestino = tabuleiro.getDestinoPassagemSecreta(nomeCasaAtual);
        Casa destino = tabuleiro.getCasa(nomeDestino);

        jogadorAtual.getCasaAtual().desocupar();
        destino.ocupar();
        jogadorAtual.setCasaAtual(destino);
    }
    
}