# Clue - Jogo de Detetive em Java

Este repositório contém uma implementação em Java do jogo **Clue**, também conhecido como **Detetive**. O projeto foi desenvolvido como trabalho acadêmico de Programação Orientada a Objetos, com foco na modelagem das regras do jogo, estruturação das classes e construção de uma interface gráfica para interação com o usuário.

## Objetivo do Projeto

O objetivo principal do projeto é simular o funcionamento completo do jogo Clue, permitindo que os jogadores se movimentem pelo tabuleiro, rolem dados, acessem cômodos, façam palpites e acusações conforme as regras originais do jogo.

Nesta etapa do desenvolvimento, o foco foi:

- Implementação do padrão **Controller** (Singleton) para centralizar o controle do fluxo de turno
- Implementação do padrão **Observer** para comunicação entre modelo e visão
- Janelas de palpite, acusação, cartas e bloco de notas
- Controle de estado dos botões conforme o momento da jogada
- Integração completa entre as camadas Model, View e Controller

## Tecnologias Utilizadas

- Java
- Java Swing
- Java AWT / Java2D
- Eclipse IDE
- Git e GitHub

## Estrutura do Projeto

```text
src/
├── controller/
│   └── ClueController.java        # Singleton — máquina de estados do turno
│
├── model/
│   ├── Carta.java
│   ├── Casa.java
│   ├── CasaComDistancia.java
│   ├── ClueFacade.java            # Fachada + Observado
│   ├── ClueModel.java
│   ├── Dado.java
│   ├── FolhaNotas.java
│   ├── Jogador.java
│   ├── PiaoDeslocamento.java
│   ├── Tabuleiro.java
│   └── TipoCarta.java
│
├── observer/
│   ├── GerenciadorEventos.java    # Implementação do Observer
│   ├── Observado.java             # Interface do observado
│   └── Observador.java            # Interface do observador
│
└── view/
    ├── Celula.java
    ├── GradeTabuleiro.java
    ├── JanelaAcusacao.java
    ├── JanelaCartas.java
    ├── JanelaFolhasNotas.java
    ├── JanelaPalpite.java
    ├── JanelaTabuleiro.java
    ├── Main.java
    ├── MapaTabuleiro.java
    ├── PainelLateral.java
    └── PainelTabuleiro.java
```

## Padrões de Design

### Singleton
`ClueController` e `ClueFacade` possuem instância única acessada via `getInstancia()`, garantindo um ponto central de controle durante toda a execução.

### Facade
`ClueFacade` é o único ponto de acesso ao modelo. A camada de visão nunca acessa `ClueModel` diretamente.

### Observer
`ClueFacade` implementa `Observado` e notifica os observadores registrados a cada evento relevante:

| Evento | Quem reage |
|--------|-----------|
| `PEAO_MOVIDO` | `PainelTabuleiro` atualiza posição do peão |
| `DADOS_LANCADOS` | `PainelTabuleiro` destaca casas alcançáveis |
| `TURNO_ALTERADO` | `PainelTabuleiro` limpa destaques |
| `PALPITE_REALIZADO` | — |
| `ACUSACAO_REALIZADA` | — |
| `JOGADOR_ELIMINADO` | — |

### Controller
`ClueController` é uma máquina de estados com 6 fases por turno:

```
INICIO_TURNO → AGUARDANDO_MOVIMENTO → APOS_MOVIMENTO_COMODO  → FIM_DE_TURNO
                                    → APOS_MOVIMENTO_CORREDOR → FIM_DE_TURNO
                                                                      ↓
                                                              JOGO_ENCERRADO
```

## Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/livramos/Clue_POO.git
   ```
2. Importe o projeto no Eclipse como **Java Project existente**
3. Certifique-se de que a pasta `src/imagens/` está incluída no build path
4. Execute `src/view/Main.java`

## Iterações

| Iteração | Período | Entregas |
|----------|---------|---------|
| 1ª | 27/04 – 10/05/2026 | Modelo base, regras, testes unitários |
| 2ª | 11/05 – 24/05/2026 | Interface gráfica, tabuleiro, movimentação de peões |
| 3ª | 25/05 – 07/06/2026 | Controller (Singleton), Observer, janelas de palpite/acusação/cartas/notas |
| 4ª | 08/06 – 21/06/2026 | Versão final |
