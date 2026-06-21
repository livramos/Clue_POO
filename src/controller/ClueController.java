package controller;

import model.ClueFacade;
import view.JanelaAcusacao;
import view.JanelaPalpite;
import view.PainelLateral;
import view.PainelTabuleiro;

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


    public void onLancarDados() {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) return;

        int[] resultado = facade.lancarDados();
        // Observer (DADOS_LANCADOS) ja destaca as casas alcancaveis no PainelTabuleiro
        processarLancamento(resultado);
    }

 
     
    public void onLancarDadosComValores(int d1, int d2) {
        if (estadoAtual != EstadoJogo.INICIO_TURNO) return;

        int[] resultado = facade.lancarDadosComValores(d1, d2);
       
        processarLancamento(resultado);
    }

  
    public void onJogadorMoveu(String destino) {
        if (estadoAtual != EstadoJogo.AGUARDANDO_MOVIMENTO) return;

        try {
            facade.moverJogadorAtual(destino);
            

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
           
        }
    }

    
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

    
    public void onPassarTurno() {
        boolean podePassar =
                estadoAtual == EstadoJogo.FIM_DE_TURNO
                || estadoAtual == EstadoJogo.APOS_MOVIMENTO_CORREDOR
                || estadoAtual == EstadoJogo.APOS_MOVIMENTO_COMODO;

        if (!podePassar) return;

        facade.passarTurno();
        

        iniciarTurno();
    }

    public void onFazerPalpite() {
        if (estadoAtual != EstadoJogo.APOS_MOVIMENTO_COMODO) return;

        JanelaPalpite janela = new JanelaPalpite(painelTabuleiro);
        janela.setVisible(true);
       
    }

    
    public void onFazerAcusacao() {
        JanelaAcusacao janela = new JanelaAcusacao();
        janela.setVisible(true);

    }

 
    public void onPalpiteConcluido() {
        estadoAtual = EstadoJogo.FIM_DE_TURNO;
        atualizarBotoes();
    }

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
        int opcao = javax.swing.JOptionPane.showConfirmDialog(
            null,
            "O vencedor foi: " + vencedor + "!\nDeseja iniciar uma nova partida?",
            "Fim de Jogo",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (opcao == javax.swing.JOptionPane.YES_OPTION) {
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
