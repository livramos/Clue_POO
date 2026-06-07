package controller;

import model.ClueFacade;
import view.JanelaAcusacao;
import view.JanelaPalpite;
import view.PainelLateral;
import view.PainelTabuleiro;

public class ClueController {

    // =========================================================
    // Estados do jogo
    // =========================================================

    public enum EstadoJogo {
        INICIO_TURNO,
        AGUARDANDO_MOVIMENTO,
        APOS_MOVIMENTO_CORREDOR,
        APOS_MOVIMENTO_COMODO,
        FIM_DE_TURNO,
        JOGO_ENCERRADO
    }

    // =========================================================
    // Singleton
    // =========================================================

    private static ClueController instancia;

    public static ClueController getInstancia() {
        if (instancia == null) {
            instancia = new ClueController();
        }
        return instancia;
    }

    // =========================================================
    // Atributos
    // =========================================================

    private EstadoJogo estadoAtual;
    private final ClueFacade facade;
    private PainelLateral painelLateral;
    private PainelTabuleiro painelTabuleiro;

    private ClueController() {
        this.facade      = ClueFacade.getInstancia();
        this.estadoAtual = EstadoJogo.INICIO_TURNO;
    }

    // =========================================================
    // Injecao de dependencia dos paineis
    // =========================================================

    public void setPainelLateral(PainelLateral painelLateral) {
        this.painelLateral = painelLateral;
    }

    public void setPainelTabuleiro(PainelTabuleiro painelTabuleiro) {
        this.painelTabuleiro = painelTabuleiro;
    }

    public EstadoJogo getEstadoAtual() {
        return estadoAtual;
    }

    // =========================================================
    // Inicializacao de turno
    // =========================================================

    public void iniciarTurno() {
        estadoAtual = EstadoJogo.INICIO_TURNO;

        if (painelLateral != null) {
            painelLateral.exibirDados(1, 1);
            painelLateral.atualizarJogadorDaVez(facade.getJogadorAtual());
        }

        atualizarBotoes();
    }

    // =========================================================
    // Eventos disparados pela view
    // =========================================================

    /** Jogar dados de forma aleatoria. */
    public void onLancarDados() {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) return;

        int[] resultado = facade.lancarDados();
        // Observer (DADOS_LANCADOS) ja destaca as casas alcancaveis no PainelTabuleiro
        processarLancamento(resultado);
    }

    /**
     * Jogar dados com valores definidos manualmente.
     * Usa lancarDadosComValores() para que o Observer seja notificado igual ao dado normal.
     */
    public void onLancarDadosComValores(int d1, int d2) {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) return;

        int[] resultado = facade.lancarDadosComValores(d1, d2);
        // Observer (DADOS_LANCADOS) ja destaca as casas alcancaveis no PainelTabuleiro
        processarLancamento(resultado);
    }

    /** Jogador clicou em uma casa destino no tabuleiro. */
    public void onJogadorMoveu(String destino) {
        if (estadoAtual != EstadoJogo.AGUARDANDO_MOVIMENTO) return;

        try {
            facade.moverJogadorAtual(destino);
            // Observer (PEAO_MOVIDO) ja atualiza o piao no PainelTabuleiro

            if (painelTabuleiro != null) {
                painelTabuleiro.limparDestaques();
            }

            if (destino.startsWith("COMODO_")) {
                estadoAtual = EstadoJogo.APOS_MOVIMENTO_COMODO;
            } else {
                estadoAtual = EstadoJogo.APOS_MOVIMENTO_CORREDOR;
            }

            atualizarBotoes();

        } catch (IllegalArgumentException ex) {
            // Destino invalido — permanece em AGUARDANDO_MOVIMENTO
        }
    }

    /** Jogador optou por usar a passagem secreta. */
    public void onUsarPassagemSecreta() {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) return;

        try {
            facade.usarPassagemSecretaJogadorDaVez();
            // Observer (PEAO_MOVIDO) ja atualiza o piao no PainelTabuleiro

            if (painelTabuleiro != null) {
                painelTabuleiro.limparDestaques();
            }

            estadoAtual = EstadoJogo.APOS_MOVIMENTO_COMODO;
            atualizarBotoes();

        } catch (IllegalArgumentException ex) {
            // Passagem indisponivel
        }
    }

    /** Jogador clicou em "Proximo" para encerrar o turno. */
    public void onPassarTurno() {
        boolean podePassar =
                estadoAtual == EstadoJogo.FIM_DE_TURNO
                || estadoAtual == EstadoJogo.APOS_MOVIMENTO_CORREDOR
                || estadoAtual == EstadoJogo.APOS_MOVIMENTO_COMODO;

        if (!podePassar) return;

        facade.passarTurno();
        // Observer (TURNO_ALTERADO) ja limpa destaques no PainelTabuleiro

        iniciarTurno();
    }

    /** Jogador quer fazer um palpite (so disponivel em APOS_MOVIMENTO_COMODO). */
    public void onFazerPalpite() {
        if (estadoAtual != EstadoJogo.APOS_MOVIMENTO_COMODO) return;

        JanelaPalpite janela = new JanelaPalpite(painelTabuleiro);
        janela.setVisible(true);
        // O estado transita para FIM_DE_TURNO via callback onPalpiteConcluido()
    }

    /** Jogador quer fazer uma acusacao (disponivel em comodo ou corredor). */
    public void onFazerAcusacao() {
        JanelaAcusacao janela = new JanelaAcusacao();
        janela.setVisible(true);
        // O estado transita via callback onAcusacaoConcluida(correta)
    }

    // =========================================================
    // Callbacks chamados pelas janelas apos confirmacao
    // =========================================================

    /** Chamado por JanelaPalpite apos o palpite ser confirmado. */
    public void onPalpiteConcluido() {
        estadoAtual = EstadoJogo.FIM_DE_TURNO;
        atualizarBotoes();
    }

    /** Chamado por JanelaAcusacao apos a acusacao ser confirmada. */
    public void onAcusacaoConcluida(boolean correta) {
        if (correta) {
            estadoAtual = EstadoJogo.JOGO_ENCERRADO;
        } else {
            estadoAtual = EstadoJogo.FIM_DE_TURNO;
        }
        atualizarBotoes();
    }

    // =========================================================
    // Atualizacao centralizada dos botoes
    // =========================================================

    private void atualizarBotoes() {
        if (painelLateral == null) return;

        boolean temPassagem = facade.jogadorDaVezTemPassagemSecreta();

        switch (estadoAtual) {

            case INICIO_TURNO:
                painelLateral.setBotaoJogarDadosHabilitado(true);
                painelLateral.setBotaoEscolherDadosHabilitado(true);
                painelLateral.setBotaoPassagemSecretaHabilitado(temPassagem);
                painelLateral.setBotaoProximoHabilitado(false);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(true);
                break;

            case AGUARDANDO_MOVIMENTO:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(false);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(false);
                break;

            case APOS_MOVIMENTO_COMODO:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(true);
                painelLateral.setBotaoPalpiteHabilitado(true);
                painelLateral.setBotaoAcusarHabilitado(true);
                break;

            case APOS_MOVIMENTO_CORREDOR:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(true);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(true);
                break;

            case FIM_DE_TURNO:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(true);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(false);
                break;

            case JOGO_ENCERRADO:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(false);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(false);
                break;
        }

        painelLateral.repaint();
    }

    // =========================================================
    // Processamento interno
    // =========================================================

    private void processarLancamento(int[] dados) {
        estadoAtual = EstadoJogo.AGUARDANDO_MOVIMENTO;

        if (painelLateral != null) {
            painelLateral.exibirDados(dados[0], dados[1]);
        }
        // Destaque das casas alcancaveis e tratado pelo Observer (DADOS_LANCADOS)
        // via PainelTabuleiro.notify()

        atualizarBotoes();
    }
}
