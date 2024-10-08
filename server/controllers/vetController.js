const Vet = require('../models/vet');

exports.getAllVets = async (req, res) => {
    try {
        const vet = await Vet.getAll();
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByName = async (req, res) => {
    try {
        const { filter } = req.params;
        const vet = await Vet.getByName(filter);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByPhone = async (req, res) => {
    try {
        const { filter } = req.params;
        const vet = await Vet.getByPhone(filter);
        res.status(200).json(vet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getBySpecie = async (req, res) => {
    try {
        const { filter } = req.params;
        const vet = await Vet.getBySpecie(filter);
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
        console.log(vet);
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
        const result = await Vet.updateVet(name, phone, spec, updateId);
        res.status(200).json({ 'result': "success" });

    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};