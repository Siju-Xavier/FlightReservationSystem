import React, { useState } from 'react';
import '../index.css';

function LoginModal({ onClose, onLoginSuccess }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Send credentials to our new Java Javalin endpoint!
    fetch('http://localhost:8080/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    })
    .then(res => {
      if (!res.ok) throw new Error('Invalid credentials');
      return res.json();
    })
    .then(data => {
      // Login successful!
      onLoginSuccess(data);
    })
    .catch(err => {
      setError(err.message);
      setLoading(false);
    });
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content glass login-modal">
        <button className="close-btn" onClick={onClose}>X</button>
        <h2>Welcome Back</h2>
        <p className="route-text">Sign in to Canada Airways</p>

        {error && <div className="error-box">{error}</div>}

        <form onSubmit={handleSubmit} className="login-form">
          <input 
            type="text" 
            placeholder="Username (e.g., testuser)" 
            value={username}
            onChange={e => setUsername(e.target.value)}
            required
          />
          <input 
            type="password" 
            placeholder="Password (e.g., password123)" 
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
          <button type="submit" className="primary-btn checkout-btn" disabled={loading}>
            {loading ? 'Authenticating...' : 'Sign In'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default LoginModal;
