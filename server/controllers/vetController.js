const Vet = require('../models/vet');

exports.getAllVets = async (req, res) => {
    try {
        const vet = await Vet.getAll();
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByName = async (req, res) => {
    try {
        const { filter } = req.params;
        const vet = await Vet.getByName(filter);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByPhone = async (req, res) => {
    try {
        const { filter } = req.params;
        const vet = await Vet.getByPhone(filter);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getBySpecie = async (req, res) => {
    try {
        const { filter } = req.params;
        const vet = await Vet.getBySpecie(filter);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};