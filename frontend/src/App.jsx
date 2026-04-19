import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { authorAPI, bookAPI, categoryAPI } from './services/api';

function App() {
  const [authors, setAuthors] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [useMethod, setUseMethod] = useState('jpa');
  const [selectedAuthor, setSelectedAuthor] = useState(null);
  const [viewMode, setViewMode] = useState('authors');
  const [selectedBookForCategory, setSelectedBookForCategory] = useState(null);
  const [selectedCategoryId, setSelectedCategoryId] = useState('');
  const [refreshKey, setRefreshKey] = useState(0); // Force re-render

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
      console.log('Loaded authors:', response.data);
      setAuthors(response.data);

      // If we have a selected author, update it with fresh data
      if (selectedAuthor) {
        const freshAuthor = response.data.find(a => a.id === selectedAuthor.id);
        if (freshAuthor) {
          setSelectedAuthor(freshAuthor);
        }
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadCategories = async () => {
    try {
      const response = await categoryAPI.getAll();
      setCategories(response.data);
    } catch (error) {
      console.error('Error loading categories:', error);
    }
  };

  useEffect(() => {
    loadAuthors();
    loadCategories();
  }, [refreshKey]); // Re-run when refreshKey changes

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
      await loadAuthors();
      await loadCategories();
      setViewMode('authors');
      setSelectedAuthor(null);
      setRefreshKey(prev => prev + 1); // Force refresh
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
    setSelectedBookForCategory(null); // Reset category selection
    setSelectedCategoryId('');
  };

  const handleAddCategory = async (bookId) => {
    if (!selectedCategoryId) {
      alert('Please select a category');
      return;
    }

    console.log('Adding category:', { bookId, selectedCategoryId });

    try {
      await bookAPI.addCategory(bookId, selectedCategoryId);
      alert('Category added to book successfully!');

      // Reload all data
      await loadAuthors();
      await loadCategories();

      // Force a re-render
      setRefreshKey(prev => prev + 1);

      // Reset form
      setSelectedBookForCategory(null);
      setSelectedCategoryId('');

    } catch (error) {
      console.error('Error adding category:', error);
      alert('Error adding category to book: ' + (error.response?.data || error.message));
    }
  };

  return (
      <div className="container mt-4">
        <h1 className="text-center mb-4">Library Management System</h1>

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
                      <option value="jpa">JPA/Hibernate</option>
                      <option value="mybatis">MyBatis</option>
                    </select>
                  </div>
                  <button type="submit" className="btn btn-success">Create Book</button>
                </form>
              </div>
            </div>
        )}

        {/* Authors Section - One-to-Many Relationship */}
        {viewMode === 'authors' ? (
            <>
              <h2 className="mb-3">📖 Authors and Their Books (One-to-Many)</h2>
              <div className="row">
                {loading ? <div>Loading...</div> : authors.map(author => (
                    <div key={author.id} className="col-md-6 mb-3">
                      <div className="card" style={{cursor: 'pointer'}} onClick={() => handleAuthorClick(author)}>
                        <div className="card-body">
                          <h3>{author.name}</h3>
                          <p>Country: {author.country || 'N/A'}</p>
                          <p>Birth Date: {author.birthDate || 'N/A'}</p>
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
              <button className="btn btn-secondary mb-3" onClick={() => {
                setViewMode('authors');
                setSelectedAuthor(null);
                setSelectedBookForCategory(null);
              }}>
                ← Back to Authors
              </button>
              {selectedAuthor && (
                  <div className="card mb-4">
                    <div className="card-header bg-success text-white">
                      <h3>Books by {selectedAuthor.name}</h3>
                    </div>
                    <div className="card-body">
                      {/* Books display for selected author */}
                      {selectedAuthor.books?.length > 0 ? (
                          selectedAuthor.books.map(book => (
                              <div key={`book-${book.id}`} className="card mb-3">  {/* Use unique key with prefix */}
                                <div className="card-body">
                                  <h4>{book.title}</h4>
                                  <p>ISBN: {book.isbn || 'N/A'} | Year: {book.publicationYear || 'N/A'}</p>

                                  {/* Display categories - this is fine */}
                                  <div className="mb-2">
                                    <strong>Categories:</strong>
                                    {book.categoryNames && book.categoryNames.length > 0 ? (
                                        <div className="mt-1">
                                          {book.categoryNames.map((catName, idx) => (
                                              <span key={`${book.id}-cat-${idx}`} className="badge bg-secondary me-1">
                                    {catName}
                                </span>
                                          ))}
                                        </div>
                                    ) : (
                                        <span className="text-muted ms-2"> No categories yet</span>
                                    )}
                                  </div>

                                  {/* Add category button - keep as is */}
                                  {selectedBookForCategory === book.id ? (
                                      <div className="mt-2">
                                        <select
                                            className="form-select mb-2"
                                            value={selectedCategoryId}
                                            onChange={(e) => setSelectedCategoryId(e.target.value)}
                                        >
                                          <option value="">Select a category...</option>
                                          {categories.map(cat => (
                                              <option key={cat.id} value={cat.id}>
                                                {cat.name}
                                              </option>
                                          ))}
                                        </select>
                                        <button
                                            className="btn btn-sm btn-success me-2"
                                            onClick={() => handleAddCategory(book.id)}
                                        >
                                          Confirm Add
                                        </button>
                                        <button
                                            className="btn btn-sm btn-secondary"
                                            onClick={() => {
                                              setSelectedBookForCategory(null);
                                              setSelectedCategoryId('');
                                            }}
                                        >
                                          Cancel
                                        </button>
                                      </div>
                                  ) : (
                                      <button
                                          className="btn btn-sm btn-primary"
                                          onClick={() => setSelectedBookForCategory(book.id)}
                                      >
                                        + Add Category to this Book
                                      </button>
                                  )}
                                </div>
                              </div>
                          ))
                      ) : (
                          <p>No books yet for this author.</p>
                      )}
                    </div>
                  </div>
              )}
            </>
        )}

        {/* Categories Section - Many-to-Many Relationship */}
        <div className="mt-5">
          <hr className="my-4" />
          <h2 className="mb-3">Categories and Their Books</h2>
          <div className="row">
            {categories.length === 0 ? (
                <div className="alert alert-info">No categories found. Add categories to see the many-to-many relationship.</div>
            ) : (
                categories.map(category => (
                    <div key={category.id} className="col-md-6 mb-3">
                      <div className="card h-100">
                        <div className="card-header bg-success text-white">
                          <h4 className="mb-0">{category.description}</h4>
                          {category.name && <small>{category.name}</small>}
                        </div>
                        <div className="card-body">
                          <h5>Books in this category:</h5>
                          {category.books && category.books.length > 0 ? (
                              <ul className="list-group">
                                {category.books.map((book, index) => (
                                    <li key={index} className="list-group-item">
                                      <strong>{book.title}</strong>
                                      {book.authorName && (
                                          <span className="text-muted ms-2">by {book.authorName}</span>
                                      )}
                                      {book.publicationYear && (
                                          <span className="text-muted ms-2">({book.publicationYear})</span>
                                      )}
                                    </li>
                                ))}
                              </ul>
                          ) : (
                              <p className="text-muted">No books in this category yet.</p>
                          )}
                        </div>
                      </div>
                    </div>
                ))
            )}
          </div>
        </div>
      </div>
  );
}

export default App;