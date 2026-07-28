document.addEventListener('DOMContentLoaded', () => {

    const input = document.getElementById('search-input');
    const dropdown = document.getElementById('suggerimenti');
    const form = document.getElementById('search-form');
    let timer;

    if (!input || !dropdown || !form) return;

    // 1. PREMENDO INVIO: va al catalogo con tutti i risultati corrispondenti
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const valore = input.value.trim();
        if (valore.length > 0) {
            dropdown.style.display = 'none';
            window.location.href = 'CatalogoServlet?search=' + encodeURIComponent(valore);
        }
    });

    // 2. DIGITAZIONE: mostra i suggerimenti in tempo reale
    input.addEventListener('input', () => { 
        clearTimeout(timer); 
        const valore = input.value.trim(); 

        if (valore.length < 2) { 
            dropdown.style.display = 'none';
            return;
        }

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
            
            // CLICK SUL SUGGERIMENTO: va direttamente alla scheda di quel singolo prodotto
            li.addEventListener('click', () => { 
                input.value = elemento;      
                dropdown.style.display = 'none'; 
                window.location.href = 'ProdottoServlet?nome=' + encodeURIComponent(elemento);
            });
            
            dropdown.appendChild(li);
        });

        dropdown.style.display = 'block'; 
    }

    document.addEventListener('click', (e) => {
        if (!input.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    });
});