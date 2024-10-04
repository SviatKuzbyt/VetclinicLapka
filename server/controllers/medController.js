const Med = require('../models/med');

exports.getAllMedCards = async (req, res) => {
    try {
        const med = await Med.getAll();
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByVet = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByVet(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByPet = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByPet(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByDiagnosis = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByDiagnosis(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByOwner = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByOwner(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByDate = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Med.getByDate(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const med = await Med.getInfo(id);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};