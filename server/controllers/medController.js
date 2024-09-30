const Med = require('../models/med');

exports.getAllMedCards = async (req, res) => {
    try {
        const owners = await Med.getAll();
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};