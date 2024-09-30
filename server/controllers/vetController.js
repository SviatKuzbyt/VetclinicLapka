const Vet = require('../models/vet');

exports.getAllVets = async (req, res) => {
    try {
        const pet = await Vet.getAll();
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};