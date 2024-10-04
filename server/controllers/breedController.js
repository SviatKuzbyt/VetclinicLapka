const Breed = require('../models/breed');

exports.getAllBreeds = async (req, res) => {
    try {
        const pet = await Breed.getAll();
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByName = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Breed.getByName(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getBySpecie = async (req, res) => {
    try {
        const { filter } = req.params;
        const med = await Breed.getBySpecie(filter);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};
