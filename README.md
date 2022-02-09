# TP2SistemasOperativosVersaoAvancada
O Problema de Corte Unidimensional (Cutting Stock Problem) consiste na realização do corte de peças pequenas a partir de uma peça de tamanho maior. Este corte deverá respeitar um determinado padrão de forma a maximizar o número de peças cortadas e minimizar o desperdício. Neste trabalho iremos assumir que as peças a cortar numa chapa de aço (denominado “placa”), são retângulos ou quadrados cuja largura é a mesma da placa. Ou seja, será apenas necessário um corte (vertical) para obter a peça desejada.

Exemplo: Sendo:

    - m o número de diferentes comprimentos de peças em que se pretende cortar cada uma das placas

    - maxComprimento o comprimento da placa, em metros

    - compPecas um vetor cujos elementos representam o comprimento (em metros) das peças a cortar

    - qtddPecas, um vetor cujos elementos representam as quantidades de peças a cortar de cada comprimento definido no vetor compPecas, respeitando a mesma ordem com os seguintes dados iniciais:

    - m = 4

    - maxComprimento = 12

    - compPecas = [3, 4, 5, 6]

    - qtddPecas = [2, 2, 1, 3]

Com esta informação conseguimos saber que se pretende gerar padrões de organização das peças na placa, que a placa tem 12m de comprimento, que as peças a cortar possuem quatro comprimentos diferentes (3, 4, 5 e 6 metros), e que pretendemos obter 2 peças de 3m, 2 peças de 4m, 1 peça de 5m, e 3 peças de 6m.

    - Lista items : 5 4 6 3 3 4 6 6
    - Corte em : | | | |
    - Desperdício : 3 0 2 6

O algoritmo AJR-E++ pertence à classe dos algoritmos evolutivos, sendo uma versão de um dos seus tipos - a Programação Evolutiva (https://en.wikipedia.org/wiki/Evolutionary_programming). Ao contrário dos algoritmos que trabalham apenas com uma solução, na programação evolutiva trabalha-se sempre com conjuntos de soluções (denominadas populações).

VERSAO BASE:

    A versão avançada é semelhante à versão base com as seguintes alterações:
1. De acordo com um parâmetro de entrada que representa uma percentagem do tempo total,
em cada múltiplo dessa percentagem de tempo procede-se à seguinte operação:
- a) Junta-se as populações de todas as threads numa única população.
- b) Seja a variável μ o tamanho de cada população em cada thread. Ordena-se a população
obtida no ponto 1.a) e escolhe-se uma nova população com as μ melhores soluções, de
acordo com a avaliação.
- c) Atualiza-se todas as threads com a nova população obtida no ponto 1.b)
2. A operação anterior não deve ser efetuada no final do tempo de execução do algoritmo
(último múltiplo da percentagem de tempo total).
