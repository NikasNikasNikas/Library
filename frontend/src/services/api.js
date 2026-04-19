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
    // NEW: Add category to existing book
    addCategory: (bookId, categoryId) => api.post(`/books/${bookId}/categories/${categoryId}`),
    // NEW: Get categories for a book
    getCategories: (bookId) => api.get(`/books/${bookId}/categories`),
    // NEW: Remove category from book
    removeCategory: (bookId, categoryId) => api.delete(`/books/${bookId}/categories/${categoryId}`),
};

// NEW: Category API
export const categoryAPI = {
    getAll: () => api.get('/categories'),
    create: (category) => api.post('/categories', category),
};

export default api;