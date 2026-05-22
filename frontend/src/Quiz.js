import React, { useState, useEffect } from 'react';
import './Quiz.css';

export default function Quiz({ userId, onQuizComplete }) {
  const [questions, setQuestions] = useState([]);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [answers, setAnswers] = useState({});
  const [timeLeft, setTimeLeft] = useState(120);
  const [submitted, setSubmitted] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/questions');
        const data = await response.json();
        setQuestions(data.questions || []);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching questions:', error);
        setLoading(false);
      }
    };
    fetchQuestions();
  }, []);

  useEffect(() => {
    if (timeLeft <= 0) {
      handleSubmit();
      return;
    }

    const timer = setTimeout(() => setTimeLeft(timeLeft - 1), 1000);
    return () => clearTimeout(timer);
  }, [timeLeft]);

  const handleAnswerSelect = (questionId, optionIndex) => {
    setAnswers({
      ...answers,
      [questionId]: optionIndex
    });
  };

  const handleNext = () => {
    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1);
    }
  };

  const handlePrevious = () => {
    if (currentQuestion > 0) {
      setCurrentQuestion(currentQuestion - 1);
    }
  };

  const handleSubmit = async () => {
    setSubmitted(true);
    try {
      const response = await fetch('http://localhost:8080/api/quiz/submit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          userId,
          answers: Object.values(answers)
        })
      });
      const data = await response.json();
      onQuizComplete(data);
    } catch (error) {
      console.error('Error submitting quiz:', error);
    }
  };

  if (loading) return <div className="quiz-container"><p>Loading questions...</p></div>;
  if (questions.length === 0) return <div className="quiz-container"><p>No questions available</p></div>;

  const question = questions[currentQuestion];
  const answered = answers.hasOwnProperty(question.id);

  return (
    <div className="quiz-container">
      <div className="quiz-header">
        <h2>Quiz</h2>
        <div className={`timer ${timeLeft < 30 ? 'warning' : ''}`}>
          Time: {Math.floor(timeLeft / 60)}:{String(timeLeft % 60).padStart(2, '0')}
        </div>
      </div>

      <div className="progress-bar">
        <div
          className="progress-fill"
          style={{ width: `${((currentQuestion + 1) / questions.length) * 100}%` }}
        ></div>
      </div>

      <div className="question-card">
        <h3>
          Question {currentQuestion + 1}/{questions.length}
        </h3>
        <p>{question.question}</p>

        <div className="options">
          {question.options.map((option, index) => (
            <button
              key={index}
              className={`option ${answers[question.id] === index ? 'selected' : ''}`}
              onClick={() => handleAnswerSelect(question.id, index)}
            >
              {option}
            </button>
          ))}
        </div>

        <div className="navigation">
          <button onClick={handlePrevious} disabled={currentQuestion === 0}>
            Previous
          </button>
          {currentQuestion === questions.length - 1 ? (
            <button onClick={handleSubmit} className="submit" disabled={submitted}>
              {submitted ? 'Submitting...' : 'Submit Quiz'}
            </button>
          ) : (
            <button onClick={handleNext}>Next</button>
          )}
        </div>
      </div>
    </div>
  );
}
