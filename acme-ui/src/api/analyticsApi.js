import api from "./api";

export async function getAnalyticsSummary() {
    const response = await api.get("/analytics/summary");
    return response.data;
}

export async function getSalaryByCountry() {
    const response = await api.get("/analytics/salary-by-country");
    return response.data;
}

export async function getSalaryByDepartment() {
    const response = await api.get("/analytics/salary-by-department");
    return response.data;
}

export async function getSalaryDistribution() {
    const response = await api.get("/analytics/salary-distribution");
    return response.data;
}