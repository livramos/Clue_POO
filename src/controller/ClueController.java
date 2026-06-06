package controller;

import java.util.List;

import model.ClueFacade;
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
        this.facade = ClueFacade.getInstancia();
        this.estadoAtual = EstadoJogo.INICIO_TURNO;
    }

    // =========================================================
    // Injeção de dependência dos painéis
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
    // Inicialização de turno
    // =========================================================

    /**
     * Deve ser chamado uma vez após os painéis serem configurados,
     * e novamente a cada troca de turno (via onPassarTurno).
     */
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

    /** Jogar dados de forma aleatória. */
    public void onLancarDados() {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) {
            return;
        }

        int[] resultado = facade.lancarDados();
        processarLancamento(resultado);
    }

    /** Jogar dados com valores definidos pelo testador. */
    public void onLancarDadosComValores(int d1, int d2) {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) {
            return;
        }

        int[] resultado = new int[]{ d1, d2 };
        processarLancamento(resultado);
    }

    /** Jogador clicou em uma casa destino no tabuleiro. */
    public void onJogadorMoveu(String destino) {
        if (estadoAtual != EstadoJogo.AGUARDANDO_MOVIMENTO) {
            return;
        }

        try {
            String jogadorAtual = facade.getJogadorAtual();

            facade.moverJogadorAtual(destino);

            if (painelTabuleiro != null) {
                painelTabuleiro.moverPiao(jogadorAtual, destino);
                painelTabuleiro.limparDestaques();
            }

            if (destino.startsWith("COMODO_")) {
                estadoAtual = EstadoJogo.APOS_MOVIMENTO_COMODO;
            } else {
                estadoAtual = EstadoJogo.APOS_MOVIMENTO_CORREDOR;
            }

            atualizarBotoes();

        } catch (IllegalArgumentException ex) {
            // Destino inválido — permanece em AGUARDANDO_MOVIMENTO
        }
    }

    /** Jogador optou por usar a passagem secreta em vez de rolar os dados. */
    public void onUsarPassagemSecreta() {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) {
            return;
        }

        try {
            String jogador = facade.getJogadorAtual();

            facade.usarPassagemSecretaJogadorDaVez();

            String destino = facade.getCasaAtualDoJogador();

            if (painelTabuleiro != null) {
                painelTabuleiro.moverPiao(jogador, destino);
                painelTabuleiro.limparDestaques();
            }

            estadoAtual = EstadoJogo.APOS_MOVIMENTO_COMODO;
            atualizarBotoes();

        } catch (IllegalArgumentException ex) {
            // Passagem indisponível — não faz nada
        }
    }

    /** Jogador clicou em "Próximo" para encerrar o turno. */
    public void onPassarTurno() {
        boolean podePassar =
                estadoAtual == EstadoJogo.FIM_DE_TURNO
                || estadoAtual == EstadoJogo.APOS_MOVIMENTO_CORREDOR
                || estadoAtual == EstadoJogo.APOS_MOVIMENTO_COMODO;

        if (!podePassar) {
            return;
        }

        facade.passarTurno();

        if (painelTabuleiro != null) {
            painelTabuleiro.limparDestaques();
        }

        iniciarTurno();
    }

    /**
     * Jogador quer fazer um palpite.
     * Só disponível quando está em um cômodo (APOS_MOVIMENTO_COMODO).
     * TODO: abrir JanelaPalpite quando implementada.
     */
    public void onFazerPalpite() {
        if (estadoAtual != EstadoJogo.APOS_MOVIMENTO_COMODO) {
            return;
        }

        // TODO: abrir janela de palpite e aguardar resposta dos outros jogadores
        // Após o palpite ser concluído, transitar para FIM_DE_TURNO:
        estadoAtual = EstadoJogo.FIM_DE_TURNO;
        atualizarBotoes();
    }

    /**
     * Jogador quer fazer uma acusação.
     * Pode ser feita a qualquer momento do turno.
     * TODO: abrir JanelaAcusacao quando implementada.
     */
    public void onFazerAcusacao() {
        // TODO: abrir janela de acusação
        // Se correta  → JOGO_ENCERRADO
        // Se incorreta → FIM_DE_TURNO (jogador continua mas não pode mais vencer)
        estadoAtual = EstadoJogo.FIM_DE_TURNO;
        atualizarBotoes();
    }

    // =========================================================
    // Atualização centralizada dos botões
    // =========================================================

    private void atualizarBotoes() {
        if (painelLateral == null) {
            return;
        }

        boolean temPassagem = facade.jogadorDaVezTemPassagemSecreta();

        switch (estadoAtual) {

            case INICIO_TURNO:
                painelLateral.setBotaoJogarDadosHabilitado(true);
                painelLateral.setBotaoEscolherDadosHabilitado(true);
                painelLateral.setBotaoPassagemSecretaHabilitado(temPassagem);
                painelLateral.setBotaoProximoHabilitado(false);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(false);
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
                painelLateral.setBotaoPalpiteHabilitado(true);   // em cômodo: pode palpitar
                painelLateral.setBotaoAcusarHabilitado(true);
                break;

            case APOS_MOVIMENTO_CORREDOR:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(true);
                painelLateral.setBotaoPalpiteHabilitado(false);  // corredor: sem palpite
                painelLateral.setBotaoAcusarHabilitado(false);
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

        String casaAtual = facade.getCasaAtualDoJogador();

        if (casaAtual != null && painelTabuleiro != null) {
            List<String> alcancaveis = facade.getCasasAlcancaveis(casaAtual, dados);
            painelTabuleiro.destacarCasasAlcancaveis(alcancaveis);
        }

        atualizarBotoes();
    }
}