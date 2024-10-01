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
    }
};

module.exports = Owner;