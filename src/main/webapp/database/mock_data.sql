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


INSERT INTO `tipologia` VALUES (1,'amplificatore'),(3,'amplificatore'),(5,'compressore'),(7,'delay'),(9,'delay'),(1,'distorsore'),(2,'distorsore'),(3,'distorsore'),(9,'distorsore'),(1,'equalizzatore'),(3,'equalizzatore'),(8,'equalizzatore'),(9,'equalizzatore'),(6,'filtro'),(9,'filtro'),(10,'filtro'),(9,'pedaliera'),(4,'riverbero'),(9,'riverbero');

INSERT INTO `tracciaaudio` VALUES (7,'Baby_noFX.wav','1784194365841_Baby_noFX.wav',0,'m.rossi@gmail.com');


INSERT INTO `metodopagamento` VALUES (123,5333111111111111,'MARIO','ROSSI','12/30');


INSERT INTO `ordine` VALUES (10,'2026-07-20',5.5,'In Lavorazione','Eq spazio voce pulizia, comp rotonditÃ  e potenza, verb per spazialitÃ ',7,5333111111111111,'g.professionista@saendwave.it');


INSERT INTO `tracciaaudio` VALUES (7,'Baby_noFX.wav','1784194365841_Baby_noFX.wav',0,'m.rossi@gmail.com');


INSERT INTO `utente` VALUES ('Admin','Gianni','adminGianni@saendwave.it','hPC6XUfMJVf9I3vsd/2YFGWoNGmheHDDrrioKaRIN2s=','1997-06-11','admin'),('Giovanni','Pro','g.professionista@saendwave.it','Dm3AOGalRYVBDqeaiwb9NvrQp41ILh8Pf08BKmetxF4=','1997-06-11','professionista'),('Mario','Rossi','m.rossi@gmail.com','tbMBeUL7idLBIU7D8zdm9mlS23xUBbjlmsCW0/GNz/I=','1999-08-21','utente registrato');


INSERT INTO `utilizzo` VALUES ('m.rossi@gmail.com',5333111111111111);

INSERT INTO `carrello` VALUES ('g.professionista@saendwave.it',7);

