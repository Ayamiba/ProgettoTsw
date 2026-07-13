document.addEventListener('DOMContentLoaded', function() {
    const scadenzaInput = document.getElementById('scadenza');
    const numeroCartaInput = document.getElementById('numeroCarta');
    
    // Formattazione Scadenza (MM/AA)
    if (scadenzaInput) {
        scadenzaInput.addEventListener('input', function(e) {
            let input = e.target.value.replace(/\D/g, ''); 
            if (input.length > 2) {
                e.target.value = input.substring(0, 2) + '/' + input.substring(2, 4);
            } else {
                e.target.value = input;
            }
        });
    }

	// Formattazione Numero Carta (1234-5678-1234-5678)
	    if (numeroCartaInput) {
	        numeroCartaInput.addEventListener('input', function(e) {
	            // Rimuove temporaneamente tutto ciò che non è un numero
	            let input = e.target.value.replace(/\D/g, '');
	            
	            // Spezza la stringa in blocchi di 4 e li unisce con il trattino (Sintassi compatibile con Eclipse)
	            let matchArray = input.match(/.{1,4}/g);
	            let formatted = matchArray ? matchArray.join('-') : '';
	            
	            e.target.value = formatted;
	        });
	    }
});