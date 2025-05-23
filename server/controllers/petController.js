const Pet = require('../models/pet');

exports.getAllPets = async (req, res) => {
    try {
        const pet = await Pet.getItems('all');
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByFilter = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const pet = await Pet.getItems(filter, key);
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.addPet = async (req, res) => {
    try {
        const { name, breed, owner, gender, date_of_birth, features } = req.body;
        const insertData = await Pet.addPet(name, breed, owner, gender, date_of_birth, features);
        res.status(201).json({ 'id': insertData.insertId, 'label': name, 'subtext': insertData.subtext });
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message }); 
        console.log(error);
    }
};

exports.getInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const owners = await Pet.getInfo(id);
        res.status(200).json(owners);
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getById = async (req, res) => {
    try {
        const { column, parentid } = req.params;
        const pet = await Pet.getById(column, parentid);
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getEditInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const pet = await Pet.getEditInfo(id);
        res.status(200).json(pet);
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.updatePet = async (req, res) => {
    try {
        const {name, breed, owner, gender, date_of_birth, features } = req.body;
        const { updateId } = req.params; 
        await Pet.updatePet(name, breed, owner, gender, date_of_birth, features, updateId);
        res.status(200).json({ 'result': "success" });

    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getReport = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const report = await Pet.getReport(filter, key);
        res.status(200).json(report);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};