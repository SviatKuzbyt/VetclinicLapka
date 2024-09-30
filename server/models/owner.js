const db = require('../config/db');

const Owner = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT owner_id as 'id', name as 'label', phone as 'subtext' FROM vetclinic_lapka.owner");
        return rows;
    },
};

module.exports = Owner;