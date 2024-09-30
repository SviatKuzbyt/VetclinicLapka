const db = require('../config/db');

const Vet = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT vet_id as 'id', name as 'label', phone as 'subtext' FROM vet");
        return rows;
    },
};

module.exports = Vet;