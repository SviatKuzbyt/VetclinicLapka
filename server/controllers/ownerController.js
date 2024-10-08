const Owner = require('../models/owner');

exports.getAllOwners = async (req, res) => {
    try {
        const owners = await Owner.getAll();
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByName = async (req, res) => {
    try {
        const { filter } = req.params;
        const owners = await Owner.getByName(filter);
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByPhone = async (req, res) => {
    try {
        const { filter } = req.params;
        const owners = await Owner.getByPhone(filter);
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.addOwner = async (req, res) => {
    try {
        const { name, phone } = req.body;
        const insertId = await Owner.addOwner(name, phone);
        res.status(201).json({ 'id': insertId, 'label': name, 'subtext': phone });
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message }); 
        console.log(error);
    }
};

exports.getInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const owners = await Owner.getInfo(id);
        res.status(200).json(owners);
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getById = async (req, res) => {
    try {
        const { column, parentid } = req.params;
        const owners = await Owner.getById(parentid);
        res.status(200).json(owners);
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getEditInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const owners = await Owner.getEditInfo(id);
        res.status(200).json(owners);
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.updateOwner = async (req, res) => {
    try {
        const {name, phone, updateId } = req.body; // Make sure the body contains the right fields
        const result = await Owner.updateOwner(name, phone, updateId);

        if (result.affectedRows > 0) {
            res.status(200).json({ 'result': "success" });
        } else {
            res.status(404).json({ 'result': "owner not found" }); // If no rows affected, owner wasn't found
        }
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};