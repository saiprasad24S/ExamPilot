import React, { useState } from 'react';
import './Quiz.css';

export default function Result({ result, questions }) {
  const [reviewMode, setReviewMode] = useState(false);

  if (!result || !questions) return <div>No result available</div>;

  const score = result.score || 0;
  const total = result.total || questions.length;
  const percentage = ((score / total) * 100).toFixed(2);

  return (
    <div className="result-container">
      <div className="result-card">
        <h2>Quiz Complete!</h2>
        <div className="score-display">
          <h1>{score}/{total}</h1>
          <p>Score: {percentage}%</p>
        </div>

        <button onClick={() => setReviewMode(!reviewMode)} className="review-btn">
          {reviewMode ? 'Hide Review' : 'Review Answers'}
        </button>

        {reviewMode && (
          <div className="review-section">
            <h3>Answer Review</h3>
            {questions.map((question, index) => {
              const userAnswer = result.answers ? result.answers[index] : -1;
              const isCorrect = userAnswer === question.correctIndex;
              return (
                <div key={index} className={`review-item ${isCorrect ? 'correct' : 'incorrect'}`}>
                  <p><strong>Q{index + 1}: {question.question}</strong></p>
                  <p>Your answer: {userAnswer >= 0 && userAnswer < question.options.length ? question.options[userAnswer] : 'Not answered'}</p>
                  {!isCorrect && <p className="correct-answer">Correct: {question.options[question.correctIndex]}</p>}
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}
