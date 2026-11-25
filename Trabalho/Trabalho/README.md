# Jogo de Damas em Java

Trabalho final da disciplina de Programação Orientada a Objetos.

## Integrantes

- Laura de Oliveira Campos dos Santos
- Maria Eduarda Meireles Amorim
- Sofia Oliveira Eufrasino
- Vanessa Lins da Silva Messias

## Descrição

Jogo de damas para dois jogadores no mesmo computador, jogado pelo console. Segue as regras brasileiras em tabuleiro 8x8 conforme o enunciado do trabalho:

- A1 é casa escura e as peças ficam só nas casas escuras.
- As brancas começam nas linhas 1, 2 e 3 e sobem; as pretas começam nas linhas 6, 7 e 8 e descem. As brancas jogam primeiro.
- Peça comum anda uma casa na diagonal para frente e captura para frente ou para trás.
- Captura obrigatória, captura múltipla e regra da maior captura.
- Promoção a dama quando a peça termina a jogada na última linha.
- Dama anda e captura a distância nas quatro diagonais.
- Vitória quando o adversário fica sem peças ou sem movimentos.
- Salvar e carregar partidas em arquivo de texto.

## Versão do Java

Java 8 ou superior. O projeto está configurado com nível de compilação 1.8 e foi testado com o JDK 21. Não usa Maven, Gradle nem bibliotecas externas.

## Como importar no Eclipse

1. Baixe o repositório (botão **Code > Download ZIP** no GitHub) e extraia, ou faça `git clone`.
2. No Eclipse, abra **File > Import > General > Existing Projects into Workspace** e clique em **Next**.
3. Em **Select root directory**, clique em **Browse** e escolha a pasta `JogoDamas` (a que contém o arquivo `.project`).
4. Confirme que o projeto **JogoDamas** está marcado e clique em **Finish**.

Se o Eclipse mostrar um aviso sobre a versão do JRE, basta ignorar: qualquer JDK 8 ou mais novo funciona.

## Como executar

**No Eclipse:** abra `src/aplicacao/Main.java`, clique com o botão direito e escolha **Run As > Java Application**. Os comandos são digitados na aba **Console**.

**Pelo terminal**, a partir da pasta `JogoDamas`:

Linux / macOS:

```bash
mkdir -p bin
javac -encoding UTF-8 -d bin src/modelo/*.java src/jogo/*.java src/persistencia/*.java src/aplicacao/*.java
java -cp bin aplicacao.Main
```

Windows:

```bat
mkdir bin
javac -encoding UTF-8 -d bin src\modelo\*.java src\jogo\*.java src\persistencia\*.java src\aplicacao\*.java
java -cp bin aplicacao.Main
```

É importante executar a partir da pasta do projeto, porque as partidas são salvas na pasta relativa `partidas/`. O Eclipse já faz isso automaticamente.

## Comandos

| Comando | O que faz |
|---|---|
| `NOVO Ana Pedro` | Começa uma partida. O primeiro nome joga com as brancas. |
| `MOVER A3 B4` | Move uma peça. Em captura múltipla, cada salto é um `MOVER` separado. |
| `ESTADO` | Mostra o tabuleiro no formato padrão (`TABULEIRO_INICIO` ... `STATUS=`). |
| `SALVAR partida.txt` | Salva a partida em `partidas/partida.txt`. Não funciona no meio de uma captura. |
| `CARREGAR partida.txt` | Carrega uma partida salva. |
| `AJUDA` | Mostra a lista de comandos. |
| `SAIR` | Encerra o programa. |

Os comandos e as coordenadas aceitam letras maiúsculas ou minúsculas.

No tabuleiro: `b` peça branca, `p` peça preta, `B` dama branca, `P` dama preta, `.` casa escura vazia.

## Estrutura do projeto

```
JogoDamas/
  src/
    aplicacao/     Main, InterpretadorComandos
    jogo/          Partida, Tabuleiro, RegrasMovimento, Movimento,
                   StatusPartida, ResultadoJogada, JogadaInvalidaException
    modelo/        Peca, PecaComum, Dama, Posicao, Salto, Jogador, Cor
    persistencia/  RepositorioPartida, ArquivoPartida
  partidas/        partidas salvas (tem um exemplo)
  testes/
    relatorio-testes.md
    cenarios/      arquivos usados nos testes
```

## Principais classes

- **Main**: só cria os objetos e inicia o interpretador.
- **InterpretadorComandos**: lê o que foi digitado, chama a partida ou o repositório e mostra as mensagens. Não tem regra do jogo.
- **Partida**: controla o turno, executa as jogadas (inclusive a captura em várias etapas), promove peças e verifica o fim de jogo.
- **RegrasMovimento**: monta a lista de jogadas permitidas para um jogador, já aplicando captura obrigatória e maior captura. Essa lista é usada tanto para validar o `MOVER` quanto para saber se o jogador ficou sem movimentos.
- **Tabuleiro**: guarda as 64 casas. Qualquer classe pode consultar, mas só as classes do pacote `jogo` conseguem alterar as peças.
- **Movimento**: uma jogada completa (origem, casas por onde a peça passa e peças capturadas).
- **Peca** (abstrata), **PecaComum** e **Dama**: cada tipo de peça sabe para onde pode andar, como captura e se deve ser promovida.
- **Posicao**: uma casa do tabuleiro, converte texto como `A3` em linha e coluna.
- **Salto**: um salto de captura (peça pulada e casa de destino).
- **Jogador** e **Cor**: nome e cor de cada participante; a cor sabe o sentido de avanço e a linha de promoção.
- **RepositorioPartida** (interface) e **ArquivoPartida**: salvar e carregar partidas.

## Os quatro pilares de POO no projeto

**Abstração.** `Peca` é uma classe abstrata que representa o que toda peça tem: uma cor e a capacidade de dizer seus movimentos, capturas e se deve virar dama, sem dizer como. `RepositorioPartida` é uma interface que define o contrato de salvar e carregar; o interpretador só conhece a interface.

**Encapsulamento.** Os atributos são privados e a maioria é `final`. O `Tabuleiro` não tem método público para colocar ou tirar peças: `moverPeca`, `removerPeca` e `promoverPeca` são visíveis só dentro do pacote `jogo`, então a única forma de mexer numa peça é pelo `Partida.mover`, que valida a jogada antes. `Posicao` e `Movimento` são imutáveis.

**Herança.** `PecaComum` e `Dama` estendem `Peca` e aproveitam a cor, o teste de adversária e a lista de diagonais da classe mãe.

**Polimorfismo.** `RegrasMovimento` percorre as peças como `Peca` e chama `destinosSemCaptura` e `saltosDeCaptura`, sem saber se é peça comum ou dama. A peça comum responde com uma casa para frente e captura de vizinha; a dama responde com movimento e captura a distância. Do mesmo jeito, `Partida` chama `devePromoverEm` e cada tipo responde do seu jeito. Não existe nenhum `if` perguntando o tipo da peça.

## Formato do arquivo de partida

Arquivo de texto (UTF-8) salvo na pasta `partidas/`:

```
JOGO_DE_DAMAS
BRANCAS=Ana
PRETAS=Pedro
TURNO=PRETAS
STATUS=EM_ANDAMENTO
TABULEIRO_INICIO
.p.p.p.p
p.p.p.p.
.p.p.p.p
........
.b......
..b.b.b.
.b.b.b.b
b.b.b.b.
TABULEIRO_FIM
```

O tabuleiro vai da linha 8 (em cima) até a linha 1, igual ao comando `ESTADO`. Linhas em branco são ignoradas.

Ao carregar, o programa confere o cabeçalho, as chaves, o turno, o status, o tamanho das linhas, os símbolos e se alguma peça está em casa clara. Se qualquer coisa estiver errada, mostra o erro e mantém a partida que estava aberta. Arquivo inexistente e arquivo vazio também são tratados.

## Como reproduzir os testes

O roteiro completo e os resultados estão em `testes/relatorio-testes.md`.

Os testes que precisam de uma posição específica usam os arquivos de `testes/cenarios/`. O comando `CARREGAR` procura o arquivo primeiro em `partidas/` e depois em `testes/cenarios/`, então basta digitar, por exemplo:

```
CARREGAR TM11-captura-multipla.txt
MOVER C3 E5
MOVER E5 G7
ESTADO
```

## Limitações conhecidas

- Não há empate (as regras oficiais de empate eram opcionais no enunciado).
- Não há desfazer jogada nem histórico.
- Os nomes dos jogadores não podem ter espaço, porque o comando `NOVO` separa os nomes por espaço.
- As mensagens do programa estão sem acento de propósito, para aparecerem corretamente em qualquer console.
