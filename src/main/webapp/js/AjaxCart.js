document.addEventListener('DOMContentLoaded', function() {
    console.log("SÆNDWAVE DEBUG 1: Script caricato e DOM pronto.");
    
    const cartForms = document.querySelectorAll('.ajax-cart-form');
    console.log("SÆNDWAVE DEBUG 2: Trovati " + cartForms.length + " form nel catalogo.");
    
    cartForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault(); 
            console.log("SÆNDWAVE DEBUG 3: Bottone cliccato! Invio al server...");
            
            const container = this.closest('.catalog-card') || this.closest('main') || document;
            const imgElement = container.querySelector('.js-prod-img');
            const nameElement = container.querySelector('.js-prod-name');
            const priceElement = container.querySelector('.js-prod-price');
            
            const imgSrc = imgElement ? imgElement.src : 'img/placeholder.png';
            const nome = nameElement ? nameElement.innerText : 'Plugin Audio';
            const prezzo = priceElement ? priceElement.innerText : '€ --';
            
            const formData = new URLSearchParams(new FormData(this));
            formData.append('ajax', 'true');
            
            fetch('AggiungiAlCarrelloServlet', {
                method: 'POST',
                body: formData,
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
            })
            .then(response => {
                console.log("SÆNDWAVE DEBUG 4: Risposta arrivata. Status HTTP: " + response.status);
                // Controllo cruciale: stiamo ricevendo JSON o una pagina HTML intera?
                const contentType = response.headers.get("content-type");
                if (!contentType || !contentType.includes("application/json")) {
                    console.error("ERRORE SERVER: Il server non sta restituendo JSON! Ha restituito: " + contentType);
                }
                return response.json();
            })
            .then(data => {
                console.log("SÆNDWAVE DEBUG 5: Dati letti correttamente:", data);
                if (data.success) {
                    
                    let badge = document.querySelector('.cart-badge');
                    if (!badge) {
                        badge = document.createElement('span');
                        badge.className = 'cart-badge';
                        badge.innerText = '0';
                        document.querySelector('.cart-icon-btn').appendChild(badge);
                    }
                    badge.innerText = parseInt(badge.innerText) + 1;
                    
                    const miniCartItems = document.querySelector('.mini-cart-items');
                    if(miniCartItems) {
                        const emptyMsg = miniCartItems.querySelector('.empty-cart-msg');
                        if (emptyMsg) emptyMsg.remove();
                        
                        const newItem = document.createElement('div');
                        newItem.className = 'mini-cart-item';
                        newItem.innerHTML = `
                            <img src="${imgSrc}" onerror="this.src='img/placeholder.png'">
                            <div class="mini-item-details">
                                <span class="mini-item-name">${nome}</span>
                                <span class="mini-item-price" style="color: var(--colore-primario); font-weight: bold; font-size: 0.85em;">${prezzo}</span>
                            </div>
                        `;
                        miniCartItems.appendChild(newItem);
                    }
                    
                    const dropdown = document.querySelector('.mini-cart-dropdown');
                    if(dropdown) dropdown.style.display = 'block';
                    
                    const btn = this.querySelector('button[type="submit"]');
                    if(btn) {
                        const oldText = btn.innerText;
                        const oldBg = btn.style.backgroundColor;
                        
                        btn.innerText = "Aggiunto!";
                        btn.style.backgroundColor = "#28a745"; 
                        
                        setTimeout(() => {
                            if(dropdown) dropdown.style.display = ''; 
                            btn.innerText = oldText;
                            btn.style.backgroundColor = oldBg; 
                        }, 2000);
                    }
                    console.log("SÆNDWAVE DEBUG 6: UI aggiornata con successo!");
                }
            })
            .catch(error => {
                console.error('SÆNDWAVE ERRORE CRITICO FETCH:', error);
            });
        });
    });
});