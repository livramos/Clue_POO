package controller;
 
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.ClueFacade;
import view.JanelaAcusacao;
import view.JanelaPalpite;
import view.PainelLateral;
import view.PainelTabuleiro;
import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import view.JanelaTabuleiro;
 
public class ClueController {
 
    public enum EstadoJogo {
        INICIO_TURNO,
        AGUARDANDO_MOVIMENTO,
        APOS_MOVIMENTO_CORREDOR,
        APOS_MOVIMENTO_COMODO,
        FIM_DE_TURNO,
        JOGO_ENCERRADO
    }
 
    private static ClueController instancia;
 
    public static ClueController getInstancia() {
        if (instancia == null) {
            instancia = new ClueController();
        }
        return instancia;
    }
 
    private EstadoJogo estadoAtual;
    private final ClueFacade facade;
    private PainelLateral painelLateral;
    private PainelTabuleiro painelTabuleiro;
 
    private ClueController() {
        this.facade      = ClueFacade.getInstancia();
        this.estadoAtual = EstadoJogo.INICIO_TURNO;
    }
 
 
    public void setPainelLateral(PainelLateral painelLateral) {
        this.painelLateral = painelLateral;
    }
 
    public void setPainelTabuleiro(PainelTabuleiro painelTabuleiro) {
        this.painelTabuleiro = painelTabuleiro;
    }
 
    public EstadoJogo getEstadoAtual() {
        return estadoAtual;
    }
 
 
    public void iniciarTurno() {
        estadoAtual = EstadoJogo.INICIO_TURNO;
 
        if (painelLateral != null) {
            painelLateral.exibirDados(1, 1);
            painelLateral.atualizarJogadorDaVez(facade.getJogadorAtual());
        }
 
        atualizarBotoes();
    }
 
 
    /* Jogar dados de forma aleatoria. */
    public void onLancarDados() {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) return;
 
        int[] resultado = facade.lancarDados();
        // Observer (DADOS_LANCADOS) ja destaca as casas alcancaveis no PainelTabuleiro
        processarLancamento(resultado);
    }
 
 
    /* Jogar dados com valores definidos manualmente. */
    public void onLancarDadosComValores(int d1, int d2) {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) return;
 
        int[] resultado = facade.lancarDadosComValores(d1, d2);
 
        processarLancamento(resultado);
    }
 
 
    /** Jogador clicou em uma casa destino no tabuleiro. */
    public void onJogadorMoveu(String destino) {
        if (estadoAtual != EstadoJogo.AGUARDANDO_MOVIMENTO) return;
 
        try {
            facade.moverJogadorAtual(destino);
 
            if (painelTabuleiro != null) {
                painelTabuleiro.limparDestaques();
            }
 
            String casaFinal = facade.getCasaAtualDoJogador();

            if (casaFinal != null && casaFinal.startsWith("COMODO_")) {
                estadoAtual = EstadoJogo.APOS_MOVIMENTO_COMODO;
            } else {
                estadoAtual = EstadoJogo.APOS_MOVIMENTO_CORREDOR;
            }
 
            atualizarBotoes();
 
        } catch (IllegalArgumentException ex) {
 
        }
    }
 
 
    /** Jogador optou por usar a passagem secreta. */
    public void onUsarPassagemSecreta() {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) return;
 
        try {
            facade.usarPassagemSecretaJogadorDaVez();
 
            if (painelTabuleiro != null) {
                painelTabuleiro.limparDestaques();
            }
 
            estadoAtual = EstadoJogo.APOS_MOVIMENTO_COMODO;
            atualizarBotoes();
 
        } catch (IllegalArgumentException ex) {
 
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
 
        iniciarTurno();
    }
 
 
    /** Jogador quer carregar uma partida salva em arquivo .txt ASCII. */
    /** Jogador quer carregar uma partida salva em arquivo .txt ASCII. */
    public boolean onCarregarJogo(Component componentePai) {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Carregar partida");
        seletor.setFileFilter(new FileNameExtensionFilter("Arquivo texto (*.txt)", "txt"));

        int resposta = seletor.showOpenDialog(componentePai);
        if (resposta != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        File arquivo = seletor.getSelectedFile();

        try {
            facade.carregarJogo(arquivo);

            // Se o jogo foi carregado a partir do menu inicial,
            // ainda não existe tabuleiro aberto.
            if (painelTabuleiro == null) {
                JanelaTabuleiro janelaTabuleiro = new JanelaTabuleiro();
                janelaTabuleiro.setVisible(true);
            }

            if (painelTabuleiro != null) {
                painelTabuleiro.limparDestaques();
            }

            iniciarTurno();

            JOptionPane.showMessageDialog(
                    componentePai,
                    "Partida carregada com sucesso.",
                    "Carregar partida",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return true;

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    componentePai,
                    "Nao foi possivel ler o arquivo:\n" + ex.getMessage(),
                    "Erro ao carregar partida",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    componentePai,
                    "Arquivo de partida invalido:\n" + ex.getMessage(),
                    "Erro ao carregar partida",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
 
 
    /** Jogador quer fazer um palpite (so disponivel em APOS_MOVIMENTO_COMODO). */
    public void onFazerPalpite() {
        if (estadoAtual != EstadoJogo.APOS_MOVIMENTO_COMODO) return;
 
        JanelaPalpite janela = new JanelaPalpite(painelTabuleiro);
        janela.setVisible(true);
 
    }
 
 
    /** Jogador quer fazer uma acusacao (disponivel em comodo ou corredor). */
    public void onFazerAcusacao() {
        JanelaAcusacao janela = new JanelaAcusacao();
        janela.setVisible(true);
 
    }
 
 
    /** Chamado por JanelaPalpite apos o palpite ser confirmado. */
    public void onPalpiteConcluido() {
        estadoAtual = EstadoJogo.FIM_DE_TURNO;
        atualizarBotoes();
    }
 
    /** Chamado por JanelaAcusacao apos a acusacao ser confirmada. */
    public void onAcusacaoConcluida(boolean correta) {
        if (correta) {
            String vencedor = facade.getJogadorAtual();
            estadoAtual = EstadoJogo.JOGO_ENCERRADO;
            atualizarBotoes();
            finalizarPartida(vencedor);
        } else {
            if (facade.getQuantidadeJogadoresAtivos() <= 1) {
                estadoAtual = EstadoJogo.JOGO_ENCERRADO;
                atualizarBotoes();
                finalizarPartida(facade.getNomeUnicoSobrevivente());
            } else {
                estadoAtual = EstadoJogo.FIM_DE_TURNO;
                atualizarBotoes();
            }
        }
    }
 
    private void finalizarPartida(String vencedor) {
        int opcao = JOptionPane.showConfirmDialog(
            null,
            "O vencedor foi: " + vencedor + "!\nDeseja iniciar uma nova partida?",
            "Fim de Jogo",
            JOptionPane.YES_NO_OPTION
        );
 
        if (opcao == JOptionPane.YES_OPTION) {
            reiniciarPartida();
 
        } else {
            System.exit(0);
        }
    }
 
    private void reiniciarPartida() {
 
        for (java.awt.Window w : java.awt.Window.getWindows()) {
            w.dispose();
        }
 
        facade.resetar();
        estadoAtual = EstadoJogo.INICIO_TURNO;
        painelLateral   = null;
        painelTabuleiro = null;
 
 
        view.janelaSelecao escolha = new view.janelaSelecao();
        escolha.setVisible(true);
    }
 
 
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
                painelLateral.setBotaoSalvarHabilitado(true);   // <-- so aqui true
                break;
 
            case AGUARDANDO_MOVIMENTO:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(false);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(false);
                painelLateral.setBotaoSalvarHabilitado(false);
                break;
 
            case APOS_MOVIMENTO_COMODO:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(true);
                painelLateral.setBotaoPalpiteHabilitado(true);
                painelLateral.setBotaoAcusarHabilitado(true);
                painelLateral.setBotaoSalvarHabilitado(false);
                break;
 
            case APOS_MOVIMENTO_CORREDOR:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(true);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(true);
                painelLateral.setBotaoSalvarHabilitado(false);
                break;
 
            case FIM_DE_TURNO:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(true);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(false);
                painelLateral.setBotaoSalvarHabilitado(false);
                break;
 
            case JOGO_ENCERRADO:
                painelLateral.setBotaoJogarDadosHabilitado(false);
                painelLateral.setBotaoEscolherDadosHabilitado(false);
                painelLateral.setBotaoPassagemSecretaHabilitado(false);
                painelLateral.setBotaoProximoHabilitado(false);
                painelLateral.setBotaoPalpiteHabilitado(false);
                painelLateral.setBotaoAcusarHabilitado(false);
                painelLateral.setBotaoSalvarHabilitado(false);
                break;
        }
 
        painelLateral.repaint();
    }
 
 
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
