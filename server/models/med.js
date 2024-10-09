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
            case 'appointment':
                filter = 'a.appointment_id';
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
    },

    getInfoCreate: async (appointment_id) => {
        const [rows] = await db.execute(
            `SELECT p.name as 'name', b.name as 'breed', CASE WHEN p.gender = 1 Then 'Самець' ELSE 'Самка' END AS 'gender', DATE_FORMAT(p.date_of_birth , '%Y.%m.%d') as 'date_of_birth', a.complaint as 'complaint'
            FROM appointment a 
            INNER JOIN pet p ON a.pet_id  = p.pet_id 
            INNER JOIN breed b ON p.breed_id = b.breed_id 
            WHERE a.appointment_id = ? LIMIT 1`,
            [appointment_id]
        )
        return [rows[0].name, rows[0].breed, rows[0].gender, rows[0].date_of_birth, rows[0].complaint]; 
    },

    addMedCard: async (appointment_id, diagnosis, treatment) => {
        await db.execute(
            'INSERT INTO medical_card(appointment_id, diagnosis, treatment) VALUES (?, ?, ?)',
            [appointment_id, diagnosis, treatment]
        );

    },

    addMedCardReturn: async (appointment_id, diagnosis, treatment) => {
        console.log(appointment_id, diagnosis, treatment)
        const [rows] = await db.execute(
            'INSERT INTO medical_card(appointment_id, diagnosis, treatment) VALUES (?, ?, ?)',
            [appointment_id, diagnosis, treatment]
        );
    
        const insertId = rows.insertId;
    
        return await db.execute(
            "SELECT mc.card_id as 'id', mc.diagnosis as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM medical_card mc INNER JOIN appointment a ON mc.appointment_id = a.appointment_id INNER JOIN pet p ON a.pet_id = p.pet_id WHERE mc.card_id = ? LIMIT 1", 
            [insertId]
        );
    },  

    getDataForEdit: async(med_id) => {
        const [rows] = await db.execute(
            `SELECT v.vet_id  as 'vet',
            v.name as 'vet_name',
            a.appointment_id as 'appointment',
            a.complaint as 'complaint',
            mc.diagnosis as 'diagnosis',
            mc.treatment as 'treatment'
            FROM medical_card mc 
            INNER JOIN appointment a ON mc.appointment_id = a.appointment_id 
            INNER JOIN vet v ON a.vet_id = v.vet_id 
            WHERE mc.card_id = 22`, [med_id]
        )

        return [
            { "data": rows[0].vet, "labelData": rows[0].vet_name },
            { "data": rows[0].appointment, "labelData": rows[0].complaint },
            { "data": rows[0].diagnosis, "labelData": "" },
            { "data": rows[0].treatment, "labelData": "" }
        ]
    },

    updateMedCard: async (appointment_id, diagnosis, treatment, card_id) => {
        await db.execute(
            'UPDATE medical_card SET appointment_id = ?, diagnosis = ?, treatment = ? WHERE card_id = ?',
            [appointment_id, diagnosis, treatment, card_id]
        );
    }
};

module.exports = Med;

