# Projeto_Integrador - Requisito 6
Requisito 6 com base na API REST desenvolvida pelo grupo Beta Campers para o Projeto Integrador feito durante o IT Bootcamp Backend Java (wave 6). 

## Autor
<a href="https://github.com/thiagosordiMELI">
  <img src="https://avatars.githubusercontent.com/u/108008559?s=120&v=4" style="width: 50px">
</a>

# Sumário

- [Detalhes](#detalhes)
- [Desafios encontrados](#desafios)
- [Primeira vez](#first)
- [Funcionalidades](#funcionalidades)
- <a href="https://app.diagrams.net/#G1X_05jbEF7Yt2yFOZ2y3OfKW_KCPjm5MC">Diagrama UML </a>
- [Responsável](#responsavel)
  - [Post - Cria um novo armazem](#createWarehouse)
  - [Post - Cria uma nova rota](#createRoute)
- [Comprador](#comprador)
  - [Get - Tempo estimado de entrega](#getEstimatedTime)
  
# Detalhes

Para esse requisito os representantes podem adicionar armazéns e conectá-los por rotas. Para isso foi utilizado um banco NoSQL orientado a grafos.
<br><br>O banco escolhido é o Neo4J rodando em um container Docker o 
qual a aplicação tem acesso através de uma dependência de dados do Spring.
<br><br>São estipulados os modelos dos nós do grafo (Armazém e Rota).
<br><br>O objetivo final é para que o comprador consiga visualizar o tempo estimado de entrega.
<br><br>O modelo de compra (PurchaseOrder) agora contem um armazém de entrega (simplificação equivalente ao endereço do cliente).

# Desafios encontrados <br name="desafios">

*Testes de integração envolvendo o banco de grafos teve algumas complicações com a Response.
<br><br>*A ideia era que calculasse a rota mais rápida passando por todos armazens contendo itens da compra.
Porém essa parte de passar por nós obrigatórios não funcionou muito bem.
Então simplesmente retorna o menor caminho entre um armazem inicial e o de entrega.

# Primeira vez <br name="first">

Entrar na pasta do projeto pelo terminal e executar o comando 

<pre><code>
docker-compose up -d
 </code></pre>

para subir o container que roda o Neo4J.

E para acessar o Dashboard oferecido pelo Neo4j basta acessar <a href="http://localhost:7474/">Dashboard</a> e digitar as credenciais conforme especificadas no arquivo application.properties (nome do banco é neo4j).

# Funcionalidades

## Responsável <br name="responsavel">

`POST /api/v1/warehouse` <br name="createWarehouse">
Cria um novo Armazém.
<pre><code><b>Payload Example:</b>
{
    "location": "Fortaleza"
}

 
 <b>Response:</b>
  [
      {
          "warehouseCode": 13,
          "location": "Fortaleza",
          "routes": []
      }
  ]
 
 </code></pre>
 
 `POST /api/v1/route/create-route` <br name="createRoute">
 Cria uma nova Rota.
<pre><code><b>Payload Example:</b>
{
    "from": 13,
    "destination": 7,
    "duration": 15
}
 
 <b>Response:</b>
  [
      {
          "id": "73fc9bb9-8c91-4b6b-9660-59cac8326bb3",
          "from": "Fortaleza",
          "destination": "Porto Alegre",
          "duration": 15.0
      }
  ]
 
 </code></pre>

## Comprador <br name="comprador">

`GET /api/v1/warehouse/{purchaseOrderId}/delivery-estimated` <br name="getEstimatedTime">
Retorna tempo estimado da entrega.
<pre><code><b>Payload Example:</b>
 
 <b>Response:</b>
  {
      "from": "Belo Horizonte",
      "to": "Farroupilha",
      "totalInTime": 17.0
  }
 </code></pre>
 
