/**
 * Register.js - Clerk SignUp component that handles new user registration and account creation.
 * Displays the Clerk sign-up form with email verification and password setup for new users.
 */

import React from 'react';
import { SignUp } from '@clerk/clerk-react';
import './Form.css';

export default function Register() {
  return (
    <div className="form-card auth-clerk">
      <SignUp 
        appearance={{
          elements: {
            rootBox: {
              width: '100%'
            },
            card: {
              boxShadow: 'none',
              backgroundColor: 'transparent'
            }
          }
        }}
      />
    </div>
  );
}
