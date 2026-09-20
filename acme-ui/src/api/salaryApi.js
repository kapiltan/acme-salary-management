import api from "./api";

export async function addSalary(employeeId, salaryData) {
    const response = await api.post(
        `/employees/${employeeId}/salary`,
        salaryData
    );

    return response.data;
}