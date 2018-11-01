<h1><b>Desafio ContaAzul</b></h1>   
Olá avaliador! Neste readme irei dividir as informações relevantes em tópicos, o desafio que me foi passado foi:
https://drive.google.com/file/d/1DvjRBTvnHwlUOoNBwAsvoRF6aKqYm7pP/view

<h3><b>Como executar o projeto:</b></h3>
Para executar o projeto existem duas formas relativamente praticas:
<UL>
  <LI><b>gradle bootRun</b><br>
    Basta entrar no root do projeto e executar o comando <b>gradle bootRun</b>.
  <LI><b>Via IDE</b><br>
    Para executar o projeto via IDE basta entrar no projeto e executar a classe <b>ApiStarter.java</b>.
</UL>
    
<h3><b>Code coverege e code quality:</b></h3>
Para code coverage, testes unitários, foi considerada apenas classes de serviço, para estas classes o código teve 100% de suas linhas cobertas.
Para code quality foi utilizada a ferramenta <b>codacy</b> apos analyse feita, a ferramenta apontou apenas 10 issues relacionadas a code style portanto com peso zero de avaliação, totalizando 3% de issues no projeto e disponibilizando ao projeto a certificação A.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/418325e3ab7a401eaacffc87e648190f)](https://www.codacy.com/app/Scarabe/DesafioContaAzul?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Scarabe/DesafioContaAzul&amp;utm_campaign=Badge_Grade)

<h3><b>Testes automatizados:</b></h3>
Existem duas switchs de tests no projeto, uma dedicada a testes unitarios e outra dedicada a testes de integração.
<UL>
  <LI><b>Testes de integração</b><br>
    Para executar os testes de integração é necessária a execução manual dos testes pós start do projeto (testes de integração <b>não funcionam</b> caso a aplicação não esteja rodando, está classe é a <b>BankSlipIntegration.java</b></br>
  <LI><b>Testes unitários</b><br>
    São testes aplicados somente a camada de serviço do projeto podendo ser executados a qualquer momento, são responsaveis       também pela line coverate do projeto, são executados automaticamente no build do projeto, podendo ser executados              manualmente via classe <b>BankSlipServiceTest.java</b>
</UL>


<h3><b>Técnologias Utilizadas:</b></h3>
<UL>
  <LI><b>Junit v4.12:</b> Ferramenta utilizada para criação de testes unitários.</LI>
  <LI><b>Mockito v1.9.5:</b> Ferramenta utilizada para criação de mocks de objetos e métodos.</LI>
  <LI><b>Rest Assure v2.9.0:</b> Ferramenta de automação de testes em api rest.</LI>
  <LI><b>Jacoco v0.8.2:</b> Utilizado para execução de testes em build e visualização de code coverage.</LI>
  <LI><b>Spring Fox(Swager2 e SwaggerUi) v2.9.2:</b> Utilizado para disponibilização do swagger, ferramenta de documentação de api.</LI>
  <LI><b>Jacoco v0.8.2:</b> Utilizado para execução de testes em build e visualização de code coverage.</LI>  
  <LI><b>SpringFramework(Web e DataJpa) v2.0.5:</b> Ferramenta facilitadora de configuração de projetos.</LI>
  <LI><b>H2 Database v1.4.197:</b> Banco de dados embarcado utilizado principalmente para testes.</LI> 
  <LI><b>Lombok v1.18.4:</b> Ferramenta que tem por principal objetivo diminuir a verbosidade do código.</LI> 
  <LI><b>GSon v2.8.5:</b> Utilizado para serialização e deserialização de JSON.</LI> 
  <LI><b>Gradle v4.2:</b> Ferramenta de gerenciamento de dependecias e atuamatização de builds, utiliza o ANT e o Maven.</LI>
  <LI><b>Intellij v2018.2.5:</b> IDE de desenvolvimento.</LI>
</UL>

<h3><b>Urls relevantes:</b></h3>
  <UL>
  <LI><b><a href='http://localhost:8080/h2'>http://localhost:8080/h2</a></b><br>
    Url destinada ao banco de dados H2, para conectar ao banco e observar suas informações os seguintes dados deveram ser         respeitados após start da aplicação.<br>
    <b>JDBC URL:</b> jdbc:h2:~/DesafioContaAzul<br>
    <b>User name:</b> admin<br>
    <b>Password:</b> admin<br>
  <LI><b><a href='http://localhost:8080/swagger-ui.html#/'>http://localhost:8080/swagger-ui.html#/</a></b><br>
    Url base para acesso ao Swagger, ferramenta que disponibiliza testar os endpoints individualmente e analisar seus         
    respectivos retornos http, utilizado também para documentar a api.<br>
  <LI><b>http://localhost:8080/rest</b><br>
    Url base da api desenvolvida.<br>
</UL>

<h3><b>Considerações finais:</b></h3>
  Gostei de executar o projeto, coloquei em pratica atividades de configuração que eu já conhecia, mas que não executava com certa frequencia, agradeço ao avaliador pela oportunidade.
  Deixo também o questionamento, no calculo de multa a ser paga dependendo do valor inserido pode existir casas decimais no percentual, mas o valor trabalhado é em centavos, no dia a dia não existe um valor real inferior a um centavo, acredito que deveria ser discutido com o PO um arredondamento deste valor.
