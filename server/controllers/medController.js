const Med = require('../models/med');

exports.getAll = async (req, res) => {
    try {
        const med = await Med.getItems('all');
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByFilter = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const med = await Med.getItems(filter, key);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const med = await Med.getInfo(id);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getById = async (req, res) => {
    try {
        const { column, parentid } = req.params;
        const med = await Med.getById(column, parentid);
        res.status(200).json(med);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getInfoCreate = async (req, res) => {
    try {
        const { id } = req.params;
        const appointment = await Med.getInfoCreate(id);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.addMedCard = async (req, res) => {
    try {
        const { appointment, ill, cure } = req.body;
        await Med.addMedCard(appointment, ill, cure);
        res.status(201).json({ 'result': 'success' });
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message }); 
        console.log(error);
    }
};

exports.addMedCardReturn = async (req, res) => {
    try {
        const { appointment, ill, cure } = req.body;
        const [med] = await Med.addMedCardReturn(appointment, ill, cure);
        res.status(201).json(med[0]);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message }); 
        console.log(error);
    }
};

exports.getDataForEdit = async (req, res) => {
    try {
        const { med_id } = req.params;
        const appointment = await Med.getDataForEdit(med_id);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.updateMedCard = async (req, res) => {
    try {
        const {appointment, ill, cure } = req.body;
        const { card_id } = req.params;
        await Med.updateMedCard(appointment, ill, cure, card_id);
        res.status(200).json({ 'result': "success" });

    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getReport = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const report = await Med.getReport(filter, key);
        res.status(200).json(report);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};