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

        const [rows] = await db.execute(`SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id WHERE ${filter} = ? ORDER BY a.time DESC`, [parentid] )
        return rows; 
    },

    addAppointment: async (pet, time, vet, complaint) => {
        await db.execute(
            'INSERT INTO appointment (pet_id, time, vet_id, complaint) VALUES (?, ?, ?, ?)',
            [pet, time, vet, complaint]
        );
    },

    getByVetId: async (vetid) => {
        const [rows] = await db.execute("SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM vetclinic_lapka.appointment a INNER JOIN vetclinic_lapka.pet p ON a.pet_id = p.pet_id INNER JOIN vet v ON a.vet_id = v.vet_id WHERE v.vet_id = ? AND DATE(a.`time`) = CURDATE() ORDER BY a.`time` DESC", [vetid] )
        return rows; 
    },

    addAppointmentReturn: async (pet, time, vet, complaint) => {
        const [rows] = await db.execute(
            'INSERT INTO appointment (pet_id, time, vet_id, complaint) VALUES (?, ?, ?, ?)',
            [pet, time, vet, complaint]
        );
    
        const insertId = rows.insertId;
    
        return await db.execute(
            "SELECT a.appointment_id as 'id', a.complaint as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' " +
            "FROM appointment a INNER JOIN pet p ON a.pet_id = p.pet_id WHERE a.appointment_id = ? LIMIT 1", 
            [insertId]
        );
    },
    
    getDataForEdit: async(appointment_id) => {
        const [rows] = await db.execute(
            `SELECT o.owner_id as 'owner',
            o.name as 'owner_name',
            p.pet_id as 'pet',
            p.name as 'pet_name',
            a.complaint as 'complaint',
            DATE_FORMAT(a.time , '%Y-%m-%d %H:%i:%s') as 'time',
            v.vet_id as 'vet',
            v.name as 'vet_name'
            FROM appointment a 
            INNER JOIN pet p ON a.pet_id = p.pet_id 
            INNER JOIN vet v ON a.vet_id = v.vet_id 
            INNER JOIN owner o ON p.owner_id = o.owner_id
            WHERE a.appointment_id = ?`, [appointment_id]
        )

        return [
            { "data": rows[0].owner, "labelData": rows[0].owner_name },
            { "data": rows[0].pet, "labelData": rows[0].pet_name },
            { "data": rows[0].time, "labelData": "" },
            { "data": rows[0].vet, "labelData": rows[0].vet_name },
            { "data": rows[0].complaint, "labelData": "" }
        ]
    },

    updateAppointment: async (pet, time, vet, complaint, updateId) => {
        await db.execute(
            'UPDATE appointment SET pet_id = ?, time = ? , vet_id = ?, complaint = ? WHERE appointment_id = ?',
            [pet, time, vet, complaint, updateId]
        );
    },

    getReport: async (filter, key) => {
        let filterRow;
        let params = [];
    
        switch (filter) {
            case 'pet':
                filterRow = `WHERE p.name LIKE ?`;
                params.push(`%${key}%`);
                break;
            case 'vet':
                filterRow = `WHERE v.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'complaint':
                filterRow = `WHERE a.complaint LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'owner':
                filterRow = `WHERE o.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'date':
                filterRow = `WHERE DATE(a.time) = ?`;
                params.push(key);
                break; 
            case 'vettoday':
                filterRow = `WHERE v.name LIKE ? AND DATE(a.time) = CURDATE()`;
                params.push(`%${key}%`);
                break; 
            default:
                filterRow = ``;
        }
    
        const [result] = await db.execute(
            `select CONCAT(p.name, ', ', b.name) as 'pet', 
                CONCAT(o.name, ' (', o.phone, ')') as 'owner', 
                CONCAT(v.name, ' (', v.phone, ')') as 'vet', 
                DATE_FORMAT(a.time , '%Y.%m.%d %H:%m') as 'time',
                a.complaint as 'complaint'
            from appointment a 
            inner join pet p on a.pet_id = p.pet_id 
            inner join vet v on a.vet_id = v.vet_id 
            inner join owner o on p.owner_id = o.owner_id
            inner join breed b on p.breed_id = b.breed_id
            ${filterRow}
            ORDER BY a.time DESC`,
            params
        );

        let formateResult = []

        for(let i in result){
            formateResult.push(
                `<b>Улюбленець:</b> ${result[i].pet}<br><b>Власник:</b> ${result[i].owner}<br>
                <b>Ветеринар:</b> ${result[i].vet}<br><b>Час прийому:</b> ${result[i].time}<br>
                <b>Скарга:</b> ${result[i].complaint}`
            )
        }
    
        return formateResult;
    }
};

module.exports = Appointment;