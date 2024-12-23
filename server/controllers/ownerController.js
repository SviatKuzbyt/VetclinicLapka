const Owner = require('../models/owner');

exports.getAllOwners = async (req, res) => {
    try {
        const owners = await Owner.getItems('all');
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByFilter = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const owners = await Owner.getItems(filter, key);
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
        const {name, phone } = req.body; 
        const { updateId } = req.params; 
        await Owner.updateOwner(name, phone, updateId);
        res.status(200).json({ 'result': "success" });
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getReport = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const report = await Owner.getReport(filter, key);
        res.status(200).json(report);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};