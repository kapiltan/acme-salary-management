import { useState } from "react";
import EmployeesPage from "./pages/EmployeesPage";
import AnalyticsPage from "./pages/AnalyticsPage";
import "./App.css";

function App() {
  const [page, setPage] = useState("employees");

  return (
    <div className="app-shell">
      <header className="app-header">
        <div className="app-header-inner">
          <div className="app-brand">
            <div className="app-brand-mark">A</div>
            <span className="app-brand-text">
              ACME Salary Management
            </span>
          </div>

          <nav className="app-nav">
            <button
              type="button"
              onClick={() => setPage("employees")}
              aria-current={page === "employees" ? "page" : undefined}
            >
              Employees
            </button>

            <button
              type="button"
              onClick={() => setPage("analytics")}
              aria-current={page === "analytics" ? "page" : undefined}
            >
              Analytics
            </button>
          </nav>
        </div>
      </header>

      <main className="app-main">
        {page === "employees" && <EmployeesPage />}
        {page === "analytics" && <AnalyticsPage />}
      </main>
    </div>
  );
}

export default App;