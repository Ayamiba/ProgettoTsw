document.addEventListener("DOMContentLoaded", function() {
    var player = document.getElementById("globalAudioPlayer");
    var playButtons = document.querySelectorAll(".btn-play-audio");
    var currentBtn = null;

    for (var i = 0; i < playButtons.length; i++) {
        playButtons[i].addEventListener("click", function() {
            var fileParam = this.getAttribute("data-file");
            var url = "AscoltaTracciaServlet?file=" + encodeURIComponent(fileParam);
            var iconSpan = this.querySelector(".play-icon");

            // Caso 1: Mette in pausa o riprende la stessa traccia
            if (currentBtn === this) {
                if (!player.paused) {
                    player.pause();
                    if (iconSpan) iconSpan.textContent = "▶";
                } else {
                    player.play();
                    if (iconSpan) iconSpan.textContent = "⏸";
                }
                return;
            }

            // Caso 2: Resetta l'icona del pulsante precedente se si cambia traccia
            if (currentBtn !== null) {
                var prevIcon = currentBtn.querySelector(".play-icon");
                if (prevIcon) {
                    prevIcon.textContent = "▶";
                }
            }

            // Riproduce la nuova traccia
            player.src = url;
            player.play();
            
            // Imposta l'icona "Pausa" sul nuovo bottone
            if (iconSpan) {
                iconSpan.textContent = "⏸";
            }
            currentBtn = this;
        });
    }

    // Ripristina l'icona quando la traccia finisce
    if (player) {
        player.addEventListener("ended", function() {
            if (currentBtn !== null) {
                var iconSpan = currentBtn.querySelector(".play-icon");
                if (iconSpan) {
                    iconSpan.textContent = "▶";
                }
                currentBtn = null;
            }
        });
    }
});