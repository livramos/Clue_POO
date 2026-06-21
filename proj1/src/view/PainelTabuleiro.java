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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import controller.ClueController;
import model.ClueFacade;
import observer.GerenciadorEventos;
import observer.Observado;
import observer.Observador;

public class PainelTabuleiro extends JPanel implements Observador {

    private Image imagemTabuleiro;
    private GradeTabuleiro grade;

    // Controle interno para saber se o movimento foi apos lancar dados.
    private boolean dadosJaLancados = false;

    private Map<String, String> posicoesPioes;
    private Map<String, Color> coresPioes;

    private static final int TABULEIRO_X = 20;
    private static final int TABULEIRO_Y = 20;
    private static final int TABULEIRO_LARGURA = 850;
    private static final int TABULEIRO_ALTURA = 820;

    public PainelTabuleiro() {
        setPreferredSize(new Dimension(
                TABULEIRO_X * 2 + TABULEIRO_LARGURA,
                TABULEIRO_Y * 2 + TABULEIRO_ALTURA
        ));
        setBackground(Color.GRAY);

        imagemTabuleiro = new ImageIcon(
                getClass().getResource("/imagens/Tabuleiros/Tabuleiro-Clue-A.jpg")
        ).getImage();

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

        // Registro como observador da fachada.
        ClueFacade.getInstancia().add(this);
    }

    @Override
    public void notify(Observado o) {
        int tipo = o.get(0);

        if (tipo == GerenciadorEventos.PEAO_MOVIDO) {
            GerenciadorEventos g = (GerenciadorEventos) o;
            String jogador = g.getString(0);
            String casa = g.getString(1);

            moverPiao(jogador, casa);
        }
        else if (tipo == GerenciadorEventos.DADOS_LANCADOS) {
            dadosJaLancados = true;

            int dado1 = o.get(1);
            int dado2 = o.get(2);

            String casaAtual = ClueFacade.getInstancia().getCasaAtualDoJogador();

            if (casaAtual != null) {
                List<String> alcancaveis = ClueFacade.getInstancia()
                        .getCasasAlcancaveis(casaAtual, new int[]{dado1, dado2});

                destacarCasasAlcancaveis(alcancaveis);
            }
        }
        else if (tipo == GerenciadorEventos.TURNO_ALTERADO) {
            dadosJaLancados = false;
            limparDestaques();
        }
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
        casa = normalizarCasaParaDesenho(casa);

        posicoesPioes.put(jogador, casa);

        // Depois que o peao se move, remove os quadradinhos de destaque.
        grade.limparDestaques();

        repaint();
    }

    private String normalizarCasaParaDesenho(String casa) {
        if (casa == null) {
            return null;
        }

        // Se o Model/Facade mandar uma porta, desenha o peao dentro do comodo.
        if (grade.ePorta(casa)) {
            String comodo = grade.getComodoDaPorta(casa);

            if (comodo != null) {
                return comodo;
            }
        }

        return casa;
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


        System.out.println("Destino final enviado ao Controller: " + destino);

        ClueController.getInstancia().onJogadorMoveu(destino);
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

        // Agora o modo debug da grade realmente aparece.
        grade.desenharDebugSeAtivo(g2);

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
        Map<String, List<String>> jogadoresPorCasa =
                new HashMap<String, List<String>>();

        for (Map.Entry<String, String> entrada : posicoesPioes.entrySet()) {
            String jogador = entrada.getKey();
            String casa = entrada.getValue();

            if (!jogadoresPorCasa.containsKey(casa)) {
                jogadoresPorCasa.put(casa, new ArrayList<String>());
            }

            jogadoresPorCasa.get(casa).add(jogador);
        }

        for (Map.Entry<String, List<String>> entrada : jogadoresPorCasa.entrySet()) {
            String casa = entrada.getKey();
            List<String> jogadoresNaCasa = entrada.getValue();

            Rectangle r = grade.getRetanguloCasaOuComodo(casa);

            if (r == null) {
                continue;
            }

            int d = calcularTamanhoPiao(casa, r);

            for (int i = 0; i < jogadoresNaCasa.size(); i++) {
                String jogador = jogadoresNaCasa.get(i);

                Color cor = coresPioes.get(jogador);

                if (cor == null) {
                    cor = Color.GRAY;
                }

                int baseX = r.x + (r.width - d) / 2;
                int baseY = r.y + (r.height - d) / 2;

                int deslocamentoX = 0;
                int deslocamentoY = 0;

                if (casa != null && casa.startsWith("COMODO_")) {
                    int[][] deslocamentos = {
                            { -18, -12 },
                            {  18, -12 },
                            { -18,  16 },
                            {  18,  16 },
                            { -36,   2 },
                            {  36,   2 }
                    };

                    int indice = i % deslocamentos.length;
                    deslocamentoX = deslocamentos[indice][0];
                    deslocamentoY = deslocamentos[indice][1];
                }

                int cx = baseX + deslocamentoX;
                int cy = baseY + deslocamentoY;

                cx = limitar(cx, r.x + 2, r.x + r.width - d - 2);
                cy = limitar(cy, r.y + 2, r.y + r.height - d - 2);

                g2.setColor(cor);
                g2.fillOval(cx, cy, d, d);

                g2.setColor(Color.BLACK);
                g2.drawOval(cx, cy, d, d);
            }
        }
    }

    private int limitar(int valor, int minimo, int maximo) {
        if (valor < minimo) {
            return minimo;
        }

        if (valor > maximo) {
            return maximo;
        }

        return valor;
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