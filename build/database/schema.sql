CREATE DATABASE IF NOT EXISTS saendwave;
USE saendwave;

DROP TABLE IF EXISTS Carrello;		 -- Relazione Utente-Prodotto
DROP TABLE IF EXISTS Contenuto;      -- Relazione Ordine-Prodotto
DROP TABLE IF EXISTS Utilizzo;       -- Relazione MetodoPagamento-Utente
DROP TABLE IF EXISTS Tipologia;      -- Relazione Prodotto-Categoria
DROP TABLE IF EXISTS Recensione;
DROP TABLE IF EXISTS Ordine;
DROP TABLE IF EXISTS TracciaAudio;
DROP TABLE IF EXISTS Prodotto;
DROP TABLE IF EXISTS Categoria;
DROP TABLE IF EXISTS MetodoPagamento;
DROP TABLE IF EXISTS Utente;

-- tabella Utente
CREATE TABLE Utente (
                        nome VARCHAR(25) NOT NULL,
                        cognome VARCHAR(25) NOT NULL,
                        email VARCHAR(75) PRIMARY KEY,
                        password VARCHAR(255) NOT NULL,
                        data_nascita DATE NOT NULL,
                        tipo VARCHAR(25) NOT NULL DEFAULT 'utente non registrato',
                        CHECK (tipo IN ('utente registrato', 'professionista', 'admin'))
);

-- tabella MetodoPagamento
CREATE TABLE MetodoPagamento (
                                 cvv INT NOT NULL,
                                 numero_carta BIGINT PRIMARY KEY,
                                 nome VARCHAR(25) NOT NULL,
                                 cognome VARCHAR(25) NOT NULL,
                                 scadenza VARCHAR(10) NOT NULL,
								 FK_utente VARCHAR(75) NOT NULL, -- Il metodo di pagamento appartiene sempre a un utente
								 FOREIGN KEY (FK_utente) REFERENCES Utente(email) ON DELETE CASCADE
);

-- tabella Categoria
CREATE TABLE Categoria (
                           nome VARCHAR(25) PRIMARY KEY,
                           studio_tool TINYINT(1) NOT NULL,
                           effetto TINYINT(1) NOT NULL
);

-- tabella Prodotto
CREATE TABLE Prodotto (
                          ID_prodotto INT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          prezzo FLOAT NOT NULL,
                          descrizione TEXT NOT NULL,
                          immagine VARCHAR(255) NOT NULL
);

-- tabella Traccia
CREATE TABLE TracciaAudio (
                              ID_traccia INT AUTO_INCREMENT PRIMARY KEY,
                              nome_file VARCHAR(50) NOT NULL,
                              percorso_file VARCHAR(100) NOT NULL,
                              `check` TINYINT(1) NOT NULL, 
                              FK_utente VARCHAR(75) NOT NULL, -- La traccia appartiene sempre a un utente
                              FOREIGN KEY (FK_utente) REFERENCES Utente(email) ON DELETE CASCADE
);

-- tabella Ordine
CREATE TABLE Ordine (
                        ID_ordine INT AUTO_INCREMENT PRIMARY KEY,
                        data_ordine DATE NOT NULL,
                        totale FLOAT NOT NULL,
                        stato VARCHAR(20) NOT NULL DEFAULT 'In attesa',
                        descrizione TEXT,
                        FK_traccia INT NOT NULL,
                        FK_metodo_pagamento BIGINT,
                        FK_email_professionista VARCHAR(45), -- per sapere quale professionista ha preso in carico la traccia
                        CHECK (stato IN ('In attesa', 'In Lavorazione', 'Completato')),
                        FOREIGN KEY (FK_traccia) REFERENCES TracciaAudio(ID_traccia) ON DELETE RESTRICT, -- la traccia non può essere eliminata se l'utente ha fatto l'ordine
                        FOREIGN KEY (FK_metodo_pagamento) REFERENCES MetodoPagamento (numero_carta) ON DELETE RESTRICT, -- Il metodo può essere eliminato dopo aver effettuato l'ordine
                        FOREIGN KEY (FK_email_professionista) REFERENCES Utente (email) ON DELETE SET NULL
);

-- tabella Recensione
CREATE TABLE Recensione(
                           FK_ordine INT PRIMARY KEY,
                           tipo VARCHAR(45) NOT NULL, -- la recensione può essere scritta sia in merito ad ogni prodotto, sia in merito all'ordine
                           voto INT NOT NULL,
                           commento TEXT,
                           data_recensione DATE NOT NULL,
    -- CHECK per il voto da 1 a 5 stelle
    					   CHECK (tipo IN ('prodotto', 'ordine')),
                           CHECK (voto BETWEEN 1 AND 5),
                           FOREIGN KEY (FK_ordine) REFERENCES Ordine(ID_ordine) ON DELETE CASCADE
);

-- tabella Utilizzo (tra utente e metodo pagamento)
CREATE TABLE Utilizzo (
                          FK_utente VARCHAR(75) NOT NULL,
                          FK_metodopagamento BIGINT NOT NULL,
                          PRIMARY KEY (FK_utente, FK_metodopagamento),
                          FOREIGN KEY (FK_utente) REFERENCES Utente(email) ON DELETE CASCADE,
                          FOREIGN KEY (FK_metodopagamento) REFERENCES MetodoPagamento(numero_carta) ON DELETE CASCADE
);

-- tabella Contenuto (tra ordine e prodotto)
CREATE TABLE Contenuto (
						   ID_riga_contenuto INT AUTO_INCREMENT PRIMARY KEY,
                           FK_ordine INT NOT NULL,
                           FK_prodotto INT NOT NULL,
                           posizione_catena INT NOT NULL,
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
    					   ID_riga_carrello INT AUTO_INCREMENT PRIMARY KEY,
						   FK_utente VARCHAR(75) NOT NULL,
						   FK_prodotto INT NOT NULL,
						   FOREIGN KEY (FK_prodotto) REFERENCES Prodotto(ID_prodotto) ON DELETE CASCADE,
						   FOREIGN KEY (FK_utente) REFERENCES Utente(email) ON DELETE CASCADE
);

INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('Amp Designer', '2.50', 'Amp Deisgner simula il suono di pù di 20 famosi amplificatori per chitarra e speaker collegati. Ognuno preconfigurato come combinazione di testata, cabinet e equalizzatore. Processa il segnale direttamente riproducendo il suono attraverso questa combinazione. Testata, cabinet ed equalizzatore possono essere combinati in numerosi modi per alterare il suono. Inoltre, vengono simulati microfoni virtuali che catturano il suono direttamente dal cabinet sui quali si può scegliere tipo e posizione rispetto agli speaker.', 'img/prodotti/Amp_Designer.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('AU Distortion', '1.50', 'L\'AU Distortion sporca il suono nel modo giusto. Dal sottile calore armonico alla distruzione totale del segnale, applichiamo una saturazione su misura per dare aggressività, presenza e \"grit\" a linee di basso, batterie o sintetizzatori altrimenti troppo puliti e spenti', 'img/prodotti/AU_Distortion.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('Bass Amp Designer', '2.50', 'Il trattamento definitivo per la tua traccia di basso, Uniamo la pulizia di una Direct Box con la spinta e la profondità di un vero amplificatore microfonato. Garantisce basse frequenze solide, compatte e definite che \"bucano\" l\'impianto senza impastare il brano.', 'img/prodotti/Bass_Amp_Designer.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('Chroma Verb', '1.50', 'Aggiungi spazio, tridimensionalità e respiro alla tua traccia. Dall\'intimità di una piccola stanza di registrazione fino all\'immensità di una cattedrale, applichiamo un riverbero algoritmico di altissima qualità per cullare il tuo sono nel giusto ambiente.', 'img/prodotti/ChromaVerb.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('Compressor', '2.00', 'Controllo totale della dinamica. Livelliamo i picchi e diamo \"corpo\" alla tua traccia (voce, basso o batteria) emulando circuiti storici (VCA, FET, Opto). Il risultato è un suono compatto, potente e presente che non scompare mai dietro gli altri strumenti', 'img/prodotti/Compressor.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('DeEsser II', '1.50', 'Il tocco essenziale per tracce vocali o podcast perfetti. Riduciamo in modo mirato e chirurgico le sibilanti fastidiose(\"s\" e \"z\" troppo taglienti al microfono) senza scurire il tono generale della voce, garantendo un ascolto morbido e professionale.', 'img/prodotti/DeEsser_II.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('Delay Designer', '1.50', 'Echi ritmici e atmosfere ipnotiche. Creiamo ritardi su misura per te: dai classici \"slapback\" per dare un tocco vintage a chitarre e voci, fino a complessi pattern stereo perfetti per arricchire sintetizzatori e produzioni elettroniche.', 'img/prodotti/Delay_Designer.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('Izotope Ozone 8 Equalizer', '2.00', 'Pulizia chirurgica e brillantezza pure. Utilizziamo questo equalizzatore di livello mastering per rimuovere frequenze fastidiose (fango e risonanze nasali) ed esaltare la chiarezza e l\'aria della tua traccia con una precisione digitale assoluta.', 'img/prodotti/Izotope_Ozone_8_Equalizer.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('Pedalboard', '2.00', 'Una pedaliera di effetti boutique a tua totale disposizione. Aggiungiamo un carattere alla tua traccia concatenando fuzz, overdrive, chorus, phaser o octaver. Ideale non solo per chitarre, ma anche per stravolgere in modo creativo synth o voci.', 'img/prodotti/Pedalboard.png');
INSERT INTO `saendwave`.`prodotto` (`nome`, `prezzo`, `descrizione`, `immagine`) VALUES ('Pitch Correction', '3.50', 'Intonazione perfetta e professionale per la tua traccia vocale. Che tu voglia una correzione invisibile e naturale, oppure l\'inconfondibile effetto \"robotico\" moderno (stile trap/pop), allineeremo le tue note all\'armonia del brano senza rovinare la tua espressività.', 'img/prodotti/Pitch_Correction.png');


INSERT INTO `saendwave`.`categoria` (`nome`, `studio_tool`, `effetto`) VALUES ('amplificatore', '1', '0');
INSERT INTO `saendwave`.`categoria` (`nome`, `studio_tool`, `effetto`) VALUES ('distorsore', '0', '1');
INSERT INTO `saendwave`.`categoria` (`nome`, `studio_tool`, `effetto`) VALUES ('riverbero', '1', '1');
INSERT INTO `saendwave`.`categoria` (`nome`, `studio_tool`, `effetto`) VALUES ('compressore', '1', '1');
INSERT INTO `saendwave`.`categoria` (`nome`, `studio_tool`, `effetto`) VALUES ('filtro', '1', '0');
INSERT INTO `saendwave`.`categoria` (`nome`, `studio_tool`, `effetto`) VALUES ('delay', '1', '1');
INSERT INTO `saendwave`.`categoria` (`nome`, `studio_tool`, `effetto`) VALUES ('equalizzatore', '1', '1');
INSERT INTO `saendwave`.`categoria` (`nome`, `studio_tool`, `effetto`) VALUES ('pedaliera', '0', '1');

INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('1', 'distorsore');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('2', 'amplificatore');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('2', 'distorsore');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('2', 'equalizzatore');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('3', 'riverbero');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('4', 'compressore');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('5', 'filtro');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('6', 'delay');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('7', 'equalizzatore');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('8', 'pedaliera');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('8', 'delay');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('8', 'distorsore');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('8', 'equalizzatore');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('8', 'riverbero');
INSERT INTO `saendwave`.`tipologia` (`FK_prodotto`, `FK_categoria`) VALUES ('9', 'filtro');

INSERT INTO `saendwave`.`utente` (`nome`,`cognome`,`email`,`password`,`data_nascita`,`tipo`) VALUES ('Admin','Mary','admin@saendwave.it','KesYRkNecN4kTMEMbCKRg0B49GW16DRsSw5iFxIQYcc=','2006-03-07','admin');
INSERT INTO `saendwave`.`utente` (`nome`,`cognome`,`email`,`password`,`data_nascita`,`tipo`) VALUES ('Carla','Fracci','carla.fracci@email.it','DsRk2LuPxYjaGMLnYivkABhHMOm9UXzJP8kCZ1/tVKQ=','1936-08-20','utente registrato');
INSERT INTO `saendwave`.`utente` (`nome`,`cognome`,`email`,`password`,`data_nascita`,`tipo`) VALUES ('Mario','Rossi','mario.rossi@email.it','ew5iMMuTS2sdC1MXpLv3kOqcOKEfLM4O31LO0pnucdE=','1987-09-10','utente registrato');
INSERT INTO `saendwave`.`utente` (`nome`,`cognome`,`email`,`password`,`data_nascita`,`tipo`) VALUES ('Professionista','Mary','professionista@saendwave.it','KesYRkNecN4kTMEMbCKRg0B49GW16DRsSw5iFxIQYcc=','2006-03-07','professionista');

INSERT INTO `saendwave`.`metodopagamento` (`cvv`, `numero_carta`, `nome`, `cognome`, `Fk_utente`, `scadenza` ) VALUES ('906', '5333111111111111', 'Mario', 'Rossi', 'mario.rossi@email.it','2026-05-30');
INSERT INTO `saendwave`.`metodopagamento` (`cvv`, `numero_carta`, `nome`, `cognome`, `Fk_utente`, `scadenza`) VALUES ('785', '5333222222222222', 'Carla', 'Fracci', 'carla.fracci@email.it','2028-08-17');

INSERT INTO `saendwave`.`utilizzo` (FK_utente, FK_metodopagamento) VALUES ('mario.rossi@email.it', '5333111111111111');
INSERT INTO `saendwave`.`utilizzo` (FK_utente, FK_metodopagamento) VALUES ('carla.fracci@email.it', '5333222222222222');

-- esempio di un ordine completato
-- 1. Creiamo la traccia audio inviata dal cliente 
INSERT INTO `saendwave`.`tracciaaudio` (`nome_file`, `percorso_file`, `check`, `FK_utente`) 
VALUES ('basso_funk_luigi.wav', 'uploads/tracce/basso_funk_luigi.wav', 1, 'mario.rossi@email.it');
-- 2. Inseriamo l'ordine completato associato al professionista loggato ('professionista@saendwave.it')
INSERT INTO `saendwave`.`ordine` (`data_ordine`, `totale`, `stato`, `descrizione`, `FK_traccia`, `FK_metodo_pagamento`, `FK_email_professionista`) 
VALUES ('2026-03-15', 12.00, 'Completato', 'Mix completo di basso con equalizzazione punchy e compressione sidechain.', LAST_INSERT_ID(), 5333111111111111, 'professionista@saendwave.it');
-- 3. Colleghiamo i prodotti dell'ordine (Contenuto)
INSERT INTO `saendwave`.`contenuto` (`FK_ordine`, `FK_prodotto`, `posizione_catena`) VALUES (LAST_INSERT_ID(), 3, 1);
INSERT INTO `saendwave`.`contenuto` (`FK_ordine`, `FK_prodotto`, `posizione_catena`) VALUES (LAST_INSERT_ID(), 5, 2);