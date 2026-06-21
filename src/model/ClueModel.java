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
    private String cartaRefutada;
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
        comodos.add(new Carta("Sala de Musica", TipoCarta.COMODO));
        comodos.add(new Carta("Jardim de Inverno", TipoCarta.COMODO));
        comodos.add(new Carta("Sala de Jantar", TipoCarta.COMODO));
        comodos.add(new Carta("Salao de Jogos", TipoCarta.COMODO));
        comodos.add(new Carta("Escritorio", TipoCarta.COMODO));
        comodos.add(new Carta("Sala de Estar", TipoCarta.COMODO));
        comodos.add(new Carta("Entrada", TipoCarta.COMODO));
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
            int indiceJogador = i % jogadoresEmOrdemDaEsquerda.size();

            Jogador jogador = jogadoresEmOrdemDaEsquerda.get(indiceJogador);
            Carta carta = cartasRestantes.get(i);

            jogador.receberCarta(carta);

            jogador.getFolhaNotas().marcarCarta(carta.getNome());
        }

        indiceJogadorAtual = encontrarIndiceScarlet();
    }

    private int encontrarIndiceScarlet() {
        for (int i = 0; i < jogadoresEmOrdemDaEsquerda.size(); i++) {
            if (jogadoresEmOrdemDaEsquerda.get(i).getNome().equalsIgnoreCase("Srta. Scarlet")) {
                return i;
            }
        }
        return 0; 
    }

    public String getNomeJogadorAtual() {
        return jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual).getNome();
    }

    public void passarTurno() {
        if (jogadoresEmOrdemDaEsquerda.isEmpty()) {
            return;
        }

        int tentativas = 0;

        do {
            indiceJogadorAtual = indiceJogadorAtual + 1;

            if (indiceJogadorAtual >= jogadoresEmOrdemDaEsquerda.size()) {
                indiceJogadorAtual = 0;
            }

            tentativas++;

        } while (
                jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual).estaEliminado()
                && tentativas < jogadoresEmOrdemDaEsquerda.size()
        );
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

        ultimasCasasMapeadas.clear();
        ultimasCasasMapeadas.addAll(nomesCasas);

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

        moverJogadorParaCasa(jogador, destino);
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

        moverJogadorParaCasa(jogadorAtual, casaInicial);
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

        moverJogadorParaCasa(jogadorAtual, destino);
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

        moverJogadorParaCasa(jogadorAtual, destino);
    }
    private boolean ehComodo(Casa casa) {
        return casa != null && casa.getNome().startsWith("COMODO_");
    }

    private String removerPrefixoComodo(String nomeCasa) {
        if (nomeCasa == null) {
            return null;
        }

        return nomeCasa.replace("COMODO_", "");
    }

    private void moverJogadorParaCasa(Jogador jogador, Casa destino) {
        Casa casaAtual = jogador.getCasaAtual();

        if (casaAtual != null && !ehComodo(casaAtual)) {
            casaAtual.desocupar();
        }

        if (!ehComodo(destino)) {
            destino.ocupar();
        }

        jogador.setCasaAtual(destino);
    }

    private List<String> converterCartasParaNomes(List<Carta> cartas) {
        List<String> nomes = new ArrayList<String>();

        for (Carta carta : cartas) {
            nomes.add(carta.getNome());
        }

        return nomes;
    }

    public List<String> getNomesCartasJogadorDaVez() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        return jogadorAtual.getNomesCartas();
    }

    public List<String> getNomesSuspeitos() {
        return converterCartasParaNomes(criarSuspeitos());
    }

    public List<String> getNomesArmas() {
        return converterCartasParaNomes(criarArmas());
    }

    public List<String> getNomesComodos() {
        return converterCartasParaNomes(criarComodos());
    }

    public List<String> getNomesSuspeitosFolhaJogadorDaVez() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        return converterCartasParaNomes(jogadorAtual.getFolhaNotas().getSuspeitos());
    }

    public List<String> getNomesArmasFolhaJogadorDaVez() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        return converterCartasParaNomes(jogadorAtual.getFolhaNotas().getArmas());
    }

    public List<String> getNomesComodosFolhaJogadorDaVez() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        return converterCartasParaNomes(jogadorAtual.getFolhaNotas().getComodos());
    }

    public boolean cartaEstaMarcadaNaFolhaJogadorDaVez(String nomeCarta) {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        return jogadorAtual.getFolhaNotas().cartaEstaMarcada(nomeCarta);
    }

    public void definirMarcacaoCartaNaFolhaJogadorDaVez(String nomeCarta, boolean marcada) {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        jogadorAtual.getFolhaNotas().definirMarcacaoCarta(nomeCarta, marcada);
    }

    public boolean jogadorDaVezEstaEmComodo() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        return ehComodo(jogadorAtual.getCasaAtual());
    }

    public String getComodoAtualJogadorDaVez() {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        if (!ehComodo(jogadorAtual.getCasaAtual())) {
            return null;
        }

        return removerPrefixoComodo(jogadorAtual.getCasaAtual().getNome());
    }
    public String realizarPalpite(String suspeito, String arma) {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        if (!ehComodo(jogadorAtual.getCasaAtual())) {
            throw new IllegalArgumentException("O jogador precisa estar em um cômodo para dar palpite.");
        }

        String comodo = removerPrefixoComodo(jogadorAtual.getCasaAtual().getNome());

        Jogador suspeitoComoJogador = jogadoresPorNome.get(suspeito);

        if (suspeitoComoJogador != null && !suspeitoComoJogador.estaEliminado()) {
            moverJogadorParaCasa(suspeitoComoJogador, jogadorAtual.getCasaAtual());
        }

        String[] cartasDoPalpite = { suspeito, arma, comodo };
        cartaRefutada = null;

        for (int deslocamento = 1; deslocamento < jogadoresEmOrdemDaEsquerda.size(); deslocamento++) {
            int indice = (indiceJogadorAtual + deslocamento) % jogadoresEmOrdemDaEsquerda.size();

            Jogador jogador = jogadoresEmOrdemDaEsquerda.get(indice);

            if (jogador.estaEliminado()) {
                continue;
            }

            for (String cartaPalpite : cartasDoPalpite) {
                if (jogador.possuiCarta(cartaPalpite)) {
                    cartaRefutada = cartaPalpite;
                    return jogador.getNome() + " pode mostrar uma carta.";
                }
            }
        }

        return "Nenhum jogador conseguiu refutar o palpite.";
    }

    public boolean realizarAcusacao(String suspeito, String arma, String comodo) {
        Jogador jogadorAtual = jogadoresEmOrdemDaEsquerda.get(indiceJogadorAtual);

        boolean suspeitoCorreto = false;
        boolean armaCorreta = false;
        boolean comodoCorreto = false;

        for (Carta carta : envelopeConfidencial) {
            if (carta.getNome().equals(suspeito)) {
                suspeitoCorreto = true;
            }

            if (carta.getNome().equals(arma)) {
                armaCorreta = true;
            }

            if (carta.getNome().equals(comodo)) {
                comodoCorreto = true;
            }
        }

        boolean acusacaoCorreta = suspeitoCorreto && armaCorreta && comodoCorreto;

        if (!acusacaoCorreta) {
            jogadorAtual.eliminarPorAcusacaoErrada();
        }

        return acusacaoCorreta;
    }
    


    public int getQuantidadeJogadoresAtivos() {
    	int ativos = 0;
    	for (Jogador j : jogadoresEmOrdemDaEsquerda) {
    		if (!j.estaEliminado()) ativos++;
    	}
    	return ativos;
    }

    public String getNomeUnicoSobrevivente() {
    	for (Jogador j : jogadoresEmOrdemDaEsquerda) {
    		if (!j.estaEliminado()) return j.getNome();
    	}
    	return null;
    }

    public String getCartaRefutada() {
    	return cartaRefutada;
    }
    public boolean ehJogador(String nome) {
        Jogador jogador = jogadoresPorNome.get(nome);

        return jogador != null && !jogador.estaEliminado();
    }

}

