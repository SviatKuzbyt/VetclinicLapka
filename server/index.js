const express = require('express');
const dotenv = require('dotenv');

const ownerRoutes = require('./routes/ownerRoutes');
const petRoutes = require('./routes/petRoutes');
const vetRoutes = require('./routes/vetRoutes');
const medRoutes = require('./routes/medRoutes');
const appointmentRoutes = require('./routes/appointmentRoutes');
const breedRoutes = require('./routes/breedRoutes');

dotenv.config();

const app = express();

app.use(express.json());

app.use('/owner', ownerRoutes);
app.use('/pet', petRoutes);
app.use('/vet', vetRoutes);
app.use('/medcard', medRoutes);
app.use('/breed', breedRoutes);
app.use('/appointment', appointmentRoutes);

app.get('/', (req, res) => {
    res.send('Welcome to the VetClinic Lapka API');
});

app.use((req, res, next) => {
    res.status(404).json({ message: 'Endpoint not found' });
});

app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ message: 'Something went wrong!', error: err.message });
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
