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

    /*
     * Agora são:
     * 23 quadradinhos na horizontal
     * 23 quadradinhos na vertical
     */
    public static final int LINHAS = 23;
    public static final int COLUNAS = 23;

    /*
     * Tamanho de cada quadradinho.
     *
     * Para diminuir todos os quadrados:
     * diminui TAMANHO_CELULA_X e TAMANHO_CELULA_Y.
     */
    public static final int TAMANHO_CELULA_X = 30;
    public static final int TAMANHO_CELULA_Y = 31;

    /*
     * Posição da grade inteira.
     *
     * X maior -> direita
     * X menor -> esquerda
     *
     * Y maior -> baixo
     * Y menor -> cima
     */
    public static final int AJUSTE_GRADE_X = 92;
    public static final int AJUSTE_GRADE_Y = 22;

    /*
     * Espaço extra por coluna.
     *
     * Exemplo:
     * ESPACO_EXTRA_COLUNA_X.put(6, 4);
     *
     * Isso significa:
     * da coluna C6 em diante, tudo anda 4 pixels para a direita.
     *
     * Como a C5 fica parada e a C6 anda, aparece um espacinho
     * entre a coluna 5 e a coluna 6.
     */
    private static final Map<Integer, Integer> ESPACO_EXTRA_COLUNA_X =
            new HashMap<>();

    static {
        /*
         * Espaço entre a coluna C5 e a coluna C6.
         *
         * C0 até C5 ficam no lugar.
         * C6 até C22 andam 4 pixels para a direita.
         */
        ESPACO_EXTRA_COLUNA_X.put(6, 4);
    }

    private static final boolean MODO_DEBUG = true;

    private static final Color COR_CORREDOR = new Color(0, 180, 255, 170);
    private static final Color COR_BORDA_COR = new Color(0, 100, 255, 255);

    private static final Color COR_PORTA = new Color(255, 140, 0, 200);
    private static final Color COR_BORDA_PORTA = new Color(255, 80, 0, 255);

    private static final String[][] PORTAS_COMODOS = {
            {
                    "Cozinha",
                    "L3C5",
                    "L4C5"
            },

            {
                    "Sala de Música",
                    "L6C8",
                    "L6C15"
            },

            {
                    "Jardim de Inverno",
                    "L3C18",
                    "L4C18"
            },

            {
                    "Sala de Jantar",
                    "L7C0"
            },

            {
                    "Salão de Jogos",
                    "L17C6"
            },

            {
                    "Biblioteca",
                    "L17C16"
            },

            {
                    "Sala de Estar",
                    "L19C6"
            },

            {
                    "Entrada",
                    "L18C11",
                    "L18C12",
                    "L21C15"
            },

            {
                    "Escritório",
                    "L21C18"
            }
    };

    private final Map<String, Rectangle> mapaCasas = new HashMap<>();

    private final Set<String> casasCorredor = new HashSet<>();

    private final Set<String> casasPorta = new HashSet<>();

    private final Map<String, String> portaParaComodo = new HashMap<>();

    private final List<String> casasDestacadas = new ArrayList<>();

    private final List<String> portasDestacadas = new ArrayList<>();

    public GradeTabuleiro() {
        inicializarMapa();
    }

    public Set<String> getCasasCorredor() {
        return casasCorredor;
    }

    public void destacarCasas(List<String> nomes) {
        casasDestacadas.clear();
        portasDestacadas.clear();

        for (String nomeOriginal : nomes) {
            String nome = normalizarNomeCasa(nomeOriginal);

            if (!mapaCasas.containsKey(nome)) {
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
            Rectangle r = e.getValue();

            boolean porta = casasPorta.contains(e.getKey());

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

            String label = e.getKey()
                    .replace("L", "")
                    .replace("C", ",");

            g2.drawString(
                    label,
                    r.x + 2,
                    r.y + 10
            );
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

                casasCorredor.add(nome);
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
                }
            }
        }
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
            if (e.getValue().contains(px, py)) {
                return e.getKey();
            }
        }

        return null;
    }

    private int extrairLinha(String nome) {
        nome = normalizarNomeCasa(nome);

        int cIndex = nome.indexOf("C");

        return Integer.parseInt(
                nome.substring(1, cIndex)
        );
    }

    private int extrairColuna(String nome) {
        nome = normalizarNomeCasa(nome);

        int cIndex = nome.indexOf("C");

        return Integer.parseInt(
                nome.substring(cIndex + 1)
        );
    }
}