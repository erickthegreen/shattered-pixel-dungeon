# Tibia × Shattered Pixel Dungeon — Bloco 1: Andares 1–5

**Status:** proposta de design (nenhum monstro, arte ou APK deste bloco foi implementado por este documento).  
**Branch de trabalho:** `mod/tibia-bloco-1-planejamento`  
**Base preservada:** `mod/infernal-especiais-estavel`

## Objetivo

Substituir a rotação procedural da primeira região por criaturas reinterpretadas do Tibia, com sprites **originais** em pixel art coerente com Shattered Pixel Dungeon. Preservar a estrutura dos cinco andares, a experiência dos personagens, os equipamentos já existentes, o desempenho Android e os eventos/saídas indispensáveis ao jogo. Não voltar à versão com 160 monstros.

**Referências de identidade (Tibia, NÃO atributos de jogo):** Rat (20 HP/5 XP); Rotworm (65 HP/40 XP); Carrion Worm (145 HP/70 XP); Troll (50 HP/20 XP); Minotaur (100 HP/50 XP); Slime (150 HP/160 XP). Os números abaixo são valores **novos para o Shattered**, não cópias de Tibia.

Fontes: [Rat](https://www.tibiawiki.com.br/wiki/Rat), [Rotworm](https://www.tibiawiki.com.br/wiki/Rotworm), [Carrion Worm](https://www.tibiawiki.com.br/wiki/Carrion_Worm), [Troll](https://www.tibiawiki.com.br/wiki/Troll), [Minotaur](https://www.tibiawiki.com.br/wiki/Minotaur), [Slime](https://www.tibiawiki.com.br/wiki/Slime_(Criatura)).

## Rotações por andar

| Andar | Criaturas na rotação | Pesos relativos (soma = 100) | Alvo inicial procedural |
|---|---|---|---:|
| 1 | Rat, Rotworm, Carrion Worm | Rat 50 / Rotworm 35 / Carrion 15 | 6 |
| 2 | Rat, Rotworm, Carrion Worm, Troll | 30 / 30 / 20 / 20 | 7 |
| 3 | Rat, Rotworm, Carrion Worm, Troll, Minotaur | 15 / 20 / 20 / 20 / 25 | 8 |
| 4 | Rat, Rotworm, Carrion Worm, Troll, Minotaur, Slime | 10 / 15 / 20 / 15 / 25 / 15 | 9 |
| 5 | Dead Slime (chefe) + mistura de criaturas do 1–4 | Escolta limitada; sem respawn infinito durante chefe | chefe + até 3 |

**Contagem máxima:** 10 inimigos procedurais simultâneos nos andares 1–4, além de criaturas de missões e eventos; no andar 5, chefe + no máximo 3 escoltas e 2 fragmentos invocados temporários (não cumulativos com escoltas). A lotação depende de células livres, sem bloquear portas/escadas. Uma região pode exceder esse número pontualmente por eventos roteirizados; nesses casos, validar que o caminho continua acessível.

## Monstros: perfil proposto para balanceamento

Os atributos de HP, dano físico e defesa listados são **base** antes do multiplicador global que está atualmente na branch estável (inimigos: HP ×30, dano físico ×9, defesa/esquiva/precisão ×8). A experiência indicada é a que o jogador recebe no Shattered **sem multiplicador por vitória**. Esses números são alvos de teste, não valores definitivos.

| Criatura | HP base → efetivo com ×30 | Dano base → bruto com ×9 | DR base | XP | Função de combate |
|---|---:|---:|---:|---:|---|
| Rat | 12 → 360 | 1–2 → 9–18 | 0–1 | 1 | rápido, recua ao ficar com pouca vida |
| Rotworm | 28 → 840 | 2–5 → 18–45 | 1–2 | 2 | resistente, aproximação subterrânea telegráfica |
| Carrion Worm | 44 → 1.320 | 3–6 → 27–54 | 2–3 | 3 | mais resistente, golpe contaminante ocasional |
| Troll | 34 → 1.020 | 3–7 → 27–63 | 1–3 | 3 | golpe pesado e lentidão curta após erro |
| Minotaur | 64 → 1.920 | 4–9 → 36–81 | 3–5 | 5 | investida curta em linha reta, anunciada |
| Slime | 80 → 2.400 | 3–8 → 27–72 | 2–4 | 6 | divide-se uma vez; fragmentos não geram XP para impedir exploração |
| Dead Slime (boss) | 480 → 14.400 | 6–12 → 54–108 | 4–8 | 20 | veneno, onda de gosma, invocação limitada, duas fases |

**Cuidado com o balanceamento atual:** armas +100 e anéis melhorados mudam o dano real do jogador; a armadura +100 pode anular muitos golpes físicos. Não compensar com ataques invisíveis/instantâneos: priorizar dano elemental e ações claramente antecipadas, com teto de dano e espaço para esquiva. Excesso de HP não é um substituto para IA divertida.

### Comportamento individual

- **Rat:** aproxima, ataca e pode fugir quando está com menos de 25% da vida; baixa ameaça sozinho.
- **Rotworm:** avança devagar, prepara por um turno um salto curto em direção ao herói; sem teleporte dentro da safe zone.
- **Carrion Worm:** golpe com 20% de chance de aplicar um efeito contaminante leve (máximo 2 turnos); resistente a veneno, vulnerável ao fogo em pequena proporção.
- **Troll:** golpe pesado com antecipação visual; ao falhar, perde a próxima reação ou fica brevemente vulnerável; sem stunlock.
- **Minotaur:** investida em corredor reto a cada 5 turnos no máximo; indicador da direção antes de avançar; colisão impede atravessar paredes ou criaturas.
- **Slime:** pode dividir-se uma única vez aos 50% HP; no máximo 2 fragmentos ativos; efeito de ácido ao morrer, telegráfico e evitável.

## Chefe 5: Dead Slime (original do mod)

**Não apresentar como chefe oficial de Tibia.** É uma criatura nova inspirada no Slime, integrada ao quinto andar do Shattered, substituindo Goo na experiência visual e no encontro, mas **preservando** o fluxo obrigatório de progressão, vitória, recompensas e conquistas de Goo.

- **Identidade:** gosma de túmulo verde-escura, ossos dissolvidos visíveis e núcleo roxo pulsante; tamanho 32×32 pixels.
- **Fase I (100%–50% HP):** golpes físicos, marcação de 2–3 células com uma onda de ácido que explode após 1 turno, intervalo mínimo de 4 turnos entre especiais.
- **Fase II (abaixo de 50%):** núcleo exposto; invoca no máximo 2 gosmas cadavéricas temporárias e libera uma onda de veneno; ataques especiais alternados, jamais dois simultaneamente.
- **Telegráfica:** uma animação ou marcação de chão precisa mostrar onde não ficar, inclusive em resoluções pequenas.
- **Recompensas:** conceder XP definido, conservar o item/recompensa de progressão do boss Goo, liberar o acesso ao andar 6 e não duplicar drops ao salvar/carregar.

### Safe zone do andar 5

Uma antecâmara de cerca de **3×3 células** contígua à entrada da arena e acessível a partir da escada. Enquanto o chefe estiver inativo, o herói pode organizar a mochila/curar-se; nenhuma criatura pode ocupar essas células ou alvejar a área de fora. Ataques do herói também são bloqueados ao atravessar o limite, evitando exploração.

Ao entrar na arena e ativar o chefe, o acesso de retorno à antecâmara fica **temporariamente selado**; reabre após a vitória. A safe zone não pode quebrar caminhos para entrada/saída, objetivos, fechamento do boss ou teleporte e precisa funcionar após salvar/carregar.

## Produção de sprites e animações

Produzir **arte original**, reinterpretando silhuetas de Tibia no estilo Shattered; não copiar atlas gráficos de Tibia. PNG transparente, pixels definidos sem filtros de borrão, paleta limitada, tamanho consistente, ponto de apoio do sprite fixo para não tremer.

| Personagem | Tamanho inicial sugerido por frame | Características visuais |
|---|---|---|
| Rat | 16×16 | corpo marrom, focinho claro, rabo de 2–3 pixels |
| Rotworm | 20×20 | corpo segmentado terroso, boca discreta |
| Carrion Worm | 24×24 | verme maior, manchas púrpuras, mandíbulas |
| Troll | 24×24 | corpo esverdeado, clava rudimentar |
| Minotaur | 24×24 ou 32×32 | chifres, machado, silhueta reconhecível |
| Slime | 20×20 ou 24×24 | massa verde translúcida e núcleo simples |
| Dead Slime | 32×32 | gosma sombria, ossos, núcleo luminoso |

Para cada criatura, validar: **idle (2–3 quadros), caminhada (4–6), ataque (3–5), dano (1–2), morte (4–6)**. Para boss, também animações de carregamento, golpe ácido e mudança de fase. Aceitar sprites somente depois de visualizar em escala 1×, 2× e na tela 16:9 e vertical; nenhum quadro cortado, anatomia inconsistente, deslocamento de âncora ou bordas tremendo.

**Ordem de produção:** 1) Rat, 2) Rotworm, 3) Carrion Worm, 4) Troll, 5) Minotaur, 6) Slime, 7) Dead Slime. Cada espécie ganha classe, spritesheet, IA, XP, loot e teste isolado antes de entrar na rotação.

## Loot e XP

- **Rat:** 1 XP; chance modesta de carne ou moedas.
- **Rotworm:** 2 XP; moedas pequenas, comida ou material simples.
- **Carrion Worm:** 3 XP; chance de presa de verme como item de valor modesto.
- **Troll:** 3 XP; moedas e item simples ocasional.
- **Minotaur:** 5 XP; arma básica ou dinheiro raramente.
- **Slime:** 6 XP; chance baixa de componente alquímico.
- **Dead Slime:** 20 XP, recompensa da progressão original e 1 consumível bônus temático (não garantido se causar desequilíbrio).
- Fragmentos invocados dão 0 XP e 0 loot: impedem criar farm infinito.

## Implementação futura (não executada neste documento)

1. Criar as sete classes com o balanceamento acima, adaptando-as ao tipo `Mob` e criando sprite classes/atlases.
2. Alterar `MobSpawner.getMobRotation(depth)` para usar somente esses tipos nos andares 1–4, preservando a rotina de outras regiões.
3. Fazer o quinto andar inserir o novo boss **sem destruir** missões, conquistas, recompensas e transição para o sexto andar.
4. Implementar barreira da safe zone para colisão, projéteis e invocações; persistir a fase no save.
5. Testar seis classes, criar/salvar/carregar partidas, andar em todos os corredores, ganhar XP por kill, testar divisão, vencer o boss e entrar no andar 6.
6. Executar APK em emuladores Android 11 e 16 antes de publicar uma release de teste separada. **Compilar não basta**.

## Critérios de aceitação

- [ ] Andar 1: só Rat, Rotworm e Carrion Worm na rotação procedural
- [ ] Andares 2–4: distribuição conforme tabela
- [ ] Andar 5: Dead Slime + pequena escolta, sem corredor bloqueado
- [ ] Safe zone realmente segura, sem tiro de dentro e sem retorno no combate
- [ ] Cada morte de criatura normal concede XP; invocações não permitem farming
- [ ] Nenhum sprite cortado ou tremendo; animações carregam sem erro
- [ ] Entrada e saída dos andares, boss, loot, conquistas e saves funcionam
- [ ] Android 11 e Android 16 passam nos testes de abertura/renderização
- [ ] Release só depois dos testes automatizados

### Pendências de outro pacote

Os pedidos anteriores (retirar amuletos específicos, mudar os bônus reais dos restantes, iniciar com 100 poções de XP, e eventual rebalanceamento global para 15×/6×/5×) precisam ser tratados como tarefas distintas e **não são considerados implementados por este plano**. Decisões de balanceamento são provisórias e poderão mudar após os testes.
