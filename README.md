![Logo](![alt text](image.png))


# EcoRota

O EcoRota é um sistema inovador que utiliza tecnologia avançada para otimizar as rotas de coleta de lixo em áreas urbanas. Nosso objetivo é tornar o processo mais eficiente, reduzindo custos operacionais e minimizando o impacto ambiental.


## Documentação da API

#### Retorna todos os itens

```http
  GET /api/rotas
  GET /api/caminhoes
  GET /api/nos-grafo
  GET /api/trechos
  GET /api/pontos-coleta
  GET /api/rotas
```

#### Retorna um item

```http
  GET /api/rotas/${id}
  GET /api/caminhoes/${id}
  GET /api/nos-grafo/${id}
  GET /api/trechos/${id}
  GET /api/pontos-coleta/${id}
  GET /api/rotas/${id}
```

## Autores

- [@Gabriel do Nascimento Cano Andrade](www.linkedin.com/in/gabriel-nascimento-a5946722a)
- [@Alexandre Lozano de Souza](https://www.linkedin.com/in/alexandre-lozano-de-souza-3367b0268/)
- [@Lucas Lima](https://www.linkedin.com/in/lucas-lima-98943b2b7/)


# Documentação

## EcoRota

*Tecnologia e inteligência para uma cidade mais limpa e eficiente.*

---

## Sobre o Projeto

O EcoRota é um sistema inovador que tem como objetivo otimizar as rotas de coleta de lixo, tornando o processo mais eficiente, econômico e sustentável. Utilizamos técnicas avançadas de geoprocessamento e algoritmos de otimização para transformar a coleta de resíduos urbanos.

---

## Recursos

- **Otimização de rota inteligente:** Algoritmos avançados para criar rotas mais eficientes, reduzindo tempo e custos.
- **Rastreamento de frota em tempo real:** Monitoramento da posição dos veículos e ajustes dinâmicos das rotas.
- **Gestão de resíduos orientada por dados:** Análises para melhorar continuamente o processo de coleta.
- **Planejamento inteligente:** Programações otimizadas considerando horários de tráfego e outras variáveis.
- **Aplicativo móvel para motoristas:** Interface para seguir rotas otimizadas e reportar ocorrências.
- **Dashboard de desempenho:** Métricas e acompanhamento da operação em tempo real.

---

## Como funciona

1. **Coleta de dados:** Mapeamento da cidade, análise dos padrões de geração de resíduos e estudo das rotas atuais.
2. **Processamento:** Aplicação dos algoritmos para calcular as rotas mais eficientes.
3. **Implementação:** Integração com sistemas de navegação e treinamento das equipes.
4. **Monitoramento:** Acompanhamento contínuo e ajustes para máxima eficiência.

---

## Benefícios

- Redução significativa no tempo de coleta.
- Economia de combustível, manutenção e horas de trabalho.
- Diminuição da emissão de poluentes.
- Melhoria na limpeza urbana.
- Maior satisfação da população.
- Otimização do uso da frota de veículos.

---

## Contato

Para dúvidas, sugestões ou solicitações de demonstração, entre em contato:

- **Email:** gabriel.naascimento18@gmail.com 
- **Email:** lucodein@gmail.com
- **Email:** alelozanodm@gmail.com

---

## Licença

Este projeto está sob a licença da **VirtuCodex**.

---

*© 2025 EcoRota. Todos os direitos reservados.*

##Inicialização do projeto

**Inicializa o Servidor**
```bash
npm run build
```

**Inicia a aplicação Front-end
```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

## Instalação

O projeto já esta com as dependências corretas só basta utilizar os comandos para instalar as bibliotecas

**Instalação das bibliotecas do Next.js**
```bash
  npm install
```
**Instalação da biblioteca utilizada para o mapa**
```leaflet - Mapa
   npm install leaflet react-leaflet
   npm install --save-dev @types/leaflet
```
**Configuração de porta de acesso**
```Porta
   caso precise mudar a porta na aplicação de java basta acessar application.properties
   e mudar a porta no server.port=8082
```
    
## Stack utilizada

**Front-end:** Next, css, TypeScript

**Back-end:** Java, Spring-Boot

**Banco de Dados:** PostgreeSql

##Documentação do Next

- [Next.js Documentation](https://nextjs.org/docs) - **learn about Next.js features and API.**
- [Learn Next.js](https://nextjs.org/learn) - **an interactive Next.js tutorial.**


