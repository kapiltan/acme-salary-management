import api from "./api";

export async function getEmployees({
    page = 0,
    size = 20,
    search = "",
    country = "",
    department = "",
} = {}) {
    const response = await api.get("/employees", {
        params: {
            page,
            size,
            search,
            country,
            department,
        },
    });

    return response.data;
}

export async function getEmployee(id) {
    const response = await api.get(`/employees/${id}`);
    return response.data;
}

export async function getCurrentSalary(employeeId) {
    const response = await api.get(`/employees/${employeeId}/salary`);
    return response.data;
}

export async function getSalaryHistory(employeeId) {
    const response = await api.get(
        `/employees/${employeeId}/salary/history`
    );
    return response.data;
}