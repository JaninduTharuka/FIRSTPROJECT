import React from "react";
import { Link } from "react-router-dom";

const HomePage = () => {
  return (
    <div style={styles.container}>
      <h1>Welcome to the Student Management System</h1>

      <div style={styles.actions}>
        <Link to="/add-student" style={styles.button}>
          Add Student
        </Link>
        <Link to="/view-student" style={styles.button}>
          View Students
        </Link>
      </div>
    </div>
  );
};

const styles = {
  container: { textAlign: "center", padding: "50px" },
  actions: { marginTop: "20px" },
  button: {
    display: "inline-block",
    margin: "10px",
    padding: "10px 20px",
    backgroundColor: "#4CAF50",
    color: "white",
    textDecoration: "none",
    borderRadius: "5px",
    fontSize: "16px",
  },
};

export default HomePage;
