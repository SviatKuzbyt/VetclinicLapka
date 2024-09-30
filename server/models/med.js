const db = require('../config/db');

const Med = {
    getAll: async () => {
        const [rows] = await db.execute("SELECT mc.card_id as 'id', mc.diagnosis as 'label', CONCAT(p.name, ', ', DATE_FORMAT(a.time, '%Y.%m.%d %H:%i')) as 'subtext' FROM medical_card mc INNER JOIN appointment a ON mc.appointment_id = a.appointment_id INNER JOIN pet p ON a.pet_id = p.pet_id ");
        return rows;
    },
};

module.exports = Med;