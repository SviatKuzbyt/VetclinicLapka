const Pet = require('../models/pet');

exports.getAllPets = async (req, res) => {
    try {
        const pet = await Pet.getAll();
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByName = async (req, res) => {
    try {
        const { filter } = req.params;
        const pet = await Pet.getByName(filter);
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByOwner = async (req, res) => {
    try {
        const { filter } = req.params;
        const pet = await Pet.getByOwner(filter);
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByBreed = async (req, res) => {
    try {
        const { filter } = req.params;
        const pet = await Pet.getByBreed(filter);
        res.status(200).json(pet);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.addPet = async (req, res) => {
    try {
        const { name, breed: breed_id, owner: owner_id, gender, date_of_birth, features } = req.body;
        const insertData = await Pet.addPet(name, breed_id, owner_id, gender, date_of_birth, features);
        console.log(`${insertData[0]} | ${name} | ${insertData[1]}`);
        res.status(201).json({ 'id': insertData.insertId, 'label': name, 'subtext': insertData.subtext });
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: 'Server Error', error: error.message }); 
    }
};