package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GradeTabuleiro {

    public static final int TABULEIRO_X = 20;
    public static final int TABULEIRO_Y = 20;

    public static final int TABULEIRO_LARGURA = 850;
    public static final int TABULEIRO_ALTURA = 820;

    public static final int LINHAS = 23;
    public static final int COLUNAS = 23;

    public static final int TAMANHO_CELULA_X = 30;
    public static final int TAMANHO_CELULA_Y = 28;

    public static final int AJUSTE_GRADE_X = 62;
    public static final int AJUSTE_GRADE_Y = 89;

    private static final Map<Integer, Integer> ESPACO_EXTRA_COLUNA_X =
            new HashMap<Integer, Integer>();

    private static final Set<String> CASAS_REMOVIDAS =
            new HashSet<String>();

    private static final boolean MODO_DEBUG = false;

    private static final Color COR_DEBUG_COMODO =
            new Color(255, 0, 180, 60);

    private static final Color COR_BORDA_COMODO =
            new Color(180, 0, 120, 180);

    private static final Color COR_CORREDOR = new Color(0, 180, 255, 170);
    private static final Color COR_BORDA_COR = new Color(0, 100, 255, 255);
    private static final boolean DESENHAR_DEBUG_COMODOS = false;

    private static final Color COR_PORTA = new Color(255, 140, 0, 200);
    private static final Color COR_BORDA_PORTA = new Color(255, 80, 0, 255);

    private static final String[][] PORTAS_COMODOS = {
            {
                    "COMODO_Cozinha",
                    "L3C5",
                    "L4C5"
            },

            {
                    "COMODO_Sala de Musica",
                    "L6C8",
                    "L6C15"
            },

            {
                    "COMODO_Jardim de Inverno",
                    "L3C18",
                    "L4C18"
            },

            {
                    "COMODO_Sala de Jantar",
                    "L7C0"
            },

            {
                    "COMODO_Salao de Jogos",
                    "L17C6"
            },

            {
                    "COMODO_Biblioteca",
                    "L17C16"
            },

            {
                    "COMODO_Sala de Estar",
                    "L19C6"
            },

            {
                    "COMODO_Entrada",
                    "L18C11",
                    "L18C12",
                    "L21C15"
            },

            {
                    "COMODO_Escritorio",
                    "L21C18"
            }
    };

    private final Map<String, Rectangle> mapaCasas =
            new HashMap<String, Rectangle>();

    private final Set<String> casasCorredor =
            new HashSet<String>();

    private final Set<String> casasPorta =
            new HashSet<String>();

    private final Map<String, String> portaParaComodo =
            new HashMap<String, String>();

    private final List<String> casasDestacadas =
            new ArrayList<String>();

    private final List<String> portasDestacadas =
            new ArrayList<String>();

    private final Map<String, List<Rectangle>> expansoesComodos =
            new HashMap<String, List<Rectangle>>();

    private final Map<String, List<Rectangle>> recortesComodos =
            new HashMap<String, List<Rectangle>>();

    static {
        ESPACO_EXTRA_COLUNA_X.put(6, 4);

        inicializarCasasRemovidas();

        restaurar("L0C8");
        restaurar("L0C9");
        restaurar("L0C14");
        restaurar("L0C15");
    }

    public GradeTabuleiro() {
        inicializarMapa();
    }

    public Set<String> getCasasCorredor() {
        return casasCorredor;
    }

    private String formatarNomeComodo(String nome) {
        return nome.replace("COMODO_", "");
    }

    public void destacarCasas(List<String> nomes) {
        casasDestacadas.clear();
        portasDestacadas.clear();

        for (String nomeOriginal : nomes) {
            String nome = normalizarNomeCasa(nomeOriginal);

            if (!mapaCasas.containsKey(nome)) {
                continue;
            }

            if (nome.startsWith("COMODO_")) {
                continue;
            }

            if (CASAS_REMOVIDAS.contains(nome) && !casasPorta.contains(nome)) {
                continue;
            }

            if (casasPorta.contains(nome)) {
                portasDestacadas.add(nome);
            } else {
                casasDestacadas.add(nome);
            }
        }
    }

    public void limparDestaques() {
        casasDestacadas.clear();
        portasDestacadas.clear();
    }

    public boolean ePorta(String nomeCasa) {
        nomeCasa = normalizarNomeCasa(nomeCasa);

        return casasPorta.contains(nomeCasa);
    }

    public String getComodoDaPorta(String nomeCasa) {
        nomeCasa = normalizarNomeCasa(nomeCasa);

        return portaParaComodo.get(nomeCasa);
    }

    public Rectangle getRetanguloCasaOuComodo(String nomeCasa) {
        nomeCasa = normalizarNomeCasa(nomeCasa);

        return mapaCasas.get(nomeCasa);
    }

    public void desenharDestaques(Graphics2D g2) {
        for (String nome : casasDestacadas) {
            Rectangle r = mapaCasas.get(nome);

            if (r == null) {
                continue;
            }

            g2.setColor(COR_CORREDOR);

            g2.fillRect(
                    r.x,
                    r.y,
                    r.width - 1,
                    r.height - 1
            );

            g2.setColor(COR_BORDA_COR);

            g2.drawRect(
                    r.x,
                    r.y,
                    r.width - 1,
                    r.height - 1
            );
        }

        for (String nome : portasDestacadas) {
            Rectangle r = mapaCasas.get(nome);

            if (r == null) {
                continue;
            }

            int margemX = 6;
            int margemY = 6;

            g2.setColor(COR_PORTA);

            g2.fillRect(
                    r.x + margemX,
                    r.y + margemY,
                    r.width - margemX * 2,
                    r.height - margemY * 2
            );

            g2.setColor(COR_BORDA_PORTA);

            g2.drawRect(
                    r.x + margemX,
                    r.y + margemY,
                    r.width - margemX * 2,
                    r.height - margemY * 2
            );
        }

        if (MODO_DEBUG) {
            desenharDebug(g2);
        }
    }

    private void desenharDebug(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 8));

        for (Map.Entry<String, Rectangle> e : mapaCasas.entrySet()) {
            String nome = e.getKey();

            if (nome.startsWith("COMODO_")) {
                continue;
            }

            if (CASAS_REMOVIDAS.contains(nome) && !casasPorta.contains(nome)) {
                continue;
            }

            Rectangle r = e.getValue();

            boolean porta = casasPorta.contains(nome);

            if (porta) {
                g2.setColor(new Color(255, 140, 0, 90));
            } else {
                g2.setColor(new Color(50, 50, 255, 60));
            }

            g2.fillRect(
                    r.x,
                    r.y,
                    r.width - 1,
                    r.height - 1
            );

            g2.setColor(Color.BLACK);

            g2.drawRect(
                    r.x,
                    r.y,
                    r.width - 1,
                    r.height - 1
            );

            String label = nome
                    .replace("L", "")
                    .replace("C", ",");

            g2.drawString(
                    label,
                    r.x + 2,
                    r.y + 10
            );
        }

        g2.setFont(new Font("Arial", Font.BOLD, 10));

        for (Map.Entry<String, Rectangle> e : mapaCasas.entrySet()) {
            String nome = e.getKey();

            if (!nome.startsWith("COMODO_")) {
                continue;
            }

            Rectangle r = e.getValue();

            g2.setColor(COR_DEBUG_COMODO);

            g2.fillRect(
                    r.x,
                    r.y,
                    r.width,
                    r.height
            );

            g2.setColor(COR_BORDA_COMODO);

            g2.drawRect(
                    r.x,
                    r.y,
                    r.width,
                    r.height
            );

            g2.setColor(Color.BLACK);

            g2.drawString(
                    formatarNomeComodo(nome),
                    r.x + 6,
                    r.y + 14
            );

            List<Rectangle> expansoes = expansoesComodos.get(nome);

            if (expansoes != null) {
                for (Rectangle extra : expansoes) {
                    g2.setColor(COR_DEBUG_COMODO);
                    g2.fillRect(extra.x, extra.y, extra.width, extra.height);

                    g2.setColor(COR_BORDA_COMODO);
                    g2.drawRect(extra.x, extra.y, extra.width, extra.height);
                }
            }

            List<Rectangle> recortes = recortesComodos.get(nome);

            if (recortes != null) {
                for (Rectangle corte : recortes) {
                    g2.setColor(new Color(255, 0, 0, 70));
                    g2.fillRect(corte.x, corte.y, corte.width, corte.height);

                    g2.setColor(new Color(180, 0, 0, 180));
                    g2.drawRect(corte.x, corte.y, corte.width, corte.height);
                }
            }
        }

        g2.setColor(Color.YELLOW);

        g2.setFont(new Font("Arial", Font.BOLD, 10));

        g2.drawString(
                "Grade: " +
                        COLUNAS + " horizontal x " +
                        LINHAS + " vertical | CEL_X=" +
                        TAMANHO_CELULA_X + " CEL_Y=" +
                        TAMANHO_CELULA_Y + " | AJUSTE_X=" +
                        AJUSTE_GRADE_X + " AJUSTE_Y=" +
                        AJUSTE_GRADE_Y,
                TABULEIRO_X + 5,
                TABULEIRO_Y + TABULEIRO_ALTURA - 5
        );
    }

    private void inicializarMapa() {
        for (int linha = 0; linha < LINHAS; linha++) {
            for (int coluna = 0; coluna < COLUNAS; coluna++) {
                String nome = nomeCelula(linha, coluna);

                Rectangle retangulo = getRetanguloCelula(linha, coluna);

                mapaCasas.put(nome, retangulo);

                if (!CASAS_REMOVIDAS.contains(nome)) {
                    casasCorredor.add(nome);
                }
            }
        }

        for (String[] entrada : PORTAS_COMODOS) {
            String nomeComodo = entrada[0];

            for (int i = 1; i < entrada.length; i++) {
                String nomeCasa = normalizarNomeCasa(entrada[i]);

                if (mapaCasas.containsKey(nomeCasa)) {
                    casasPorta.add(nomeCasa);

                    portaParaComodo.put(
                            nomeCasa,
                            nomeComodo
                    );

                    casasCorredor.add(nomeCasa);
                }
            }
        }

        inicializarRetangulosComodos();
        inicializarAjustesFinosComodos();
        inicializarCasasIniciais();
    }

    private void inicializarCasasIniciais() {
        mapaCasas.put(
                "INICIO_Srta. Scarlet",
                new Rectangle(275, 750, 65, 45)
        );

        mapaCasas.put(
                "INICIO_Coronel Mostarda",
                new Rectangle(65, 535, 55, 70)
        );

        mapaCasas.put(
                "INICIO_Sra. Peacock",
                new Rectangle(770, 225, 50, 70)
        );

        mapaCasas.put(
                "INICIO_Sra. White",
                new Rectangle(340, 60, 55, 60)
        );

        mapaCasas.put(
                "INICIO_Sr. Green",
                new Rectangle(490, 60, 65, 60)
        );

        mapaCasas.put(
                "INICIO_Professor Plum",
                new Rectangle(770, 596, 65, 65)
        );
    }

    private void inicializarRetangulosComodos() {
        mapaCasas.put(
                "COMODO_Cozinha",
                new Rectangle(78, 75, 185, 170)
        );

        mapaCasas.put(
                "COMODO_Sala de Musica",
                new Rectangle(322, 104, 245, 198)
        );

        mapaCasas.put(
                "COMODO_Jardim de Inverno",
                new Rectangle(624, 104, 184, 143)
        );

        mapaCasas.put(
                "COMODO_Sala de Jantar",
                new Rectangle(78, 329, 242, 199)
        );

        mapaCasas.put(
                "COMODO_Salao de Jogos",
                new Rectangle(624, 302, 183, 141)
        );

        mapaCasas.put(
                "COMODO_Biblioteca",
                new Rectangle(625, 472, 183, 140)
        );

        mapaCasas.put(
                "COMODO_Sala de Estar",
                new Rectangle(78, 613, 211, 170)
        );

        mapaCasas.put(
                "COMODO_Entrada",
                new Rectangle(352, 584, 182, 170)
        );

        mapaCasas.put(
                "COMODO_Escritorio",
                new Rectangle(625, 670, 183, 113)
        );
    }

    private void inicializarAjustesFinosComodos() {
        adicionarExpansaoComodo(
                "COMODO_Cozinha",
                criarRetanguloAreaGrade(5, 1, 5, 5)
        );

        /*
         * SALA DE JANTAR
         * Recorte no canto superior direito.
         * Isso corrige a "escadinha" para a Sala de Jantar não pegar
         * as casas L8C5, L8C6 e L8C7.
         */
        adicionarRecorteComodo(
                "COMODO_Sala de Jantar",
                criarRetanguloAreaGrade(8, 5, 8, 7)
        );
        removerArea(8, 5, 8, 7);

        adicionarRecorteComodo(
                "COMODO_Sala de Musica",
                criarRetanguloAreaGrade(0, 8, 0, 9)
        );
        removerArea(0, 8, 0, 9);

        adicionarRecorteComodo(
                "COMODO_Sala de Musica",
                criarRetanguloAreaGrade(0, 14, 0, 15)
        );
        removerArea(0, 14, 0, 15);

        /*
         * Como L0C8, L0C9, L0C14 e L0C15 precisam ser caminháveis,
         * restauramos novamente depois dos removerArea acima.
         */
        restaurar("L0C8");
        restaurar("L0C9");
        restaurar("L0C14");
        restaurar("L0C15");

        adicionarRecorteComodo(
                "COMODO_Biblioteca",
                criarRetanguloAreaGrade(13, 23, 13, 23)
        );
        removerArea(13, 23, 13, 23);

        adicionarRecorteComodo(
                "COMODO_Biblioteca",
                criarRetanguloAreaGrade(17, 23, 17, 23)
        );
        removerArea(17, 23, 17, 23);

        adicionarRecorteComodo(
                "COMODO_Sala de Estar",
                criarRetanguloAreaGrade(22, 6, 22, 6)
        );

        removerArea(22, 6, 22, 6);

        adicionarExpansaoComodo(
                "COMODO_Biblioteca",
                criarRetanguloAreaGrade(14, 17, 16, 17)
        );

        adicionarExpansaoComodo(
                "COMODO_Entrada",
                criarRetanguloAreaGrade(23, 10, 23, 13)
        );

        adicionarExpansaoComodo(
                "COMODO_Escritorio",
                criarRetanguloAreaGrade(20, 17, 22, 17)
        );
    }

    public static String nomeCelula(int linha, int coluna) {
        return "L" + linha + "C" + coluna;
    }

    public static String normalizarNomeCasa(String nomeCasa) {
        if (nomeCasa == null) {
            return null;
        }

        if (!nomeCasa.startsWith("L") || !nomeCasa.contains("C")) {
            return nomeCasa;
        }

        try {
            int cIndex = nomeCasa.indexOf("C");

            int linha = Integer.parseInt(
                    nomeCasa.substring(1, cIndex)
            );

            int coluna = Integer.parseInt(
                    nomeCasa.substring(cIndex + 1)
            );

            if (linha < 0) {
                linha = 0;
            }

            if (coluna < 0) {
                coluna = 0;
            }

            if (linha >= LINHAS) {
                linha = LINHAS - 1;
            }

            if (coluna >= COLUNAS) {
                coluna = COLUNAS - 1;
            }

            return nomeCelula(linha, coluna);

        } catch (NumberFormatException e) {
            return nomeCasa;
        }
    }

    private int calcularEspacoExtraColuna(int coluna) {
        int espacoExtra = 0;

        for (Map.Entry<Integer, Integer> entrada
                : ESPACO_EXTRA_COLUNA_X.entrySet()) {
            int colunaInicio = entrada.getKey();
            int pixels = entrada.getValue();

            if (coluna >= colunaInicio) {
                espacoExtra += pixels;
            }
        }

        return espacoExtra;
    }

    public Rectangle getRetanguloCelula(int linha, int coluna) {
        int espacoExtraX = calcularEspacoExtraColuna(coluna);

        int x = TABULEIRO_X
                + AJUSTE_GRADE_X
                + coluna * TAMANHO_CELULA_X
                + espacoExtraX;

        int y = TABULEIRO_Y
                + AJUSTE_GRADE_Y
                + linha * TAMANHO_CELULA_Y;

        return new Rectangle(
                x,
                y,
                TAMANHO_CELULA_X,
                TAMANHO_CELULA_Y
        );
    }

    public String getCasaClicada(int px, int py) {
        for (Map.Entry<String, Rectangle> e : mapaCasas.entrySet()) {
            String nome = e.getKey();

            if (!nome.startsWith("COMODO_")) {
                continue;
            }

            if (pontoPertenceAoComodo(nome, px, py)) {
                return nome;
            }
        }

        for (Map.Entry<String, Rectangle> e : mapaCasas.entrySet()) {
            String nome = e.getKey();

            if (nome.startsWith("COMODO_")) {
                continue;
            }

            if (CASAS_REMOVIDAS.contains(nome) && !casasPorta.contains(nome)) {
                continue;
            }

            if (e.getValue().contains(px, py)) {
                return nome;
            }
        }

        return null;
    }

    public static boolean casaEstaRemovida(String nomeCasa) {
        nomeCasa = normalizarNomeCasa(nomeCasa);

        return CASAS_REMOVIDAS.contains(nomeCasa);
    }

    private static void remover(String nomeCasa) {
        CASAS_REMOVIDAS.add(nomeCasa);
    }

    private static void restaurar(String nomeCasa) {
        CASAS_REMOVIDAS.remove(nomeCasa);
    }

    private static void removerArea(
            int linhaInicial,
            int colunaInicial,
            int linhaFinal,
            int colunaFinal
    ) {
        for (int linha = linhaInicial; linha <= linhaFinal; linha++) {
            for (int coluna = colunaInicial; coluna <= colunaFinal; coluna++) {
                remover(nomeCelula(linha, coluna));
            }
        }
    }

    private Rectangle criarRetanguloAreaGrade(
            int linhaInicial,
            int colunaInicial,
            int linhaFinal,
            int colunaFinal
    ) {
        Rectangle inicio = getRetanguloCelula(linhaInicial, colunaInicial);
        Rectangle fim = getRetanguloCelula(linhaFinal, colunaFinal);

        int x = inicio.x;
        int y = inicio.y;
        int largura = (fim.x + fim.width) - inicio.x;
        int altura = (fim.y + fim.height) - inicio.y;

        return new Rectangle(x, y, largura, altura);
    }

    private void adicionarExpansaoComodo(String nomeComodo, Rectangle area) {
        if (!expansoesComodos.containsKey(nomeComodo)) {
            expansoesComodos.put(nomeComodo, new ArrayList<Rectangle>());
        }

        expansoesComodos.get(nomeComodo).add(area);
    }

    private void adicionarRecorteComodo(String nomeComodo, Rectangle area) {
        if (!recortesComodos.containsKey(nomeComodo)) {
            recortesComodos.put(nomeComodo, new ArrayList<Rectangle>());
        }

        recortesComodos.get(nomeComodo).add(area);
    }

    private boolean pontoPertenceAoComodo(String nomeComodo, int px, int py) {
        Rectangle base = mapaCasas.get(nomeComodo);

        boolean dentro = false;

        if (base != null && base.contains(px, py)) {
            dentro = true;
        }

        List<Rectangle> expansoes = expansoesComodos.get(nomeComodo);

        if (!dentro && expansoes != null) {
            for (Rectangle expansao : expansoes) {
                if (expansao.contains(px, py)) {
                    dentro = true;
                    break;
                }
            }
        }

        if (!dentro) {
            return false;
        }

        List<Rectangle> recortes = recortesComodos.get(nomeComodo);

        if (recortes != null) {
            for (Rectangle recorte : recortes) {
                if (recorte.contains(px, py)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void inicializarCasasRemovidas() {
        removerArea(0, 0, 0, 5);

        removerArea(1, 0, 1, 4);
        removerArea(2, 0, 2, 4);
        removerArea(3, 0, 3, 4);
        removerArea(4, 0, 4, 4);
        removerArea(5, 0, 5, 4);

        removerArea(8, 0, 8, 4);
        removerArea(9, 0, 9, 7);
        removerArea(10, 0, 10, 7);
        removerArea(11, 0, 11, 7);
        removerArea(12, 0, 12, 7);
        removerArea(13, 0, 13, 7);
        removerArea(14, 0, 14, 7);

        removerArea(18, 0, 18, 6);
        removerArea(19, 0, 19, 6);
        removerArea(20, 0, 20, 6);
        removerArea(21, 0, 21, 6);
        removerArea(22, 0, 22, 6);

        remover("L1C5");
        remover("L2C5");
        remover("L3C5");
        remover("L4C5");
        remover("L5C5");
        remover("L0C6");

        removerArea(0, 10, 0, 13);
        removerArea(1, 8, 1, 15);
        removerArea(2, 8, 2, 15);
        removerArea(3, 8, 3, 15);
        removerArea(4, 8, 4, 15);
        removerArea(5, 8, 5, 15);
        removerArea(6, 8, 6, 15);

        removerArea(0, 18, 0, 22);
        removerArea(1, 18, 1, 22);
        removerArea(2, 18, 2, 22);
        removerArea(3, 18, 3, 22);
        removerArea(4, 19, 4, 22);

        removerArea(7, 18, 7, 22);
        removerArea(8, 18, 8, 22);
        removerArea(9, 18, 9, 22);
        removerArea(10, 18, 10, 22);
        removerArea(11, 18, 11, 22);

        removerArea(9, 10, 15, 14);

        removerArea(13, 18, 13, 22);

        removerArea(14, 17, 14, 22);
        removerArea(15, 17, 15, 22);
        removerArea(16, 17, 16, 22);

        removerArea(17, 18, 17, 22);

        remover("L0C17");
        remover("L15C0");
        remover("L16C0");
        remover("L17C0");
        remover("L7C0");

        removerArea(20, 17, 22, 22);

        removerArea(17, 9, 22, 14);
    }
}