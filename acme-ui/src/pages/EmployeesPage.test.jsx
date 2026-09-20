import { render, screen, waitFor } from "@testing-library/react";
import { vi } from "vitest";
import EmployeesPage from "./EmployeesPage";
import { getEmployees } from "../api/employeeApi";

vi.mock("../api/employeeApi", () => ({
    getEmployees: vi.fn(),
}));

describe("EmployeesPage", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("loads and displays employees", async () => {
        getEmployees.mockResolvedValue({
            content: [
                {
                    id: 1,
                    employeeCode: "EMP00001",
                    firstName: "John",
                    lastName: "Doe",
                    email: "john.doe@acme.com",
                    country: "India",
                    department: "Engineering",
                    jobTitle: "Software Engineer",
                },
                {
                    id: 2,
                    employeeCode: "EMP00002",
                    firstName: "Jane",
                    lastName: "Smith",
                    email: "jane.smith@acme.com",
                    country: "USA",
                    department: "Finance",
                    jobTitle: "Financial Analyst",
                },
            ],
            totalPages: 1,
            totalElements: 2,
            number: 0,
            size: 20,
        });

        render(<EmployeesPage />);

        await waitFor(() => {
            expect(screen.getByText("EMP00001")).toBeInTheDocument();
        });

        expect(
            screen.getByRole("button", { name: "John Doe" })
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", { name: "Jane Smith" })
        ).toBeInTheDocument();

        expect(screen.getByText("john.doe@acme.com")).toBeInTheDocument();
        expect(screen.getByText("jane.smith@acme.com")).toBeInTheDocument();

        expect(getEmployees).toHaveBeenCalled();
    });

    test("displays loading state while employees are being fetched", async () => {
        getEmployees.mockImplementation(
            () => new Promise(() => { })
        );

        render(<EmployeesPage />);

        expect(screen.getByText(/loading/i)).toBeInTheDocument();
    });

    test("displays error message when employee API fails", async () => {
        getEmployees.mockRejectedValue(new Error("API failure"));

        render(<EmployeesPage />);

        await waitFor(() => {
            expect(screen.getByText(/failed/i)).toBeInTheDocument();
        });
    });

    test("displays zero employees when no employees are returned", async () => {
        getEmployees.mockResolvedValue({
            content: [],
            totalPages: 0,
            totalElements: 0,
            number: 0,
            size: 20,
        });

        render(<EmployeesPage />);

        await waitFor(() => {
            expect(screen.getByText(/Total employees:\s*0/i)).toBeInTheDocument();
        });
    });
});