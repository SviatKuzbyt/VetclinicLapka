const Breed = require('../models/breed');

exports.getAllBreeds = async (req, res) => {
    try {
        const pet = await Breed.getAll();
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};