function filtraTabellaLive() {
            // 1. Prendi il valore digitato e trasformalo in maiuscolo (per rendere la ricerca non case-sensitive)
            let input = document.getElementById("adminSearch");
            let filtro = input.value.toUpperCase();
            
            // 2. Prendi la tabella e tutte le sue righe (tr) nel tbody
            let tabella = document.getElementById("tabellaProdotti");
            let righe = tabella.getElementsByTagName("tbody")[0].getElementsByTagName("tr");

            // 3. Cicla attraverso tutte le righe della tabella
            for (let i = 0; i < righe.length; i++) {
                
                // Se la riga contiene il messaggio "Nessun prodotto", saltala
                if (righe[i].getElementsByTagName("td").length === 1) continue;

                // Prendi la cella dell'ID (indice 1) e del Nome (indice 2)
                let cellaID = righe[i].getElementsByTagName("td")[1];
                let cellaNome = righe[i].getElementsByTagName("td")[2];

                if (cellaID && cellaNome) {
                    let testoID = cellaID.textContent || cellaID.innerText;
                    let testoNome = cellaNome.textContent || cellaNome.innerText;

                    // Se il testo cercato è presente nel Nome o nell'ID, mostra la riga, altrimenti nascondila
                    if (testoNome.toUpperCase().indexOf(filtro) > -1 || testoID.toUpperCase().indexOf(filtro) > -1) {
                        righe[i].style.display = ""; // Mostra
                    } else {
                        righe[i].style.display = "none"; // Nascondi
                    }
                }       
            }
        }