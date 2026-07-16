document.addEventListener("DOMContentLoaded", function() {
    var player = document.getElementById("globalAudioPlayer");
    var playButtons = document.querySelectorAll(".btn-play-audio");
    var currentBtn = null;

    // Ciclo standard compatibile con tutti i validatori
    for (var i = 0; i < playButtons.length; i++) {
        playButtons[i].addEventListener("click", function() {
            var fileParam = this.getAttribute("data-file");
            // Costruiamo l'URL relativo alla servlet
            var url = "AscoltaTracciaServlet?file=" + encodeURIComponent(fileParam);
            
            var iconSpan = this.querySelector(".play-icon");

            // Caso 1: Clicco sullo stesso pulsante attualmente in riproduzione -> Metti in pausa o riprendi
            if (currentBtn === this) {
                if (!player.paused) {
                    player.pause();
                    if (iconSpan) iconSpan.textContent = "▶";
                    this.childNodes[1].textContent = " Ascolta";
                } else {
                    player.play();
                    if (iconSpan) iconSpan.textContent = "⏸";
                    this.childNodes[1].textContent = " Pausa";
                }
                return;
            }

            // Caso 2: Clicco su un pulsante diverso mentre un altro suonava -> Resetta il vecchio
            if (currentBtn !== null) {
                var prevIcon = currentBtn.querySelector(".play-icon");
                if (prevIcon) {
                    prevIcon.textContent = "▶";
                }
                currentBtn.childNodes[1].textContent = " Ascolta";
            }

            // Carica il nuovo brano ed esegui il play
            player.src = url;
            player.play();
            
            // Aggiorna lo stato attuale del nuovo bottone cliccato
            if (iconSpan) {
                iconSpan.textContent = "⏸";
            }
            this.childNodes[1].textContent = " Pausa";
            currentBtn = this;
        });
    }

    // Se la traccia finisce da sola, ripristina lo stato del bottone a "Ascolta"
    if (player) {
        player.addEventListener("ended", function() {
            if (currentBtn !== null) {
                var iconSpan = currentBtn.querySelector(".play-icon");
                if (iconSpan) {
                    iconSpan.textContent = "▶";
                }
                currentBtn.childNodes[1].textContent = " Ascolta";
                currentBtn = null;
            }
        });
    }
});