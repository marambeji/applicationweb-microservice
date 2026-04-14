import Keycloak from 'keycloak-js';

// Configuration de Keycloak
// Assurez-vous que le client 'react-client' existe dans le realm 'MedicalSync'
// et que 'Valid Redirect URIs' inclut 'http://localhost:5173/*'
const keycloakConfig = {
  url: "http://localhost:8088",
  realm: "MedicalSync",
  clientId: "react-client",
};

const keycloak = new Keycloak(keycloakConfig);

export default keycloak;
