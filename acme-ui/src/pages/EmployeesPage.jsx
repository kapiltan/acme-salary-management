import { useEffect, useState } from "react";
import { getEmployees } from "../api/employeeApi";
import EmployeeDetailsPage from "./EmployeeDetailsPage";

function EmployeesPage() {
    const [employees, setEmployees] = useState([]);

    const [page, setPage] = useState(0);
    const [size] = useState(20);

    const [search, setSearch] = useState("");
    const [country, setCountry] = useState("");
    const [department, setDepartment] = useState("");

    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [selectedEmployeeId, setSelectedEmployeeId] = useState(null);

    useEffect(() => {
        loadEmployees();
    }, [page, search, country, department]);

    async function loadEmployees() {
        try {
            setLoading(true);
            setError("");

            const data = await getEmployees({
                page,
                size,
                search,
                country,
                department,
            });

            setEmployees(data.content);
            setTotalPages(data.totalPages);
            setTotalElements(data.totalElements);
        } catch (err) {
            console.error(err);
            setError("Failed to load employees.");
        } finally {
            setLoading(false);
        }
    }

    function handleSearchChange(event) {
        setSearch(event.target.value);
        setPage(0);
    }

    function handleCountryChange(event) {
        setCountry(event.target.value);
        setPage(0);
    }

    function handleDepartmentChange(event) {
        setDepartment(event.target.value);
        setPage(0);
    }

    function goToPreviousPage() {
        if (page > 0) {
            setPage(page - 1);
        }
    }

    function goToNextPage() {
        if (page < totalPages - 1) {
            setPage(page + 1);
        }
    }

    if (selectedEmployeeId !== null) {
        return (
            <EmployeeDetailsPage
                employeeId={selectedEmployeeId}
                onBack={() => setSelectedEmployeeId(null)}
            />
        );
    }

    return (
        <div>
            <div className="page-header">
                <h1>Employee Management</h1>
                <p>Search, filter and manage employee salary information.</p>
            </div>

            <div className="card">
                <div className="filters">
                    <input
                        type="text"
                        placeholder="Search by name, email or employee ID"
                        value={search}
                        onChange={handleSearchChange}
                        aria-label="Search employees"
                    />

                    <input
                        type="text"
                        placeholder="Country"
                        value={country}
                        onChange={handleCountryChange}
                        aria-label="Filter by country"
                    />

                    <input
                        type="text"
                        placeholder="Department"
                        value={department}
                        onChange={handleDepartmentChange}
                        aria-label="Filter by department"
                    />
                </div>

                <p style={{ marginBottom: "16px", fontSize: "14px" }}>
                    <strong>{totalElements.toLocaleString()}</strong>{" "}
                    employees found
                </p>

                {loading && (
                    <p role="status">Loading employees...</p>
                )}

                {error && (
                    <p className="error-message" role="alert">
                        {error}
                    </p>
                )}

                {!loading && !error && (
                    <>
                        {employees.length === 0 ? (
                            <p>No employees found.</p>
                        ) : (
                            <div className="data-table">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Employee Code</th>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th>Country</th>
                                            <th>Department</th>
                                            <th>Job Title</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        {employees.map((employee) => (
                                            <tr key={employee.id}>
                                                <td>{employee.employeeCode}</td>

                                                <td>
                                                    <button
                                                        type="button"
                                                        className="link-button"
                                                        onClick={() =>
                                                            setSelectedEmployeeId(
                                                                employee.id
                                                            )
                                                        }
                                                    >
                                                        {employee.firstName}{" "}
                                                        {employee.lastName}
                                                    </button>
                                                </td>

                                                <td>{employee.email}</td>
                                                <td>{employee.country}</td>
                                                <td>{employee.department}</td>
                                                <td>{employee.jobTitle}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        )}

                        <div className="pagination">
                            <span>
                                Page {page + 1} of {totalPages}
                            </span>

                            <div className="pagination-actions">
                                <button
                                    type="button"
                                    className="secondary-button"
                                    onClick={goToPreviousPage}
                                    disabled={page === 0}
                                >
                                    Previous
                                </button>

                                <button
                                    type="button"
                                    className="primary-button"
                                    onClick={goToNextPage}
                                    disabled={page >= totalPages - 1}
                                >
                                    Next
                                </button>
                            </div>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}

export default EmployeesPage;