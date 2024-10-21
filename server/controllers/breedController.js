const Breed = require('../models/breed');

exports.getAll = async (req, res) => {
    try {
        const pet = await Breed.getItems(`all`);
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByFilter = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const med = await Breed.getItems(filter, key);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};