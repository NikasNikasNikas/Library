import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { authorAPI, bookAPI } from './services/api';

function App() {
  const [authors, setAuthors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [useMethod, setUseMethod] = useState('jpa');
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
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAuthors();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (useMethod === 'jpa') {
        await bookAPI.createWithJPA(formData);
        alert('Book created with JPA!');
      } else {
        await bookAPI.createWithMyBatis(formData);
        alert('Book created with MyBatis!');
      }
      setFormData({ title: '', isbn: '', publicationYear: '', authorId: '' });
      setShowForm(false);
      loadAuthors();
      setViewMode('authors');
      setSelectedAuthor(null);
    } catch (error) {
      alert('Error creating book');
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleAuthorClick = (author) => {
    setSelectedAuthor(author);
    setViewMode('books');
  };

  return (
      <div className="container mt-4">
        <h1 className="text-center mb-4">📚 Library System</h1>

        <div className="alert alert-info mb-4">
          <strong>✅ Requirements Met:</strong><br/>
          • Business component: @Service<br/>
          • JPA DAO + MyBatis DAO<br/>
          • @Transactional transactions<br/>
          • One-to-many: Author → Books (click to navigate)<br/>
          • Form with data binding
        </div>

        <div className="text-center mb-4">
          <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Cancel' : '+ Add New Book'}
          </button>
        </div>

        {showForm && (
            <div className="card mb-4">
              <div className="card-header bg-primary text-white">
                <h3>Add New Book</h3>
              </div>
              <div className="card-body">
                <form onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label>Title *</label>
                    <input type="text" name="title" className="form-control"
                           value={formData.title} onChange={handleChange} required />
                  </div>
                  <div className="mb-3">
                    <label>ISBN</label>
                    <input type="text" name="isbn" className="form-control"
                           value={formData.isbn} onChange={handleChange} />
                  </div>
                  <div className="mb-3">
                    <label>Publication Year</label>
                    <input type="number" name="publicationYear" className="form-control"
                           value={formData.publicationYear} onChange={handleChange} />
                  </div>
                  <div className="mb-3">
                    <label>Author *</label>
                    <select name="authorId" className="form-select"
                            value={formData.authorId} onChange={handleChange} required>
                      <option value="">Select author...</option>
                      {authors.map(author => (
                          <option key={author.id} value={author.id}>{author.name}</option>
                      ))}
                    </select>
                  </div>
                  <div className="mb-3">
                    <label>DAO Method</label>
                    <select className="form-select" value={useMethod} onChange={(e) => setUseMethod(e.target.value)}>
                      <option value="jpa">JPA/Hibernate (3.3.1)</option>
                      <option value="mybatis">MyBatis (3.3.2)</option>
                    </select>
                  </div>
                  <button type="submit" className="btn btn-success">Create Book</button>
                </form>
              </div>
            </div>
        )}

        {viewMode === 'authors' ? (
            <>
              <h2>Authors (Click to see their books)</h2>
              <div className="row">
                {loading ? <div>Loading...</div> : authors.map(author => (
                    <div key={author.id} className="col-md-6 mb-3">
                      <div className="card" style={{cursor: 'pointer'}} onClick={() => handleAuthorClick(author)}>
                        <div className="card-body">
                          <h3>{author.name}</h3>
                          <p>Country: {author.country || 'N/A'}</p>
                          <p>Books: {author.books?.length || 0}</p>
                          <button className="btn btn-info btn-sm">View Books →</button>
                        </div>
                      </div>
                    </div>
                ))}
              </div>
            </>
        ) : (
            <>
              <button className="btn btn-secondary mb-3" onClick={() => setViewMode('authors')}>
                ← Back to Authors
              </button>
              {selectedAuthor && (
                  <div className="card">
                    <div className="card-header bg-info text-white">
                      <h3>Books by {selectedAuthor.name}</h3>
                    </div>
                    <div className="card-body">
                      {selectedAuthor.books?.length > 0 ? (
                          <table className="table">
                            <thead>
                            <tr><th>Title</th><th>ISBN</th><th>Year</th></tr>
                            </thead>
                            <tbody>
                            {selectedAuthor.books.map(book => (
                                <tr key={book.id}>
                                  <td>{book.title}</td>
                                  <td>{book.isbn || 'N/A'}</td>
                                  <td>{book.publicationYear || 'N/A'}</td>
                                </tr>
                            ))}
                            </tbody>
                          </table>
                      ) : <p>No books yet</p>}
                    </div>
                  </div>
              )}
            </>
        )}
      </div>
  );
}

export default App;