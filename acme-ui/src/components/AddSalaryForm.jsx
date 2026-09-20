import { useState } from "react";
import { addSalary } from "../api/salaryApi";

function AddSalaryForm({ employeeId, onSuccess }) {
    const [amount, setAmount] = useState("");
    const [currency, setCurrency] = useState("INR");
    const [effectiveFrom, setEffectiveFrom] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    async function handleSubmit(event) {
        event.preventDefault();

        try {
            setLoading(true);
            setError("");

            await addSalary(employeeId, {
                amount: Number(amount),
                currency,
                effectiveFrom,
            });

            setAmount("");
            setCurrency("INR");
            setEffectiveFrom("");

            onSuccess();
        } catch (err) {
            console.error(err);

            if (err.response?.data?.message) {
                setError(err.response.data.message);
            } else {
                setError("Failed to add salary.");
            }
        } finally {
            setLoading(false);
        }
    }

    return (
        <section>
            <h2>Add Salary</h2>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Amount</label>
                    <br />
                    <input
                        type="number"
                        min="0.01"
                        step="0.01"
                        value={amount}
                        onChange={(event) =>
                            setAmount(event.target.value)
                        }
                        required
                    />
                </div>

                <br />

                <div>
                    <label>Currency</label>
                    <br />
                    <input
                        type="text"
                        maxLength="3"
                        value={currency}
                        onChange={(event) =>
                            setCurrency(
                                event.target.value.toUpperCase()
                            )
                        }
                        required
                    />
                </div>

                <br />

                <div>
                    <label>Effective From</label>
                    <br />
                    <input
                        type="date"
                        value={effectiveFrom}
                        onChange={(event) =>
                            setEffectiveFrom(event.target.value)
                        }
                        required
                    />
                </div>

                <br />

                <button type="submit" disabled={loading}>
                    {loading ? "Saving..." : "Add Salary"}
                </button>

                {error && (
                    <p>{error}</p>
                )}
            </form>
        </section>
    );
}

export default AddSalaryForm;