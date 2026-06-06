package view;

/**
 * Representa uma célula do tabuleiro, mapeando sua posição lógica (linha/coluna)
 * às coordenadas de pixel na imagem do tabuleiro.
 *
 * Cada célula conhece:
 *  - sua linha e coluna no grid (0-indexed)
 *  - o pixel (x, y) do seu canto superior esquerdo na imagem
 *  - o nome da "casa" do Model a que pertence (corredor ou cômodo)
 *
 * Para corredores, nomeCasa é algo como "C_12_7" (coluna 12, linha 7).
 * Para cômodos, nomeCasa é o nome exato usado no Model, ex: "Cozinha".
 */
public class Celula {

    // Dimensão de cada célula na imagem do tabuleiro (pixels)
    public static final int LARGURA  = 25;
    public static final int ALTURA   = 25;

    // Offset do canto superior esquerdo do tabuleiro dentro da janela
    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 0;

    private final int linha;
    private final int coluna;
    private final int pixelX;   // canto superior esquerdo
    private final int pixelY;
    private final String nomeCasa; // nome no Model

    public Celula(int linha, int coluna, String nomeCasa) {
        this.linha    = linha;
        this.coluna   = coluna;
        this.pixelX   = OFFSET_X + coluna * LARGURA;
        this.pixelY   = OFFSET_Y + linha   * ALTURA;
        this.nomeCasa = nomeCasa;
    }

    // --- Getters ---

    public int getLinha()    { return linha; }
    public int getColuna()   { return coluna; }
    public int getPixelX()   { return pixelX; }
    public int getPixelY()   { return pixelY; }
    public String getNomeCasa() { return nomeCasa; }

    /**
     * Retorna o centro X da célula — útil para centralizar o pião.
     */
    public int getCentroX() { return pixelX + LARGURA / 2; }

    /**
     * Retorna o centro Y da célula — útil para centralizar o pião.
     */
    public int getCentroY() { return pixelY + ALTURA / 2; }

    /**
     * Verifica se o ponto (mx, my) de um clique do mouse está dentro desta célula.
     */
    public boolean contemPonto(int mx, int my) {
        return mx >= pixelX && mx < pixelX + LARGURA
            && my >= pixelY && my < pixelY + ALTURA;
    }

    @Override
    public String toString() {
        return "Celula[" + linha + "," + coluna + " -> " + nomeCasa + " (" + pixelX + "," + pixelY + ")]";
    }
}
