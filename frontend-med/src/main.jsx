import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import keycloak from './keycloak'

const root = createRoot(document.getElementById('root'));

/**
 * Initialisation de Keycloak
 * onLoad: 'login-required' force la redirection vers la page de login si non authentifié.
 * checkLoginIframe: false évite certains problèmes de rechargement en boucle en local.
 */
keycloak.init({ 
    onLoad: 'login-required',
    checkLoginIframe: false 
}).then((authenticated) => {
    if (authenticated) {
        root.render(
            <StrictMode>
                <App />
            </StrictMode>,
        );
    } else {
        window.location.reload();
    }
}).catch((error) => {
    console.error("Keycloak initialization failed", error);
    root.render(
        <div style={{ color: 'white', padding: '20px', background: '#e11d48', borderRadius: '8px' }}>
            <h1>Erreur d'authentification</h1>
            <p>Impossible de se connecter au serveur Keycloak. Vérifiez que le conteneur est lancé sur le port 8088.</p>
        </div>
    );
});

