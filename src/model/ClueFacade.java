package model;
 
import java.io.File;
import java.io.IOException;
import java.util.List;
 
import observer.GerenciadorEventos;
import observer.Observado;
import observer.Observador;
import view.GradeTabuleiro;
 
public class ClueFacade implements Observado {
 
    private static ClueFacade instancia;
 
    private ClueFacade() {
        modelo      = new ClueModel();
        gerenciador = new GerenciadorEventos();
    }
 
    public static ClueFacade getInstancia() {
        if (instancia == null) {
            instancia = new ClueFacade();
        }
        return instancia;
    }
 
    private ClueModel modelo;
    private GerenciadorEventos gerenciador;
 
 
    @Override
    public void add(Observador o) {
        gerenciador.add(o);
    }
 
    @Override
    public void remove(Observador o) {
        gerenciador.remove(o);
    }
 
    @Override
    public int get(int i) {
        return gerenciador.get(i);
    }
 
    public String getString(int i) {
        return gerenciador.getString(i);
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
        gerenciador.notificarPeaoMovido(nomeJogador, nomeCasa);
    }
 
    public void posicionarJogadorAtual(String nomeCasa) {
        nomeCasa = GradeTabuleiro.normalizarNomeCasa(nomeCasa);
        modelo.posicionarPiaoDaVez(nomeCasa);
        gerenciador.notificarPeaoMovido(modelo.getNomeJogadorAtual(), nomeCasa);
    }
 
 
    public String getJogadorAtual() {
        return modelo.getNomeJogadorAtual();
    }
 
    public void passarTurno() {
        modelo.passarTurno();
        gerenciador.notificarTurnoAlterado(modelo.getNomeJogadorAtual());
    }
 
 
/* Carrega */
    public void carregarJogo(File arquivo) throws IOException {
        modelo.carregarEstadoDeArquivo(arquivo);
 
        for (String jogador : modelo.getNomesJogadoresEmOrdem()) {
            String casa = modelo.getNomeCasaAtualDoJogador(jogador);
            if (casa != null) {
                gerenciador.notificarPeaoMovido(jogador, casa);
            }
        }
 
        gerenciador.notificarTurnoAlterado(modelo.getNomeJogadorAtual());
    }
 
/* Salvamento */
    public boolean salvarPartida(File arquivo) {
        return modelo.salvarEstado(arquivo);
    }
 
 
    public int[] lancarDados() {
        int[] resultado = modelo.lancarDados();
        gerenciador.notificarDadosLancados(resultado[0], resultado[1]);
        return resultado;
    }
 
    /**
     * Versao com valores manuais (modo teste/escolha de dados).
     * Dispara a mesma notificacao DADOS_LANCADOS para manter o Observer informado.
     */
    public int[] lancarDadosComValores(int d1, int d2) {
        gerenciador.notificarDadosLancados(d1, d2);
        return new int[]{d1, d2};
    }
 
 
    public List<String> getCasasAlcancaveis(String casaAtual, int[] dados) {
        casaAtual = GradeTabuleiro.normalizarNomeCasa(casaAtual);
        return modelo.mapearCasas(casaAtual, dados);
    }
 
    public void moverJogadorAtual(String casaDestino) {
        casaDestino = GradeTabuleiro.normalizarNomeCasa(casaDestino);

        /*
         * Primeiro move para a casa clicada, tipo L17C6.
         * Isso mantém a validação normal das casas alcançáveis.
         */
        modelo.deslocarPiaoDaVez(casaDestino);

        /*
         * Se a casa clicada é uma porta, a posição lógica final
         * do jogador deve virar o cômodo correspondente.
         */
        String comodoDestino = getComodoDaPorta(casaDestino);

        if (comodoDestino != null) {
            modelo.posicionarPiaoDaVez(comodoDestino);
            gerenciador.notificarPeaoMovido(modelo.getNomeJogadorAtual(), comodoDestino);
        } else {
            gerenciador.notificarPeaoMovido(modelo.getNomeJogadorAtual(), casaDestino);
        }
    }
    
    private String getComodoDaPorta(String nomeCasa) {
        nomeCasa = GradeTabuleiro.normalizarNomeCasa(nomeCasa);

        if ("L6C4".equals(nomeCasa)) {
            return "COMODO_Cozinha";
        }

        if ("L4C16".equals(nomeCasa)
                || "L7C14".equals(nomeCasa)
                || "L7C9".equals(nomeCasa)
                || "L4C7".equals(nomeCasa)) {
            return "COMODO_Sala de Musica";
        }

        if ("L4C18".equals(nomeCasa)) {
            return "COMODO_Jardim de Inverno";
        }

        if ("L11C8".equals(nomeCasa)
                || "L15C16".equals(nomeCasa)
                || "L15C6".equals(nomeCasa)) {
            return "COMODO_Sala de Jantar";
        }

        if ("L8C17".equals(nomeCasa)
                || "L12C22".equals(nomeCasa)) {
            return "COMODO_Salao de Jogos";
        }

        if ("L12C20".equals(nomeCasa)) {
            return "COMODO_Biblioteca";
        }

        if ("L17C6".equals(nomeCasa)) {
            return "COMODO_Sala de Estar";
        }

        if ("L16C11".equals(nomeCasa)
                || "L16C12".equals(nomeCasa)) {
            return "COMODO_Entrada";
        }

        if ("L19C17".equals(nomeCasa)) {
            return "COMODO_Escritorio";
        }

        return null;
    }
    public String getCasaAtualDoJogador() {
        String casaAtual = modelo.getNomeCasaAtualDoJogadorDaVez();
        return GradeTabuleiro.normalizarNomeCasa(casaAtual);
    }
 
 
    public void adicionarPassagemSecreta(String origem, String destino) {
        origem  = GradeTabuleiro.normalizarNomeCasa(origem);
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
        modelo.usarPassagemSecretaJogadorDaVez(); // lança exceção se inválido
        String destino = modelo.getNomeCasaAtualDoJogadorDaVez();
        gerenciador.notificarPeaoMovido(modelo.getNomeJogadorAtual(), destino);
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
        String resultado = modelo.realizarPalpite(suspeito, arma);

        String comodoAtual = modelo.getComodoAtualJogadorDaVez();

        if (comodoAtual != null && modelo.jogadorEstaAtivo(suspeito)) {
            gerenciador.notificarPeaoMovido(suspeito, "COMODO_" + comodoAtual);
        }

        gerenciador.notificarPalpiteRealizado(resultado);

        return resultado;
    }
 
    public boolean realizarAcusacao(String suspeito, String arma, String comodo) {
        String jogadorAtual = modelo.getNomeJogadorAtual();
 
        boolean correta = modelo.realizarAcusacao(suspeito, arma, comodo);
 
        gerenciador.notificarAcusacaoRealizada(jogadorAtual, correta);
 
        if (!correta) {
            gerenciador.notificarJogadorEliminado(jogadorAtual);
        }
 
        return correta;
    }
 
 
    public void notificarCartasExibidas() {
        gerenciador.notificarJanelaCartasAberta(modelo.getNomeJogadorAtual());
    }
 
    public void notificarNotasExibidas() {
        gerenciador.notificarJanelaNotasAberta(modelo.getNomeJogadorAtual());
    }
 
    public int getQuantidadeJogadoresAtivos() {
        return modelo.getQuantidadeJogadoresAtivos();
    }
 
    public String getNomeUnicoSobrevivente() {
        return modelo.getNomeUnicoSobrevivente();
    }
 
    public void resetar() {
        modelo = new ClueModel();
    }
    public String getCartaRefutada() {
        return modelo.getCartaRefutada();
    }

    public String getJogadorQueRefutou() {
        return modelo.getJogadorQueRefutou();
    }
}