package model;
 
import java.util.List;
 
// Ponte que liga model a view

public class ClueFacade {
 
    private static ClueFacade instancia;
    private ClueModel modelo;
 
    private ClueFacade() {
        this.modelo = new ClueModel();
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
        modelo.adicionarCasaAoTabuleiro(nome);
    }
 
    public void conectarCasas(String a, String b) {
        modelo.conectarCasas(a, b);
    }
 
    public void posicionarJogadorAtual(String nomeCasa) {
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
        return modelo.mapearCasas(casaAtual, dados);
    }
 
    public void moverJogadorAtual(String casaDestino) {
        modelo.deslocarPiaoDaVez(casaDestino);
    }
 
    public String getCasaAtualDoJogador() {
        return modelo.getNomeCasaAtualDoJogadorDaVez();
    }
}