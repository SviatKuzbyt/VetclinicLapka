const db = require('../config/db');

const Appointment = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id ");
        return rows;
    },
};

module.exports = Appointment;