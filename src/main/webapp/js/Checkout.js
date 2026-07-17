document.addEventListener('DOMContentLoaded', () => {
    // Rendiamo la funzione disponibile globalmente se chiamata dall'HTML
    window.toggleTracciaSource = function(source) {
        const uploadBox = document.getElementById('uploadBoxNuova');
        const selectCloud = document.getElementById('selectCloud');
        
        if (source === 'cloud') {
            if (uploadBox) uploadBox.classList.add('d-none');
            if (selectCloud) selectCloud.classList.remove('d-none');
        } else {
            if (uploadBox) uploadBox.classList.remove('d-none');
            if (selectCloud) selectCloud.classList.add('d-none');
        }
    };
});