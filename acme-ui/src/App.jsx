import { useState } from "react";
import EmployeesPage from "./pages/EmployeesPage";
import AnalyticsPage from "./pages/AnalyticsPage";

function App() {
  const [page, setPage] = useState("employees");

  return (
    <div>
      <nav>
        <button onClick={() => setPage("employees")}>
          Employees
        </button>

        <button onClick={() => setPage("analytics")}>
          Analytics
        </button>
      </nav>

      <hr />

      {page === "employees" && <EmployeesPage />}

      {page === "analytics" && <AnalyticsPage />}
    </div>
  );
}

export default App;