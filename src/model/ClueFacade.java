package model;

import java.util.List;

import view.GradeTabuleiro;

public class ClueFacade {

    private static ClueFacade instancia;
    private ClueModel modelo;

    private ClueFacade() {
        modelo = new ClueModel();
    }

    public static ClueFacade getInstancia() {
        if (instancia == null) {
            instancia = new ClueFacade();
        }

        return instancia;
    }

    public void prepararJogo(List<String> nomes) {
        modelo.prepararJogo(nomes);
    }

    public void adicionarCasa(String nome) {
        nome = GradeTabuleiro.normalizarNomeCasa(nome);

        modelo.adicionarCasaAoTabuleiro(nome);
    }

    public void conectarCasas(String casa1, String casa2) {
        casa1 = GradeTabuleiro.normalizarNomeCasa(casa1);
        casa2 = GradeTabuleiro.normalizarNomeCasa(casa2);

        modelo.conectarCasas(casa1, casa2);
    }

    public void posicionarJogador(String nomeJogador, String nomeCasa) {
        nomeCasa = GradeTabuleiro.normalizarNomeCasa(nomeCasa);

        modelo.deslocarPiao(nomeJogador, nomeCasa);
    }

    public void posicionarJogadorAtual(String nomeCasa) {
        nomeCasa = GradeTabuleiro.normalizarNomeCasa(nomeCasa);

        modelo.posicionarPiaoDaVez(nomeCasa);
    }

    public String getJogadorAtual() {
        return modelo.getNomeJogadorAtual();
    }

    public void passarTurno() {
        modelo.passarTurno();
    }

    public int[] lancarDados() {
        return modelo.lancarDados();
    }

    public List<String> getCasasAlcancaveis(String casaAtual, int[] dados) {
        casaAtual = GradeTabuleiro.normalizarNomeCasa(casaAtual);

        return modelo.mapearCasas(casaAtual, dados);
    }

    public void moverJogadorAtual(String casaDestino) {
        casaDestino = GradeTabuleiro.normalizarNomeCasa(casaDestino);

        modelo.deslocarPiaoDaVez(casaDestino);
    }

    public String getCasaAtualDoJogador() {
        String casaAtual = modelo.getNomeCasaAtualDoJogadorDaVez();

        return GradeTabuleiro.normalizarNomeCasa(casaAtual);
    }

    public void adicionarPassagemSecreta(String origem, String destino) {
        origem = GradeTabuleiro.normalizarNomeCasa(origem);
        destino = GradeTabuleiro.normalizarNomeCasa(destino);

        modelo.adicionarPassagemSecreta(origem, destino);
    }

    public boolean jogadorDaVezTemPassagemSecreta() {
        return modelo.jogadorDaVezTemPassagemSecreta();
    }

    public String getDestinoPassagemSecretaDoJogadorDaVez() {
        String destino = modelo.getDestinoPassagemSecretaDoJogadorDaVez();

        return GradeTabuleiro.normalizarNomeCasa(destino);
    }

    public void usarPassagemSecretaJogadorDaVez() {
        modelo.usarPassagemSecretaJogadorDaVez();
    }
    
    public List<String> getCartasJogadorDaVez() {
        return modelo.getNomesCartasJogadorDaVez();
    }

    public List<String> getSuspeitos() {
        return modelo.getNomesSuspeitos();
    }

    public List<String> getArmas() {
        return modelo.getNomesArmas();
    }

    public List<String> getComodos() {
        return modelo.getNomesComodos();
    }

    public List<String> getSuspeitosFolhaJogadorDaVez() {
        return modelo.getNomesSuspeitosFolhaJogadorDaVez();
    }

    public List<String> getArmasFolhaJogadorDaVez() {
        return modelo.getNomesArmasFolhaJogadorDaVez();
    }

    public List<String> getComodosFolhaJogadorDaVez() {
        return modelo.getNomesComodosFolhaJogadorDaVez();
    }

    public boolean cartaEstaMarcadaNaFolhaJogadorDaVez(String nomeCarta) {
        return modelo.cartaEstaMarcadaNaFolhaJogadorDaVez(nomeCarta);
    }

    public void definirMarcacaoCartaNaFolhaJogadorDaVez(String nomeCarta, boolean marcada) {
        modelo.definirMarcacaoCartaNaFolhaJogadorDaVez(nomeCarta, marcada);
    }

    public boolean jogadorDaVezEstaEmComodo() {
        return modelo.jogadorDaVezEstaEmComodo();
    }

    public String getComodoAtualJogadorDaVez() {
        return modelo.getComodoAtualJogadorDaVez();
    }

    public String realizarPalpite(String suspeito, String arma) {
        return modelo.realizarPalpite(suspeito, arma);
    }

    public boolean realizarAcusacao(String suspeito, String arma, String comodo) {
        return modelo.realizarAcusacao(suspeito, arma, comodo);
    }
}