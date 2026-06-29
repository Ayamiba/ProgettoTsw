    const input = document.getElementById('search-input');
    const dropdown = document.getElementById('suggestions');
    const form = document.getElementById('search-form');
    let timer; // Cronometro Debounce

    input.addEventListener('input', () => { //Funzione per leggere la digitazione dell'utente
        clearTimeout(timer); 
        const valore = input.value.trim(); //Eliminazione di spazi accidentali

        if (valore.length < 2) { // Se l'utente ha scritto poco interrompi
            dropdown.style.display = 'none';
            return;
        }

        // Timer di 150ms quando l'utente termina la digitazione
        timer = setTimeout(() => {
			eseguiAJAX(valore);
		}, 150);
	});
          function eseguiAJAX(test) { //Invio della richiesta asincrona alla Servlet
            fetch('OttieniSuggerimenti?q=' + encodeURIComponent(testo)) //Invio della query alla Servlet "OttieniSuggerimenti"
                .then(response => response.json()) //Trasformazione della risposta in un json
                .then(dati => mostraSuggerimenti(dati)) //Invio dei dati alla funzione per mostrarli all'utente
                .catch(errore => console.error("Errore AJAX:", errore));
			}
			
    function mostraSuggerimenti(lista) { 
        dropdown.innerHTML = ''; //Pulizia vecchi suggerimenti

        if (lista.length === 0) { //Se la lista è vuota non far vedere la tendina
            dropdown.style.display = 'none';
            return;
        }

        lista.forEach(elemento => { //Per ogni elemento creiamo un li e ci inseriamo il nomer prodotto
            const li = document.createElement('li');
            li.textContent = elemento;
            
            li.addEventListener('click', () => { 
                input.value = elemento;      // Inserisce il testo cliccato nella barra
                dropdown.style.display = 'none'; // Chiude la tendina
                form.submit();               // Invia automaticamente la ricerca al CatalogoServlet!
            });
            
            dropdown.appendChild(li);
        });

        dropdown.style.display = 'block'; //Rendiamo visibile al'utente la tendina con i suggerimenti
    }

    // Chiude la tendina se si clicca altrove
    document.addEventListener('click', (e) => {
        if (!input.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    });