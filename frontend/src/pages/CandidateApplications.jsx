import React, { useEffect, useState } from "react";
import {
  Box,
  Button,
  Chip,
  CircularProgress,
  Container,
  Paper,
  Snackbar,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Stack
} from "@mui/material";
import api from "../axiosConfig";

export default function CandidateApplications() {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [feedback, setFeedback] = useState({ open: false, message: "", severity: "success" });

  const statusColor = {
    pending: "default",
    accepted: "success",
    rejected: "error"
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      setFeedback({
        open: true,
        message: "Please login to view applications",
        severity: "warning"
      });
      return;
    }

    const userId = JSON.parse(localStorage.getItem("user")).id;
    
    setLoading(true);
    api
      .get(`/api/applications/candidat/${userId}`)
      .then((res) => {
        setApplications(res.data || []);
      })
      .catch((err) => {
        const errorMessage = err.response?.data?.message || err.message || "Failed to load applications";
        setFeedback({ open: true, message: `Error: ${errorMessage}`, severity: "error" });
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        My Job Applications
      </Typography>
      
      {loading ? (
        <Box display="flex" justifyContent="center" my={4}>
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Job Title</TableCell>
                <TableCell>Company</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Applied On</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {applications.length > 0 ? (
                applications.map((application) => (
                  <TableRow key={application.id}>
                    <TableCell>{application.jobOffer?.title || 'N/A'}</TableCell>
                    <TableCell>{application.jobOffer?.companyName || 'N/A'}</TableCell>
                    <TableCell>
                      <Chip 
                        label={application.status} 
                        color={statusColor[application.status.toLowerCase()] || 'default'}
                      />
                    </TableCell>
                    <TableCell>
                      {new Date(application.applicationDate).toLocaleDateString()}
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={4} align="center">
                    No applications found
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      <Snackbar
        open={feedback.open}
        autoHideDuration={6000}
        onClose={() => setFeedback({ ...feedback, open: false })}
      >
        <Alert 
          onClose={() => setFeedback({ ...feedback, open: false })} 
          severity={feedback.severity}
          sx={{ width: '100%' }}
        >
          {feedback.message}
        </Alert>
      </Snackbar>
    </Container>
  );
}
