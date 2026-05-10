package model;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class clue_testes {


	@Test
	public void depoisDaScarletDeveJogarQuemEstaASuaEsquerda() {
		System.out.println("TESTE ESQUERDA SRTA SCARLET\n");
		
	    List<Jogador> jogadores = new ArrayList<>();

	    jogadores.add(new Jogador("Coronel Mostarda"));
	    jogadores.add(new Jogador("Dona Branca"));
	    jogadores.add(new Jogador("Srta. Scarlet"));
	    jogadores.add(new Jogador("Professor Black"));

	    Jogo jogo = new Jogo(jogadores);

        System.out.println("Jogador inicial: " + jogo.getJogadorAtual().getNome());

        assertEquals("Srta. Scarlet", jogo.getJogadorAtual().getNome());

        jogo.passarTurno();

        System.out.println("Depois de passar o turno: " + jogo.getJogadorAtual().getNome());

        assertEquals("Professor Black", jogo.getJogadorAtual().getNome());
        
        jogo.passarTurno();

        System.out.println("Depois de passar o turno: " + jogo.getJogadorAtual().getNome());
        

        assertEquals("Coronel Mostarda", jogo.getJogadorAtual().getNome());
        
        System.out.println("\n");
        System.out.println("*".repeat(50));
	}
	
	
	@Test
	public void todosJogadoresDevemReceberFolhaDeNotas() {
		System.out.println("JOGADORES DEVEM RECEBER NOTAS\n");
	    ClueModel model = new ClueModel();

	    List<String> jogadores = new ArrayList<String>();
	    jogadores.add("Coronel Mostarda");
	    jogadores.add("Dona Branca");
	    jogadores.add("Srta. Scarlet");
	    jogadores.add("Professor Black");

	    model.prepararJogo(jogadores);

	    for (int i = 0; i < model.getQuantidadeJogadores(); i++) {
	        FolhaNotas folha = model.getFolhaNotasDoJogador(i);
	        System.out.println("Jogador " + i + " recebeu folha de notas.");

	        System.out.println("Quantidade de suspeitos: " + folha.getSuspeitos().size());
	        System.out.println("Quantidade de armas: " + folha.getArmas().size());
	        System.out.println("Quantidade de cômodos: " + folha.getComodos().size());

	        assertNotNull(folha);
	        assertEquals(6, folha.getSuspeitos().size());
	        assertEquals(6, folha.getArmas().size());
	        assertEquals(9, folha.getComodos().size());
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }
	}

	    @Test
	    public void casaDeveAdicionarVizinha() {
	    	System.out.println("TESTE CASA DEVE ADICIONAR VIZINHA\n");
	    	
	        Casa cozinha = new Casa("Cozinha");
	        Casa corredor = new Casa("Corredor");

	        cozinha.adicionarCasaAdjacente(corredor);

	        System.out.println("Casa: " + cozinha.getNome());
	        System.out.println("Vizinha adicionada: " + corredor.getNome());

	        assertEquals(1, cozinha.getCasasAdjacentes().size());
	        assertTrue(cozinha.getCasasAdjacentes().contains(corredor));
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }

	    @Test
	    public void tabuleiroDeveAdicionarEBuscarCasaPeloNome() {
	    	System.out.println("TESTE TABULEIRO BUSCA CASA\n");
	    	
	        Tabuleiro tabuleiro = new Tabuleiro();

	        Casa cozinha = new Casa("Cozinha");

	        tabuleiro.adicionarCasa(cozinha);

	        System.out.println("Casa buscada: " + tabuleiro.getCasa("Cozinha").getNome());

	        assertEquals(cozinha, tabuleiro.getCasa("Cozinha"));
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }

	    @Test
	    public void tabuleiroDeveMapearCasasAlcancaveisComValorFixoDoDado() {
	    	System.out.println("TESTE CASAS ALCANÇAVEIS\n");
	    	
	        Tabuleiro tabuleiro = new Tabuleiro();

	        Casa casaA = new Casa("A");
	        Casa casaB = new Casa("B");
	        Casa casaC = new Casa("C");
	        Casa casaD = new Casa("D");

	        casaA.adicionarCasaAdjacente(casaB);
	        casaB.adicionarCasaAdjacente(casaA);

	        casaB.adicionarCasaAdjacente(casaC);
	        casaC.adicionarCasaAdjacente(casaB);

	        casaC.adicionarCasaAdjacente(casaD);
	        casaD.adicionarCasaAdjacente(casaC);

	        tabuleiro.adicionarCasa(casaA);
	        tabuleiro.adicionarCasa(casaB);
	        tabuleiro.adicionarCasa(casaC);
	        tabuleiro.adicionarCasa(casaD);

	        int valorDado = 2;

	        List<Casa> casasAlcancaveis = tabuleiro.mapearCasasAlcancaveis(casaA, valorDado);

	        System.out.println("Valor do dado usado no teste: " + valorDado);
	        System.out.println("Casas alcançáveis saindo da casa A:");

	        for (Casa casa : casasAlcancaveis) {
	            System.out.println(casa.getNome());
	        }

	        assertTrue(casasAlcancaveis.contains(casaB));
	        assertTrue(casasAlcancaveis.contains(casaC));
	        assertFalse(casasAlcancaveis.contains(casaD));

	        assertEquals(2, casasAlcancaveis.size());
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }
	    @Test
	    public void deveCriarEnvelopeComTresCartas() {
	    	System.out.println("TESTE ENVELOPE DEVE CONTER 3 CARTAS\n");
	        ClueModel model = new ClueModel();

	        List<String> jogadores = new ArrayList<String>();
	        jogadores.add("Coronel Mostarda");
	        jogadores.add("Dona Branca");
	        jogadores.add("Srta. Scarlet");
	        jogadores.add("Professor Black");

	        model.prepararJogo(jogadores);

	        System.out.println("Quantidade de cartas no envelope: " + model.getQuantidadeCartasEnvelope());

	        assertEquals(3, model.getQuantidadeCartasEnvelope());
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }

	    @Test
	    public void deveCriarQuantidadeCorretaDeJogadores() {
	    	System.out.println("TESTE QUANTIDADE DE JOGADORES IGUAL A 3\n");
	        ClueModel model = new ClueModel();

	        List<String> jogadores = new ArrayList<String>();
	        jogadores.add("Coronel Mostarda");
	        jogadores.add("Dona Branca");
	        jogadores.add("Srta. Scarlet");
	        jogadores.add("Professor Black");

	        model.prepararJogo(jogadores);

	        System.out.println("Quantidade de jogadores criados: " + model.getQuantidadeJogadores());

	        assertEquals(4, model.getQuantidadeJogadores());
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }

	    @Test
	    public void dadosDevemTerValoresEntreUmESeis() {
	    	System.out.println("TESTE DADO RETORNA 1 A 6\n");
	        ClueModel model = new ClueModel();

	        int[] dados = model.lancarDados();

	        System.out.println("Valor do dado 1: " + dados[0]);
	        System.out.println("Valor do dado 2: " + dados[1]);

	        assertTrue(dados[0] >= 1 && dados[0] <= 6);
	        assertTrue(dados[1] >= 1 && dados[1] <= 6);
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }
	    
	    @Test
	    public void prepararJogoDuasVezesNaoDeveAcumularJogadores() {
	    	System.out.println("TESTE LISTA QTD JOGADORES É LIMPA\n");
	        ClueModel model = new ClueModel();

	        List<String> jogadores1 = new ArrayList<String>();
	        jogadores1.add("Coronel Mostarda");
	        jogadores1.add("Dona Branca");
	        jogadores1.add("Srta. Scarlet");
	        jogadores1.add("Professor Black");

	        List<String> jogadores2 = new ArrayList<String>();
	        jogadores2.add("Coronel Mostarda");
	        jogadores2.add("Srta. Scarlet");
	        jogadores2.add("Professor Black");

	        model.prepararJogo(jogadores1);

	        System.out.println("Quantidade de jogadores após primeiro preparo: " + model.getQuantidadeJogadores());

	        model.prepararJogo(jogadores2);

	        System.out.println("Quantidade de jogadores após segundo preparo: " + model.getQuantidadeJogadores());

	        assertEquals(3, model.getQuantidadeJogadores());
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }
	    @Test
	    public void deveDistribuirTodasAsCartasRestantes() {
	    	System.out.println("TESTE CARTAS RESTANTES DEVEM SER DISTRIBUIDAS\n");
	        ClueModel model = new ClueModel();

	        List<String> jogadores = new ArrayList<String>();
	        jogadores.add("Coronel Mostarda");
	        jogadores.add("Dona Branca");
	        jogadores.add("Srta. Scarlet");
	        jogadores.add("Professor Black");

	        model.prepararJogo(jogadores);

	        int totalCartasDistribuidas = 0;

	        for (int i = 0; i < model.getQuantidadeJogadores(); i++) {
	            int quantidadeCartas = model.getQuantidadeCartasDoJogador(i);

	            System.out.println("Jogador " + i + " recebeu " + quantidadeCartas + " cartas.");

	            totalCartasDistribuidas += quantidadeCartas;
	        }

	        System.out.println("Total de cartas distribuídas: " + totalCartasDistribuidas);

	        assertEquals(18, totalCartasDistribuidas);
	        
	        System.out.println("\n");
	        System.out.println("*".repeat(50));
	    }
}