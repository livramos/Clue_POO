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

    public static final int GRADE_X = 78;
    public static final int GRADE_Y = 52;

    public static final int LARGURA_CELULA = 28;
    public static final int ALTURA_CELULA = 29;

    public static final int LINHAS = 25;
    public static final int COLUNAS = 24;

    private static final boolean MODO_DEBUG = true;
    private static final Color COR_CORREDOR = new Color(0, 180, 255, 170);
    private static final Color COR_BORDA_COR = new Color(0, 100, 255, 255);
    private static final Color COR_PORTA = new Color(255, 140, 0, 200);
    private static final Color COR_BORDA_PORTA = new Color(255, 80, 0, 255);

    private static final String[] CASAS_CORREDOR_ARRAY = {
    	    // Linha 0
    	    "L0C6",
    	    "L0C7",
    	    "L0C16",
    	    // Linha 1
    	    "L1C6",
    	    "L1C7",
    	    "L1C16",
    	    // Linha 2
    	    "L2C6",
    	    "L2C7",
    	    "L2C16",
    	    // Linha 3
    	    "L3C6",
    	    "L3C7",
    	    "L3C16",
    	    // Linha 4
    	    "L4C6",
    	    "L4C7",
    	    "L4C16",
    	    // Linha 5
    	    "L5C6",
    	    "L5C7",
    	    "L5C16",
    	    // Linha 6
    	    "L6C6",
    	    "L6C7",
    	    "L6C8",
    	    "L6C9",
    	    "L6C10",
    	    "L6C11",
    	    "L6C12",
    	    "L6C13",
    	    "L6C14",
    	    "L6C15",
    	    "L6C16",
    	    // Linha 7
    	    "L7C0",
    	    "L7C1",
    	    "L7C2",
    	    "L7C3",
    	    "L7C4",
    	    "L7C5",
    	    "L7C6",
    	    "L7C7",
    	    "L7C8",
    	    "L7C9",
    	    "L7C10",
    	    "L7C11",
    	    "L7C12",
    	    "L7C13",
    	    "L7C14",
    	    "L7C15",
    	    "L7C16",
    	    "L7C17",
    	    "L7C18",
    	    "L7C19",
    	    "L7C20",
    	    "L7C21",
    	    "L7C22",
    	    "L7C23",
    	    // Linha 8
    	    "L8C7",
    	    "L8C8",
    	    "L8C9",
    	    "L8C10",
    	    "L8C11",
    	    "L8C12",
    	    "L8C13",
    	    "L8C14",
    	    "L8C15",
    	    "L8C16",
    	    // Linha 9
    	    "L9C7",
    	    "L9C8",
    	    "L9C9",
    	    "L9C10",
    	    "L9C11",
    	    "L9C12",
    	    "L9C13",
    	    "L9C14",
    	    "L9C15",
    	    "L9C16",
    	    // Linha 10
    	    "L10C7",
    	    "L10C8",
    	    "L10C9",
    	    "L10C10",
    	    "L10C11",
    	    "L10C12",
    	    "L10C13",
    	    "L10C14",
    	    "L10C15",
    	    "L10C16",
    	    // Linha 11
    	    "L11C7",
    	    "L11C8",
    	    "L11C9",
    	    "L11C10",
    	    "L11C11",
    	    "L11C12",
    	    "L11C13",
    	    "L11C14",
    	    "L11C15",
    	    "L11C16",
    	    // Linha 12
    	    "L12C7",
    	    "L12C8",
    	    "L12C9",
    	    "L12C10",
    	    "L12C11",
    	    "L12C12",
    	    "L12C13",
    	    "L12C14",
    	    "L12C15",
    	    "L12C16",
    	    // Linha 13
    	    "L13C7",
    	    "L13C8",
    	    "L13C9",
    	    "L13C10",
    	    "L13C11",
    	    "L13C12",
    	    "L13C13",
    	    "L13C14",
    	    "L13C15",
    	    "L13C16",
    	    // Linha 14
    	    "L14C7",
    	    "L14C8",
    	    "L14C9",
    	    "L14C10",
    	    "L14C11",
    	    "L14C12",
    	    "L14C13",
    	    "L14C14",
    	    "L14C15",
    	    "L14C16",
    	    // Linha 15
    	    "L15C7",
    	    "L15C8",
    	    "L15C9",
    	    "L15C10",
    	    "L15C11",
    	    "L15C12",
    	    "L15C13",
    	    "L15C14",
    	    "L15C15",
    	    "L15C16",
    	    // Linha 16

    	    // Linha 17
    	    "L17C1",
    	    "L17C2",
    	    "L17C3",
    	    "L17C4",
    	    "L17C5",
    	    "L17C6",
    	    "L17C7",
    	    "L17C8",
    	    "L17C9",
    	    "L17C15",
    	    "L17C16",
    	    // Linha 18
    	    "L18C1",
    	    "L18C2",
    	    "L18C3",
    	    "L18C4",
    	    "L18C5",
    	    "L18C6",
    	    "L18C7",
    	    "L18C8",
    	    "L18C9",
    	    "L18C10",
    	    "L18C11",
    	    "L18C12",
    	    "L18C13",
    	    "L18C14",
    	    "L18C15",
    	    "L18C16",
    	    // Linha 19
    	    "L19C1",
    	    "L19C2",
    	    "L19C3",
    	    "L19C4",
    	    "L19C5",
    	    "L19C6",
    	    "L19C7",
    	    "L19C8",
    	    "L19C15",
    	    "L19C16",
    	    "L19C17",
    	    // Linha 20
    	    "L20C7",
    	    "L20C8",
    	    "L20C15",
    	    "L20C16",
    	    "L20C17",
    	    "L20C18",
    	    "L20C19",
    	    "L20C20",
    	    "L20C21",
    	    "L20C22",
    	    // Linha 21
    	    "L21C7",
    	    "L21C8",
    	    "L21C15",
    	    "L21C16",
    	    "L21C17",
    	    "L21C18",
    	    "L21C19",
    	    "L21C20",
    	    "L21C21",
    	    "L21C22",
    	    // Linha 22
    	    "L22C7",
    	    "L22C8",
    	    "L22C15",
    	    "L22C16",
    	    // Linha 23
    	    "L23C7",
    	    "L23C8",
    	    "L23C15",
    	    "L23C16",
    	    // Linha 24
    	    "L24C7",
    	    "L24C8",
    	    "L24C15",
    	    "L24C16",
    	};
    
    private static final Set<String> CASAS_CORREDOR = new HashSet<>();
    
    static {
        for (String casa : CASAS_CORREDOR_ARRAY) {
            CASAS_CORREDOR.add(casa);
        }
    }

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

    private final Map<String, Rectangle> mapaCasas =
            new HashMap<>();

    private final Set<String> casasPorta =
            new HashSet<>();

    private final Map<String, String> portaParaComodo =
            new HashMap<>();

    private final List<String> casasDestacadas =
            new ArrayList<>();

    private final List<String> portasDestacadas =
            new ArrayList<>();
    
    private static final Map<String, Integer> OFFSET_X =
            new HashMap<>();
    
    private static final Map<String, Integer> OFFSET_Y =
            new HashMap<>();
    
    private static void offset(
            String casa,
            int x,
            int y
    ) {

        OFFSET_X.put(casa, x);

        OFFSET_Y.put(casa, y);
    }
    
    private static void offsetLinha(
            int linha,
            int colunaInicio,
            int colunaFim,
            int offsetX,
            int offsetY
    ) {

        for (int c = colunaInicio; c <= colunaFim; c++) {

            offset(
                    nomeCelula(linha, c),
                    offsetX,
                    offsetY
            );
        }
    }
    
    static {
    	// 24
        offset("L24C7", 23, -10);
        offset("L24C8", 23, -10);
        offset("L24C15", 44, -13);
        offset("L24C16", 44, -13);
        
        // 23
        offsetLinha(23, 7, 16, 23, -10);
        offsetLinha(22, 7, 16, 23, -10);
        offsetLinha(21, 7, 16, 23, -10);
        offsetLinha(20, 7, 16, 23, -10);
        offsetLinha(19, 7, 16, 23, -10);
        offsetLinha(18, 7, 16, 23, -10);
        offsetLinha(17, 7, 16, 23, -10);
        
        offset("L23C15", 44, -13);
        offset("L23C16", 44, -13);
        offset("L22C15", 44, -13);
        offset("L22C16", 44, -13);
        offset("L21C15", 44, -13);
        offset("L21C16", 44, -13);
        offset("L20C15", 44, -13);
        offset("L20C16", 44, -13);
        offset("L19C15", 44, -13);
        offset("L19C16", 44, -13);
        offset("L18C15", 44, -13);
        offset("L18C16", 44, -13);
        offset("L17C15", 44, -13);
        offset("L17C16", 44, -13);
        
        offset("L17C9", 27, -10);
        offset("L18C9", 27, -10);
        offset("L18C10", 30, -10);
        offset("L18C11", 32, -12);
        offset("L18C12", 35, -12);
        offset("L18C13", 37, -12);
        offset("L18C14", 39, -12);
        
        offsetLinha(19, 1, 6, 21, -13);
        offset("L19C17", 44, -13);
        offset("L19C5", 18, -13);
        offset("L19C4", 16, -13);
        offset("L19C3", 13, -13);
        offset("L19C2", 10, -13);
        offset("L19C1", 6, -13);
        
        offsetLinha(18, 1, 6, 21, -14);
        offset("L18C5", 18, -14);
        offset("L18C4", 16, -14);
        offset("L18C3", 13, -14);
        offset("L18C2", 10, -14);
        offset("L18C1", 6, -14);
        
        offsetLinha(17, 1, 6, 21, -15);
        offset("L17C5", 18, -15);
        offset("L17C4", 16, -15);
        offset("L17C3", 13, -15);
        offset("L17C2", 10, -15);
        offset("L17C1", 6, -15);
        
        offset("L20C17", 44, -13);
        offset("L20C18", 47, -13);
        offset("L20C19", 49, -13);
        offset("L20C20", 51, -13);
        offset("L20C21", 53, -13);
        offset("L20C22", 55, -13);
        
        offset("L21C17", 44, -13);
        offset("L21C18", 47, -13);
        offset("L21C19", 49, -13);
        offset("L21C20", 51, -13);
        offset("L21C21", 53, -13);
        offset("L21C22", 55, -13);
        
    }

    public GradeTabuleiro() {
        inicializarMapa();
    }

    public Set<String> getCasasCorredor() {
        return CASAS_CORREDOR;
    }

    public void destacarCasas(List<String> nomes) {
        casasDestacadas.clear();
        portasDestacadas.clear();

        for (String nome : nomes) {
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
        return casasPorta.contains(nomeCasa);
    }

    public String getComodoDaPorta(String nomeCasa) {
        return portaParaComodo.get(nomeCasa);
    }

    public Rectangle getRetanguloCasaOuComodo(
            String nomeCasa
    ) {
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
        g2.setFont(
                new Font("Arial", Font.PLAIN, 8)
        );

        for (Map.Entry<String, Rectangle> e
                : mapaCasas.entrySet()) {
            Rectangle r = e.getValue();
            boolean porta =
                    casasPorta.contains(e.getKey());
            if (porta) {
                g2.setColor(
                        new Color(255, 140, 0, 90)
                );
            } else {
                g2.setColor(
                        new Color(50, 50, 255, 60)
                );
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

        g2.setFont(
                new Font("Arial", Font.BOLD, 10)
        );

        g2.drawString(
                "GX=" + GRADE_X +
                " GY=" + GRADE_Y +
                " CW=" + LARGURA_CELULA +
                " CH=" + ALTURA_CELULA,
                TABULEIRO_X + 5,
                TABULEIRO_Y + TABULEIRO_ALTURA - 5
        );
    }

    private void inicializarMapa() {
        for (String nome : CASAS_CORREDOR) {
            int linha = extrairLinha(nome);
            int coluna = extrairColuna(nome);
            mapaCasas.put(
                    nome,
                    getRetanguloCelula(
                            linha,
                            coluna
                    )
            );
        }

        for (String[] entrada : PORTAS_COMODOS) {
            String nomeComodo = entrada[0];
            for (int i = 1; i < entrada.length; i++) {
                String nomeCasa = entrada[i];
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

    public static String nomeCelula(
            int linha,
            int coluna
    ) {

        return "L" + linha + "C" + coluna;
    }

    public Rectangle getRetanguloCelula(
            int linha,
            int coluna
    ) {

    	int x =
                GRADE_X +
                coluna * LARGURA_CELULA;

        int y =
                GRADE_Y +
                linha * ALTURA_CELULA;

        String nome = nomeCelula(linha, coluna);

        x += OFFSET_X.getOrDefault(nome, 0);

        y += OFFSET_Y.getOrDefault(nome, 0);

        return new Rectangle(
                x,
                y,
                LARGURA_CELULA,
                ALTURA_CELULA
        );
    }

    public String getCasaClicada(
            int px,
            int py
    ) {
        for (Map.Entry<String, Rectangle> e
                : mapaCasas.entrySet()) {
            if (e.getValue().contains(px, py)) {
                return e.getKey();
            }
        }
        return null;
    }

    private int extrairLinha(String nome) {
        int cIndex = nome.indexOf("C");
        return Integer.parseInt(
                nome.substring(1, cIndex)
        );
    }

    private int extrairColuna(String nome) {
        int cIndex = nome.indexOf("C");
        return Integer.parseInt(
                nome.substring(cIndex + 1)
        );
    }
}
