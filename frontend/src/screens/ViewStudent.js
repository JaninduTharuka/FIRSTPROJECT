import React, { useState } from "react";
import axios from "axios";

const ViewStudent = () => {
  const [studentId, setStudentId] = useState("");
  const [student, setStudent] = useState(null);

  const fetchStudent = async () => {
    if (!studentId) {
      // Reset student data if the ID is empty
      setStudent(null);
      return;
    }

    try {
      const response = await axios.get(
        `http://localhost:8080/student/getStudent?id=${studentId}`
      );
      if (response.data) {
        setStudent(response.data);
      } else {
        alert("Student not found!");
        setStudent(null); // Reset student state in case of no data
      }
    } catch (error) {
      console.error("Error fetching student:", error);
      alert("Could not find student with the given ID.");
      setStudent(null); // Reset student state in case of error
    }
  };

  return (
    <div style={styles.container}>
      <h1>View Student</h1>
      <input
        type="number"
        placeholder="Student ID"
        value={studentId}
        onChange={(e) => setStudentId(e.target.value)}
      />
      <button onClick={fetchStudent}>View</button>

      {/* Show student details only if student is found */}
      {student ? (
        <div>
          <h3>Student Details</h3>
          <h4>Name: {student.name}</h4>
          <h4>Date of Birth: {student.dob}</h4>
          <h4>Teacher: {student.teacherName}</h4>
        </div>
      ) : (
        <h4>No student data to display.</h4>
      )}
    </div>
  );
};

export default ViewStudent;


const styles = {
  container: { padding: "20px", fontFamily: "Arial", textAlign: "center" },
};