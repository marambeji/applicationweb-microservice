const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv').config();
const { Eureka } = require('eureka-js-client');

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(express.json());

// MongoDB Connection
mongoose.connect(process.env.MONGODB_URI || 'mongodb://localhost:27017/reclamations_db')
    .then(() => console.log('✅ Connected to MongoDB (Réclamations)'))
    .catch(err => console.error('❌ MongoDB Connection Error:', err));

// Define Reclamation Schema
const reclamationSchema = new mongoose.Schema({
    userId: String,
    sujet: String,
    description: String,
    statut: { type: String, default: 'OUVERT' }, // 'OUVERT', 'EN_COURS', 'RESOLU'
    createdAt: { type: Date, default: Date.now }
});

const Reclamation = mongoose.model('Reclamation', reclamationSchema);

// Routes
app.get('/', (req, res) => {
    res.send('Réclamation Service (Node.js + MongoDB) is running.');
});

// Get all reclamations
app.get('/api/reclamations', async (req, res) => {
    try {
        const reclamations = await Reclamation.find().sort({ createdAt: -1 });
        res.json(reclamations);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Create a reclamation
app.post('/api/reclamations', async (req, res) => {
    try {
        const { userId, sujet, description } = req.body;
        const newReclamation = new Reclamation({ userId, sujet, description });
        await newReclamation.save();
        res.status(201).json(newReclamation);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

// Get a reclamation by ID
app.get('/api/reclamations/:id', async (req, res) => {
    try {
        const reclamation = await Reclamation.findById(req.params.id);
        if (!reclamation) return res.status(404).json({ error: 'Not found' });
        res.json(reclamation);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Update a reclamation
app.put('/api/reclamations/:id', async (req, res) => {
    try {
        const updated = await Reclamation.findByIdAndUpdate(req.params.id, req.body, { new: true });
        if (!updated) return res.status(404).json({ error: 'Not found' });
        res.json(updated);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
});

// Delete a reclamation
app.delete('/api/reclamations/:id', async (req, res) => {
    try {
        const result = await Reclamation.findByIdAndDelete(req.params.id);
        if (!result) return res.status(404).json({ error: 'Not found' });
        res.json({ message: 'Deleted' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

app.listen(PORT, () => {
    console.log(`🚀 Réclamation Service running on port ${PORT}`);

    const client = new Eureka({
        instance: {
            app: 'ms-reclamation',
            hostName: 'localhost',
            ipAddr: '127.0.0.1',
            statusPageUrl: `http://localhost:${PORT}/`,
            port: {
                '$': PORT,
                '@enabled': 'true',
            },
            vipAddress: 'ms-reclamation',
            dataCenterInfo: {
                '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
                name: 'MyOwn',
            },
        },
        eureka: {
            host: 'localhost',
            port: 8761,
            servicePath: '/eureka/apps/',
        },
    });
    client.start();
});
