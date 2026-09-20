import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { vi } from "vitest";
import AddSalaryForm from "./AddSalaryForm";
import { addSalary } from "../api/salaryApi";

vi.mock("../api/salaryApi", () => ({
    addSalary: vi.fn(),
}));

describe("AddSalaryForm", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("renders salary form fields", () => {
        const { container } = render(
            <AddSalaryForm employeeId={1} onSuccess={vi.fn()} />
        );

        expect(
            screen.getByRole("heading", { name: "Add Salary" })
        ).toBeInTheDocument();

        expect(container.querySelector('input[type="number"]'))
            .toBeInTheDocument();

        expect(container.querySelector('input[type="text"]'))
            .toBeInTheDocument();

        expect(container.querySelector('input[type="date"]'))
            .toBeInTheDocument();

        expect(
            screen.getByRole("button", { name: /add salary/i })
        ).toBeInTheDocument();
    });

    test("submits salary data successfully", async () => {
        const user = userEvent.setup();
        const onSuccess = vi.fn();

        addSalary.mockResolvedValue({
            id: 10,
            amount: 75000,
            currency: "INR",
            effectiveFrom: "2026-09-20",
        });

        const { container } = render(
            <AddSalaryForm employeeId={1} onSuccess={onSuccess} />
        );

        const amountInput = container.querySelector(
            'input[type="number"]'
        );

        const currencyInput = container.querySelector(
            'input[type="text"]'
        );

        const dateInput = container.querySelector(
            'input[type="date"]'
        );

        await user.type(amountInput, "75000");

        await user.clear(currencyInput);
        await user.type(currencyInput, "INR");

        await user.type(dateInput, "2026-09-20");

        await user.click(
            screen.getByRole("button", { name: /add salary/i })
        );

        await waitFor(() => {
            expect(addSalary).toHaveBeenCalledWith(1, {
                amount: 75000,
                currency: "INR",
                effectiveFrom: "2026-09-20",
            });
        });

        expect(onSuccess).toHaveBeenCalled();
    });

    test("does not submit when required fields are missing", async () => {
        const user = userEvent.setup();

        render(
            <AddSalaryForm employeeId={1} onSuccess={vi.fn()} />
        );

        await user.click(
            screen.getByRole("button", { name: /add salary/i })
        );

        expect(addSalary).not.toHaveBeenCalled();
    });

    test("displays error when salary API fails", async () => {
        const user = userEvent.setup();

        addSalary.mockRejectedValue(new Error("API failure"));

        const { container } = render(
            <AddSalaryForm employeeId={1} onSuccess={vi.fn()} />
        );

        const amountInput = container.querySelector(
            'input[type="number"]'
        );

        const currencyInput = container.querySelector(
            'input[type="text"]'
        );

        const dateInput = container.querySelector(
            'input[type="date"]'
        );

        await user.type(amountInput, "75000");

        await user.clear(currencyInput);
        await user.type(currencyInput, "INR");

        await user.type(dateInput, "2026-09-20");

        await user.click(
            screen.getByRole("button", { name: /add salary/i })
        );

        await waitFor(() => {
            expect(screen.getByText(/failed/i)).toBeInTheDocument();
        });
    });
});