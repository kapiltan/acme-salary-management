import { render, screen, waitFor } from "@testing-library/react";
import { vi } from "vitest";
import AnalyticsPage from "./AnalyticsPage";

vi.mock("../api/analyticsApi", () => ({
    getAnalyticsSummary: vi.fn(),
    getSalaryByCountry: vi.fn(),
    getSalaryByDepartment: vi.fn(),
    getSalaryDistribution: vi.fn(),
}));

import {
    getAnalyticsSummary,
    getSalaryByCountry,
    getSalaryByDepartment,
    getSalaryDistribution,
} from "../api/analyticsApi";

describe("AnalyticsPage", () => {
    beforeEach(() => {
        vi.clearAllMocks();

        getAnalyticsSummary.mockResolvedValue({
            totalEmployees: 10000,
            averageSalary: 99995,
            highestSalary: 149990,
            lowestSalary: 50000,
        });

        getSalaryByCountry.mockResolvedValue([
            {
                groupName: "India",
                employeeCount: 2000,
                averageSalary: 99985,
            },
        ]);

        getSalaryByDepartment.mockResolvedValue([
            {
                groupName: "Engineering",
                employeeCount: 1667,
                averageSalary: 99990,
            },
        ]);

        getSalaryDistribution.mockResolvedValue([
            {
                salaryRange: "0-75K",
                employeeCount: 2500,
            },
        ]);
    });

    it("displays organization summary", async () => {
        render(<AnalyticsPage />);

        expect(
            screen.getByText("Loading analytics...")
        ).toBeInTheDocument();

        await waitFor(() => {
            expect(
                screen.getByText("Organization Summary")
            ).toBeInTheDocument();
        });

        expect(screen.getByText("10000")).toBeInTheDocument();
        expect(screen.getByText("99,995")).toBeInTheDocument();
        expect(screen.getByText("1,49,990")).toBeInTheDocument();
        expect(screen.getByText("50,000")).toBeInTheDocument();
    });

    it("displays salary by country", async () => {
        render(<AnalyticsPage />);

        await waitFor(() => {
            expect(screen.getByText("India")).toBeInTheDocument();
        });

        expect(screen.getByText("2000")).toBeInTheDocument();
    });

    it("displays salary by department", async () => {
        render(<AnalyticsPage />);

        await waitFor(() => {
            expect(
                screen.getByText("Engineering")
            ).toBeInTheDocument();
        });

        expect(screen.getByText("1667")).toBeInTheDocument();
    });

    it("displays salary distribution", async () => {
        render(<AnalyticsPage />);

        await waitFor(() => {
            expect(screen.getByText("0-75K")).toBeInTheDocument();
        });

        expect(screen.getByText("2500")).toBeInTheDocument();
    });
});