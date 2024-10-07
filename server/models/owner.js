const db = require('../config/db');

const Owner = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT owner_id as 'id', name as 'label', phone as 'subtext' FROM vetclinic_lapka.owner ORDER BY name");
        return rows;
    },

    getByName: async (filter) => {
        const [rows] = await db.execute("SELECT owner_id as 'id', name as 'label', phone as 'subtext' FROM vetclinic_lapka.owner WHERE name LIKE ? ORDER BY name", [`%${filter}%`] )
        return rows; 
    },

    getByPhone: async (filter) => {
        const [rows] = await db.execute("SELECT owner_id as 'id', name as 'label', phone as 'subtext' FROM vetclinic_lapka.owner WHERE phone LIKE ? ORDER BY name", [`%${filter}%`] )
        return rows; 
    },

    addOwner: async (name, phone) => {
        const [result] = await db.execute(
            'INSERT INTO owner (name, phone) VALUES (?, ?)',
            [name, phone]
        );
        return result.insertId;
    },

    getInfo: async (owner_id) => {
        const [rows] = await db.execute('SELECT name, phone FROM owner WHERE owner_id = ? LIMIT 1', [owner_id]);
        return [rows[0].name, rows[0].phone];
    },

    getById: async (filter) => {
        const [rows] = await db.execute("SELECT o.owner_id as 'id', o.name as 'label', o.phone as 'subtext' FROM pet p INNER JOIN owner o ON p.owner_id = o.owner_id WHERE p.pet_id = ?", [filter  ] )
        return rows; 
    }
};

module.exports = Owner;