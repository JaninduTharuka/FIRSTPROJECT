import React, { useState } from "react";
import axios from "axios";

const AddStudent = () => {
  const [name, setName] = useState("");
  const [age, setAge] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    const studentData = { name, age};
    console.log("Student data:", studentData);
    

    axios
      .post("http://localhost:8080/students", 
      studentData,
      { headers: { "Content-Type": "application/json" } },
      console.log("request sent:", studentData)
    )
      .then((response) => {
        alert("Student added successfully!");
        setName("");
        setAge("");
      })
      .catch((error) => {
        console.error("There was an error adding the student:", error);
      });
  };

  return (
    <div style={styles.container}>
      <h1>Add New Student</h1>
      <form onSubmit={handleSubmit}>
        <div style={styles.inputGroup}>
          <label>Name:</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            style={styles.input}
            required
          />
        </div>
        <div style={styles.inputGroup}>
          <label>Age:</label>
          <input
            type="number"
            value={age}
            onChange={(e) => setAge(e.target.value)}
            style={styles.input}
            required
          />
        </div>

        <button type="submit" style={styles.submitButton}>
          Add Student
        </button>
      </form>
    </div>
  );
};

const styles = {
  container: { padding: "20px", fontFamily: "Arial", textAlign: "center" },
  inputGroup: { marginBottom: "10px" },
  input: { padding: "8px", width: "200px", marginBottom: "10px" },
  submitButton: {
    padding: "10px 20px",
    backgroundColor: "#4CAF50",
    color: "white",
    border: "none",
    cursor: "pointer",
  },
};

export default AddStudent;
