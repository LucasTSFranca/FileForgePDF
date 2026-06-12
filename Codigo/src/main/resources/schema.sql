-- SCHEMA SQLite  (banco/pdf_db)
-- Criação executada automaticamente por Database.initSchema() na inicialização.
-- Este arquivo serve apenas como referência/documentação.
-- ──────────────────────────────────────────────────────────────────────────────

-- Armazena os arquivos originais antes da conversão
CREATE TABLE IF NOT EXISTS file (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    nome    TEXT    NOT NULL,
    caminho TEXT    NOT NULL,
    tamanho REAL    NOT NULL
);

-- Armazena os arquivos PDF gerados após a conversão
CREATE TABLE IF NOT EXISTS file_convert (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    nome         TEXT    NOT NULL,
    novo_caminho TEXT    NOT NULL,
    tamanho      REAL    NOT NULL,
    dt_criacao   TEXT    DEFAULT (date('now'))
);

-- Limpar tabela (SQLite não tem TRUNCATE; use DELETE + reset de sequência)
-- DELETE FROM file;
-- DELETE FROM sqlite_sequence WHERE name = 'file';