const db = require('../config/db');

const Appointment = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id ORDER BY a.time");
        return rows;
    },

    getByVet: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id INNER JOIN vet v ON a.vet_id = v.vet_id WHERE v.name LIKE ? ORDER BY a.`time`", [`%${filter}%`] )
        return rows; 
    },

    getByPet: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id WHERE p.name LIKE ? ORDER BY a.`time`", [`%${filter}%`] )
        return rows; 
    },

    getByOwner: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE o.name LIKE ? ORDER BY a.`time`", [`%${filter}%`] )
        return rows; 
    },

    getByComplaint: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id WHERE a.complaint LIKE ? ORDER BY a.`time`", [`%${filter}%`] )
        return rows; 
    },

    getByVetToday: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id INNER JOIN vet v ON a.vet_id = v.vet_id WHERE v.name LIKE ? AND DATE(a.`time`) = CURDATE() ORDER BY a.`time`", [`%${filter}%`] )
        return rows; 
    }
};

module.exports = Appointment;