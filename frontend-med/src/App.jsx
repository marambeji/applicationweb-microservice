import React, { useState, useEffect } from 'react';
import './App.css';
import keycloak from './keycloak';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [data, setData] = useState({
    users: [],
    materiels: [],
    rendezvous: [],
    pharmacies: [],
    dons: []
  });

  // --- Form States ---
  const [userForm, setUserForm] = useState({ nom: "", prenom: "", email: "", telephone: "" });
  const [rdvForm, setRdvForm] = useState({ patientNom: "", medecinNom: "", specialite: "", dateHeure: "", statut: "PREVU" });
  const [donForm, setDonForm] = useState({ donneurNom: "", groupeSanguin: "", quantiteMl: "", dateDon: "" });
  const [materielForm, setMaterielForm] = useState({ nom: "", description: "", prix: "" });
  const [pharmacieForm, setPharmacieForm] = useState({ nom: "", adresse: "", ville: "", telephone: "" });

  const fetchData = () => {
    if (!keycloak.token) return;
    const headers = { Authorization: `Bearer ${keycloak.token}` };

    const fetchCategory = (url, key) => {
      fetch(url, { headers })
        .then(res => res.json())
        .then(val => setData(prev => ({ ...prev, [key]: Array.isArray(val) ? val : [] })))
        .catch(err => console.error(`Error fetching ${key}:`, err));
    };

    fetchCategory('/api/users', 'users');
    fetchCategory('/api/rendez-vous', 'rendezvous');
    fetchCategory('/api/location-materiel', 'materiels');
    fetchCategory('/api/pharmacie', 'pharmacies');
    fetchCategory('/api/don-de-sang', 'dons');
  };

  useEffect(() => {
    fetchData();
    const interval = setInterval(() => {
      if (keycloak.token) keycloak.updateToken(70).catch(() => console.error("❌ Refresh fail"));
    }, 60000);
    return () => clearInterval(interval);
  }, []);

  const handlePost = async (url, body, msg, resetForm) => {
    const headers = { 'Content-Type': 'application/json', Authorization: `Bearer ${keycloak.token}` };
    try {
      const res = await fetch(url, { method: 'POST', headers, body: JSON.stringify(body) });
      if (!res.ok) throw new Error();
      alert(`✅ ${msg} enregistré !`);
      resetForm();
      fetchData();
    } catch (err) { alert("❌ Erreur lors de l'enregistrement"); }
  };

  return (
    <div className="dashboard-container">
      <nav className="sidebar">
        <div className="logo"><span>🩺</span> MedicalSync</div>
        <ul>
          <li className={activeTab === 'dashboard' ? 'active' : ''} onClick={() => setActiveTab('dashboard')}>📊 Dashboard</li>
          <li className={activeTab === 'users' ? 'active' : ''} onClick={() => setActiveTab('users')}>👥 Utilisateurs</li>
          <li className={activeTab === 'materiels' ? 'active' : ''} onClick={() => setActiveTab('materiels')}>🛠️ Matériel</li>
          <li className={activeTab === 'pharmacies' ? 'active' : ''} onClick={() => setActiveTab('pharmacies')}>💊 Pharmacie</li>
          <li className={activeTab === 'rendezvous' ? 'active' : ''} onClick={() => setActiveTab('rendezvous')}>📅 Rendez-vous</li>
          <li className={activeTab === 'dons' ? 'active' : ''} onClick={() => setActiveTab('dons')}>🩸 Don de Sang</li>
        </ul>
        <div className="user-info-sidebar">
          <p>Session: <strong>{keycloak.tokenParsed?.preferred_username}</strong></p>
          <div className="actions-group">
            <button className="btn-refresh" onClick={fetchData}>🔄 Actualiser</button>
            <button className="btn-logout" onClick={() => keycloak.logout()}>🚪 Déconnexion</button>
          </div>
        </div>
      </nav>

      <main className="main-content">
        <header>
          <h1>{activeTab.toUpperCase()}</h1>
          <div className="user-profile">{keycloak.tokenParsed?.given_name} {keycloak.tokenParsed?.family_name}</div>
        </header>

        {/* --- VIEW: DASHBOARD --- */}
        {activeTab === 'dashboard' && (
          <>
            <section className="stats-grid">
              <div className="stat-card"><h3>Utilisateurs</h3><p className="number">{data.users.length}</p></div>
              <div className="stat-card"><h3>Rendez-vous</h3><p className="number">{data.rendezvous.length}</p></div>
              <div className="stat-card"><h3>Matériels</h3><p className="number">{data.materiels.length}</p></div>
              <div className="stat-card"><h3>Pharmacies</h3><p className="number">{data.pharmacies.length}</p></div>
              <div className="stat-card"><h3>Dons de Sang</h3><p className="number">{data.dons.length}</p></div>
            </section>
            
            <section className="dashboard-welcome">
              <div className="action-card" style={{ width: '100%', textAlign: 'center' }}>
                <h2>🌍 Bienvenue sur MedicalSync</h2>
                <p style={{ color: 'var(--text-muted)', marginTop: '1rem' }}>
                  Utilisez le menu latéral pour gérer les différentes entités du système.
                  Tous les microservices sont connectés via l'API Gateway (8089).
                </p>
              </div>
            </section>
          </>
        )}

        {/* --- VIEW: USERS --- */}
        {activeTab === 'users' && (
          <section className="tab-view">
            <div className="action-card compact-form">
              <h2>👤 Ajouter un Utilisateur</h2>
              <form onSubmit={(e) => { e.preventDefault(); handlePost('/api/users', userForm, "Utilisateur", () => setUserForm({ nom: "", prenom: "", email: "", telephone: "" })); }}>
                <div className="form-row">
                  <input type="text" placeholder="Nom" value={userForm.nom} onChange={e => setUserForm({ ...userForm, nom: e.target.value })} required />
                  <input type="text" placeholder="Prénom" value={userForm.prenom} onChange={e => setUserForm({ ...userForm, prenom: e.target.value })} required />
                  <input type="email" placeholder="Email" value={userForm.email} onChange={e => setUserForm({ ...userForm, email: e.target.value })} required />
                  <input type="text" placeholder="Tel" value={userForm.telephone} onChange={e => setUserForm({ ...userForm, telephone: e.target.value })} required />
                  <button type="submit">Ajouter</button>
                </div>
              </form>
            </div>
            <DataTable tab="users" data={data.users} />
          </section>
        )}

        {/* --- VIEW: RENDEZ-VOUS --- */}
        {activeTab === 'rendezvous' && (
          <section className="tab-view">
            <div className="action-card compact-form">
              <h2>📅 Nouveau RDV</h2>
              <form onSubmit={(e) => { e.preventDefault(); handlePost('/api/rendez-vous', rdvForm, "Rendez-vous", () => setRdvForm({ patientNom: "", medecinNom: "", specialite: "", dateHeure: "", statut: "PREVU" })); }}>
                <div className="form-row">
                  <input type="text" placeholder="Patient" value={rdvForm.patientNom} onChange={e => setRdvForm({ ...rdvForm, patientNom: e.target.value })} required />
                  <input type="text" placeholder="Médecin" value={rdvForm.medecinNom} onChange={e => setRdvForm({ ...rdvForm, medecinNom: e.target.value })} required />
                  <input type="text" placeholder="Spécialité" value={rdvForm.specialite} onChange={e => setRdvForm({ ...rdvForm, specialite: e.target.value })} required />
                  <input type="text" placeholder="Date" value={rdvForm.dateHeure} onChange={e => setRdvForm({ ...rdvForm, dateHeure: e.target.value })} required />
                  <button type="submit" style={{ background: 'var(--success)' }}>Réserver</button>
                </div>
              </form>
            </div>
            <DataTable tab="rendezvous" data={data.rendezvous} />
          </section>
        )}

        {/* --- VIEW: DONS --- */}
        {activeTab === 'dons' && (
          <section className="tab-view">
            <div className="action-card compact-form">
              <h2>🩸 Enregistrer un Don</h2>
              <form onSubmit={(e) => { e.preventDefault(); handlePost('/api/don-de-sang', donForm, "Don", () => setDonForm({ donneurNom: "", groupeSanguin: "", quantiteMl: "", dateDon: "" })); }}>
                <div className="form-row">
                  <input type="text" placeholder="Donneur" value={donForm.donneurNom} onChange={e => setDonForm({ ...donForm, donneurNom: e.target.value })} required />
                  <input type="text" placeholder="Groupe" value={donForm.groupeSanguin} onChange={e => setDonForm({ ...donForm, groupeSanguin: e.target.value })} required />
                  <input type="number" placeholder="ml" value={donForm.quantiteMl} onChange={e => setDonForm({ ...donForm, quantiteMl: e.target.value })} required />
                  <input type="text" placeholder="Date" value={donForm.dateDon} onChange={e => setDonForm({ ...donForm, dateDon: e.target.value })} required />
                  <button type="submit" style={{ background: '#ef4444' }}>Scanner</button>
                </div>
              </form>
            </div>
            <DataTable tab="dons" data={data.dons} />
          </section>
        )}

        {/* --- VIEW: MATERIELS --- */}
        {activeTab === 'materiels' && (
          <section className="tab-view">
            <div className="action-card compact-form">
              <h2>🛠️ Nouveau Matériel</h2>
              <form onSubmit={(e) => { e.preventDefault(); handlePost('/api/location-materiel', materielForm, "Matériel", () => setMaterielForm({ nom: "", description: "", prix: "" })); }}>
                <div className="form-row">
                  <input type="text" placeholder="Nom" value={materielForm.nom} onChange={e => setMaterielForm({ ...materielForm, nom: e.target.value })} required />
                  <input type="text" placeholder="Description" value={materielForm.description} onChange={e => setMaterielForm({ ...materielForm, description: e.target.value })} required />
                  <input type="number" placeholder="Prix" value={materielForm.prix} onChange={e => setMaterielForm({ ...materielForm, prix: e.target.value })} required />
                  <button type="submit" style={{ background: 'var(--accent)' }}>Ajouter</button>
                </div>
              </form>
            </div>
            <DataTable tab="materiels" data={data.materiels} />
          </section>
        )}

        {/* --- VIEW: PHARMACIES --- */}
        {activeTab === 'pharmacies' && (
          <section className="tab-view">
            <div className="action-card compact-form">
              <h2>💊 Nouvelle Pharmacie</h2>
              <form onSubmit={(e) => { e.preventDefault(); handlePost('/api/pharmacie', pharmacieForm, "Pharmacie", () => setPharmacieForm({ nom: "", adresse: "", ville: "", telephone: "" })); }}>
                <div className="form-row">
                  <input type="text" placeholder="Nom" value={pharmacieForm.nom} onChange={e => setPharmacieForm({ ...pharmacieForm, nom: e.target.value })} required />
                  <input type="text" placeholder="Adresse" value={pharmacieForm.adresse} onChange={e => setPharmacieForm({ ...pharmacieForm, adresse: e.target.value })} required />
                  <input type="text" placeholder="Ville" value={pharmacieForm.ville} onChange={e => setPharmacieForm({ ...pharmacieForm, ville: e.target.value })} required />
                  <input type="text" placeholder="Tel" value={pharmacieForm.telephone} onChange={e => setPharmacieForm({ ...pharmacieForm, telephone: e.target.value })} required />
                  <button type="submit" style={{ background: 'var(--primary)' }}>Enregistrer</button>
                </div>
              </form>
            </div>
            <DataTable tab="pharmacies" data={data.pharmacies} />
          </section>
        )}

        <footer className="status-section">
          <div className="status-indicator"></div>
          <p>MedicalSync Cloud Ops • {data[activeTab]?.length || 0} entités détectées</p>
        </footer>
      </main>
    </div>
  );
}

// 🗄️ COMPONENT: Data Table
function DataTable({ tab, data }) {
  const headers = {
    users: ["ID", "Nom", "Email", "Tel"],
    pharmacies: ["Nom", "Adresse", "Ville", "Tel"],
    rendezvous: ["Patient", "Médecin", "Spécialité", "Date"],
    materiels: ["Nom", "Description", "Prix", "Statut"],
    dons: ["Donneur", "Groupe", "Quantité", "Date"]
  };

  return (
    <div className="data-table-container">
      <table className="data-table">
        <thead>
          <tr>
            {headers[tab].map(h => <th key={h}>{h}</th>)}
          </tr>
        </thead>
        <tbody>
          {data?.map((item, i) => (
            <tr key={i}>
              {tab === 'users' && <><td>{item.id}</td><td>{item.nom} {item.prenom}</td><td>{item.email}</td><td>{item.telephone}</td></>}
              {tab === 'pharmacies' && <><td>{item.nom}</td><td>{item.adresse}</td><td>{item.ville}</td><td>{item.telephone}</td></>}
              {tab === 'rendezvous' && <><td>{item.patientNom}</td><td>{item.medecinNom}</td><td>{item.specialite}</td><td>{item.dateHeure}</td></>}
              {tab === 'materiels' && <><td>{item.nom}</td><td>{item.description}</td><td>{item.prix}€</td><td><span className="status-pill">Dispo</span></td></>}
              {tab === 'dons' && <><td>{item.donneurNom}</td><td>{item.groupeSanguin}</td><td>{item.quantiteMl}ml</td><td>{item.dateDon}</td></>}
            </tr>
          ))}
          {(!data || data.length === 0) && (
            <tr><td colSpan="4" style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-muted)' }}>Aucune donnée disponible. Ajoutez-en une !</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default App;