CREATE DATABASE IF NOT EXISTS saendwave;
USE saendwave;

DROP TABLE IF EXISTS Carrello;		 -- Relazione Utente-Prodotto
DROP TABLE IF EXISTS Contenuto;      -- Relazione Ordine-Prodotto
DROP TABLE IF EXISTS Utilizzo;       -- Relazione MetodoPagamento-Utente
DROP TABLE IF EXISTS Tipologia;      -- Relazione Prodotto-Categoria
DROP TABLE IF EXISTS TracciaAudio;
DROP TABLE IF EXISTS Ordine;
DROP TABLE IF EXISTS Prodotto;
DROP TABLE IF EXISTS Categoria;
DROP TABLE IF EXISTS MetodoPagamento;
DROP TABLE IF EXISTS Utente;

-- tabella Utente
CREATE TABLE Utente (
                        nome VARCHAR(25) NOT NULL,
                        cognome VARCHAR(25) NOT NULL,
                        email VARCHAR(25) PRIMARY KEY,
                        password VARCHAR(25) NOT NULL,
                        tipo VARCHAR(25) NOT NULL DEFAULT 'utente non registrato',
                        CHECK (tipo IN ('utente non registrato', 'utente registrato', 'professionista', 'admin'))
);

-- tabella MetodoPagamento
CREATE TABLE MetodoPagamento (
                                 cvv INT NOT NULL,
                                 numero_carta BIGINT PRIMARY KEY,
                                 nome VARCHAR(25) NOT NULL,
                                 cognome VARCHAR(25) NOT NULL
);

-- tabella Categoria
CREATE TABLE Categoria (
                           nome VARCHAR(25) PRIMARY KEY,
                           studio_tool TINYINT(1) NOT NULL,
                           effetto TINYINT(1) NOT NULL,
                           tipo VARCHAR(25) NOT NULL
);

-- tabella Prodotto
CREATE TABLE Prodotto (
                          ID_prodotto INT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          prezzo FLOAT NOT NULL,
                          descrizione TEXT NOT NULL
);

-- tabella Traccia
CREATE TABLE TracciaAudio (
                              ID_traccia INT AUTO_INCREMENT PRIMARY KEY,
                              nome_file VARCHAR(50) NOT NULL,
                              percorso_file VARCHAR(100) NOT NULL,
                              FK_utente VARCHAR(25) NOT NULL, -- La traccia appartiene sempre a un utente
                              FOREIGN KEY (FK_utente) REFERENCES Utente(email) ON DELETE CASCADE
);

-- tabella Ordine
CREATE TABLE Ordine (
                        ID_ordine INT AUTO_INCREMENT PRIMARY KEY,
                        data_ordine DATE NOT NULL,
                        totale FLOAT NOT NULL,
                        stato VARCHAR(20) NOT NULL,
                        descrizione TEXT,
                        FK_traccia INT NOT NULL,
                        FOREIGN KEY (FK_traccia) REFERENCES TracciaAudio(ID_traccia) ON DELETE RESTRICT -- la traccia non può essere eliminata se l'utente ha fatto l'ordine
);

-- tabella Recensione
CREATE TABLE Recensione(
                           FK_ordine INT PRIMARY KEY,
                           voto INT NOT NULL,
                           commento TEXT,
                           data_recensione DATE NOT NULL,
    -- CHECK per il voto da 1 a 5 stelle
                           CHECK (voto BETWEEN 1 AND 5),
                           FOREIGN KEY (FK_ordine) REFERENCES Ordine(ID_ordine) ON DELETE CASCADE
);

-- tabella Utilizzo (tra utente e metodo pagamento)
CREATE TABLE Utilizzo (
                          FK_utente VARCHAR(25) NOT NULL,
                          FK_metodopagamento BIGINT NOT NULL,
                          PRIMARY KEY (FK_utente, FK_metodopagamento),
                          FOREIGN KEY (FK_utente) REFERENCES Utente(email) ON DELETE CASCADE,
                          FOREIGN KEY (FK_metodopagamento) REFERENCES MetodoPagamento(numero_carta) ON DELETE CASCADE
);

-- tabella Contenuto (tra ordine e prodotto)
CREATE TABLE Contenuto (
                           FK_ordine INT NOT NULL,
                           FK_prodotto INT NOT NULL,
                           PRIMARY KEY (FK_ordine, FK_prodotto),
                           FOREIGN KEY (FK_ordine) REFERENCES Ordine(ID_ordine) ON DELETE CASCADE,
                           FOREIGN KEY (FK_prodotto) REFERENCES Prodotto(ID_prodotto) ON DELETE CASCADE
);

-- tabella tipologia (tra categoria e prodotto)
CREATE TABLE Tipologia (
                           FK_prodotto INT NOT NULL,
                           FK_categoria VARCHAR(25) NOT NULL,
                           PRIMARY KEY (FK_prodotto, FK_categoria),
                           FOREIGN KEY (FK_prodotto) REFERENCES Prodotto(ID_prodotto) ON DELETE CASCADE,
                           FOREIGN KEY (FK_categoria) REFERENCES Categoria(nome) ON DELETE CASCADE
);

-- tabella carrello (tra utente e prodotto)
CREATE TABLE Carrello(
                         FK_utente VARCHAR(25) NOT NULL,
                         FK_prodotto INT NOT NULL,
                         PRIMARY KEY(FK_utente, FK_prodotto),
                         FOREIGN KEY (FK_prodotto) REFERENCES Prodotto(ID_prodotto) ON DELETE CASCADE,
                         FOREIGN KEY (FK_utente) REFERENCES Utente(email) ON DELETE CASCADE
);
