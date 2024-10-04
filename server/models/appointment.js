const db = require('../config/db');

const Appointment = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id ORDER BY a.time DESC");
        return rows;
    },

    getByVet: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id INNER JOIN vet v ON a.vet_id = v.vet_id WHERE v.name LIKE ? ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByPet: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id WHERE p.name LIKE ? ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByOwner: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE o.name LIKE ? ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByComplaint: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id WHERE a.complaint LIKE ? ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByVetToday: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id INNER JOIN vet v ON a.vet_id = v.vet_id WHERE v.name LIKE ? AND DATE(a.`time`) = CURDATE() ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByDate: async (filter) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id WHERE DATE(a.`time`) = ? ORDER BY a.time DESC", [filter] )
        return rows; 
    },

    getInfo: async (appointment_id) => {
        const [rows] = await db.execute(
            `SELECT p.name as 'pet', o.name as 'owner', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i') as 'date', v.name as 'vet', a.complaint
            FROM appointment a 
            INNER JOIN pet p ON a.pet_id  = p.pet_id 
            INNER JOIN vet v ON a.vet_id = v.vet_id 
            INNER JOIN owner o ON p.owner_id = o.owner_id 
            WHERE a.appointment_id = ? LIMIT 1`,
            [appointment_id]
        )
        return [rows[0].pet, rows[0].owner, rows[0].date, rows[0].vet, rows[0].complaint]; 
    }
};

module.exports = Appointment;