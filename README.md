# Scraper Project

Este projeto é um scraper desenvolvido em Java que utiliza Gradle como sistema de build. Ele inclui um serviço de envio de e-mails para notificar quando os arquivos gerados estão prontos para download.
É utilizado para extrair algumas informações específicas de relatórios de saúde do Governo e gerar um arquivo CSV com os dados extraídos.

O que ele faz a partir do site gov:

1. Baixa os aquivos da documentação do padrão TISS (Troca de Informações na Saúde Suplementar), na versão mais recente.
2. Acessa o campo "Histórico das versões dos Componentes do Padrão TISS" e coleta, em um arquivo .csv, os dados de competência, publicação e início de vigência a partir da competência jan/2016; 
3. Acessa o campo "Tabelas relacionadas" e baixa a "Tabela de erros no envio para a ANS".
4. Envia um email para os destinatários informando que os arquivos estão prontos para download.


By: Gustavo de Araújo Garcia

## Requisitos

- Java 11 ou superior
- Gradle 6.0 ou superior

## Configuração

1. Clone o repositório:
    ```sh
    git clone https://github.com/dev-gst/simple-scraper
    cd simple-scraper
    ```

2. Configure as variáveis de ambiente necessárias no arquivo `src/main/resources/config/application.yaml`
   (rename the file application.template.yaml to application.yaml):
    ```yaml
    email:
      host: SMTP_HOST
      port: SMTP_PORT
      username: <SEU_EMAIL>
      password: <SUA_SENHA>
      from: <SEU_EMAIL>
      starttls-enable: "true/false"
      auth: "true/false"
    ```

## Execução

1. Compile o projeto:
    ```sh
    ./gradlew build
    ```

2. Execute o projeto:
    ```sh
    ./gradlew run
    ```