document.addEventListener('DOMContentLoaded', function() {
    
    // Cerca il banner della notifica nella pagina
    var toast = document.getElementById("toast-notifica");
    
    // Se il banner esiste (cioè se la Servlet ha passato il parametro)
    if (toast) {
        
        // Aspetta 4 secondi (4000 millisecondi)
        setTimeout(function() {
            
            // 1. Avvia l'animazione di scomparsa (richiede la transition CSS nell'HTML)
            toast.style.opacity = "0"; 
            
            // Aspetta mezzo secondo per la fine dell'animazione e poi lo elimina dal DOM
            setTimeout(function() { 
                toast.remove(); 
            }, 500);
            
            // 2. Pulisce l'URL cancellando "?messaggio=..." senza ricaricare la pagina
            const url = new URL(window.location);
            if (url.searchParams.has('messaggio')) {
                url.searchParams.delete('messaggio');
                window.history.replaceState(null, '', url);
            }
            
        }, 4000);
    }
});