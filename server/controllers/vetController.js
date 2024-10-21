const Vet = require('../models/vet');

exports.getAllVets = async (req, res) => {
    try {
        const vet = await Vet.getItems('all');
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByFilter = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const vet = await Vet.getItems(filter, key);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.addVet = async (req, res) => {
    try {
        const { name, phone, spec } = req.body;
        const insertId = await Vet.addVet(name, phone, spec);
        res.status(201).json({ 'id': insertId, 'label': name, 'subtext': phone });
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message }); 
        console.log(error);
    }
};

exports.getInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const vet = await Vet.getInfo(id);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getById = async (req, res) => {
    try {
        const { column, parentid } = req.params;
        const pet = await Vet.getById(column, parentid);
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getEditInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const vet = await Vet.getEditInfo(id);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.updateVet = async (req, res) => {
    try {
        const {name, phone, spec } = req.body;
        const { updateId } = req.params; 
        await Vet.updateVet(name, phone, spec, updateId);
        res.status(200).json({ 'result': "success" });

    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.isAvailable = async (req, res) => {
    try {
        const { id } = req.params;
        const vet = await Vet.isAvailable(id);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.updateAvailable = async (req, res) => {
    try {
        const { available } = req.body;
        const { updateId } = req.params; 
        await Vet.updateAvailable(available, updateId);
        res.status(200).json({ 'result': "success" });

    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getVetsAppointment = async (req, res) => {
    try {
        const { petId, date } = req.params;
        const vet = await Vet.getVetsAppointment(petId, date);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getAvailable = async (req, res) => {
    try {
        const vet = await Vet.getAvailable();
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getReport = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const report = await Vet.getReport(filter, key);
        res.status(200).json(report);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};
