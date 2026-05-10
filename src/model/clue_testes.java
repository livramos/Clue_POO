package model;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class clue_testes {


		@Test
		public void depoisDaScarletDeveJogarQuemEstaASuaEsquerda() {
		    System.out.println("TESTE ESQUERDA SRTA SCARLET\n");
	
		    ClueModel model = new ClueModel();
	
		    List<String> jogadores = new ArrayList<String>();
	
		    jogadores.add("Coronel Mostarda");
		    jogadores.add("Dona Branca");
		    jogadores.add("Srta. Scarlet");
		    jogadores.add("Professor Black");
	
		    model.prepararJogo(jogadores);
	
		    System.out.println("Jogador inicial: " + model.getNomeJogadorAtual());
	
		    assertEquals("Srta. Scarlet", model.getNomeJogadorAtual());
	
		    model.passarTurno();
	
		    System.out.println("Depois de passar o turno: " + model.getNomeJogadorAtual());
	
		    assertEquals("Professor Black", model.getNomeJogadorAtual());
	
		    model.passarTurno();
	
		    System.out.println("Depois de passar o turno: " + model.getNomeJogadorAtual());
	
		    assertEquals("Coronel Mostarda", model.getNomeJogadorAtual());
	
		    System.out.println();
		    System.out.println("*".repeat(50));
		}
	
		@Test
		public void todosJogadoresDevemReceberFolhaDeNotas() {
		    System.out.println("JOGADORES DEVEM RECEBER FOLHA DE NOTAS\n");
	
		    ClueModel model = new ClueModel();
	
		    List<String> jogadores = new ArrayList<String>();
		    jogadores.add("Coronel Mostarda");
		    jogadores.add("Dona Branca");
		    jogadores.add("Srta. Scarlet");
		    jogadores.add("Professor Black");
	
		    model.prepararJogo(jogadores);
	
		    for (int i = 0; i < model.getQuantidadeJogadores(); i++) {
		        System.out.println("Jogador " + i + " possui folha de notas: " + model.jogadorTemFolhaNotas(i));
	
		        System.out.println("Quantidade de suspeitos: " + model.getQuantidadeSuspeitosNaFolhaDoJogador(i));
		        System.out.println("Quantidade de armas: " + model.getQuantidadeArmasNaFolhaDoJogador(i));
		        System.out.println("Quantidade de cômodos: " + model.getQuantidadeComodosNaFolhaDoJogador(i));
	
		        assertTrue(model.jogadorTemFolhaNotas(i));
		        assertEquals(6, model.getQuantidadeSuspeitosNaFolhaDoJogador(i));
		        assertEquals(6, model.getQuantidadeArmasNaFolhaDoJogador(i));
		        assertEquals(9, model.getQuantidadeComodosNaFolhaDoJogador(i));
	
		        System.out.println();
		        System.out.println("*".repeat(51));
		    }
		}

		@Test
		public void cartaDeveComecarDesmarcadaNaFolhaDoJogador() {
		    System.out.println("CARTA DEVE COMEÇAR DESMARCADA NA FOLHA\n");
	
		    ClueModel model = new ClueModel();
	
		    List<String> jogadores = new ArrayList<String>();
		    jogadores.add("Coronel Mostarda");
		    jogadores.add("Dona Branca");
		    jogadores.add("Srta. Scarlet");
		    jogadores.add("Professor Black");
	
		    model.prepararJogo(jogadores);
	
		    boolean marcada = model.cartaEstaMarcadaNaFolhaDoJogador(0, "Corda");
	
		    System.out.println("Carta Corda está marcada inicialmente: " + marcada);
	
		    assertFalse(marcada);
	
		    System.out.println("*".repeat(51));
		}
	
		@Test
		public void deveMarcarCartaNaFolhaDoJogador() {
		    System.out.println("DEVE MARCAR CARTA NA FOLHA DO JOGADOR\n");
	
		    ClueModel model = new ClueModel();
	
		    List<String> jogadores = new ArrayList<String>();
		    jogadores.add("Coronel Mostarda");
		    jogadores.add("Dona Branca");
		    jogadores.add("Srta. Scarlet");
		    jogadores.add("Professor Black");
	
		    model.prepararJogo(jogadores);
	
		    System.out.println("Antes de marcar Corda: " + model.cartaEstaMarcadaNaFolhaDoJogador(0, "Corda"));
	
		    model.marcarCartaNaFolhaDoJogador(0, "Corda");
	
		    System.out.println("Depois de marcar Corda: " + model.cartaEstaMarcadaNaFolhaDoJogador(0, "Corda"));
	
		    assertTrue(model.cartaEstaMarcadaNaFolhaDoJogador(0, "Corda"));
	
		    System.out.println("*".repeat(51));
		}
	
		@Test
		public void marcarCartaEmUmJogadorNaoDeveMarcarNaFolhaDosOutros() {
		    System.out.println("MARCAR CARTA EM UM JOGADOR NÃO DEVE MARCAR NOS OUTROS\n");
	
		    ClueModel model = new ClueModel();
	
		    List<String> jogadores = new ArrayList<String>();
		    jogadores.add("Coronel Mostarda");
		    jogadores.add("Dona Branca");
		    jogadores.add("Srta. Scarlet");
		    jogadores.add("Professor Black");
	
		    model.prepararJogo(jogadores);
	
		    model.marcarCartaNaFolhaDoJogador(0, "Corda");
	
		    System.out.println("Jogador 0 - Corda marcada: " + model.cartaEstaMarcadaNaFolhaDoJogador(0, "Corda"));
		    System.out.println("Jogador 1 - Corda marcada: " + model.cartaEstaMarcadaNaFolhaDoJogador(1, "Corda"));
		    System.out.println("Jogador 2 - Corda marcada: " + model.cartaEstaMarcadaNaFolhaDoJogador(2, "Corda"));
		    System.out.println("Jogador 3 - Corda marcada: " + model.cartaEstaMarcadaNaFolhaDoJogador(3, "Corda"));
	
		    assertTrue(model.cartaEstaMarcadaNaFolhaDoJogador(0, "Corda"));
		    assertFalse(model.cartaEstaMarcadaNaFolhaDoJogador(1, "Corda"));
		    assertFalse(model.cartaEstaMarcadaNaFolhaDoJogador(2, "Corda"));
		    assertFalse(model.cartaEstaMarcadaNaFolhaDoJogador(3, "Corda"));
	
		    System.out.println("*".repeat(51));
		}
		
		@Test(expected = IllegalArgumentException.class)
		public void naoDeveMarcarCartaInexistenteNaFolha() {
		    System.out.println("NÃO DEVE MARCAR CARTA INEXISTENTE NA FOLHA\n");
	
		    ClueModel model = new ClueModel();
	
		    List<String> jogadores = new ArrayList<String>();
		    jogadores.add("Coronel Mostarda");
		    jogadores.add("Dona Branca");
		    jogadores.add("Srta. Scarlet");
		    jogadores.add("Professor Black");
	
		    model.prepararJogo(jogadores);
	
		    System.out.println("Tentando marcar carta inexistente: Espada");
	
		    model.marcarCartaNaFolhaDoJogador(0, "Espada");
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
	    @Test
	    public void deveDeslocarPiaoDaVezParaCasaMapeada() {
	        System.out.println("DESLOCAR PIÃO DA VEZ PARA UMA CASA MAPEADA\n");

	        ClueModel model = new ClueModel();

	        List<String> jogadores = new ArrayList<String>();
	        jogadores.add("Coronel Mostarda");
	        jogadores.add("Dona Branca");
	        jogadores.add("Srta. Scarlet");
	        jogadores.add("Professor Black");

	        model.prepararJogo(jogadores);

	        model.adicionarCasaAoTabuleiro("A");
	        model.adicionarCasaAoTabuleiro("B");
	        model.adicionarCasaAoTabuleiro("C");
	        model.adicionarCasaAoTabuleiro("D");

	        model.conectarCasas("A", "B");
	        model.conectarCasas("B", "C");
	        model.conectarCasas("C", "D");

	        model.posicionarPiaoDaVez("A");

	        System.out.println("Jogador da vez: " + model.getNomeJogadorAtual());
	        System.out.println("Casa inicial: " + model.getNomeCasaAtualDoJogadorDaVez());

	        int[] dados = {1, 1};

	        List<String> casasMapeadas = model.mapearCasas("A", dados);

	        System.out.println("Dados usados: " + dados[0] + " e " + dados[1]);
	        System.out.println("Casas mapeadas: " + casasMapeadas);

	        model.deslocarPiaoDaVez("C");

	        System.out.println("Casa final: " + model.getNomeCasaAtualDoJogadorDaVez());

	        assertEquals("C", model.getNomeCasaAtualDoJogadorDaVez());

	        System.out.println("*".repeat(51));
	    }
	    
	    @Test
	    public void casaAtualDoJogadorDaVezDeveMudarAposDeslocamento() {
	        System.out.println("CASA ATUAL DO JOGADOR DEVE MUDAR APÓS DESLOCAMENTO\n");

	        ClueModel model = new ClueModel();

	        List<String> jogadores = new ArrayList<String>();
	        jogadores.add("Coronel Mostarda");
	        jogadores.add("Dona Branca");
	        jogadores.add("Srta. Scarlet");
	        jogadores.add("Professor Black");

	        model.prepararJogo(jogadores);

	        model.adicionarCasaAoTabuleiro("Entrada");
	        model.adicionarCasaAoTabuleiro("Corredor");
	        model.adicionarCasaAoTabuleiro("Biblioteca");

	        model.conectarCasas("Entrada", "Corredor");
	        model.conectarCasas("Corredor", "Biblioteca");

	        model.posicionarPiaoDaVez("Entrada");

	        System.out.println("Casa antes do deslocamento: " + model.getNomeCasaAtualDoJogadorDaVez());

	        int[] dados = {1, 1};

	        List<String> casasMapeadas = model.mapearCasas("Entrada", dados);

	        System.out.println("Casas mapeadas: " + casasMapeadas);

	        model.deslocarPiaoDaVez("Biblioteca");

	        System.out.println("Casa depois do deslocamento: " + model.getNomeCasaAtualDoJogadorDaVez());

	        assertEquals("Biblioteca", model.getNomeCasaAtualDoJogadorDaVez());

	        System.out.println("*".repeat(51));
	    }
	    @Test(expected = IllegalArgumentException.class)
	    public void naoDeveDeslocarPiaoAntesDeMapearCasas() {
	        System.out.println("NÃO DEVE DESLOCAR PIÃO ANTES DE MAPEAR CASAS\n");

	        ClueModel model = new ClueModel();

	        List<String> jogadores = new ArrayList<String>();
	        jogadores.add("Coronel Mostarda");
	        jogadores.add("Dona Branca");
	        jogadores.add("Srta. Scarlet");
	        jogadores.add("Professor Black");

	        model.prepararJogo(jogadores);

	        model.adicionarCasaAoTabuleiro("A");
	        model.adicionarCasaAoTabuleiro("B");

	        model.conectarCasas("A", "B");

	        model.posicionarPiaoDaVez("A");

	        System.out.println("Tentando mover para B sem chamar mapearCasas antes.");

	        model.deslocarPiaoDaVez("B");
	    }
}