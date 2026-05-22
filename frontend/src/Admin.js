/**
 * Admin.js - Admin panel for managing quiz questions with options to add individual questions or bulk upload.
 * Includes fields for question text, multiple choice options, correct answer selection, and timer configuration.
 */

import React, { useState } from 'react';
import './Form.css';

export default function Admin() {
  const [adminMode, setAdminMode] = useState('add');
  const [question, setQuestion] = useState('');
  const [options, setOptions] = useState(['', '', '', '']);
  const [correctIndex, setCorrectIndex] = useState(0);
  const [questionTimer, setQuestionTimer] = useState(30);
  const [jsonInput, setJsonInput] = useState('');
  const [message, setMessage] = useState('');

  const handleAddQuestion = async (e) => {
    e.preventDefault();
    const validOptions = options.filter(opt => opt.trim());

    if (!question || validOptions.length < 2) {
      setMessage('Question and at least 2 options required');
      return;
    }

    if (questionTimer <= 0) {
      setMessage('Timer must be greater than 0 seconds');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/admin/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          text: question,
          options: validOptions,
          correctIndex: Math.min(correctIndex, validOptions.length - 1),
          timer: questionTimer
        })
      });
      const data = await response.json();
      setMessage(data.message || 'Question added successfully');
      setQuestion('');
      setOptions(['', '', '', '']);
      setCorrectIndex(0);
      setQuestionTimer(30);
    } catch (error) {
      setMessage('Error adding question');
    }
  };

  const handleBulkUpload = async (e) => {
    e.preventDefault();
    try {
      const questions = JSON.parse(jsonInput);
      const response = await fetch('http://localhost:8080/api/admin/upload', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(questions)
      });
      const data = await response.json();
      setMessage(data.message || 'Questions uploaded');
      setJsonInput('');
    } catch (error) {
      setMessage('Invalid JSON format');
    }
  };

  return (
    <div className="admin-container">
      <h2>Admin Panel</h2>
      <div className="tab-buttons">
        <button
          className={adminMode === 'add' ? 'active' : ''}
          onClick={() => setAdminMode('add')}
        >
          Add Question
        </button>
        <button
          className={adminMode === 'bulk' ? 'active' : ''}
          onClick={() => setAdminMode('bulk')}
        >
          Bulk Upload
        </button>
      </div>

      {adminMode === 'add' && (
        <form onSubmit={handleAddQuestion} className="form-card">
          <input
            type="text"
            placeholder="Question"
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            required
          />
          {options.map((opt, idx) => (
            <input
              key={idx}
              type="text"
              placeholder={`Option ${idx + 1}`}
              value={opt}
              onChange={(e) => {
                const newOptions = [...options];
                newOptions[idx] = e.target.value;
                setOptions(newOptions);
              }}
            />
          ))}
          <select value={correctIndex} onChange={(e) => setCorrectIndex(parseInt(e.target.value))}>
            {options.map((_, idx) => (
              <option key={idx} value={idx}>
                Correct Answer: Option {idx + 1}
              </option>
            ))}
          </select>
          <div className="timer-input-group">
            <label htmlFor="timer">Timer per question (seconds):</label>
            <input
              id="timer"
              type="number"
              min="5"
              max="300"
              value={questionTimer}
              onChange={(e) => setQuestionTimer(parseInt(e.target.value) || 30)}
              placeholder="Seconds"
            />
          </div>
          <button type="submit">Add Question</button>
        </form>
      )}

      {adminMode === 'bulk' && (
        <form onSubmit={handleBulkUpload} className="form-card">
          <textarea
            placeholder="Paste JSON array of questions with timer field: {text, options[], correctIndex, timer}"
            value={jsonInput}
            onChange={(e) => setJsonInput(e.target.value)}
            rows="10"
          ></textarea>
          <button type="submit">Upload Questions</button>
        </form>
      )}

      {message && <p className="message">{message}</p>}
    </div>
  );
}
