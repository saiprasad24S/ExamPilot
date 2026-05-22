# ExamPilot - Complete Quiz Application

A full-stack quiz application built with minimal dependencies: **Core Java backend** (no Maven/Spring) and **React frontend** with modern styling.

## 🚀 Quick Start

### Prerequisites
- Java JDK 11+
- Node.js & npm

### 1. Start Backend Server
```bash
cd backend
javac -d out *.java
java -cp out SimpleServer
```
Server will run on `http://localhost:8080`

### 2. Start Frontend
```bash
cd frontend
npm install  # First time only
npm start
```
App will open at `http://localhost:3000`

### 3. Test Login
Use default admin credentials:
- **Username**: `admin`
- **Password**: `admin`

---

## 📁 Project Organization

See **PROJECT_STRUCTURE.md** for detailed folder layout.

```
backend/               # 9 custom Java classes, no external dependencies
├── Models:           User.java, Question.java, Result.java
├── Services:         UserService.java, QuestionService.java, QuizService.java
├── Database:         InMemoryDatabase.java (singleton, no DB needed)
├── HTTP Server:      SimpleServer.java (JDK HttpServer)
└── Test Interface:   MainApp.java (console-based testing)

frontend/             # React application
├── Components:       Login, Register, Quiz, Result, Admin
├── Styling:          Global (index.css), Layout (App.css), Forms (Form.css), Quiz (Quiz.css)
├── API Client:       api.js (centralized backend calls)
└── State:            App.js (routing, authentication, results)
```

---

## 🏗️ Architecture

### Backend (Java)
- **Protocol**: HTTP REST API (JSON)
- **Server**: JDK HttpServer (no Servlet container)
- **Database**: In-memory with JSON serialization (no external DB)
- **Dependencies**: Zero external libraries (pure Java 11+)

### Frontend (React)
- **Framework**: React 18+ with Hooks
- **Styling**: CSS3 with animations, gradients, responsive design
- **HTTP**: Fetch API for backend communication
- **State**: React hooks (useState) for component state

### Communication
```
React App (localhost:3000)
        ↓ (HTTP JSON)
    API Requests
        ↓
Java HTTP Server (localhost:8080)
        ↓
InMemoryDatabase
```

---

## 📚 Documentation

### For Users
- **Getting Started**: Follow "Quick Start" above
- **Using the App**: Create account → Login → Take Quiz → View Results

### For Developers
1. **[PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md)** - Folder organization & file purposes
2. **[DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md)** - Deep dives, workflows, debugging
3. **[backend/BACKEND.md](backend/BACKEND.md)** - API endpoint details
4. **Code Comments** - Every class, method, and complex logic is humanized with comments

---

## ✨ Features

### User Features
- ✅ Register new account with password validation
- ✅ Login with credentials
- ✅ Take timed quiz (120-second countdown)
- ✅ View score & detailed answer review
- ✅ See correct vs. user's answers

### Admin Features
- ✅ Add single questions via form
- ✅ Bulk upload questions from JSON file
- ✅ View all questions

### Technical Highlights
- ✅ No external dependencies (backend is pure Java)
- ✅ No database setup needed (in-memory storage)
- ✅ CORS-enabled for cross-origin requests
- ✅ Comprehensive code documentation
- ✅ Modern, responsive UI
- ✅ Interactive timer with color warnings

---

## 🔍 Code Quality

**Humanized Code**: Every file includes:
- File-level documentation explaining purpose
- Class-level javadoc/comments
- Method documentation with parameters
- Inline comments explaining logic
- Section headers for code organization

### Example (User.java):
```java
/**
 * User Model - Represents a quiz application user
 * Fields: id, username, password
 * Plain text passwords (demo only - NOT secure)
 */
public class User {
    private String id;       // Unique user identifier
    private String username; // User's login name
    private String password; // User's password
    
    // Constructor with documentation
    // Getters and setters with javadoc
}
```

---

## 🛠️ Backend Components

### HTTP Endpoints
```
POST   /api/register      - Register new user
POST   /api/login         - Authenticate user
GET    /api/questions     - Fetch quiz questions
POST   /api/admin/add     - Add single question
POST   /api/admin/upload  - Bulk upload from JSON
POST   /api/quiz/submit   - Submit quiz result
```

### Data Models
```javascript
User: { id, username, password }
Question: { id, text, options[], correctIndex }
Result: { id, userId, total, correct, percentage, takenAt }
```

### Service Layer
- **UserService**: Registration & authentication
- **QuestionService**: Question storage & retrieval
- **QuizService**: Quiz execution & scoring

---

## 🎨 Frontend Components

### Pages
1. **Login** - User authentication
2. **Register** - New account creation
3. **Quiz** - Interactive quiz with timer
4. **Result** - Score display & answer review
5. **Admin** - Add/manage questions (logged-in users only)

### Styling Approach
- **Color Scheme**: Purple gradient (#667eea → #764ba2)
- **Animations**: Slide-up, fade-in, bounce effects
- **Responsive**: Mobile-friendly design
- **Modern**: Glass morphism, gradient buttons

---

## 🧪 Testing

### Via Web UI
1. Register new account
2. Login with credentials
3. Answer 5 questions in Quiz
4. Review results with detailed answers

### Via Console Interface
```bash
cd backend
java -cp out MainApp
# Choose: 1) Register, 2) Login & Quiz, 3) Admin, 4) Exit
```

### Via Command Line (curl)
```bash
# Test registration
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123"}'

# Test login
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

---

## 🔒 Security Notes

⚠️ **This is a demo/educational project.** For production:
- ✅ Hash passwords with bcrypt
- ✅ Implement JWT token authentication
- ✅ Add input validation & sanitization
- ✅ Use HTTPS instead of HTTP
- ✅ Implement proper database with encryption
- ✅ Add rate limiting & CORS restrictions

---

## 📋 File Organization Standards

### Java Files
- **Naming**: PascalCase (User.java, SimpleServer.java)
- **Structure**: Package → Imports → Javadoc → Class → Methods
- **Comments**: File-level, class-level, method-level

### React Files
- **Naming**: PascalCase components (Login.js), camelCase utilities (api.js)
- **Structure**: Comments → Imports → Component → Exports
- **Organization**: State first, then handlers, then render

### CSS Files
- **Naming**: kebab-case selectors (.quiz-container)
- **Organization**: Section headers (/* ========== Section ========== */)
- **Comments**: Explain purpose, color meanings, responsive breakpoints

---

## 🚀 Next Steps

### Enhancement Ideas
1. **Database Migration**: Replace InMemoryDatabase with PostgreSQL/MongoDB
2. **Authentication**: Add JWT tokens & refresh tokens
3. **Admin Features**: User management, statistics, question categories
4. **User Features**: Quiz history, performance tracking, difficulty levels
5. **Testing**: Add unit tests (JUnit), integration tests, e2e tests
6. **Deployment**: Docker containerization, production hosting

### Performance Improvements
1. Add caching for frequently accessed questions
2. Optimize API response sizes
3. Lazy load components in React
4. Compress and minify production builds

---

## 📞 Support & Documentation

- **Architecture Details**: See DEVELOPER_GUIDE.md
- **Project Structure**: See PROJECT_STRUCTURE.md
- **API Documentation**: See backend/BACKEND.md
- **Code Comments**: Every file has comprehensive inline documentation

---

## 📄 License

Educational project. Feel free to use and modify for learning purposes.

---

## Summary

**ExamPilot** demonstrates full-stack development with:
- ✅ Minimal dependencies (pure Java, plain HTML/CSS/JavaScript)
- ✅ Clean architecture (models → services → API)
- ✅ Modern UI (React, responsive CSS)
- ✅ Comprehensive documentation (every line explained)
- ✅ Production-ready patterns (singletons, services, API design)

Perfect for learning or as a starting point for a quiz/assessment platform.

