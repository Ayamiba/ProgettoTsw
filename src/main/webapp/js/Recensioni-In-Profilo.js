
// Gestione Storico Ordini in Profilo dell'Utente
 
function toggleOrdini() {
    var hiddenRows = document.querySelectorAll('.extra-order-row');
    var btn = document.getElementById('btnToggleOrdini');
    if (!hiddenRows.length) return;

    var isHidden = hiddenRows[0].classList.contains('hidden-row');

    hiddenRows.forEach(function(row) {
        if (isHidden) {
            row.classList.remove('hidden-row');
        } else {
            row.classList.add('hidden-row');
        }
    });

    if (isHidden) {
        btn.textContent = 'Mostra meno ordini';
    } else {
        btn.textContent = 'Mostra altri ordini (' + hiddenRows.length + ')';
    }
}