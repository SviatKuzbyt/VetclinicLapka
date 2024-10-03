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
    }
};

module.exports = Vet;