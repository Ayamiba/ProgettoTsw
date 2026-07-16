// Aspetta che la pagina HTML sia completamente caricata
document.addEventListener('DOMContentLoaded', () => {

    const input = document.getElementById('search-input');
    const dropdown = document.getElementById('suggerimenti');
    const form = document.getElementById('search-form');
    let timer; // Cronometro Debounce

    // Se per caso siamo in una pagina senza barra di ricerca, il JS si ferma senza dare errori
    if (!input || !dropdown || !form) return;

    input.addEventListener('input', () => { 
        clearTimeout(timer); 
        const valore = input.value.trim(); 

        if (valore.length < 2) { 
            dropdown.style.display = 'none';
            return;
        }

        // Timer di 150ms quando l'utente termina la digitazione
        timer = setTimeout(() => {
            eseguiAJAX(valore);
        }, 150);
    });

    function eseguiAJAX(test) { 
        fetch('OttieniSuggerimenti?q=' + encodeURIComponent(test)) 
            .then(response => response.json()) 
            .then(dati => mostraSuggerimenti(dati)) 
            .catch(errore => console.error("Errore AJAX:", errore));
    }
            
    function mostraSuggerimenti(lista) { 
        dropdown.innerHTML = ''; 

        if (lista.length === 0) { 
            dropdown.style.display = 'none';
            return;
        }

        lista.forEach(elemento => { 
            const li = document.createElement('li');
            li.textContent = elemento;
            
            li.addEventListener('click', () => { 
                input.value = elemento;      
                dropdown.style.display = 'none'; 
                
                // REINDIRIZZAMENTO DIRETTO ALLA PAGINA DEL PRODOTTO
                // Sostituisci "DettaglioProdottoServlet" con il nome reale della tua Servlet per i singoli prodotti
                window.location.href = 'ProdottoServlet?nome=' + encodeURIComponent(elemento);
            });
            
            dropdown.appendChild(li);
        });

        dropdown.style.display = 'block'; 
    }

    // Chiude la tendina se si clicca altrove
    document.addEventListener('click', (e) => {
        if (!input.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    });

});