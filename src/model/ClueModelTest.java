package model;
import static org.junit.Assert.*;
import org.junit.Test;

public class ClueModelTest {
	@Test
    public void deveCriarEnvelopeComTresCartas() {
       ClueModel model = new ClueModel();

       model.prepararJogo(4);

       assertEquals(3, model.getQuantidadeCartasEnvelope());
    }

    @Test
    public void deveCriarQuantidadeCorretaDeJogadores() {
        ClueModel model = new ClueModel();

        model.prepararJogo(4);

        assertEquals(4, model.getQuantidadeJogadores());
    }

    @Test
    public void dadosDevemTerValoresEntreUmESeis() {
        ClueModel model = new ClueModel();

        int[] dados = model.lancarDados();

        assertTrue(dados[0] >= 1 && dados[0] <= 6);
        assertTrue(dados[1] >= 1 && dados[1] <= 6);
    }
    @Test
    public void prepararJogoDuasVezesNaoDeveAcumularJogadores() {
        ClueModel model = new ClueModel();

        model.prepararJogo(4);
        model.prepararJogo(3);

        assertEquals(3, model.getQuantidadeJogadores());
    }
    @Test
    public void deveDistribuirTodasAsCartasRestantes() {
        ClueModel model = new ClueModel();

        model.prepararJogo(4);

        int totalCartasDistribuidas = 0;

        for (int i = 0; i < model.getQuantidadeJogadores(); i++) {
            totalCartasDistribuidas += model.getQuantidadeCartasDoJogador(i);
        }

        assertEquals(18, totalCartasDistribuidas);
    }
}
