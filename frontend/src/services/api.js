import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const authorAPI = {
    getAll: () => api.get('/authors'),
};

export const bookAPI = {
    create: (book) => api.post('/books', book),
};

export default api;