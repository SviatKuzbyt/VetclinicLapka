const Appointment = require('../models/appointment');

exports.getAllAppointments = async (req, res) => {
    try {
        const appointment = await Appointment.getAll();
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByVet = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByVet(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByPet = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByPet(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByOwner = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByOwner(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByComplaint = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByComplaint(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByVetToday = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByVetToday(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getByDate = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByDate(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getInfo = async (req, res) => {
    try {
        const { id } = req.params;
        const appointment = await Appointment.getInfo(id);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};

exports.getById = async (req, res) => {
    try {
        const { column, parentid } = req.params;
        const appointment = await Appointment.getById(column, parentid);
        res.status(200).json(appointment);
    } catch (error) {
        console.log(error)
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};
