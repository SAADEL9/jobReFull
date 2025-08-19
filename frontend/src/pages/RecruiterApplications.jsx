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
  MenuItem,
  Select,
  FormControl,
  InputLabel
} from "@mui/material";
import api from "../axiosConfig";

export default function RecruiterApplications() {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);
  const [feedback, setFeedback] = useState({ open: false, message: "", severity: "success" });

  const statusOptions = ["PENDING", "ACCEPTED", "REJECTED"];
  const statusColor = {
    PENDING: "default",
    ACCEPTED: "success",
    REJECTED: "error"
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
    fetchApplications(userId);
  }, []);

  const fetchApplications = (userId) => {
    setLoading(true);
    api
      .get(`/api/applications/recruiter/${userId}`)
      .then((res) => {
        setApplications(res.data || []);
      })
      .catch((err) => {
        const errorMessage = err.response?.data?.message || err.message || "Failed to load applications";
        setFeedback({ open: true, message: `Error: ${errorMessage}`, severity: "error" });
      })
      .finally(() => setLoading(false));
  };

  const handleStatusChange = async (applicationId, newStatus) => {
    try {
      setUpdating(true);
      await api.put(`/api/applications/${applicationId}/status`, null, {
        params: { newStatus }
      });
      
      setApplications(prev => 
        prev.map(app => 
          app.id === applicationId 
            ? { ...app, status: newStatus } 
            : app
        )
      );
      
      setFeedback({
        open: true,
        message: "Application status updated successfully",
        severity: "success"
      });
    } catch (err) {
      setFeedback({
        open: true,
        message: err.response?.data?.message || "Failed to update application status",
        severity: "error"
      });
    } finally {
      setUpdating(false);
    }
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        Job Applications
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
                <TableCell>Applicant</TableCell>
                <TableCell>Job Title</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Applied On</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {applications.length > 0 ? (
                applications.map((application) => (
                  <TableRow key={application.id}>
                    <TableCell>
                      {application.candidate?.name || 'N/A'}
                    </TableCell>
                    <TableCell>{application.jobOffer?.title || 'N/A'}</TableCell>
                    <TableCell>
                      <Chip 
                        label={application.status} 
                        color={statusColor[application.status] || 'default'}
                      />
                    </TableCell>
                    <TableCell>
                      {new Date(application.applicationDate).toLocaleDateString()}
                    </TableCell>
                    <TableCell>
                      <FormControl size="small" fullWidth disabled={updating}>
                        <Select
                          value={application.status}
                          onChange={(e) => handleStatusChange(application.id, e.target.value)}
                        >
                          {statusOptions.map((status) => (
                            <MenuItem key={status} value={status}>
                              {status}
                            </MenuItem>
                          ))}
                        </Select>
                      </FormControl>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={5} align="center">
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
