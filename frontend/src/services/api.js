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
    createWithJPA: (book) => api.post('/books/jpa', book),
    createWithMyBatis: (book) => api.post('/books/mybatis', book),
};

export default api;