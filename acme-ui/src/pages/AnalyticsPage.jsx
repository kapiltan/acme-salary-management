import { useEffect, useState } from "react";
import {
    getAnalyticsSummary,
    getSalaryByCountry,
    getSalaryByDepartment,
    getSalaryDistribution,
} from "../api/analyticsApi";

function AnalyticsPage() {
    const [summary, setSummary] = useState(null);
    const [countryData, setCountryData] = useState([]);
    const [departmentData, setDepartmentData] = useState([]);
    const [distributionData, setDistributionData] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadAnalytics();
    }, []);

    async function loadAnalytics() {
        try {
            setLoading(true);
            setError("");

            const [
                summaryData,
                countrySalaryData,
                departmentSalaryData,
                distributionSalaryData,
            ] = await Promise.all([
                getAnalyticsSummary(),
                getSalaryByCountry(),
                getSalaryByDepartment(),
                getSalaryDistribution(),
            ]);

            setSummary(summaryData);
            setCountryData(countrySalaryData);
            setDepartmentData(departmentSalaryData);
            setDistributionData(distributionSalaryData);
        } catch (err) {
            console.error(err);
            setError("Failed to load analytics.");
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return <p>Loading analytics...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <div>
            <h1>Salary Analytics</h1>

            {summary && (
                <section>
                    <h2>Organization Summary</h2>

                    <div>
                        <div>
                            <strong>Total Employees</strong>
                            <p>{summary.totalEmployees}</p>
                        </div>

                        <div>
                            <strong>Average Salary</strong>
                            <p>
                                {Number(
                                    summary.averageSalary
                                ).toLocaleString()}
                            </p>
                        </div>

                        <div>
                            <strong>Highest Salary</strong>
                            <p>
                                {Number(
                                    summary.highestSalary
                                ).toLocaleString()}
                            </p>
                        </div>

                        <div>
                            <strong>Lowest Salary</strong>
                            <p>
                                {Number(
                                    summary.lowestSalary
                                ).toLocaleString()}
                            </p>
                        </div>
                    </div>
                </section>
            )}

            <section>
                <h2>Salary by Country</h2>

                <table>
                    <thead>
                        <tr>
                            <th>Country</th>
                            <th>Employees</th>
                            <th>Average Salary</th>
                        </tr>
                    </thead>

                    <tbody>
                        {countryData.map((item) => (
                            <tr key={item.groupName}>
                                <td>{item.groupName}</td>
                                <td>{item.employeeCount}</td>
                                <td>
                                    {Number(
                                        item.averageSalary
                                    ).toLocaleString()}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </section>

            <section>
                <h2>Salary by Department</h2>

                <table>
                    <thead>
                        <tr>
                            <th>Department</th>
                            <th>Employees</th>
                            <th>Average Salary</th>
                        </tr>
                    </thead>

                    <tbody>
                        {departmentData.map((item) => (
                            <tr key={item.groupName}>
                                <td>{item.groupName}</td>
                                <td>{item.employeeCount}</td>
                                <td>
                                    {Number(
                                        item.averageSalary
                                    ).toLocaleString()}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </section>

            <section>
                <h2>Salary Distribution</h2>

                <table>
                    <thead>
                        <tr>
                            <th>Salary Range</th>
                            <th>Employees</th>
                        </tr>
                    </thead>

                    <tbody>
                        {distributionData.map((item) => (
                            <tr key={item.salaryRange}>
                                <td>{item.salaryRange}</td>
                                <td>{item.employeeCount}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </section>
        </div>
    );
}

export default AnalyticsPage;