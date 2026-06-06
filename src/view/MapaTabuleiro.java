package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constrói e mantém o mapa completo do tabuleiro:
 *  - uma grade 25x24 de Celulas
 *  - corredores acessíveis mapeados a nomes "C_col_lin"
 *  - cômodos mapeados ao nome exato usado no Model
 *  - um ponto central por cômodo para desenhar o pião
 *
 * Uso típico na View:
 *   MapaTabuleiro mapa = new MapaTabuleiro();
 *   // Ao receber clique do mouse:
 *   String nomeCasa = mapa.getNomeCasaPorClique(mouseX, mouseY);
 *   // Para desenhar pião:
 *   int[] centro = mapa.getCentroComodo("Cozinha");  // ou getCelula("C_5_3").getCentroX/Y()
 */
public class MapaTabuleiro {

    // Tabuleiro Clue-C: 600x625 px, células de 25x25 px → 24 colunas x 25 linhas
    public static final int NUM_COLUNAS = 24;
    public static final int NUM_LINHAS  = 25;

    // grade[linha][coluna] — null onde não há célula jogável (dentro de cômodo sem corredor)
    private Celula[][] grade;

    // Lookup rápido: nomeCasa → Celula (para corredores e portais de entrada de cômodos)
    private Map<String, Celula> celulaPorNome;

    // Centro visual de cada cômodo para desenhar o pião quando ele está no cômodo
    private Map<String, int[]> centroComodo;

    public MapaTabuleiro() {
        grade         = new Celula[NUM_LINHAS][NUM_COLUNAS];
        celulaPorNome = new HashMap<String, Celula>();
        centroComodo  = new HashMap<String, int[]>();
        construirMapa();
    }

    // -------------------------------------------------------------------------
    // Consultas públicas
    // -------------------------------------------------------------------------

    /**
     * Dado um clique (mx, my), retorna o nome da casa clicada,
     * ou null se o clique caiu fora de qualquer área jogável.
     */
    public String getNomeCasaPorClique(int mx, int my) {
        int col = mx / Celula.LARGURA;
        int lin = my / Celula.ALTURA;
        if (lin < 0 || lin >= NUM_LINHAS || col < 0 || col >= NUM_COLUNAS) return null;
        Celula c = grade[lin][col];
        return (c != null) ? c.getNomeCasa() : null;
    }

    /**
     * Retorna a Celula de um corredor pelo nome "C_col_lin".
     */
    public Celula getCelula(String nomeCasa) {
        return celulaPorNome.get(nomeCasa);
    }

    /**
     * Retorna todas as Celulas (corredores + portais) — útil para destacar
     * casas alcançáveis após rolar os dados.
     */
    public List<Celula> todasCelulas() {
        List<Celula> lista = new ArrayList<Celula>();
        for (int l = 0; l < NUM_LINHAS; l++)
            for (int c = 0; c < NUM_COLUNAS; c++)
                if (grade[l][c] != null) lista.add(grade[l][c]);
        return lista;
    }

    /**
     * Centro visual (px) de um cômodo para posicionar o pião.
     * @return int[]{x, y} ou null se o cômodo não existir.
     */
    public int[] getCentroComodo(String nomeComodo) {
        return centroComodo.get(nomeComodo);
    }

    // -------------------------------------------------------------------------
    // Construção do mapa
    // -------------------------------------------------------------------------

    private void construirMapa() {
        registrarCentrosComodos();
        registrarCorredores();
        registrarPortaisComodos();
    }

    /**
     * Centro visual de cada cômodo (ponto onde o pião é desenhado quando está no cômodo).
     * Coordenadas medidas sobre Tabuleiro-Clue-C.jpg (600x625).
     */
    private void registrarCentrosComodos() {
        // Nome deve ser idêntico ao usado em ClueModel
        centroComodo.put("Cozinha",          new int[]{  75,  75 });
        centroComodo.put("Jardim de Inverno",new int[]{ 390,  62 });
        centroComodo.put("Sala de Música",   new int[]{ 187, 137 });
        centroComodo.put("Salão de Jogos",   new int[]{ 362, 200 });
        centroComodo.put("Sala de Jantar",   new int[]{  75, 275 });
        centroComodo.put("Detetive",         new int[]{ 212, 275 });
        centroComodo.put("Biblioteca",       new int[]{ 387, 325 });
        centroComodo.put("Sala de Estar",    new int[]{  75, 462 });
        centroComodo.put("Entrada",          new int[]{ 212, 450 });
        centroComodo.put("Escritório",       new int[]{ 412, 500 });
    }

    /**
     * Registra cada célula de corredor na grade.
     * O tabuleiro do Clue tem paredes nos cômodos; só as células de corredor são jogáveis.
     *
     * Convenção do nome: "C_<col>_<lin>"  (ex: "C_5_0" = coluna 5, linha 0)
     * Esse nome é o mesmo registrado como Casa no Model para corredores.
     *
     * A lista abaixo mapeia todas as células de corredor visíveis no Tabuleiro-Clue-C.
     * Células de cômodo ficam null na grade (acesso pelo nome do cômodo nos portais).
     */
    private void registrarCorredores() {

        // --- Linha 0 (topo) ---
        corredor(0,  6); corredor(0,  7); corredor(0,  8); corredor(0,  9);
        corredor(0, 14); corredor(0, 15); corredor(0, 16); corredor(0, 17);

        // --- Linha 1 ---
        corredor(1,  6); corredor(1,  7); corredor(1,  8); corredor(1,  9);
        corredor(1, 14); corredor(1, 15); corredor(1, 16); corredor(1, 17);

        // --- Linha 2 ---
        corredor(2,  6); corredor(2,  7); corredor(2,  8); corredor(2,  9);
        corredor(2, 14); corredor(2, 15); corredor(2, 16); corredor(2, 17);

        // --- Linhas 3-4: corredor central começa ---
        corredor(3,  6); corredor(3,  7); corredor(3,  8); corredor(3,  9);
        corredor(3, 14); corredor(3, 15); corredor(3, 16); corredor(3, 17);
        corredor(4,  5); corredor(4,  6); corredor(4,  7); corredor(4,  8); corredor(4,  9);
        corredor(4, 14); corredor(4, 15); corredor(4, 16); corredor(4, 17);

        // --- Linhas 5-7 ---
        for (int l = 5; l <= 7; l++) {
            for (int c = 5; c <= 9; c++) corredor(l, c);
            corredor(l, 14); corredor(l, 15); corredor(l, 16); corredor(l, 17);
        }

        // --- Linhas 8-12: corredor central largo ---
        for (int l = 8; l <= 12; l++) {
            for (int c = 5; c <= 9; c++) corredor(l, c);
            for (int c = 10; c <= 13; c++) corredor(l, c);
            corredor(l, 14); corredor(l, 15); corredor(l, 16); corredor(l, 17);
        }

        // --- Linhas 13-15 ---
        for (int l = 13; l <= 15; l++) {
            for (int c = 5; c <= 9; c++) corredor(l, c);
            for (int c = 10; c <= 17; c++) corredor(l, c);
        }

        // --- Linhas 16-18 ---
        for (int l = 16; l <= 18; l++) {
            for (int c = 0; c <= 4; c++) corredor(l, c);
            for (int c = 5; c <= 9; c++) corredor(l, c);
            for (int c = 10; c <= 17; c++) corredor(l, c);
        }

        // --- Linhas 19-22 ---
        for (int l = 19; l <= 22; l++) {
            for (int c = 0; c <= 4; c++) corredor(l, c);
            for (int c = 5; c <= 9; c++) corredor(l, c);
            for (int c = 10; c <= 17; c++) corredor(l, c);
        }

        // --- Linhas 23-24 (fundo) ---
        for (int l = 23; l <= 24; l++) {
            for (int c = 0; c <= 17; c++) corredor(l, c);
        }
    }

    /**
     * Portais de entrada dos cômodos: são células na grade que, quando clicadas,
     * levam ao cômodo correspondente.  O nome delas É o nome do cômodo no Model.
     *
     * Coordenadas baseadas nas setas de entrada visíveis no Tabuleiro-Clue-C.
     */
    private void registrarPortaisComodos() {
        // Cozinha
        portal(4,  4, "Cozinha");
        portal(8,  5, "Cozinha");

        // Jardim de Inverno
        portal(4, 14, "Jardim de Inverno");   // portal superior
        portal(7, 17, "Jardim de Inverno");   // portal lateral

        // Sala de Música
        portal(5,  8, "Sala de Música");
        portal(9,  5, "Sala de Música");

        // Salão de Jogos
        portal(8, 14, "Salão de Jogos");
        portal(12, 17, "Salão de Jogos");

        // Sala de Jantar
        portal(11,  4, "Sala de Jantar");
        portal(15,  5, "Sala de Jantar");

        // Detetive
        portal(10,  9, "Detetive");
        portal(14,  6, "Detetive");

        // Biblioteca
        portal(13, 14, "Biblioteca");
        portal(17, 14, "Biblioteca");

        // Sala de Estar
        portal(18,  4, "Sala de Estar");
        portal(22,  5, "Sala de Estar");

        // Entrada
        portal(18,  9, "Entrada");
        portal(22,  8, "Entrada");

        // Escritório
        portal(19, 14, "Escritório");
        portal(23, 17, "Escritório");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void corredor(int lin, int col) {
        String nome = "C_" + col + "_" + lin;
        Celula c = new Celula(lin, col, nome);
        grade[lin][col] = c;
        celulaPorNome.put(nome, c);
    }

    private void portal(int lin, int col, String nomeComodo) {
        // O portal sobrescreve a célula de corredor (se houver) com o nome do cômodo
        Celula c = new Celula(lin, col, nomeComodo);
        grade[lin][col] = c;
        // Não adicionamos ao celulaPorNome por chave do cômodo — o Model usa o nome direto.
        // Mas registramos pelo nome composto para evitar colisão com outros portais do mesmo cômodo.
        celulaPorNome.put("PORTAL_" + col + "_" + lin, c);
    }
}
