package view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class JanelaCartaRefutada extends JFrame {

    public JanelaCartaRefutada(String jogadorQueRefutou, String nomeCarta) {
        setTitle("Carta mostrada");
        setSize(320, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel painel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel(
                jogadorQueRefutou + " mostrou a carta:",
                SwingConstants.CENTER
        );

        JLabel nome = new JLabel(nomeCarta, SwingConstants.CENTER);

        JLabel imagem = new JLabel();
        imagem.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon icone = carregarImagemCarta(nomeCarta);

        if (icone != null) {
            Image img = icone.getImage().getScaledInstance(220, 320, Image.SCALE_SMOOTH);
            imagem.setIcon(new ImageIcon(img));
        } else {
            imagem.setText("Imagem não encontrada: " + nomeCarta);
        }

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(imagem, BorderLayout.CENTER);
        painel.add(nome, BorderLayout.SOUTH);

        setContentPane(painel);
    }

    private ImageIcon carregarImagemCarta(String nomeCarta) {
        String caminho = getCaminhoImagemCarta(nomeCarta);

        if (caminho == null) {
            return null;
        }

        java.net.URL url = getClass().getResource(caminho);

        if (url == null) {
            System.out.println("Imagem não encontrada no caminho: " + caminho);
            return null;
        }

        return new ImageIcon(url);
    }

    private String getCaminhoImagemCarta(String nomeCarta) {
        if (nomeCarta == null) {
            return null;
        }

        // =========================
        // SUSPEITOS
        // =========================
        if (nomeCarta.equals("Srta. Scarlet")) {
            return "/imagens/Suspeitos/Scarlet.jpg";
        }

        if (nomeCarta.equals("Coronel Mostarda")) {
            return "/imagens/Suspeitos/Mustard.jpg";
        }

        if (nomeCarta.equals("Sra. White")) {
            return "/imagens/Suspeitos/White.jpg";
        }

        if (nomeCarta.equals("Sr. Green")) {
            return "/imagens/Suspeitos/Green.jpg";
        }

        if (nomeCarta.equals("Sra. Peacock")) {
            return "/imagens/Suspeitos/Peacock.jpg";
        }

        if (nomeCarta.equals("Professor Plum")) {
            return "/imagens/Suspeitos/Plum.jpg";
        }

        // =========================
        // ARMAS
        // =========================
        if (nomeCarta.equals("Corda")) {
            return "/imagens/Armas/Corda.jpg";
        }

        if (nomeCarta.equals("Cano de Chumbo")) {
            return "/imagens/Armas/Cano.jpg";
        }

        if (nomeCarta.equals("Faca")) {
            return "/imagens/Armas/Faca.jpg";
        }

        if (nomeCarta.equals("Chave Inglesa")) {
            return "/imagens/Armas/ChaveInglesa.jpg";
        }

        if (nomeCarta.equals("Castiçal")) {
            return "/imagens/Armas/Castical.jpg";
        }

        if (nomeCarta.equals("Revólver")) {
            return "/imagens/Armas/Revolver.jpg";
        }

        // =========================
        // CÔMODOS
        // =========================
        if (nomeCarta.equals("Biblioteca")) {
            return "/imagens/Comodos/Biblioteca.jpg";
        }

        if (nomeCarta.equals("Cozinha")) {
            return "/imagens/Comodos/Cozinha.jpg";
        }

        if (nomeCarta.equals("Entrada")) {
            return "/imagens/Comodos/Entrada.jpg";
        }

        if (nomeCarta.equals("Escritorio")) {
            return "/imagens/Comodos/Escritorio.jpg";
        }

        if (nomeCarta.equals("Jardim de Inverno")) {
            return "/imagens/Comodos/JardimInverno.jpg";
        }

        if (nomeCarta.equals("Sala de Estar")) {
            return "/imagens/Comodos/SalaDeEstar.jpg";
        }

        if (nomeCarta.equals("Sala de Jantar")) {
            return "/imagens/Comodos/SalaDeJantar.jpg";
        }

        if (nomeCarta.equals("Sala de Musica")) {
            return "/imagens/Comodos/SalaDeMusica.jpg";
        }

        if (nomeCarta.equals("Salao de Jogos")) {
            return "/imagens/Comodos/SalaoDeJogos.jpg";
        }

        return null;
    }
}