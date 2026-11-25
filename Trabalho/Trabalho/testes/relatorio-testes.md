# Relatório dos testes manuais

Os testes seguem o roteiro da Parte III do enunciado (TM01 a TM23 e testes adicionais TA01 a TA10).

Os cenários com posição específica estão em `testes/cenarios/` e são carregados direto com `CARREGAR nome-do-arquivo.txt`. Neles os jogadores se chamam Ana (brancas) e Pedro (pretas). Quando o enunciado pede que a partida continue depois da captura, foi colocada uma peça preta extra em A7 (ou H4 no TM14), longe da jogada testada.

## Testes principais

| Teste | Cenário / comandos | Resultado obtido | Situação |
|---|---|---|---|
| TM01 | `NOVO Ana Pedro`, `ESTADO` | Tabuleiro 8x8 com 12 peças de cada cor nas posições do enunciado, `TURNO=BRANCAS`, `STATUS=EM_ANDAMENTO`. | APROVADO |
| TM02 | `MOVER A3 B4`, `ESTADO` | Peça saiu de A3 e foi para B4, A3 ficou vazia, turno passou para as pretas. | APROVADO |
| TM03 | `MOVER A3 C3` | Recusado com "o movimento precisa ser na diagonal". Tabuleiro igual e turno das brancas. | APROVADO |
| TM04 | `MOVER A3 C1` | Recusado com "a casa C1 esta ocupada". Nenhuma peça mexeu. | APROVADO |
| TM05 | `MOVER B6 A5` | Recusado com "a peca em B6 pertence ao adversario". Turno das brancas. | APROVADO |
| TM06 | `MOVER A3 Z9` | "Coordenada invalida: Z9". Nada mudou e o programa continuou. | APROVADO |
| TM07 | `MOVER A3 B4`, `MOVER B6 A5`, `ESTADO` | Os dois movimentos foram aceitos, branca em B4, preta em A5, turno voltou às brancas. | APROVADO |
| TM08 | `MOVER A3 B4`, `MOVER C3 D4` | Segundo movimento recusado (peça do adversário). Turno das pretas e tabuleiro sem alteração. | APROVADO |
| TM09 | `TM09-captura-simples.txt`, `MOVER C3 E5` | Branca terminou em E5, D4 ficou vazia, turno das pretas. | APROVADO |
| TM10 | `TM10-captura-obrigatoria.txt`, `MOVER C3 B4` | Recusado com "captura obrigatoria de 1 peca(s). Opcoes: C3 E5". Peça continuou em C3. | APROVADO |
| TM11 | `TM11-captura-multipla.txt`, `MOVER C3 E5`, `MOVER E5 G7` | Após o primeiro salto o turno continuou com as brancas e D4 ainda aparecia no `ESTADO`. Após o segundo, D4 e F6 foram removidas, branca em G7, turno das pretas. | APROVADO |
| TM12 | `TM12-captura-para-tras.txt`, `MOVER E5 C3` | Captura para trás aceita, branca em C3, D4 removida. | APROVADO |
| TM13 | `TM13-maior-captura.txt`, `MOVER C3 A5`, depois `MOVER C3 E5` e `MOVER E5 G7` | C3 A5 (uma captura) recusado com "captura obrigatoria de 2 peca(s)". A sequência de duas capturas foi aceita e o turno só mudou no fim. | APROVADO |
| TM14 | `TM14-promocao.txt`, `MOVER A7 B8` | Peça promovida, aparece como `B` em B8, turno das pretas. | APROVADO |
| TM15 | `TM15-dama-longa.txt`, `MOVER C3 F6` | Dama foi para F6, C3 vazia. | APROVADO |
| TM16 | `TM16-dama-para-tras.txt`, `MOVER F6 C3` | Dama voltou para C3, F6 vazia. | APROVADO |
| TM17 | `TM17-dama-bloqueada.txt`, `MOVER C3 F6` | Recusado, a dama não passou pela peça branca em E5. Tabuleiro e turno iguais. | APROVADO |
| TM18 | `TM18-vitoria-eliminacao.txt`, `MOVER C3 E5` | Última preta capturada, "Fim de jogo! Vencedor: Ana (Brancas)", `STATUS=VITORIA_BRANCAS`. Novo `MOVER` recusado. | APROVADO |
| TM19 | `TM19-sem-movimentos.txt`, `ESTADO` | Vez das pretas com uma peça em A3 travada pelas brancas em B2 e C1. Ao carregar o programa já mostrou vitória das brancas, `STATUS=VITORIA_BRANCAS`, e recusou novos movimentos. | APROVADO |
| TM20 | `NOVO Ana Pedro`, `MOVER A3 B4`, `SALVAR partida.txt` | Arquivo `partidas/partida.txt` criado com nomes, `TURNO=PRETAS`, status e tabuleiro. | APROVADO |
| TM21 | Programa fechado e aberto de novo, `CARREGAR partida.txt`, `ESTADO` | B4 ocupada, A3 vazia, turno das pretas, nomes mantidos. `MOVER B6 A5` foi aceito em seguida. | APROVADO |
| TM22 | `CARREGAR inexistente.txt` | Mensagem de arquivo não encontrado, partida aberta mantida, programa continuou. | APROVADO |
| TM23 | `CARREGAR invalido.txt` (arquivo com texto qualquer) | "conteudo invalido", a partida anterior continuou igual (conferido com `ESTADO`). | APROVADO |

## Testes adicionais

| Teste | Situação testada | Resultado obtido | Situação |
|---|---|---|---|
| TA01 | Comando `PULAR` | "Comando desconhecido", programa continuou. | APROVADO |
| TA02 | `MOVER` e `MOVER A3` | Mostrou o uso correto do comando, nada mudou. | APROVADO |
| TA03 | `MOVER A3 A4` (A4 é clara) | Recusado: "as pecas so andam pelas casas escuras". | APROVADO |
| TA04 | `MOVER D4 E5` na posição inicial | Recusado: "nao existe peca em D4". | APROVADO |
| TA05 | `TA05-volta-sem-captura.txt`, `MOVER D4 C3` | Peça comum não andou para trás. `MOVER D4 E5` foi aceito depois. | APROVADO |
| TA06 | `MOVER` depois do fim do TM18 | Recusado: "a partida ja terminou". | APROVADO |
| TA07 | `CARREGAR vazio.txt` | "o arquivo vazio.txt esta vazio", partida mantida. | APROVADO |
| TA08 | `novo ana pedro`, `mover a3 b4`, `estado`, `sair` | Todos reconhecidos. | APROVADO |
| TA09 | `SALVAR` entre os dois saltos do TM11 | Recusado sem criar arquivo; a captura continuou normalmente. | APROVADO |
| TA10 | `NOVO Ana Pedro` depois do fim do TM18 | Tabuleiro inicial, turno das brancas, `EM_ANDAMENTO`. | APROVADO |

## Testes extras feitos pelo grupo

Além do roteiro, foram testadas situações que apareceram durante o desenvolvimento:

- Peça comum que passa pela última linha no meio de uma captura e termina fora dela: não é promovida (B6 captura C7 indo para D8 e depois E7 indo para F6, termina como `b`).
- Captura múltipla de dama a distância (dama em A1 capturando C3 e F6).
- Sequência de quatro capturas que volta para a casa de origem (C3, E5, C7, A5, C3).
- Tentar continuar a captura com outra peça ou para uma casa errada: recusado, com as opções válidas na mensagem.
- `SALVAR ../fora.txt` e `CARREGAR` com caminho: recusados, o programa só aceita nome de arquivo.

## Problemas encontrados

Na primeira versão do projeto o código rodava no VS Code mas tinha problemas para abrir no Eclipse, e a classe `Partida` concentrava quase toda a lógica. O projeto foi reorganizado (classe `RegrasMovimento`, movimentos dentro de `PecaComum` e `Dama`, `Tabuleiro` protegido) e os testes acima foram refeitos depois da mudança.

Não ficou nenhum teste reprovado.
