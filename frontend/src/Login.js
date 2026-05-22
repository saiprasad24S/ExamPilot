/**
 * Login.js - Clerk SignIn component that handles user authentication and login functionality.
 * Displays the Clerk sign-in form with email and password fields for existing users.
 */

import React from 'react';
import { SignIn } from '@clerk/clerk-react';
import './Form.css';

export default function Login() {
  return (
    <div className="form-card auth-clerk">
      <SignIn 
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
