
        function toggleOrders() {
            var button = document.getElementById('btnToggleOrders');
            // Cerca tutte le righe della tabella all'interno del corpo (escludendo l'intestazione)
            var rows = document.querySelectorAll('.dash-table tbody tr');
            
            // Variabile per capire se attualmente la tabella è espansa o no
            var isExpanded = button.getAttribute('data-expanded') === 'true';
            var hiddenCount = 0;

            // Il ciclo parte da 3, così salta i primi 3 ordini (che devono essere sempre visibili)
            for (var i = 3; i < rows.length; i++) {
                hiddenCount++;
                if (isExpanded) {
                    // Se era espansa, nascondi di nuovo le righe
                    rows[i].classList.add('order-row-hidden');
                } else {
                    // Se era ridotta, mostra le righe
                    rows[i].classList.remove('order-row-hidden');
                }
            }

            // Cambia il testo del bottone in base allo stato
            if (isExpanded) {
                button.setAttribute('data-expanded', 'false');
                button.innerText = 'Mostra altri ordini (' + hiddenCount + ')';
            } else {
                button.setAttribute('data-expanded', 'true');
                button.innerText = 'Nascondi ordini \u2191'; // Aggiunge una freccina in sù
            }
        }
  