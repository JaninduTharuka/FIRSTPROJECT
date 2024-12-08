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


export default HomePage;



const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    height: "100vh",
  },
  actions: {
    display: "flex",
    flexDirection: "row",
    marginTop: 20,
  },
  button: {
    display: "block",
    padding: 10,
    margin: 10,
    backgroundColor: "#4CAF50",
    color: "white",
    textDecoration: "none",
    borderRadius: 5,
  },
};