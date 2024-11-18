const db = require('../config/db');

const Med = {

    getItems: async (filter, key = '') => {
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
            case 'diagnosis':
                filterRow = `WHERE mc.diagnosis LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'owner':
                filterRow = `INNER JOIN owner o ON p.owner_id = o.owner_id WHERE o.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'date':
                filterRow = `WHERE DATE(a.time) = ?`;
                params.push(key);
                break; 
            default:
                filterRow = ``;
        }

        const [result] = await db.execute(
            `SELECT 
                mc.card_id as 'id', 
                mc.diagnosis as 'label', 
                CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext'
            FROM medical_card mc 
            INNER JOIN appointment a ON mc.appointment_id = a.appointment_id 
            INNER JOIN pet p ON a.pet_id = p.pet_id 
            INNER JOIN vet v ON a.vet_id = v.vet_id 
            ${filterRow} ORDER BY a.time DESC`, params);

        return result;
    },

    getInfo: async (card_id) => {
        const [rows] = await db.execute(
            `SELECT 
                p.name as 'pet', 
                o.name as 'owner', 
                DATE_FORMAT(a.time, '%Y.%m.%d %H:%i') as 'date',
                v.name as 'vet', mc.diagnosis, 
                mc.treatment
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
            `SELECT 
                p.name as 'name', 
                b.name as 'breed', 
                CASE WHEN p.gender = 1 Then 'Самець' ELSE 'Самка' END AS 'gender', 
                DATE_FORMAT(p.date_of_birth , '%Y.%m.%d') as 'date_of_birth',
                CASE WHEN p.features is null Then 'Немає' WHEN p.features = '' Then 'Немає' ELSE p.features END AS 'features',
                a.complaint as 'complaint'
            FROM appointment a 
            INNER JOIN pet p ON a.pet_id  = p.pet_id 
            INNER JOIN breed b ON p.breed_id = b.breed_id 
            WHERE a.appointment_id = ? LIMIT 1`,
            [appointment_id]
        )
        return [rows[0].name, rows[0].breed, rows[0].gender, rows[0].date_of_birth, rows[0].features, rows[0].complaint]; 
    },

    addMedCard: async (appointment_id, diagnosis, treatment) => {
        await db.execute(
            'INSERT INTO medical_card(appointment_id, diagnosis, treatment) VALUES (?, ?, ?)',
            [appointment_id, diagnosis, treatment]
        );

    },

    addMedCardReturn: async (appointment_id, diagnosis, treatment) => {
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
            WHERE mc.card_id = ?`, [med_id]
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
            case 'diagnosis':
                filterRow = `WHERE mc.diagnosis LIKE ?`;
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
            default:
                filterRow = ``;
        }
    
        const [result] = await db.execute(
            `select CONCAT(p.name, ', ', b.name) as 'pet', 
                CONCAT(o.name, ' (', o.phone, ')') as 'owner', 
                CONCAT(v.name, ' (', v.phone, ')') as 'vet', 
                DATE_FORMAT(a.time , '%Y.%m.%d %H:%m') as 'time',
                mc.diagnosis as 'diagnosis',
                mc.treatment as 'treatment'
            from medical_card mc 
            inner join appointment a on mc.appointment_id = a.appointment_id  
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
                <b>Діагноз:</b> ${result[i].diagnosis}<br><b>Лікування:</b> ${result[i].treatment}`
            )
        }
    
        return formateResult;
    }
};

module.exports = Med;