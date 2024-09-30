const Appointment = require('../models/appointment');

exports.getAllAppointments = async (req, res) => {
    try {
        const owners = await Appointment.getAll();
        res.status(200).json(owners);
    } catch (error) {
        res.status(500).json({ message: 'Server Error', error: error.message });
    }
};