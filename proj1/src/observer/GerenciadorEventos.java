package observer;
 
import java.util.ArrayList;
import java.util.List;
 
public class GerenciadorEventos implements Observado {
 
    // Constantes de tipo de evento — usadas pelos Observadores no notify().
    public static final int PEAO_MOVIDO = 0;
    public static final int DADOS_LANCADOS = 1;
    public static final int TURNO_ALTERADO = 2;
    public static final int JANELA_CARTAS_ABERTA = 3;
    public static final int JANELA_NOTAS_ABERTA = 4;
    public static final int PALPITE_REALIZADO = 5;
    public static final int ACUSACAO_REALIZADA = 6;
    public static final int JOGADOR_ELIMINADO = 7;
 
    private final List<Observador> observadores = new ArrayList<Observador>();
 
    private int tipoEvento;
    private int dado1;
    private int dado2;
    private String jogador;
    private String casaDestino;
 
   

    @Override
    public void add(Observador o) {
        if (!observadores.contains(o)) {
            observadores.add(o);
        }
    }
 
    @Override
    public void remove(Observador o) {
        observadores.remove(o);
    }
 
    @Override
    public int get(int i) {
        switch (i) {
            case 0: return tipoEvento;
            case 1: return dado1;
            case 2: return dado2;
            default: throw new IllegalArgumentException("Índice inválido: " + i);
        }
    }
 
    public String getString(int i) {
        switch (i) {
            case 0: return jogador;
            case 1: return casaDestino;
            default: throw new IllegalArgumentException("Índice inválido: " + i);
        }
    }
 
    public void notificarPeaoMovido(String nomeJogador, String nomeCasa) {
        tipoEvento = PEAO_MOVIDO;
        jogador = nomeJogador;
        casaDestino = nomeCasa;
        notificar();
    }
 
    public void notificarDadosLancados(int d1, int d2) {
        tipoEvento = DADOS_LANCADOS;
        dado1 = d1;
        dado2 = d2;
        notificar();
    }
 
    public void notificarTurnoAlterado(String nomeJogador) {
        tipoEvento = TURNO_ALTERADO;
        jogador = nomeJogador;
        notificar();
    }
 
    private void notificar() {
        List<Observador> copia = new ArrayList<Observador>(observadores);
        for (Observador obs : copia) {
            obs.notify(this);
        }
    }
    
    public void notificarJanelaCartasAberta(String nomeJogador) {
        tipoEvento = JANELA_CARTAS_ABERTA;
        jogador    = nomeJogador;
        notificar();
    }

    public void notificarJanelaNotasAberta(String nomeJogador) {
        tipoEvento = JANELA_NOTAS_ABERTA;
        jogador    = nomeJogador;
        notificar();
    }

    public void notificarPalpiteRealizado(String resultado) {
        tipoEvento   = PALPITE_REALIZADO;
        casaDestino  = resultado; // reutiliza o campo String para o texto
        notificar();
    }

    public void notificarAcusacaoRealizada(String nomeJogador, boolean correta) {
        tipoEvento  = ACUSACAO_REALIZADA;
        jogador     = nomeJogador;
        dado1       = correta ? 1 : 0; // reutiliza dado1 como flag booleana
        notificar();
    }

    public void notificarJogadorEliminado(String nomeJogador) {
        tipoEvento = JOGADOR_ELIMINADO;
        jogador    = nomeJogador;
        notificar();
    }
}
