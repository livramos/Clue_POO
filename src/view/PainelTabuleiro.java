package view;
 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.awt.Rectangle;
 
import javax.swing.ImageIcon;
import javax.swing.JPanel;
 
import model.ClueFacade;
 
public class PainelTabuleiro extends JPanel {
 
    private Image imagemTabuleiro;
    private GradeTabuleiro grade;
    private PainelLateral painelLateral;
    private Map<String, String> posicoesPioes = new HashMap<String, String>();
    private Map<String, Color> coresPioes = new HashMap<String, Color>();
 
    private static final int TABULEIRO_X = 20;
    private static final int TABULEIRO_Y = 20;
    private static final int TABULEIRO_LARGURA = 850;
    private static final int TABULEIRO_ALTURA = 820;
 
    public PainelTabuleiro() {
        setPreferredSize(new Dimension(TABULEIRO_X * 2 + TABULEIRO_LARGURA,
                                      TABULEIRO_Y * 2 + TABULEIRO_ALTURA));
        setBackground(Color.GRAY);
 
        ImageIcon icone = new ImageIcon("imagens/Tabuleiros/Tabuleiro-Original.JPG");
        imagemTabuleiro = icone.getImage();
 
        grade = new GradeTabuleiro();
        
        coresPioes.put("Srta. Scarlet",    Color.RED);
        coresPioes.put("Coronel Mostarda", Color.YELLOW);
        coresPioes.put("Sra. White",       Color.WHITE);
        coresPioes.put("Sr. Green",        new Color(30, 160, 30));
        coresPioes.put("Sra. Peacock",     Color.BLUE);
        coresPioes.put("Professor Plum",   new Color(120, 40, 160));
        
        registrarCliqueMouse();
    }
 
    public void setPainelLateral(PainelLateral painelLateral) {
        this.painelLateral = painelLateral;
    }
 
    public void destacarCasasAlcancaveis(List<String> casas) {
        grade.destacarCasas(casas);
        repaint();
    }
 
    public void limparDestaques() {
        grade.limparDestaques();
        repaint();
    }
    
    public void moverPiao(String jogador, String casa) {
        posicoesPioes.put(jogador, casa);
        repaint();
    }
 
    private void registrarCliqueMouse() {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tratarClique(e.getX(), e.getY());
            }
        });
    }
 
    private void tratarClique(int px, int py) {
        String casaClicada = grade.getCasaClicada(px, py);
        if (casaClicada == null) {
            return;
        }
 
        try {
            ClueFacade.getInstancia().moverJogadorAtual(casaClicada);
            grade.limparDestaques();
            repaint();
            if (painelLateral != null) {
                painelLateral.onMovimentoConcluido();
            }
        } catch (IllegalArgumentException ex) {
        }
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
 
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
 
        if (imagemTabuleiro != null) {
            g2.drawImage(imagemTabuleiro,
                         TABULEIRO_X, TABULEIRO_Y,
                         TABULEIRO_LARGURA, TABULEIRO_ALTURA,
                         null);
        }
 
        grade.desenharDestaques(g2);
        
        for (Map.Entry<String, String> entrada : posicoesPioes.entrySet()) {

            Rectangle r = grade.getRetanguloCasaOuComodo(entrada.getValue());
            if (r == null) continue;

            Color cor = coresPioes.get(entrada.getKey());
            if (cor == null) cor = Color.GRAY;

            int d = Math.min(r.width, r.height) - 6;
            int cx = r.x + (r.width - d) / 2;
            int cy = r.y + (r.height - d) / 2;

            g2.setColor(cor);
            g2.fillOval(cx, cy, d, d);

            g2.setColor(Color.BLACK);
            g2.drawOval(cx, cy, d, d);
        }
    }
}
