const Appointment = require('../models/appointment');

exports.getAllAppointments = async (req, res) => {
    try {
        const appointment = await Appointment.getAll();
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByVet = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByVet(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByPet = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByPet(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByOwner = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByOwner(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByComplaint = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByComplaint(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByVetToday = async (req, res) => {
    try {
        const { filter } = req.params;
        const appointment = await Appointment.getByVetToday(filter);
        res.status(200).json(appointment);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
        console.log(error);
    }
};

exports.getByDate = async (req, res) => {
    try {exports.addPet = async (req, res) => {
        try {
            const { name, breed: breed_id, owner: owner_id, gender, date_of_birth, features } = req.body;
            const insertData = await Pet.addPet(name, breed_id, owner_id, gender, date_of_birth, features);
            res.status(201).json({ 'id': insertData.insertId, 'label': name, 'subtext': insertData.subtext });
        } catch (error) {
            res.status(500).json({ message: 'Server Error', error: error.message }); 
            console.log(error);
        }
    };
        const { filter } = req.params;
        const appointment = await Appointment.getByDate(filter);
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