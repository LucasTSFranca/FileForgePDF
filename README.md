# 🛠️ FileForge (FileForgePDF)

O **FileForge** é uma aplicação desktop leve e robusta desenvolvida em **Java Swing** para conversão, mesclagem e compressão de múltiplos formatos de arquivos em arquivos PDF de alta qualidade. Com uma interface moderna em modo escuro, o aplicativo permite gerenciar de forma simples a conversão de documentos e imagens, mantendo um histórico detalhado em um banco de dados local **SQLite**.

---

## ✨ Principais Funcionalidades

*   **Conversão de Múltiplos Formatos:**
    *   **Documentos de Texto:** `.txt`
    *   **Documentos Word:** `.docx` (preservando o layout original através da integração local com o Microsoft Word)
    *   **Planilhas CSV:** `.csv` (convertidos em tabelas PDF estilizadas com cabeçalhos destacados e linhas alternadas)
    *   **Páginas Web:** `.html` e `.htm` (com extração inteligente de texto e quebras de linha)
    *   **Imagens:** `.jpg`, `.jpeg`, `.png`, `.bmp` e `.gif`
*   **Mesclagem Inteligente (Merge):** Combine múltiplos arquivos de diferentes extensões e formatos em um único arquivo PDF unificado.
*   **Compressão por Meta de Tamanho:** Defina um limite de tamanho máximo (em MB) para o PDF gerado. O motor de compressão ajustará a escala e a qualidade das imagens recursivamente de forma automática até que o arquivo atinja a meta estipulada.
*   **Histórico de Conversões Integrado:** Histórico em tempo real integrado a um banco de dados embutido **SQLite**. Ele rastreia:
    *   Arquivos originais (nome, caminho e tamanho original).
    *   Arquivos convertidos/gerados (nome, caminho, tamanho final, data de criação e percentual de redução/variação obtido).
*   **Interface Customizada Modernizada:** Tela em modo escuro desenvolvida em Swing com paleta de cores harmoniosa baseada em tons escuros e detalhes em laranja, tabelas responsivas e barra de progresso com execução em segundo plano (*SwingWorker*), evitando o travamento da interface.

---

## 🏗️ Estrutura do Projeto

O projeto segue uma arquitetura organizada por responsabilidades:

```text
src/main/java/
├── app/
│   └── Main.java                      # Inicialização do banco e carregamento da UI
├── config/
│   └── Database.java                  # Conexão JDBC e configuração automática do SQLite
├── model/
│   ├── FileModel.java                 # Modelo do arquivo original
│   └── FileConvertModel.java          # Modelo do arquivo PDF gerado
├── repository/
│   ├── FileRepository.java            # Persistência de dados dos arquivos originais
│   └── FileConvertRepository.java     # Persistência de dados dos PDFs gerados
├── resource/
│   └── Converter.java                 # Interface base para os conversores de arquivos
├── service/
│   ├── ConvertService.java            # Regras de negócio, compressão e fluxo de conversão
│   ├── CsvToPdfConverter.java         # Conversor específico de planilhas CSV
│   ├── DocxToPdfConverter.java        # Conversor de DOCX usando Documents4J
│   ├── HtmlToPdfConverter.java        # Conversor de páginas HTML
│   ├── ImageToPdfConverter.java       # Conversor de imagens (JPEG, PNG, etc.)
│   └── TxtToPdfConverter.java         # Conversor de texto puro
└── view/
    └── MainView.java                  # Painel visual Java Swing e tratamento de eventos da interface
```

---

## 💾 Banco de Dados (SQLite)

O FileForge armazena os dados das conversões localmente em um banco de dados SQLite. O arquivo é inicializado e criado automaticamente pelo aplicativo em um caminho seguro de acordo com o sistema operacional:
*   **Windows:** `%APPDATA%\FileForge\banco\pdf_db` (ex: `C:\Users\<Usuario>\AppData\Roaming\FileForge\banco\pdf_db`)
*   **Linux/macOS:** `~/FileForge/banco/pdf_db`

### Estrutura do Schema (`schema.sql`)

```sql
-- Tabela para os arquivos originais
CREATE TABLE IF NOT EXISTS file (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    nome    TEXT    NOT NULL,
    caminho TEXT    NOT NULL,
    tamanho REAL    NOT NULL
);

-- Tabela para os arquivos PDF gerados
CREATE TABLE IF NOT EXISTS file_convert (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    nome         TEXT    NOT NULL,
    novo_caminho TEXT    NOT NULL,
    tamanho      REAL    NOT NULL,
    dt_criacao   TEXT    DEFAULT (date('now'))
);
```

---

## 🛠️ Tecnologias e Bibliotecas Utilizadas

*   **Linguagem:** Java (Compilador configurado para a versão **25**)
*   **Interface Gráfica:** Java Swing
*   **Gerenciador de Dependências:** Maven
*   **Bibliotecas Principais:**
    *   **iText / OpenPDF (`com.lowagie.itext`):** Criação e estruturação do documento PDF.
    *   **SQLite JDBC (`org.xerial:sqlite-jdbc`):** Driver de conexão ao banco de dados SQLite.
    *   **Apache POI (`org.apache.poi:poi-ooxml`):** Manipulação de formatos Office Open XML.
    *   **Documents4J (`com.documents4j`):** Ponte local para conversão altamente fiel de documentos `.docx` gerados a partir do Microsoft Word local.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
1.  **JDK 25** instalado e configurado nas variáveis de ambiente.
2.  **Maven** instalado.
3.  **Microsoft Word** instalado localmente na máquina (necessário caso queira realizar a conversão de arquivos `.docx` via Documents4J).

### Passo a Passo

1.  **Clone o repositório** e entre no diretório do projeto:
    ```bash
    git clone <url-do-repositorio>
    cd Codigo
    ```

2.  **Compilar e empacotar a aplicação:**
    Este comando compila os fontes e gera um **Fat JAR** executável contendo todas as dependências sob o diretório `target/` chamado `arquivo.jar`.
    ```bash
    mvn clean package
    ```

3.  **Executar a aplicação:**
    *   **Via JAR gerado:**
        ```bash
        java -jar target/arquivo.jar
        ```
    *   **Diretamente pelo Maven:**
        ```bash
        mvn exec:java
        ```

---

## 🖥️ Como Utilizar a Aplicação

1.  Ao abrir a interface do **FileForge**, selecione um diretório de destino clicando no botão **"Pasta Destino"** no cabeçalho.
2.  Adicione arquivos à lista utilizando o botão **"Adicionar"** no painel esquerdo.
3.  Se desejar unir todos os arquivos em um único PDF, marque a opção **"Mesclar em um único PDF"** e dê um nome ao arquivo.
4.  Se precisar limitar o tamanho final do arquivo gerado para upload em portais que possuem restrições, marque **"Limitar tamanho"** e configure o valor em megabytes (MB) desejado.
5.  Clique em **"Converter Selecionado"** ou **"Converter Todos"**.
6.  Gerencie os resultados diretamente no painel **"Arquivos Convertidos"** ou no **"Histórico de Conversões"** no rodapé (onde é possível abrir os PDFs gerados ou excluí-los do histórico de registros).
