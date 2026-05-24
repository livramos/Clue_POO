package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.ClueFacade;

public class PainelTabuleiro extends JPanel {

    private Image imagemTabuleiro;
    private GradeTabuleiro grade;
    private PainelLateral painelLateral;

    private Map<String, String> posicoesPioes;
    private Map<String, Color> coresPioes;

    private static final int TABULEIRO_X = 20;
    private static final int TABULEIRO_Y = 20;
    private static final int TABULEIRO_LARGURA = 850;
    private static final int TABULEIRO_ALTURA = 820;

    public PainelTabuleiro() {
        setPreferredSize(
                new Dimension(
                        TABULEIRO_X * 2 + TABULEIRO_LARGURA,
                        TABULEIRO_Y * 2 + TABULEIRO_ALTURA
                )
        );

        setBackground(Color.GRAY);

        imagemTabuleiro =
                new ImageIcon("imagens/Tabuleiros/Tabuleiro-Clue-A.jpg").getImage();

        grade = new GradeTabuleiro();

        posicoesPioes = new HashMap<String, String>();
        coresPioes = new HashMap<String, Color>();

        coresPioes.put("Srta. Scarlet", Color.RED);
        coresPioes.put("Coronel Mostarda", Color.YELLOW);
        coresPioes.put("Sra. White", Color.WHITE);
        coresPioes.put("Sr. Green", new Color(30, 160, 30));
        coresPioes.put("Sra. Peacock", Color.BLUE);
        coresPioes.put("Professor Plum", new Color(120, 40, 160));

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
        System.out.println("Clique em pixel: x=" + px + " y=" + py);

        String destino = grade.getCasaClicada(px, py);

        System.out.println("Destino detectado: " + destino);

        if (destino == null) {
            return;
        }

        if (grade.ePorta(destino)) {
            destino = grade.getComodoDaPorta(destino);
        }

        System.out.println("Destino final: " + destino);

        tentarMoverJogadorAtual(destino);
    }

    private void tentarMoverJogadorAtual(String destino) {
        try {
            ClueFacade facade = ClueFacade.getInstancia();

            String jogadorAtual = facade.getJogadorAtual();

            facade.moverJogadorAtual(destino);

            moverPiao(jogadorAtual, destino);

            grade.limparDestaques();

            if (painelLateral != null) {
                painelLateral.onMovimentoConcluido();
            }

            repaint();

        } catch (IllegalArgumentException ex) {
            /*
             * Movimento inválido:
             * não move, não troca jogador e não mostra mensagem,
             * conforme o enunciado pediu.
             */
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        desenharFundo(g2);
        desenharImagemTabuleiro(g2);

        grade.desenharDestaques(g2);

        desenharPioes(g2);
    }

    private void desenharFundo(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void desenharImagemTabuleiro(Graphics2D g2) {
        if (imagemTabuleiro != null) {
            g2.drawImage(
                    imagemTabuleiro,
                    TABULEIRO_X,
                    TABULEIRO_Y,
                    TABULEIRO_LARGURA,
                    TABULEIRO_ALTURA,
                    null
            );
        }
    }

    private void desenharPioes(Graphics2D g2) {
        for (Map.Entry<String, String> entrada : posicoesPioes.entrySet()) {
            String jogador = entrada.getKey();
            String casa = entrada.getValue();

            Rectangle r = grade.getRetanguloCasaOuComodo(casa);

            if (r == null) {
                continue;
            }

            Color cor = coresPioes.get(jogador);

            if (cor == null) {
                cor = Color.GRAY;
            }

            int d = calcularTamanhoPiao(casa, r);

            int cx = r.x + (r.width - d) / 2;
            int cy = r.y + (r.height - d) / 2;

            g2.setColor(cor);
            g2.fillOval(cx, cy, d, d);

            g2.setColor(Color.BLACK);
            g2.drawOval(cx, cy, d, d);
        }
    }

    private int calcularTamanhoPiao(String casa, Rectangle r) {
        if (casa != null && casa.startsWith("COMODO_")) {
            return 24;
        }

        int d = Math.min(r.width, r.height) - 6;

        if (d < 10) {
            d = 10;
        }

        if (d > 24) {
            d = 24;
        }

        return d;
    }
}