import { useEffect, useState } from "react";
import {
    getEmployee,
    getCurrentSalary,
    getSalaryHistory,
} from "../api/employeeApi";
import AddSalaryForm from "../components/AddSalaryForm";

function EmployeeDetailsPage({ employeeId, onBack }) {
    const [employee, setEmployee] = useState(null);
    const [currentSalary, setCurrentSalary] = useState(null);
    const [salaryHistory, setSalaryHistory] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadEmployeeDetails();
    }, [employeeId]);

    function handleSalaryAdded() {
        loadEmployeeDetails();
    }

    async function loadEmployeeDetails() {
        try {
            setLoading(true);
            setError("");

            const [
                employeeData,
                salaryData,
                historyData,
            ] = await Promise.all([
                getEmployee(employeeId),
                getCurrentSalary(employeeId),
                getSalaryHistory(employeeId),
            ]);

            setEmployee(employeeData);
            setCurrentSalary(salaryData);
            setSalaryHistory(historyData);
        } catch (err) {
            console.error(err);
            setError("Failed to load employee details.");
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return <p>Loading employee...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    if (!employee) {
        return <p>Employee not found.</p>;
    }

    return (
        <div>
            <button onClick={onBack}>
                ← Back to Employees
            </button>

            <h1>Employee Details</h1>

            <section>
                <h2>Employee Information</h2>

                <p>
                    <strong>Employee Code:</strong>{" "}
                    {employee.employeeCode}
                </p>

                <p>
                    <strong>Name:</strong>{" "}
                    {employee.firstName} {employee.lastName}
                </p>

                <p>
                    <strong>Email:</strong> {employee.email}
                </p>

                <p>
                    <strong>Country:</strong> {employee.country}
                </p>

                <p>
                    <strong>Department:</strong>{" "}
                    {employee.department}
                </p>

                <p>
                    <strong>Job Title:</strong>{" "}
                    {employee.jobTitle}
                </p>
            </section>

            <section>
                <h2>Current Salary</h2>

                {currentSalary ? (
                    <div>
                        <p>
                            <strong>Amount:</strong>{" "}
                            {Number(currentSalary.amount).toLocaleString()}
                        </p>

                        <p>
                            <strong>Currency:</strong>{" "}
                            {currentSalary.currency}
                        </p>

                        <p>
                            <strong>Effective From:</strong>{" "}
                            {currentSalary.effectiveFrom}
                        </p>
                    </div>
                ) : (
                    <p>No current salary found.</p>
                )}
            </section>

            <section>
                <h2>Salary History</h2>

                {salaryHistory.length === 0 ? (
                    <p>No salary history found.</p>
                ) : (
                    <table>
                        <thead>
                            <tr>
                                <th>Amount</th>
                                <th>Currency</th>
                                <th>Effective From</th>
                            </tr>
                        </thead>

                        <tbody>
                            {salaryHistory.map((salary) => (
                                <tr key={salary.id}>
                                    <td>
                                        {Number(
                                            salary.amount
                                        ).toLocaleString()}
                                    </td>

                                    <td>
                                        {salary.currency}
                                    </td>

                                    <td>
                                        {salary.effectiveFrom}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </section>
            <AddSalaryForm
                employeeId={employeeId}
                onSuccess={handleSalaryAdded}
            />
        </div>
    );
}

export default EmployeeDetailsPage;