// Aspetta che il documento sia caricato
document.addEventListener('DOMContentLoaded', () => {
    
    // Logica per il menù Hamburger su dispositivi Mobile
    const hamburger = document.getElementById('hamburger-menu');
    const sidebar = document.getElementById('mobile-sidebar');

    if(hamburger && sidebar) {
        hamburger.addEventListener('click', () => {
            hamburger.classList.toggle('active');
            sidebar.classList.toggle('active');
        });
    }

    
});