const Med = require('../models/med');

exports.getAllMedCards = async (req, res) => {
    try {
        const med = await Med.getAll();
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByVet = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByVet(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByPet = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByPet(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByDiagnosis = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByDiagnosis(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByOwner = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByOwner(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByDate = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByDate(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const med = await Med.getInfo(id);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getById = async (req, res) => {
    try {
        const { column, parentid } = req.params;
        const med = await Med.getById(column, parentid);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getInfoCreate = async (req, res) => {
    try {
        const { id } = req.params;
        const appointment = await Med.getInfoCreate(id);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.addMedCard = async (req, res) => {
    try {
        const { appointment, ill, cure } = req.body;
        await Med.addMedCard(appointment, ill, cure);
        res.status(201).json({ 'result': 'success' });
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message }); 
        console.log(error);
    }
};