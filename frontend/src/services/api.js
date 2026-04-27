import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

const sessionApi = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const authorAPI = {
    getAll: () => api.get('/authors/mybatis'), // Change 'jpa' to 'mybatis' to use it instead of jpa or vice versa
};

export const bookAPI = {
    createWithJPA: (book) => api.post('/books/jpa', book),
    createWithMyBatis: (book) => api.post('/books/mybatis', book),
    addCategory: (bookId, categoryId) => api.post(`/books/${bookId}/categories/${categoryId}/mybatis`),  // Change 'jpa' to 'mybatis' to use it instead of jpa or vice versa
    getCategories: (bookId) => api.get(`/books/${bookId}/categories`),
    removeCategory: (bookId, categoryId) => api.delete(`/books/${bookId}/categories/${categoryId}`),
};

export const categoryAPI = {
    getAll: () => api.get('/categories/jpa'), // Change 'jpa' to 'mybatis' to use it instead of jpa or vice versa
    create: (category) => api.post('/categories', category),
};


export const scopeAPI = {
    incrementRequest: () => sessionApi.post('/scope-demo/request/increment'),
    getRequestValue: () => sessionApi.get('/scope-demo/request/value'),

    incrementSession: () => sessionApi.post('/scope-demo/session/increment'),
    getSessionValue: () => sessionApi.get('/scope-demo/session/value'),

    incrementApplication: () => sessionApi.post('/scope-demo/application/increment'),
    getApplicationValue: () => sessionApi.get('/scope-demo/application/value'),
};


export default api;