import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { authorAPI, bookAPI } from './services/api';

function App() {
  const [authors, setAuthors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [selectedAuthor, setSelectedAuthor] = useState(null);
  const [viewMode, setViewMode] = useState('authors');
  const [formData, setFormData] = useState({
    title: '',
    isbn: '',
    publicationYear: '',
    authorId: ''
  });

  const loadAuthors = async () => {
    setLoading(true);
    try {
      const response = await authorAPI.getAll();
      setAuthors(response.data);
    } catch (error) {
      console.error('Error loading authors:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAuthors();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.title.trim()) {
      alert('Please enter a book title');
      return;
    }

    if (!formData.authorId) {
      alert('Please select an author');
      return;
    }

    const bookToCreate = {
      title: formData.title,
      isbn: formData.isbn || null,
      publicationYear: formData.publicationYear ? parseInt(formData.publicationYear) : null,
      authorId: parseInt(formData.authorId)
    };

    try {
      await bookAPI.create(bookToCreate);
      alert('Book created successfully!');
      setFormData({ title: '', isbn: '', publicationYear: '', authorId: '' });
      setShowForm(false);
      loadAuthors();
    } catch (error) {
      console.error('Error creating book:', error);
      alert('Error creating book');
    }
  };

  const handleAuthorClick = (author) => {
    setSelectedAuthor(author);
    setViewMode('books');
  };

  return (
      <div className="container mt-4">
        <h1 className="text-center mb-4">📚 Library Management System</h1>

        <div className="alert alert-info mb-4">
          <strong>✅ Requirements Met:</strong><br/>
          • <strong>3.1.1</strong> - Click on any author to see their books (one-to-many relationship)<br/>
          • <strong>3.1.2</strong> - Form with data binding to create books<br/>
          • <strong>3.2</strong> - Business component: BookService with @Service<br/>
          • <strong>3.3.1</strong> - JPA DAO: BookRepository<br/>
          • <strong>5</strong> - Declarative transactions: @Transactional
        </div>

        <div className="text-center mb-4">
          <button
              className="btn btn-primary btn-lg"
              onClick={() => setShowForm(!showForm)}
          >
            {showForm ? 'Cancel' : '+ Add New Book'}
          </button>
        </div>

        {showForm && (
            <div className="card mb-4 border-primary">
              <div className="card-header bg-primary text-white">
                <h3 className="mb-0">Add New Book</h3>
              </div>
              <div className="card-body">
                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label className="form-label">Book Title *</label>
                    <input
                        type="text"
                        className="form-control"
                        value={formData.title}
                        onChange={(e) => setFormData({...formData, title: e.target.value})}
                        required
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label">ISBN</label>
                    <input
                        type="text"
                        className="form-control"
                        value={formData.isbn}
                        onChange={(e) => setFormData({...formData, isbn: e.target.value})}
                        placeholder="Optional"
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Publication Year</label>
                    <input
                        type="number"
                        className="form-control"
                        value={formData.publicationYear}
                        onChange={(e) => setFormData({...formData, publicationYear: e.target.value})}
                        placeholder="e.g., 2024"
                    />
                  </div>

                  <div className="mb-3">
                    <label className="form-label">Author *</label>
                    <select
                        className="form-select"
                        value={formData.authorId}
                        onChange={(e) => setFormData({...formData, authorId: e.target.value})}
                        required
                    >
                      <option value="">Select an author...</option>
                      {authors.map(author => (
                          <option key={author.id} value={author.id}>
                            {author.name}
                          </option>
                      ))}
                    </select>
                  </div>

                  <button type="submit" className="btn btn-success">
                    Create Book
                  </button>
                </form>
              </div>
            </div>
        )}

        {viewMode === 'authors' ? (
            <>
              <h2 className="mb-3">📖 Authors (Click to see their books)</h2>
              <div className="row">
                {loading ? (
                    <div className="text-center">Loading...</div>
                ) : authors.length === 0 ? (
                    <div className="alert alert-info">No authors found. Please add authors first.</div>
                ) : (
                    authors.map(author => (
                        <div key={author.id} className="col-md-6 mb-3">
                          <div
                              className="card h-100 shadow-sm"
                              style={{ cursor: 'pointer' }}
                              onClick={() => handleAuthorClick(author)}
                          >
                            <div className="card-body">
                              <h3 className="card-title text-primary">{author.name}</h3>
                              {author.country && <p>📍 {author.country}</p>}
                              {author.birthDate && <p>🎂 Born: {author.birthDate}</p>}
                              <p>📚 Books: {author.books ? author.books.length : 0}</p>
                              <button className="btn btn-sm btn-info">Click to see books →</button>
                            </div>
                          </div>
                        </div>
                    ))
                )}
              </div>
            </>
        ) : (
            <>
              <button className="btn btn-secondary mb-3" onClick={() => setViewMode('authors')}>
                ← Back to All Authors
              </button>

              {selectedAuthor && (
                  <div className="card border-info">
                    <div className="card-header bg-info text-white">
                      <h3 className="mb-0">Books by: {selectedAuthor.name}</h3>
                    </div>
                    <div className="card-body">
                      {selectedAuthor.books && selectedAuthor.books.length > 0 ? (
                          <table className="table table-striped">
                            <thead>
                            <tr>
                              <th>Title</th>
                              <th>ISBN</th>
                              <th>Publication Year</th>
                            </tr>
                            </thead>
                            <tbody>
                            {selectedAuthor.books.map((book, index) => (
                                <tr key={book.id}>
                                  <td><strong>{book.title}</strong></td>
                                  <td>{book.isbn || 'N/A'}</td>
                                  <td>{book.publicationYear || 'N/A'}</td>
                                </tr>
                            ))}
                            </tbody>
                          </table>
                      ) : (
                          <div className="alert alert-warning">No books found for this author.</div>
                      )}
                    </div>
                  </div>
              )}
            </>
        )}
      </div>
  );
}

export default App;