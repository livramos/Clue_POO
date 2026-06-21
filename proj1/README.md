# Clue - Jogo de Detetive em Java

Este repositório contém uma implementação em Java do jogo **Clue**, também conhecido como **Detetive**. O projeto foi desenvolvido como trabalho acadêmico de Programação Orientada a Objetos, com foco na modelagem das regras do jogo, estruturação das classes e construção de uma interface gráfica simples para interação com o usuário.

## Objetivo do Projeto

O objetivo principal do projeto é simular o funcionamento básico do jogo Clue, permitindo que os jogadores se movimentem pelo tabuleiro, rolem dados, acessem cômodos e participem da dinâmica de investigação do jogo.

Nesta etapa do desenvolvimento, o foco principal foi implementar:

- A estrutura visual do tabuleiro;
- A movimentação dos peões;
- O controle de turnos;
- A rolagem de dados;
- A indicação de casas possíveis para movimentação;
- A interface lateral com informações do jogo;
- A ativação e desativação de botões conforme o momento da jogada.

## Tecnologias Utilizadas

- Java
- Java Swing
- Java AWT / Java2D
- Eclipse IDE
- Git e GitHub

## Estrutura Geral do Projeto

O projeto está organizado seguindo uma divisão básica entre as camadas de modelo, visão e controle/fachada.

Uma estrutura simplificada esperada é:

```text
src/
├── model/
│   ├── Carta.java
│   ├── Casa.java
│   ├── Jogador.java
│   ├── TipoCarta.java
│   └── ClueFacade.java
│
├── view/
│   ├── JanelaPrincipal.java
│   ├── PainelTabuleiro.java
│   ├── PainelLateral.java
│   └── GradeTabuleiro.java
│
└── main/
    └── Main.java 
