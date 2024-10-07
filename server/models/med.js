const db = require('../config/db');

const Med = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT mc.card_id as 'id', mc.diagnosis as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM medical_card mc INNER JOIN appointment a ON mc.appointment_id = a.appointment_id INNER JOIN pet p ON a.pet_id = p.pet_id ORDER BY a.`time` DESC");
        return rows;
    },

    getByVet: async (filter) => {
        const [rows] = await db.execute("SELECT mc.card_id as 'id', mc.diagnosis as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext'FROM medical_card mc INNER JOIN appointment a ON mc.appointment_id = a.appointment_id INNER JOIN pet p ON a.pet_id = p.pet_id INNER JOIN vet v ON a.vet_id = v.vet_id WHERE v.name LIKE ? ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByPet: async (filter) => {
        const [rows] = await db.execute("SELECT mc.card_id as 'id', mc.diagnosis as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext'FROM medical_card mc INNER JOIN appointment a ON mc.appointment_id = a.appointment_id INNER JOIN pet p ON a.pet_id = p.pet_id WHERE p.name LIKE ? ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByDiagnosis: async (filter) => {
        const [rows] = await db.execute("SELECT mc.card_id as 'id', mc.diagnosis as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext'FROM medical_card mc INNER JOIN appointment a ON mc.appointment_id = a.appointment_id INNER JOIN pet p ON a.pet_id = p.pet_id WHERE mc.diagnosis LIKE ? ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByOwner: async (filter) => {
        const [rows] = await db.execute("SELECT mc.card_id as 'id', mc.diagnosis as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext'FROM medical_card mc INNER JOIN appointment a ON mc.appointment_id = a.appointment_id INNER JOIN pet p ON a.pet_id = p.pet_id INNER JOIN owner o ON p.owner_id = o.owner_id WHERE o.name LIKE ? ORDER BY a.`time` DESC", [`%${filter}%`] )
        return rows; 
    },

    getByDate: async (filter) => {
        const [rows] = await db.execute("SELECT mc.card_id as 'id', mc.diagnosis as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM medical_card mc INNER JOIN appointment a ON mc.appointment_id = a.appointment_id INNER JOIN pet p ON a.pet_id = p.pet_id WHERE DATE(a.`time`) = ? ORDER BY a.`time` DESC", [filter] )
        return rows; 
    },

    getInfo: async (card_id) => {
        const [rows] = await db.execute(
            `SELECT p.name as 'pet', o.name as 'owner', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i') as 'date', v.name as 'vet', mc.diagnosis, mc.treatment
            FROM medical_card mc
            INNER JOIN appointment a ON mc.appointment_id = a.appointment_id 
            INNER JOIN pet p ON a.pet_id  = p.pet_id 
            INNER JOIN vet v ON a.vet_id = v.vet_id 
            INNER JOIN owner o ON p.owner_id = o.owner_id 
            WHERE mc.card_id = ? LIMIT 1`,
            [card_id]
        )
        return [rows[0].pet, rows[0].owner, rows[0].date, rows[0].vet, rows[0].diagnosis, rows[0].treatment]; 
    },

    getById: async (column, parentid) => {
        let filter;

        switch (column) {
            case 'pet':
                filter = 'a.pet_id';
                break;
            case 'vet':
                filter = 'a.vet_id';
                break;
            default:
                filter = 'mc.card_id';
        }

        const [rows] = await db.execute(
            `SELECT mc.card_id as 'id', mc.diagnosis as 'label', 
             CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext'
             FROM medical_card mc 
             INNER JOIN appointment a ON mc.appointment_id = a.appointment_id 
             INNER JOIN pet p ON a.pet_id = p.pet_id 
             WHERE ${filter} = ? 
             ORDER BY a.time DESC`, 
            [parentid]
        );
        return rows;
    }
};

module.exports = Med;

