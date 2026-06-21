package view;
 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
 
import javax.swing.JFrame;
 
import controller.ClueController;
import model.ClueFacade;
 
public class JanelaTabuleiro extends JFrame {
 
    private PainelTabuleiro painelTabuleiro;
    private PainelLateral   painelLateral;
    private final List<String> jogadoresEscolhidos;
 
    public JanelaTabuleiro() {
        this(personagensPadrao());
    }
 
    public JanelaTabuleiro(List<String> jogadoresEscolhidos) {
        this.jogadoresEscolhidos = jogadoresEscolhidos;
        setTitle("Clue");
        setSize(1200, 900);
        setMaximumSize(new Dimension(1400, 1050));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
 
        inicializarJogo();
 
        painelTabuleiro = new PainelTabuleiro();
        painelLateral   = new PainelLateral();
 
        ClueController controller = ClueController.getInstancia();
        controller.setPainelLateral(painelLateral);
        controller.setPainelTabuleiro(painelTabuleiro);
 
        inicializarPioesNaTela();
 
        setLayout(new BorderLayout());
        add(painelTabuleiro, BorderLayout.CENTER);
        add(painelLateral,   BorderLayout.EAST);
    }
 
    // =========================================================
    // Inicializacao do modelo
    // =========================================================
 
    private void inicializarJogo() {
        ClueFacade facade = ClueFacade.getInstancia();
 
        facade.prepararJogo(jogadoresEscolhidos);
 
        GradeTabuleiro grade = new GradeTabuleiro();
        Set<String> corredores = grade.getCasasCorredor();
 
        adicionarCorredores(facade, corredores);
        adicionarComodos(facade);
        adicionarPortasComodos(facade);
        adicionarCasasIniciais(facade);
 
        conectarCorredores(facade, corredores);
        conectarComodos(facade);
        conectarCasasIniciais(facade);
 
        adicionarPassagensSecretas(facade);
        posicionarJogadoresIniciais(facade);
    }
 
    private void adicionarCasasIniciais(ClueFacade facade) {
        facade.adicionarCasa("INICIO_Srta. Scarlet");
        facade.adicionarCasa("INICIO_Coronel Mostarda");
        facade.adicionarCasa("INICIO_Sra. Peacock");
        facade.adicionarCasa("INICIO_Sra. White");
        facade.adicionarCasa("INICIO_Sr. Green");
        facade.adicionarCasa("INICIO_Professor Plum");
    }
 
    private void conectarCasasIniciais(ClueFacade facade) {
        facade.conectarCasas("INICIO_Srta. Scarlet",    "L22C7");
        facade.conectarCasas("INICIO_Coronel Mostarda", "L16C1");
        facade.conectarCasas("INICIO_Sra. Peacock",     "L5C22");
        facade.conectarCasas("INICIO_Sra. White",       "L0C9");
        facade.conectarCasas("INICIO_Sr. Green",        "L0C16");
        facade.conectarCasas("INICIO_Professor Plum",   "L18C22");
    }
 
    private void adicionarCorredores(ClueFacade facade, Set<String> corredores) {
        for (String casa : corredores) {
            facade.adicionarCasa(casa);
        }
    }
 
    private void adicionarComodos(ClueFacade facade) {
        facade.adicionarCasa("COMODO_Cozinha");
        facade.adicionarCasa("COMODO_Sala de Musica");
        facade.adicionarCasa("COMODO_Jardim de Inverno");
        facade.adicionarCasa("COMODO_Sala de Jantar");
        facade.adicionarCasa("COMODO_Salao de Jogos");
        facade.adicionarCasa("COMODO_Biblioteca");
        facade.adicionarCasa("COMODO_Sala de Estar");
        facade.adicionarCasa("COMODO_Entrada");
        facade.adicionarCasa("COMODO_Escritorio");
    }
 
    private void adicionarPortasComodos(ClueFacade facade) {
        // Cozinha
        facade.adicionarCasa("L6C4");

        // Sala de Musica
        facade.adicionarCasa("L4C16");
        facade.adicionarCasa("L7C14");
        facade.adicionarCasa("L7C9");
        facade.adicionarCasa("L4C7");

        // Jardim de Inverno
        facade.adicionarCasa("L4C18");

        // Sala de Jantar
        facade.adicionarCasa("L11C8");
        facade.adicionarCasa("L15C16");
        facade.adicionarCasa("L15C6");

        // Salao de Jogos
        facade.adicionarCasa("L8C17");
        facade.adicionarCasa("L12C22");

        // Biblioteca
        facade.adicionarCasa("L12C20");

        // Sala de Estar
        facade.adicionarCasa("L17C6");

        // Entrada
        facade.adicionarCasa("L16C11");
        facade.adicionarCasa("L16C12");

        // Escritorio
        facade.adicionarCasa("L19C17");
    }
 
    private void conectarCorredores(ClueFacade facade, Set<String> corredores) {
        for (String casa : corredores) {
            int linha  = extrairLinha(casa);
            int coluna = extrairColuna(casa);
 
            String direita = GradeTabuleiro.nomeCelula(linha, coluna + 1);
            String baixo   = GradeTabuleiro.nomeCelula(linha + 1, coluna);
 
            if (corredores.contains(direita)) {
                facade.conectarCasas(casa, direita);
            }
 
            if (corredores.contains(baixo)) {
                facade.conectarCasas(casa, baixo);
            }
        }
    }
 
    private void conectarComodos(ClueFacade facade) {
        // Cozinha
        facade.conectarCasas("COMODO_Cozinha", "L6C4");

        // Sala de Musica
        facade.conectarCasas("COMODO_Sala de Musica", "L4C16");
        facade.conectarCasas("COMODO_Sala de Musica", "L7C14");
        facade.conectarCasas("COMODO_Sala de Musica", "L7C9");
        facade.conectarCasas("COMODO_Sala de Musica", "L4C7");

        // Jardim de Inverno
        facade.conectarCasas("COMODO_Jardim de Inverno", "L4C18");

        // Sala de Jantar
        facade.conectarCasas("COMODO_Sala de Jantar", "L11C8");
        facade.conectarCasas("COMODO_Sala de Jantar", "L15C16");
        facade.conectarCasas("COMODO_Sala de Jantar", "L15C6");

        // Salao de Jogos
        facade.conectarCasas("COMODO_Salao de Jogos", "L8C17");
        facade.conectarCasas("COMODO_Salao de Jogos", "L12C22");

        // Biblioteca
        facade.conectarCasas("COMODO_Biblioteca", "L12C20");

        // Sala de Estar
        facade.conectarCasas("COMODO_Sala de Estar", "L17C6");

        // Entrada
        facade.conectarCasas("COMODO_Entrada", "L16C11");
        facade.conectarCasas("COMODO_Entrada", "L16C12");

        // Escritorio
        facade.conectarCasas("COMODO_Escritorio", "L19C17");
    }
 
    private void adicionarPassagensSecretas(ClueFacade facade) {
        facade.adicionarPassagemSecreta("COMODO_Cozinha",           "COMODO_Sala de Estar");
        facade.adicionarPassagemSecreta("COMODO_Jardim de Inverno", "COMODO_Escritorio");
    }
 
    private void posicionarJogadoresIniciais(ClueFacade facade) {
        for (String nome : jogadoresEscolhidos) {
            facade.posicionarJogador(nome, "INICIO_" + nome);
        }
    }
 
    private void inicializarPioesNaTela() {
        for (String nome : jogadoresEscolhidos) {
            painelTabuleiro.moverPiao(nome, "INICIO_" + nome);
        }
        ClueController.getInstancia().iniciarTurno();
    }
 
    private int extrairLinha(String nome) {
        int cIndex = nome.indexOf("C");
        return Integer.parseInt(nome.substring(1, cIndex));
    }
 
    private int extrairColuna(String nome) {
        int cIndex = nome.indexOf("C");
        return Integer.parseInt(nome.substring(cIndex + 1));
    }
 
    private static List<String> personagensPadrao() {
        List<String> padrao = new ArrayList<String>();
        padrao.add("Srta. Scarlet");
        padrao.add("Coronel Mostarda");
        padrao.add("Sra. White");
        padrao.add("Sr. Green");
        padrao.add("Sra. Peacock");
        padrao.add("Professor Plum");
        return padrao;
    }
}
