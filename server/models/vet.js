const db = require('../config/db');

const Vet = {

    getItems: async (filter, key = '') => {
        let filterRow;
        let params = [];
    
        switch (filter) {
            case 'name':
                filterRow = `WHERE v.name LIKE ?`;
                params.push(`%${key}%`);
                break;
            case 'phone':
                filterRow = `WHERE v.phone LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'specie':
                filterRow = `INNER JOIN vet_speciality vs ON v.vet_id = vs.vet_id INNER JOIN specie s ON vs.specie_id = s.specie_id WHERE s.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            default:
                filterRow = ``;
        }

        const [result] = await db.execute(
            `SELECT v.vet_id as 'id', v.name as 'label', v.phone as 'subtext' FROM vet v 
            ${filterRow} ORDER BY v.name`, params
        );

        return result;
    },

    addVet: async (name, phone, spec) => {
        const [result] = await db.execute(
            'INSERT INTO vet (name, phone) VALUES (?, ?)',
            [name, phone]
        );

        for (let i = 0; i < spec.length; i++) {
            if (spec[i] === '1') {
                await db.execute(`INSERT INTO vet_speciality (vet_id, specie_id) VALUES (${result.insertId}, ${i+1})`)
            }
        }
        return result.insertId;
    },

    getInfo: async (vet_id) => {
        const [rows] = await db.execute(
            `SELECT v.name, 
                    v.phone,
                    GROUP_CONCAT(s.name SEPARATOR ', ') AS species
             FROM vet v
             INNER JOIN vet_speciality vs ON v.vet_id = vs.vet_id
             INNER JOIN specie s ON vs.specie_id = s.specie_id
             WHERE v.vet_id = ?
             GROUP BY v.vet_id
             LIMIT 1`, [vet_id]);
        
        return [rows[0].name, rows[0].phone, rows[0].species];
    },    
    
    getById: async (column, parentid) => {

        switch (column) {
            case 'appointment':
                filter = 'INNER JOIN appointment a ON v.vet_id = a.vet_id WHERE a.appointment_id';
                break;
            case 'medcard':
                filter = 'INNER JOIN appointment a ON v.vet_id = a.vet_id INNER JOIN medical_card mc ON a.appointment_id = mc.appointment_id WHERE mc.card_id';
                break; 
            default:
                filter = 'mc.card_id';
        }

        const [rows] = await db.execute(`SELECT v.vet_id as 'id', v.name as 'label', v.phone as 'subtext' FROM vet v ${filter} = ?`, [parentid])
        return rows; 
    },

    getEditInfo: async (vet_id) => {
        const [rows] = await db.execute(
            `SELECT 
                v.name AS vet_name,
                v.phone AS vet_phone,
                CONCAT(
                    MAX(CASE WHEN vs.specie_id = 1 THEN '1' ELSE '0' END),
                    MAX(CASE WHEN vs.specie_id = 2 THEN '1' ELSE '0' END),
                    MAX(CASE WHEN vs.specie_id = 3 THEN '1' ELSE '0' END),
                    MAX(CASE WHEN vs.specie_id = 4 THEN '1' ELSE '0' END)
                ) AS species
            FROM 
                vet v
            LEFT JOIN 
                vet_speciality vs ON v.vet_id = vs.vet_id
            WHERE 
                v.vet_id = ?
            GROUP BY 
                v.vet_id`, [vet_id]);
    
        const result = [];
    
        if (rows.length > 0) {
            result.push({ data: rows[0].vet_name, labelData: "" });
            result.push({ data: rows[0].vet_phone, labelData: "" });
            result.push({ data: rows[0].species, labelData: "" });
        }
    
        return result;
    },

    updateVet: async (name, phone, spec, updateId) => {
        await db.execute(
            'UPDATE vet SET name = ?, phone = ? WHERE vet_id = ?',
            [name, phone, updateId]
        );

        await db.execute(`DELETE FROM vet_speciality WHERE vet_id = ?`, [updateId])

        for (let i = 0; i < spec.length; i++) {
            if (spec[i] === '1') {
                await db.execute(`INSERT INTO vet_speciality (vet_id, specie_id) VALUES (?, ${i+1})`, [updateId])
            }
        }
    },

    isAvailable: async (id) => {
        const [rows] = await db.execute(`SELECT is_available FROM vet WHERE vet_id = ?`, [id])
        return rows[0].is_available; 
    },

    updateAvailable: async (available, updateId) => {
        await db.execute(
            'UPDATE vet SET is_available = ? WHERE vet_id = ?',
            [available, updateId]
        );
    },

    getVetsAppointment: async (petId, date) => {
        const [specie] = await db.execute(
            `SELECT b.specie_id as 'specie_id' FROM pet p
            INNER JOIN breed b ON p.breed_id = b.breed_id
            WHERE p.pet_id = ?`, [petId]
        );

        const [vets] = await db.execute(
            `SELECT v.vet_id AS 'id', v.name AS 'label', v.phone AS 'subtext'
            FROM vet v
            INNER JOIN vet_speciality vs ON v.vet_id = vs.vet_id
            LEFT JOIN appointment a ON v.vet_id = a.vet_id AND a.time = ?
            WHERE vs.specie_id = ? AND a.vet_id IS NULL AND v.is_available = 1
            GROUP BY v.vet_id
            ORDER BY v.name`, [date, specie[0].specie_id]
        );
        
        return vets; 
    },

    getAvailable: async () => {
        const [rows] = await db.execute("SELECT vet_id as 'id', name as 'label', phone as 'subtext' FROM vet WHERE is_available=1 ORDER BY name");
        return rows;
    },

    getReport: async (filter, key) => {
        let filterRow;
        let params = [];
    
        switch (filter) {
            case 'name':
                filterRow = `WHERE v.name LIKE ?`;
                params.push(`%${key}%`);
                break;
            case 'phone':
                filterRow = `WHERE v.phone LIKE ?`;
                params.push(`%${key}%`);
                break; 
            case 'specie':
                filterRow = `WHERE s.name LIKE ?`;
                params.push(`%${key}%`);
                break; 
            default:
                filterRow = ``;
        }
    
        const [result] = await db.execute(
            `SELECT 
                v.name AS vet_name,
                v.phone AS vet_phone,
                CASE WHEN v.is_available = 1 Then 'Доступний' ELSE 'Не доступний' END AS 'is_available',
                GROUP_CONCAT(DISTINCT s.name ORDER BY s.name ASC SEPARATOR ', ') AS species_names,
                COUNT(DISTINCT mc.card_id) AS medical_card_count
            FROM vet v
            LEFT JOIN vet_speciality vs ON v.vet_id = vs.vet_id
            LEFT JOIN specie s ON vs.specie_id = s.specie_id
            LEFT JOIN appointment a ON v.vet_id = a.vet_id
            LEFT JOIN medical_card mc ON a.appointment_id = mc.appointment_id
            ${filterRow}
            GROUP BY v.vet_id ORDER BY v.name`, params
        );

        let formateResult = []

        for(let i in result){
            formateResult.push(
                `<b>Ім'я:</b> ${result[i].vet_name}<br><b>Телефон:</b> ${result[i].vet_phone}<br>
                <b>Дотсупний:</b> ${result[i].is_available}<br><b>Спеціальність:</b> ${result[i].species_names}<br>
                <b>К-сть медичних записів:</b> ${result[i].medical_card_count}`
            )
        }
    
        return formateResult;
    }
};

module.exports = Vet;