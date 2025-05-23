const Appointment = require('../models/appointment');

exports.getAll = async (req, res) => {
    try {
        const appointment = await Appointment.getItems('all');
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByFilter = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const appointment = await Appointment.getItems(filter, key);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const appointment = await Appointment.getInfo(id);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getById = async (req, res) => {
    try {
        const { column, parentid } = req.params;
        const appointment = await Appointment.getById(column, parentid);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.addAppointment = async (req, res) => {
    try {
        const { pet, time, vet, complaint } = req.body;
        await Appointment.addAppointment(pet, time, vet, complaint);
        res.status(201).json({ 'result': 'success' });
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message }); 
        console.log(error);
    }
};

exports.getByVetId = async (req, res) => {
    try {
        const { vetId } = req.params;
        const appointment = await Appointment.getByVetId(vetId);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.addAppointmentReturn = async (req, res) => {
    try {
        const { pet, time, vet, complaint } = req.body;
        const [appointment] = await Appointment.addAppointmentReturn(pet, time, vet, complaint);
        res.status(201).json(appointment[0]);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message }); 
        console.log(error);
    }
};

exports.getDataForEdit = async (req, res) => {
    try {
        const { id } = req.params;
        const appointment = await Appointment.getDataForEdit(id);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.updateAppointment = async (req, res) => {
    try {
        const {pet, time, vet, complaint } = req.body;
        const { updateId } = req.params; 
        await Appointment.updateAppointment(pet, time, vet, complaint, updateId);
        res.status(200).json({ 'result': "success" });

    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getReport = async (req, res) => {
    try {
        const { filter, key } = req.params;
        const report = await Appointment.getReport(filter, key);
        res.status(200).json(report);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};