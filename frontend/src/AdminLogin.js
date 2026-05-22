/**
 * AdminLogin.js - Admin panel login component with username and password authentication.
 * Separate from Clerk authentication, allowing admins to access the admin panel independently.
 */

import React, { useState } from 'react';
import './Form.css';

export default function AdminLogin({ onLoginSuccess }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // Simple authentication check
    if (username === 'admin' && password === '2724') {
      onLoginSuccess(username);
    } else {
      setError('Invalid admin credentials');
    }
    setLoading(false);
  };

  return (
    <div className="form-card">
      <h2>Admin Login</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Logging in...' : 'Login as Admin'}
        </button>
      </form>
      {error && <p className="error-message">{error}</p>}
    </div>
  );
}
