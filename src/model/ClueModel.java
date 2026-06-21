package model;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
 
public class ClueModel {
    private List<Carta> envelopeConfidencial;
    private List<Jogador> jogadoresEmOrdemDaEsquerda;
    private Map<String, Jogador> jogadoresPorNome;
    private Dado dado;
    private Tabuleiro tabuleiro;
    private int indiceJogadorAtual;
    private List<String> ultimasCasasMapeadas;
    private String cartaRefutada;
    private String jogadorQueRefutou;
 
    public ClueModel() {
        this.envelopeConfidencial = new ArrayList<Carta>();
        this.jogadoresEmOrdemDaEsquerda = new ArrayList<Jogador>();
        this.jogadoresPorNome = new HashMap<String, Jogador>();
        this.dado = new Dado();
        this.tabuleiro = new Tabuleiro();
        this.indiceJogadorAtual = 0;
        this.ultimasCasasMapeadas = new ArrayList<String>();
        this.cartaRefutada = null;
        this.jogadorQueRefutou = null;
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
        throw new IllegalArgumentException("Srta. Scarlet precisa estar na lista de jogadores.");
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
        jogadorQueRefutou = null;

        for (int deslocamento = 1; deslocamento < jogadoresEmOrdemDaEsquerda.size(); deslocamento++) {
            int indice = (indiceJogadorAtual + deslocamento) % jogadoresEmOrdemDaEsquerda.size();

            Jogador jogador = jogadoresEmOrdemDaEsquerda.get(indice);

            if (jogador.estaEliminado()) {
                continue;
            }

            for (String cartaPalpite : cartasDoPalpite) {
                if (jogador.possuiCarta(cartaPalpite)) {
                    cartaRefutada = cartaPalpite;
                    jogadorQueRefutou = jogador.getNome();

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
 
    public void carregarEstadoDeArquivo(File arquivo) throws IOException {
        Properties propriedades = lerPropriedadesAscii(arquivo);
 
        String formato = propriedades.getProperty("formato");
        if (!"CLUE_SAVE_V1".equals(formato)) {
            throw new IllegalArgumentException("Formato de arquivo invalido. Esperado: CLUE_SAVE_V1.");
        }
 
        List<Carta> novoEnvelope = lerEnvelope(propriedades);
 
        int quantidadeJogadores = lerInteiroObrigatorio(propriedades, "jogador.quantidade");
        if (quantidadeJogadores < 3) {
            throw new IllegalArgumentException("O arquivo precisa conter pelo menos 3 jogadores.");
        }
 
        List<Carta> todosSuspeitos = criarSuspeitos();
        List<Carta> todosComodos = criarComodos();
        List<Carta> todasArmas = criarArmas();
 
        List<Jogador> novosJogadores = new ArrayList<Jogador>();
        Map<String, Jogador> novoMapaJogadores = new HashMap<String, Jogador>();
        List<String> posicoesJogadores = new ArrayList<String>();
 
        for (int i = 0; i < quantidadeJogadores; i++) {
            String prefixo = "jogador." + i + ".";
 
            String nome = lerTextoObrigatorio(propriedades, prefixo + "nome");
            String posicao = propriedades.getProperty(prefixo + "posicao", "").trim();
            boolean eliminado = Boolean.parseBoolean(
                    propriedades.getProperty(prefixo + "eliminado", "false").trim()
            );
 
            if (novoMapaJogadores.containsKey(nome)) {
                throw new IllegalArgumentException("Jogador repetido no arquivo: " + nome);
            }
 
            Jogador jogador = new Jogador(nome);
            jogador.receberFolha(new FolhaNotas(todosSuspeitos, todosComodos, todasArmas));
            jogador.definirEliminado(eliminado);
 
            carregarCartasDoJogador(jogador, propriedades.getProperty(prefixo + "cartas", ""));
            carregarMarcacoesDaFolha(jogador, propriedades.getProperty(prefixo + "notas", ""));
 
            novosJogadores.add(jogador);
            novoMapaJogadores.put(nome, jogador);
            posicoesJogadores.add(posicao);
        }
 
        String nomeJogadorAtual = lerTextoObrigatorio(propriedades, "jogadorAtual");
        int novoIndiceJogadorAtual = encontrarIndicePorNome(novosJogadores, nomeJogadorAtual);
 
        if (novoIndiceJogadorAtual < 0) {
            throw new IllegalArgumentException("Jogador atual nao existe na lista de jogadores: " + nomeJogadorAtual);
        }
 
        validarPosicoesNoTabuleiro(posicoesJogadores);
 
        tabuleiro.limparOcupacao();
 
        this.envelopeConfidencial.clear();
        this.envelopeConfidencial.addAll(novoEnvelope);
        this.jogadoresEmOrdemDaEsquerda = novosJogadores;
        this.jogadoresPorNome = novoMapaJogadores;
        this.indiceJogadorAtual = novoIndiceJogadorAtual;
        this.ultimasCasasMapeadas.clear();
 
        for (int i = 0; i < novosJogadores.size(); i++) {
            String posicao = posicoesJogadores.get(i);
 
            if (posicao == null || posicao.length() == 0 || "null".equalsIgnoreCase(posicao)) {
                continue;
            }
 
            Casa casa = tabuleiro.getCasa(posicao);
            if (casa == null) {
                throw new IllegalArgumentException("Casa do arquivo nao existe no tabuleiro: " + posicao);
            }
            moverJogadorParaCasa(novosJogadores.get(i), casa);
        }
    }
 
    public List<String> getNomesJogadoresEmOrdem() {
        List<String> nomes = new ArrayList<String>();
 
        for (Jogador jogador : jogadoresEmOrdemDaEsquerda) {
            nomes.add(jogador.getNome());
        }
        return nomes;
    }
 
    public String getNomeCasaAtualDoJogador(String nomeJogador) {
        Jogador jogador = jogadoresPorNome.get(nomeJogador);
 
        if (jogador == null || jogador.getCasaAtual() == null) {
            return null;
        }
        return jogador.getCasaAtual().getNome();
    }
 
    private void validarPosicoesNoTabuleiro(List<String> posicoesJogadores) {
        for (String posicao : posicoesJogadores) {
            if (posicao == null || posicao.length() == 0 || "null".equalsIgnoreCase(posicao)) {
                continue;
            }
 
            if (tabuleiro.getCasa(posicao) == null) {
                throw new IllegalArgumentException("Casa do arquivo nao existe no tabuleiro: " + posicao);
            }
        }
    }
 
    private Properties lerPropriedadesAscii(File arquivo) throws IOException {
        if (arquivo == null) {
            throw new IllegalArgumentException("Arquivo nao informado.");
        }
 
        byte[] bytes = Files.readAllBytes(arquivo.toPath());
 
        for (byte b : bytes) {
            if ((b & 0xFF) > 127) {
                throw new IllegalArgumentException("O arquivo precisa estar em ASCII puro. Use escapes como \\u00f3 para acentos.");
            }
        }
 
        Properties propriedades = new Properties();
        propriedades.load(new StringReader(new String(bytes, StandardCharsets.US_ASCII)));
 
        return propriedades;
    }
 
    private List<Carta> lerEnvelope(Properties propriedades) {
        List<Carta> novoEnvelope = new ArrayList<Carta>();
 
        for (int i = 0; i < 3; i++) {
            novoEnvelope.add(lerCartaObrigatoria(propriedades, "envelope." + i));
        }
 
        validarEnvelope(novoEnvelope);
 
        return novoEnvelope;
    }
 
    private void validarEnvelope(List<Carta> cartas) {
        boolean temSuspeito = false;
        boolean temComodo = false;
        boolean temArma = false;
 
        for (Carta carta : cartas) {
            if (carta.getTipo() == TipoCarta.SUSPEITO) {
                temSuspeito = true;
            } else if (carta.getTipo() == TipoCarta.COMODO) {
                temComodo = true;
            } else if (carta.getTipo() == TipoCarta.ARMA) {
                temArma = true;
            }
        }
 
        if (!temSuspeito || !temComodo || !temArma) {
            throw new IllegalArgumentException("O envelope precisa conter 1 suspeito, 1 comodo e 1 arma.");
        }
    }
 
    private void carregarCartasDoJogador(Jogador jogador, String textoCartas) {
        if (textoCartas == null || textoCartas.trim().length() == 0) {
            return;
        }
 
        String[] cartas = textoCartas.split("\\|");
 
        for (String textoCarta : cartas) {
            textoCarta = textoCarta.trim();
            if (textoCarta.length() == 0) {
                continue;
            }
 
            jogador.receberCarta(criarCartaDeTexto(textoCarta));
        }
    }
 
    private void carregarMarcacoesDaFolha(Jogador jogador, String textoNotas) {
        if (textoNotas == null || textoNotas.trim().length() == 0) {
            return;
        }
 
        String[] marcacoes = textoNotas.split("\\|");
 
        for (String marcacao : marcacoes) {
            marcacao = marcacao.trim();
            if (marcacao.length() == 0) {
                continue;
            }
 
            int separador = marcacao.lastIndexOf(":");
            if (separador <= 0 || separador == marcacao.length() - 1) {
                throw new IllegalArgumentException("Marcacao invalida na folha de notas: " + marcacao);
            }
 
            String nomeCarta = marcacao.substring(0, separador).trim();
            boolean marcada = Boolean.parseBoolean(marcacao.substring(separador + 1).trim());
 
            jogador.getFolhaNotas().definirMarcacaoCarta(nomeCarta, marcada);
        }
    }
 
    private Carta lerCartaObrigatoria(Properties propriedades, String chave) {
        String textoCarta = lerTextoObrigatorio(propriedades, chave);
        return criarCartaDeTexto(textoCarta);
    }
 
    private Carta criarCartaDeTexto(String textoCarta) {
        int separador = textoCarta.indexOf(":");
 
        if (separador <= 0 || separador == textoCarta.length() - 1) {
            throw new IllegalArgumentException("Carta invalida: " + textoCarta + ". Use TIPO:Nome da Carta.");
        }
 
        String textoTipo = textoCarta.substring(0, separador).trim();
        String nome = textoCarta.substring(separador + 1).trim();
        TipoCarta tipo = TipoCarta.valueOf(textoTipo);
 
        return new Carta(nome, tipo);
    }
 
    private int lerInteiroObrigatorio(Properties propriedades, String chave) {
        String texto = lerTextoObrigatorio(propriedades, chave);
 
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inteiro invalido em " + chave + ": " + texto);
        }
    }
 
    private String lerTextoObrigatorio(Properties propriedades, String chave) {
        String valor = propriedades.getProperty(chave);
 
        if (valor == null || valor.trim().length() == 0) {
            throw new IllegalArgumentException("Campo obrigatorio ausente no arquivo: " + chave);
        }
        return valor.trim();
    }
 
    private int encontrarIndicePorNome(List<Jogador> jogadores, String nome) {
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).getNome().equals(nome)) {
                return i;
            }
        }
        return -1;
    }
 
    // =========================================================
    // SALVAMENTO
    // =========================================================
 
    public boolean salvarEstado(File arquivo) {
        try {
            StringBuilder conteudo = new StringBuilder();
 
            conteudo.append("formato=CLUE_SAVE_V1\n");
            conteudo.append("jogadorAtual=")
                    .append(escaparAscii(getNomeJogadorAtual()))
                    .append("\n");
 
            for (int i = 0; i < envelopeConfidencial.size(); i++) {
                Carta carta = envelopeConfidencial.get(i);
                conteudo.append("envelope.").append(i).append("=")
                        .append(carta.getTipo().name()).append(":")
                        .append(escaparAscii(carta.getNome()))
                        .append("\n");
            }
 
            conteudo.append("jogador.quantidade=")
                    .append(jogadoresEmOrdemDaEsquerda.size())
                    .append("\n");
 
            conteudo.append("\n");
 
            for (int i = 0; i < jogadoresEmOrdemDaEsquerda.size(); i++) {
                Jogador jogador = jogadoresEmOrdemDaEsquerda.get(i);
                String prefixo = "jogador." + i + ".";
 
                String posicao = (jogador.getCasaAtual() != null)
                        ? jogador.getCasaAtual().getNome()
                        : "null";
 
                conteudo.append(prefixo).append("nome=")
                        .append(escaparAscii(jogador.getNome())).append("\n");
 
                conteudo.append(prefixo).append("posicao=")
                        .append(escaparAscii(posicao)).append("\n");
 
                conteudo.append(prefixo).append("eliminado=")
                        .append(jogador.estaEliminado()).append("\n");
 
                conteudo.append(prefixo).append("cartas=")
                        .append(montarTextoCartas(jogador)).append("\n");
 
                conteudo.append(prefixo).append("notas=")
                        .append(montarTextoNotas(jogador)).append("\n");
 
                conteudo.append("\n");
            }
 
            Writer escritor = new OutputStreamWriter(
                    new FileOutputStream(arquivo), "US-ASCII");
            try {
                escritor.write(conteudo.toString());
            } finally {
                escritor.close();
            }
 
            return true;
 
        } catch (IOException ex) {
            return false;
        }
    }
 
    private String montarTextoCartas(Jogador jogador) {
        StringBuilder texto = new StringBuilder();
 
        List<Carta> cartas = jogador.getCartas();
 
        for (int i = 0; i < cartas.size(); i++) {
            if (i > 0) {
                texto.append("|");
            }
 
            Carta carta = cartas.get(i);
            texto.append(carta.getTipo().name())
                 .append(":")
                 .append(escaparAscii(carta.getNome()));
        }
        return texto.toString();
    }
 
    private String montarTextoNotas(Jogador jogador) {
        StringBuilder texto = new StringBuilder();
 
        FolhaNotas folha = jogador.getFolhaNotas();
 
        List<Carta> todasAsCartas = new ArrayList<Carta>();
        todasAsCartas.addAll(folha.getSuspeitos());
        todasAsCartas.addAll(folha.getComodos());
        todasAsCartas.addAll(folha.getArmas());
 
        for (int i = 0; i < todasAsCartas.size(); i++) {
            if (i > 0) {
                texto.append("|");
            }
 
            String nomeCarta = todasAsCartas.get(i).getNome();
            boolean marcada = folha.cartaEstaMarcada(nomeCarta);
 
            texto.append(escaparAscii(nomeCarta))
                 .append(":")
                 .append(marcada);
        }
        return texto.toString();
    }
 
    /**
     * Escapa qualquer caractere fora do ASCII de 7 bits para o
     * formato \\uXXXX, que Properties.load() decodifica de volta
     * ao caractere original automaticamente
     * (ex.: "Castiçal" -> "Casti\u00e7al").
     */
    private String escaparAscii(String texto) {
        if (texto == null) {
            return "null";
        }
 
        StringBuilder resultado = new StringBuilder();
 
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
 
            if (c < 128) {
                resultado.append(c);
            } else {
                resultado.append(String.format("\\u%04x", (int) c));
            }
        }
        return resultado.toString();
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
    
    public boolean jogadorEstaAtivo(String nome) {
        Jogador jogador = jogadoresPorNome.get(nome);

        return jogador != null && !jogador.estaEliminado();
    }
    public String getCartaRefutada() {
        return cartaRefutada;
    }

    public String getJogadorQueRefutou() {
        return jogadorQueRefutou;
    }
}