import React, { useState, useEffect } from "react";
import { BrowserRouter, Routes, Route, Navigate, useNavigate } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import RegisterForm from "./pages/RegisterForm";
import Navbar from "./components/navbar";
import { ThemeProvider, createTheme, CssBaseline } from "@mui/material";
import Profile from "./pages/profile";
import CreateOffer from "./pages/CreateOffer";
import Offers from "./pages/Offers";
import OfferDetail from "./pages/OfferDetail";
import EditOffer from "./pages/EditOffer";
import CandidateApplications from "./pages/CandidateApplications";
import RecruiterApplications from "./pages/RecruiterApplications";

function App() {
  const [authenticated, setAuthenticated] = useState(!!localStorage.getItem("token"));
  const [userRole, setUserRole] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      const user = JSON.parse(localStorage.getItem("user"));
      if (user && user.roles) {
        // Handle both array of roles and single role object
        const roles = Array.isArray(user.roles) 
          ? user.roles 
          : [user.roles];
        
        // Extract role names
        const roleNames = roles.map(role => 
          typeof role === 'string' ? role : (role.name || '')
        );
        
        // Set the first role or default to 'CANDIDAT' for backward compatibility
        setUserRole(roleNames[0] || 'ROLE_CANDIDAT');
      }
    }
  }, [authenticated]);

  const handleLoginSuccess = () => {
    setAuthenticated(true);
    navigate("/");
  };

  const theme = createTheme({
    palette: {
      mode: 'light',
      primary: { main: '#1976d2' },
      secondary: { main: '#9c27b0' },
      background: { default: '#f4f6f8', paper: '#ffffff' }
    },
    shape: { borderRadius: 10 },
    typography: {
      fontFamily: 'Inter, Segoe UI, Roboto, Helvetica, Arial, sans-serif',
      h4: { fontWeight: 700 },
      button: { textTransform: 'none', fontWeight: 600 }
    },
    components: {
      MuiButton: { styleOverrides: { root: { borderRadius: 8 } } },
      MuiCard: { styleOverrides: { root: { borderRadius: 12 } } },
    }
  });

  // Protected route component
  const ProtectedRoute = ({ children, requiredRole }) => {
    if (!authenticated) {
      return <Navigate to="/login" />;
    }
    
    // If a specific role is required and user doesn't have it
    if (requiredRole && userRole !== requiredRole) {
      return <Navigate to="/" />;
    }
    
    return children;
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Routes>
        <Route path="/login" element={<Login onLogin={handleLoginSuccess} />} />
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <Home />
            </ProtectedRoute>
          }
        />
        <Route 
          path="/offers" 
          element={
            <ProtectedRoute>
              <Offers />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/offers/:id" 
          element={
            <ProtectedRoute>
              <OfferDetail />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/offers/:id/edit" 
          element={
            <ProtectedRoute requiredRole="ROLE_RECRUITER">
              <EditOffer />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/offers/create" 
          element={
            <ProtectedRoute requiredRole="ROLE_RECRUITER">
              <CreateOffer />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/register" 
          element={
            <ProtectedRoute>
              <RegisterForm />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/profile" 
          element={
            <ProtectedRoute>
              <Profile />
            </ProtectedRoute>
          } 
        />
        {/* Role-based application routes */}
        <Route 
          path="/applications" 
          element={
            <ProtectedRoute>
              {userRole === 'ROLE_RECRUITER' ? 
                <RecruiterApplications /> : 
                <CandidateApplications />}
            </ProtectedRoute>
          } 
        />
      </Routes>
    </ThemeProvider>
  );
}

// A wrapper component is needed to use the useNavigate hook
const AppWrapper = () => (
  <BrowserRouter>
    <Navbar />
    <App />
  </BrowserRouter>
);

export default AppWrapper;