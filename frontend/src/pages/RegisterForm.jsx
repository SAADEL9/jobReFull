import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

const RegisterForm = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    roles: ["CANDIDATE"],
    firstName: "",
    lastName: "",
    age: "",
    cvUrl: "",
    entreprise: "",
  });

  const navigate = useNavigate();
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: "" }));
    setServerError("");
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.username.trim()) newErrors.username = "Username is required";
    if (!formData.password.trim()) newErrors.password = "Password is required";
    else if (formData.password.length < 6)
      newErrors.password = "Password must be at least 6 characters";

    if (!formData.firstName.trim()) newErrors.firstName = "First name is required";
    if (!formData.lastName.trim()) newErrors.lastName = "Last name is required";

    if (formData.roles[0] === "CANDIDATE") {
      if (!formData.age || isNaN(formData.age)) newErrors.age = "Valid age is required";
      if (!formData.cvUrl.trim()) newErrors.cvUrl = "CV URL is required";
    } else if (formData.roles[0] === "RECRUITER") {
      if (!formData.entreprise.trim()) newErrors.entreprise = "Entreprise name is required";
    }

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      const roleToSend = formData.roles[0].toLowerCase();

      const payload = {
        username: formData.username,
        password: formData.password,
        roles: [roleToSend],
        firstName: formData.firstName,
        lastName: formData.lastName,
      };

      if (roleToSend === "candidate") {
        payload.age = parseInt(formData.age);
        payload.cvUrl = formData.cvUrl;
      } else if (roleToSend === "recruiter") {
        payload.entreprise = formData.entreprise;
      }

      const response = await fetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const result = await response.json();
        throw new Error(result.message || "Registration failed");
      }

      navigate("/login");
    } catch (err) {
      setServerError(err.message);
    }
  };

  return (
    <div className="container">
      <h2>Register</h2>
      {serverError && <div className="error">{serverError}</div>}

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="Username"
          className="mt-1 p-2 border rounded w-full"
          value={formData.username}
          onChange={handleChange}
        />
        {errors.username && <div className="error">{errors.username}</div>}

        <input
          type="password"
          name="password"
          placeholder="Password"
          className="mt-1 p-2 border rounded w-full"
          value={formData.password}
          onChange={handleChange}
        />
        {errors.password && <div className="error">{errors.password}</div>}

        <select
          name="roles"
          value={formData.roles[0]}
          onChange={(e) =>
            setFormData((prev) => ({
              ...prev,
              roles: [e.target.value],
              // Reset specific fields on role change
              age: "",
              cvUrl: "",
              entreprise: "",
            }))
          }
          required
        >
          <option value="CANDIDATE">Candidate</option>
          <option value="RECRUITER">Recruiter</option>
        </select>

        <input
          type="text"
          name="firstName"
          placeholder="First Name"
          className="mt-1 p-2 border rounded w-full"
          value={formData.firstName}
          onChange={handleChange}
        />
        {errors.firstName && <div className="error">{errors.firstName}</div>}

        <input
          type="text"
          name="lastName"
          placeholder="Last Name"
          className="mt-1 p-2 border rounded w-full"
          value={formData.lastName}
          onChange={handleChange}
        />
        {errors.lastName && <div className="error">{errors.lastName}</div>}

        {formData.roles[0] === "CANDIDATE" && (
          <>
            <input
              type="number"
              name="age"
              placeholder="Age"
              className="mt-1 p-2 border rounded w-full"
              value={formData.age}
              onChange={handleChange}
            />
            {errors.age && <div className="error">{errors.age}</div>}

            <input
              type="text"
              name="cvUrl"
              placeholder="CV URL"
              className="mt-1 p-2 border rounded w-full"
              value={formData.cvUrl}
              onChange={handleChange}
            />
            {errors.cvUrl && <div className="error">{errors.cvUrl}</div>}
          </>
        )}

        {formData.roles[0] === "RECRUITER" && (
          <>
            <input
              type="text"
              name="entreprise"
              placeholder="Entreprise"
              className="mt-1 p-2 border rounded w-full"
              value={formData.entreprise}
              onChange={handleChange}
            />
            {errors.entreprise && <div className="error">{errors.entreprise}</div>}
          </>
        )}

        <button type="submit" className="mt-3 p-2 bg-blue-500 text-white rounded">
          Register
        </button>
      </form>

      <Link to="/login" className="link mt-2 block text-blue-600">
        Already have an account? Login
      </Link>
    </div>
  );
};

export default RegisterForm;
