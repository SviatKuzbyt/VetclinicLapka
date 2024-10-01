const Owner = require('../models/owner');

exports.getAllOwners = async (req, res) => {
    try {
        const owners = await Owner.getAll();
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByName = async (req, res) => {
    try {
        const { filter } = req.params;
        const owners = await Owner.getByName(filter);
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByPhone = async (req, res) => {
    try {
        const { filter } = req.params;
        const owners = await Owner.getByPhone(filter);
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};