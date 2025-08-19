import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../axiosConfig';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  IconButton,
  Avatar,
  Tooltip,
  Menu,
  MenuItem,
  ListItemIcon,
  Divider,
  Box
} from '@mui/material';
import PersonAdd from '@mui/icons-material/PersonAdd';
import WorkOutlineIcon from '@mui/icons-material/WorkOutline';
import Settings from '@mui/icons-material/Settings';
import Logout from '@mui/icons-material/Logout';

export default function Navbar() {
  const token = localStorage.getItem('token');
  const [userRole, setUserRole] = useState('');
  const [username, setUsername] = useState('');
  const [menuAnchor, setMenuAnchor] = useState(null);
  const menuOpen = Boolean(menuAnchor);
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      setUserRole('');
      setUsername('');
      return;
    }
    
    const fetchUserData = async () => {
      try {
        const res = await api.get('/api/user/me');
        const rolesRaw = res.data?.roles || [];
        const roles = Array.isArray(rolesRaw) 
          ? rolesRaw.map((r) => (typeof r === 'string' ? r : r.name || ''))
          : [];
        
        // Set the first role or default to empty string
        setUserRole(roles[0] || '');
        setUsername(res.data?.username || '');
        
        // Store user data in localStorage for easy access
        localStorage.setItem('user', JSON.stringify({
          id: res.data?.id,
          username: res.data?.username,
          roles: roles
        }));
      } catch (error) {
        console.error('Error fetching user data:', error);
        setUserRole('');
        setUsername('');
      }
    };

    fetchUserData();
  }, [token]);

  const handleOpenMenu = (event) => setMenuAnchor(event.currentTarget);
  const handleCloseMenu = () => setMenuAnchor(null);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    handleCloseMenu();
    navigate('/login');
  };

  const getApplicationMenuText = () => {
    return userRole === 'ROLE_RECRUITER' 
      ? 'View Applications' 
      : 'My Applications';
  };

  const avatarLetter = username ? username.charAt(0).toUpperCase() : 'U';
  const isRecruiter = userRole === 'ROLE_RECRUITER';

  return (
    <AppBar position="static" color="default" elevation={1}>
      <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Typography
            variant="h6"
            component={Link}
            to="/"
            sx={{
              textDecoration: 'none',
              color: 'inherit',
              fontWeight: 700,
              '&:hover': { color: 'primary.main' }
            }}
          >
            Job<Typography component="span" color="primary" sx={{ fontWeight: 800 }}>Rec</Typography>
          </Typography>

          {token && (
            <Button 
              component={Link} 
              to="/offers" 
              color="inherit"
              sx={{ textTransform: 'none' }}
            >
              Browse Jobs
            </Button>
          )}
        </Box>

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {token && isRecruiter && (
            <Button 
              component={Link} 
              to="/offers/create" 
              variant="contained" 
              color="primary"
              size="small"
            >
              Create Job Offer
            </Button>
          )}

          {!token ? (
            <>
              <Button 
                component={Link} 
                to="/register" 
                color="inherit"
                size="small"
              >
                Register
              </Button>
              <Button 
                component={Link} 
                to="/login" 
                variant="contained" 
                color="primary"
                size="small"
              >
                Login
              </Button>
            </>
          ) : (
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Tooltip title="Account settings">
                <IconButton
                  onClick={handleOpenMenu}
                  size="small"
                  sx={{ ml: 1 }}
                  aria-controls={menuOpen ? 'account-menu' : undefined}
                  aria-haspopup="true"
                  aria-expanded={menuOpen ? 'true' : undefined}
                >
                  <Avatar sx={{ width: 32, height: 32, bgcolor: 'primary.main' }}>
                    {avatarLetter}
                  </Avatar>
                </IconButton>
              </Tooltip>
              <Menu
                anchorEl={menuAnchor}
                id="account-menu"
                open={menuOpen}
                onClose={handleCloseMenu}
                onClick={handleCloseMenu}
                slotProps={{
                  paper: {
                    elevation: 1,
                    sx: {
                      overflow: 'visible',
                      mt: 1.5,
                      minWidth: 200,
                      '& .MuiAvatar-root': {
                        width: 32,
                        height: 32,
                        ml: -0.5,
                        mr: 1,
                      },
                      '&::before': {
                        content: '""',
                        display: 'block',
                        position: 'absolute',
                        top: 0,
                        right: 14,
                        width: 10,
                        height: 10,
                        bgcolor: 'background.paper',
                        transform: 'translateY(-50%) rotate(45deg)',
                        zIndex: 0,
                        boxShadow: '0 0 10px rgba(0,0,0,0.1)'
                      },
                    },
                  },
                }}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
              >
                <MenuItem onClick={() => navigate('/profile')}>
                  <Avatar /> Profile
                </MenuItem>
                <Divider />
                <MenuItem onClick={() => navigate('/applications')}>
                  <ListItemIcon>
                    <WorkOutlineIcon fontSize="small" />
                  </ListItemIcon>
                  {getApplicationMenuText()}
                </MenuItem>
                {isRecruiter && (
                  <MenuItem onClick={() => navigate('/offers/create')}>
                    <ListItemIcon>
                      <PersonAdd fontSize="small" />
                    </ListItemIcon>
                    Create New Job
                  </MenuItem>
                )}
                <Divider />
                <MenuItem onClick={handleLogout}>
                  <ListItemIcon>
                    <Logout fontSize="small" />
                  </ListItemIcon>
                  Logout
                </MenuItem>
              </Menu>
            </Box>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}
