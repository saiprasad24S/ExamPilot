/**
 * App.js - Main application component that manages authentication, routing, and lazy loading of all pages.
 * Integrates Clerk authentication for secure user login/signup and uses React.lazy for code splitting.
 * Handles the main app state including quiz results, questions, and page navigation.
 */

import React, { useState, useEffect, Suspense, lazy } from 'react';
import { useUser, useAuth, SignedIn, SignedOut, UserButton } from '@clerk/clerk-react';
import './App.css';

// Lazy load components
const Login = lazy(() => import('./Login'));
const Register = lazy(() => import('./Register'));
const Quiz = lazy(() => import('./Quiz'));
const Result = lazy(() => import('./Result'));
const Admin = lazy(() => import('./Admin'));
const AdminLogin = lazy(() => import('./AdminLogin'));

// Loading spinner component
const LoadingSpinner = () => (
  <div className="loading-spinner">
    <div className="spinner"></div>
    <p>Loading...</p>
  </div>
);

export default function App() {
  const { user, isLoaded } = useUser();
  const { isSignedIn } = useAuth();
  const [currentPage, setCurrentPage] = useState('login');
  const [userId, setUserId] = useState(null);
  const [userEmail, setUserEmail] = useState('');
  const [username, setUsername] = useState('');
  const [quizResult, setQuizResult] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [isAdminLogged, setIsAdminLogged] = useState(false);
  const [authMode, setAuthMode] = useState(null); // 'admin' or 'user'

  // Update userId, userEmail and username when Clerk user changes
  useEffect(() => {
    if (isLoaded && user) {
      setUserId(user.id);
      setUserEmail(user.emailAddresses[0]?.emailAddress || 'user@example.com');
      setUsername(user.firstName || user.emailAddresses[0]?.emailAddress || 'User');
    }
  }, [user, isLoaded]);

  // Fetch questions on mount
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

  const handleQuizComplete = (result) => {
    setQuizResult(result);
    setCurrentPage('result');
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleAdminLogin = (adminUsername) => {
    setIsAdminLogged(true);
    setUsername(adminUsername);
    setCurrentPage('admin');
  };

  const handleAdminLogout = () => {
    setIsAdminLogged(false);
    setAuthMode(null);
    setCurrentPage('login');
  };

  if (!isLoaded) {
    return <LoadingSpinner />;
  }

  // Admin logged in view
  if (isAdminLogged) {
    return (
      <div className="app">
        <header className="header">
          <h1>ExamPilot - Admin Panel</h1>
          <div className="user-info">
            <span>Admin: {username}</span>
            <button onClick={handleAdminLogout} className="logout-btn">
              Logout
            </button>
          </div>
        </header>

        <main className="main-content">
          <Suspense fallback={<LoadingSpinner />}>
            {currentPage === 'admin' && <Admin />}
          </Suspense>
        </main>
      </div>
    );
  }

  // User/Clerk authenticated view
  if (isSignedIn && isLoaded) {
    return (
      <div className="app">
        <header className="header">
          <h1>ExamPilot - Quiz System</h1>
          <div className="user-info">
            <span>Welcome, {username}</span>
            <UserButton afterSignOutUrl="/" />
          </div>
        </header>

        <nav className="navigation">
          <button
            className={currentPage === 'quiz' ? 'active' : ''}
            onClick={() => handlePageChange('quiz')}
          >
            Take Quiz
          </button>
        </nav>

        <main className="main-content">
          <Suspense fallback={<LoadingSpinner />}>
            {currentPage === 'quiz' && userId && (
              <Quiz userId={userId} userEmail={userEmail} onQuizComplete={handleQuizComplete} />
            )}

            {currentPage === 'result' && quizResult && (
              <Result result={quizResult} questions={questions} />
            )}
          </Suspense>
        </main>
      </div>
    );
  }

  // Initial login choice screen
  return (
    <div className="app">
      <header className="header">
        <h1>ExamPilot - Quiz System</h1>
      </header>
      <main className="main-content auth-container">
        <div className="auth-choice">
          <h2>Welcome to ExamPilot</h2>
          <p>Please select your login type:</p>
          <div className="choice-buttons">
            <button
              className="choice-btn admin-btn"
              onClick={() => setAuthMode('admin')}
            >
              Admin Login
            </button>
            <button
              className="choice-btn user-btn"
              onClick={() => setAuthMode('user')}
            >
              User Login (Quiz)
            </button>
          </div>
        </div>

        {authMode === 'admin' && (
          <Suspense fallback={<LoadingSpinner />}>
            <AdminLogin onLoginSuccess={handleAdminLogin} />
          </Suspense>
        )}

        {authMode === 'user' && (
          <Suspense fallback={<LoadingSpinner />}>
            {currentPage === 'login' ? (
              <div>
                <Login />
                <p className="toggle-text">
                  Don't have an account?{' '}
                  <button
                    onClick={() => setCurrentPage('register')}
                    className="link-btn"
                  >
                    Sign Up
                  </button>
                </p>
              </div>
            ) : (
              <div>
                <Register />
                <p className="toggle-text">
                  Already have an account?{' '}
                  <button
                    onClick={() => setCurrentPage('login')}
                    className="link-btn"
                  >
                    Sign In
                  </button>
                </p>
              </div>
            )}
          </Suspense>
        )}
      </main>
    </div>
  );
}
