import React, { useState, useEffect } from 'react';
import './App.css';
import Login from './Login';
import Register from './Register';
import Quiz from './Quiz';
import Result from './Result';
import Admin from './Admin';

export default function App() {
  const [currentPage, setCurrentPage] = useState('login');
  const [userId, setUserId] = useState(null);
  const [username, setUsername] = useState('');
  const [quizResult, setQuizResult] = useState(null);
  const [questions, setQuestions] = useState([]);

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/questions');
        const data = await response.json();
        setQuestions(data.questions || []);
      } catch (error) {
        console.error('Error fetching questions:', error);
      }
    };
    fetchQuestions();
  }, []);

  const handleLoginSuccess = (id, name) => {
    setUserId(id);
    setUsername(name);
    setCurrentPage('quiz');
  };

  const handleRegisterSuccess = (id, name) => {
    setUserId(id);
    setUsername(name);
    setCurrentPage('quiz');
  };

  const handleQuizComplete = (result) => {
    setQuizResult(result);
    setCurrentPage('result');
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
    if (page === 'login') {
      setUserId(null);
      setUsername('');
      setQuizResult(null);
    }
  };

  return (
    <div className="app">
      <header className="header">
        <h1>ExamPilot</h1>
        {userId && (
          <div className="user-info">
            <span>Welcome, {username}</span>
            <button onClick={() => handlePageChange('login')} className="logout-btn">
              Logout
            </button>
          </div>
        )}
      </header>

      <nav className="navigation">
        {userId && (
          <>
            <button
              className={currentPage === 'quiz' ? 'active' : ''}
              onClick={() => handlePageChange('quiz')}
            >
              Quiz
            </button>
            <button
              className={currentPage === 'admin' ? 'active' : ''}
              onClick={() => handlePageChange('admin')}
            >
              Admin
            </button>
          </>
        )}
      </nav>

      <main className="main-content">
        {currentPage === 'login' && (
          <div>
            <Login onLoginSuccess={handleLoginSuccess} />
            <p className="toggle-text">
              Don't have an account?{' '}
              <button onClick={() => setCurrentPage('register')} className="link-btn">
                Register
              </button>
            </p>
          </div>
        )}

        {currentPage === 'register' && (
          <div>
            <Register onRegisterSuccess={handleRegisterSuccess} />
            <p className="toggle-text">
              Already have an account?{' '}
              <button onClick={() => setCurrentPage('login')} className="link-btn">
                Login
              </button>
            </p>
          </div>
        )}

        {currentPage === 'quiz' && userId && (
          <Quiz userId={userId} onQuizComplete={handleQuizComplete} />
        )}

        {currentPage === 'result' && quizResult && (
          <Result result={quizResult} questions={questions} />
        )}

        {currentPage === 'admin' && <Admin />}
      </main>
    </div>
  );
}
