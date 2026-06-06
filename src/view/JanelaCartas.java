package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.ClueFacade;

public class JanelaCartas extends JFrame {

    public JanelaCartas(String nomeJogador, List<String> cartas) {
        setTitle("Clue - " + nomeJogador.toUpperCase() + "'S Cards");
        setSize(540, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        ClueFacade facade = ClueFacade.getInstancia();

        List<String> comodos = new ArrayList<String>();
        List<String> armas = new ArrayList<String>();
        List<String> suspeitos = new ArrayList<String>();

        List<String> nomesComodos = facade.getComodos();
        List<String> nomesArmas = facade.getArmas();
        List<String> nomesSuspeitos = facade.getSuspeitos();

        for (String carta : cartas) {
            if (nomesComodos.contains(carta)) {
                comodos.add(carta);
            } else if (nomesArmas.contains(carta)) {
                armas.add(carta);
            } else if (nomesSuspeitos.contains(carta)) {
                suspeitos.add(carta);
            }
        }

        JPanel painelPrincipal = new JPanel(new GridLayout(0, 1, 12, 12));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        if (!comodos.isEmpty()) {
            painelPrincipal.add(criarSecaoCartas("Comodo(s)", comodos));
        }

        if (!armas.isEmpty()) {
            painelPrincipal.add(criarSecaoCartas("Arma(s)", armas));
        }

        if (!suspeitos.isEmpty()) {
            painelPrincipal.add(criarSecaoCartas("Suspeito(s)", suspeitos));
        }

        JScrollPane scroll = new JScrollPane(painelPrincipal);
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel criarSecaoCartas(String titulo, List<String> cartas) {
        JPanel secao = new JPanel(new BorderLayout(10, 10));

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        labelTitulo.setPreferredSize(new Dimension(90, 150));

        JPanel painelCartas = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));

        for (String carta : cartas) {
            painelCartas.add(criarComponenteCarta(carta));
        }

        secao.add(labelTitulo, BorderLayout.WEST);
        secao.add(painelCartas, BorderLayout.CENTER);

        return secao;
    }

    private JPanel criarComponenteCarta(String nomeCarta) {
        JPanel painelCarta = new JPanel(new BorderLayout(5, 5));
        painelCarta.setPreferredSize(new Dimension(120, 165));

        JLabel labelImagem = new JLabel();
        labelImagem.setHorizontalAlignment(JLabel.CENTER);
        labelImagem.setVerticalAlignment(JLabel.CENTER);

        ImageIcon imagem = carregarImagemCarta(nomeCarta);

        if (imagem != null) {
            Image imagemRedimensionada =
                    imagem.getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH);

            labelImagem.setIcon(new ImageIcon(imagemRedimensionada));
        } else {
            labelImagem.setText(nomeCarta);
        }

        JLabel labelNome = new JLabel(nomeCarta, JLabel.CENTER);
        labelNome.setFont(new Font("Arial", Font.PLAIN, 11));

        painelCarta.add(labelImagem, BorderLayout.CENTER);
        painelCarta.add(labelNome, BorderLayout.SOUTH);

        return painelCarta;
    }

    private ImageIcon carregarImagemCarta(String nomeCarta) {
        String caminho = getCaminhoImagemCarta(nomeCarta);

        if (caminho == null) {
            return null;
        }

        URL url = getClass().getResource(caminho);

        if (url == null) {
            System.out.println("Imagem não encontrada: " + caminho);
            return null;
        }

        return new ImageIcon(url);
    }

    private String getCaminhoImagemCarta(String nomeCarta) {
        // CÔMODOS
        if (nomeCarta.equals("Biblioteca")) return "/imagens/Comodos/Biblioteca.jpg";
        if (nomeCarta.equals("Cozinha")) return "/imagens/Comodos/Cozinha.jpg";
        if (nomeCarta.equals("Entrada")) return "/imagens/Comodos/Entrada.jpg";
        if (nomeCarta.equals("Escritorio")) return "/imagens/Comodos/Escritorio.jpg";
        if (nomeCarta.equals("Jardim de Inverno")) return "/imagens/Comodos/JardimInverno.jpg";
        if (nomeCarta.equals("Sala de Estar")) return "/imagens/Comodos/SalaDeEstar.jpg";
        if (nomeCarta.equals("Sala de Jantar")) return "/imagens/Comodos/SalaDeJantar.jpg";
        if (nomeCarta.equals("Sala de Musica")) return "/imagens/Comodos/SalaDeMusica.jpg";
        if (nomeCarta.equals("Salao de Jogos")) return "/imagens/Comodos/SalaoDeJogos.jpg";

        // SUSPEITOS
        if (nomeCarta.equals("Sr. Green")) return "/imagens/Suspeitos/Green.jpg";
        if (nomeCarta.equals("Coronel Mostarda")) return "/imagens/Suspeitos/Mustard.jpg";
        if (nomeCarta.equals("Sra. Peacock")) return "/imagens/Suspeitos/Peacock.jpg";
        if (nomeCarta.equals("Professor Plum")) return "/imagens/Suspeitos/Plum.jpg";
        if (nomeCarta.equals("Srta. Scarlet")) return "/imagens/Suspeitos/Scarlet.jpg";
        if (nomeCarta.equals("Sra. White")) return "/imagens/Suspeitos/White.jpg";

        // ARMAS
        if (nomeCarta.equals("Corda")) return "/imagens/Armas/Corda.jpg";
        if (nomeCarta.equals("Cano de Chumbo")) return "/imagens/Armas/Cano.jpg";
        if (nomeCarta.equals("Faca")) return "/imagens/Armas/Faca.jpg";
        if (nomeCarta.equals("Chave Inglesa")) return "/imagens/Armas/ChaveInglesa.jpg";
        if (nomeCarta.equals("Castiçal")) return "/imagens/Armas/Castical.jpg";
        if (nomeCarta.equals("Revólver")) return "/imagens/Armas/Revolver.jpg";

        return null;
    }
}