const db = require('../config/db');

const Vet = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT vet_id as 'id', name as 'label', phone as 'subtext' FROM vet ORDER BY name");
        return rows;
    },

    getByName: async (filter) => {
        const [rows] = await db.execute("SELECT vet_id as 'id', name as 'label', phone as 'subtext' FROM vet WHERE name LIKE ? ORDER BY name ", [`%${filter}%`] )
        return rows; 
    },

    getByPhone: async (filter) => {
        const [rows] = await db.execute("SELECT vet_id as 'id', name as 'label', phone as 'subtext' FROM vet WHERE phone LIKE ? ORDER BY name ", [`%${filter}%`] )
        return rows; 
    },

    getBySpecie: async (filter) => {
        const [rows] = await db.execute("SELECT v.vet_id as 'id', v.name as 'label', v.phone as 'subtext' FROM vet v INNER JOIN vet_speciality vs ON v.vet_id = vs.vet_id INNER JOIN specie s ON vs.specie_id = s.specie_id WHERE s.name LIKE ? ORDER BY v.name", [`%${filter}%`] )
        return rows; 
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
                    CASE WHEN v.is_available = 1 THEN 'Так' ELSE 'Ні' END AS 'is_available', 
                    GROUP_CONCAT(s.name SEPARATOR ', ') AS species
             FROM vet v
             INNER JOIN vet_speciality vs ON v.vet_id = vs.vet_id
             INNER JOIN specie s ON vs.specie_id = s.specie_id
             WHERE v.vet_id = ?
             GROUP BY v.vet_id
             LIMIT 1`, [vet_id]);
        
        return [rows[0].name, rows[0].phone, rows[0].species, rows[0].is_available];
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
        const [rows] = await db.execute(`
            SELECT 
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
    
        // Format the result in the desired output structure
        if (rows.length > 0) {
            result.push({ data: rows[0].vet_name, labelData: "" });
            result.push({ data: rows[0].vet_phone, labelData: "" });
            result.push({ data: rows[0].species, labelData: "" }); // Add species data
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
};

module.exports = Vet;