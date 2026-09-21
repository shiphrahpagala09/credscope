import { FormEvent, useEffect, useState } from "react";
import "./App.css";
import UnderwriterDashboard from "./UnderwriterDashboard";
import AdminDashboard from "./AdminDashboard";

const API_URL = "http://localhost:8080";

type Application = {
  id: number;
  applicantName: string;
  loanAmount: number;
  purpose: string;
  riskScore: number | null;
  riskCategory: string | null;
  riskFactors: string[];
  aiRiskExplanation: string | null;
  status: string;
  createdAt: string;
};

function App() {
  const [isRegister, setIsRegister] = useState(false);

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const [token, setToken] = useState(
    localStorage.getItem("token")
  );

  const [applications, setApplications] = useState<Application[]>([]);
  const [loadingApplications, setLoadingApplications] = useState(false);

  const [showLoanForm, setShowLoanForm] = useState(false);

  const [loanAmount, setLoanAmount] = useState("");
  const [purpose, setPurpose] = useState("HOME");
  const [monthlyIncome, setMonthlyIncome] = useState("");
  const [existingEmi, setExistingEmi] = useState("");
  const [employmentType, setEmploymentType] = useState("SALARIED");

  const userName = localStorage.getItem("name") || "Applicant";
  const role = localStorage.getItem("role");

  useEffect(() => {
    if (token) {
      fetchApplications();
    }
  }, [token]);

  const fetchApplications = async () => {
    const jwt = localStorage.getItem("token");

    if (!jwt) {
      return;
    }

    setLoadingApplications(true);

    try {
      const response = await fetch(
        `${API_URL}/api/applications/my`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Unable to fetch applications");
      }

      const data = await response.json();
      setApplications(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoadingApplications(false);
    }
  };

  const handleAuth = async (event: FormEvent) => {
    event.preventDefault();

    setMessage("");
    setLoading(true);

    try {
      const endpoint = isRegister
        ? `${API_URL}/api/auth/register`
        : `${API_URL}/api/auth/login`;

      const body = isRegister
        ? {
            name,
            email,
            password,
            role: "APPLICANT",
          }
        : {
            email,
            password,
          };

      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(
          data.message ||
            data.error ||
            "Authentication failed"
        );
      }

      localStorage.setItem("token", data.token);
      localStorage.setItem("role", data.role);
      localStorage.setItem("name", data.name);

      setToken(data.token);

      setMessage(
        isRegister
          ? "Registration successful!"
          : "Login successful!"
      );
    } catch (error) {
      setMessage(
        error instanceof Error
          ? error.message
          : "Unable to connect to server"
      );
    } finally {
      setLoading(false);
    }
  };

  const submitLoanApplication = async (
    event: FormEvent
  ) => {
    event.preventDefault();

    const jwt = localStorage.getItem("token");

    if (!jwt) {
      setMessage("Please login again.");
      return;
    }

    setLoading(true);
    setMessage("");

    try {
      const response = await fetch(
        `${API_URL}/api/applications`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${jwt}`,
          },
          body: JSON.stringify({
            loanAmount: Number(loanAmount),
            purpose,
            monthlyIncome: Number(monthlyIncome),
            existingEmi: Number(existingEmi),
            employmentType,
          }),
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(
          data.message ||
            data.error ||
            "Loan application failed"
        );
      }

      setMessage(
        `Application #${data.id} submitted successfully.`
      );

      setLoanAmount("");
      setMonthlyIncome("");
      setExistingEmi("");
      setPurpose("HOME");
      setEmploymentType("SALARIED");

      setShowLoanForm(false);

      await fetchApplications();
    } catch (error) {
      setMessage(
        error instanceof Error
          ? error.message
          : "Unable to submit application"
      );
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.clear();
    setToken(null);
    setApplications([]);
  };

  if (token) {

    if (role === "ADMIN") {
      return <AdminDashboard />;
    }
  
    if (role === "UNDERWRITER") {
      return <UnderwriterDashboard />;
    }

    return (
      <div className="dashboard-page">
        <header className="dashboard-header">
          <div>
            <h1>CredScope</h1>
            <p>AI-Powered Credit Risk Assessment</p>
          </div>

          <div className="header-right">
            <span>Hi, {userName}</span>

            <button
              className="logout-button"
              onClick={logout}
            >
              Logout
            </button>
          </div>
        </header>

        <main className="dashboard-content">
          <div className="welcome-section">
            <div>
              <h2>Applicant Dashboard</h2>
              <p>
                Track your loan applications and AI-assisted
                risk assessments.
              </p>
            </div>

            <button
              className="primary-button"
              onClick={() => setShowLoanForm(true)}
            >
              + New Loan Application
            </button>
          </div>

          {message && (
            <div className="dashboard-message">
              {message}
            </div>
          )}

          {showLoanForm && (
            <div className="loan-form-card">
              <div className="card-header">
                <h3>New Loan Application</h3>

                <button
                  className="close-button"
                  onClick={() => setShowLoanForm(false)}
                >
                  ×
                </button>
              </div>

              <form onSubmit={submitLoanApplication}>
                <div className="form-grid">

                  <div className="form-group">
                    <label>Loan Amount</label>
                    <input
                      type="number"
                      value={loanAmount}
                      onChange={(e) =>
                        setLoanAmount(e.target.value)
                      }
                      placeholder="500000"
                      min="1"
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Purpose</label>

                    <select
                      value={purpose}
                      onChange={(e) =>
                        setPurpose(e.target.value)
                      }
                    >
                      <option value="HOME">Home</option>
                      <option value="EDUCATION">
                        Education
                      </option>
                      <option value="VEHICLE">
                        Vehicle
                      </option>
                      <option value="PERSONAL">
                        Personal
                      </option>
                      <option value="BUSINESS">
                        Business
                      </option>
                    </select>
                  </div>

                  <div className="form-group">
                    <label>Monthly Income</label>

                    <input
                      type="number"
                      value={monthlyIncome}
                      onChange={(e) =>
                        setMonthlyIncome(e.target.value)
                      }
                      placeholder="60000"
                      min="1"
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Existing EMI</label>

                    <input
                      type="number"
                      value={existingEmi}
                      onChange={(e) =>
                        setExistingEmi(e.target.value)
                      }
                      placeholder="5000"
                      min="0"
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Employment Type</label>

                    <select
                      value={employmentType}
                      onChange={(e) =>
                        setEmploymentType(e.target.value)
                      }
                    >
                      <option value="SALARIED">
                        Salaried
                      </option>

                      <option value="SELF_EMPLOYED">
                        Self Employed
                      </option>

                      <option value="BUSINESS_OWNER">
                        Business Owner
                      </option>
                    </select>
                  </div>

                </div>

                <button
                  className="primary-button"
                  type="submit"
                  disabled={loading}
                >
                  {loading
                    ? "Submitting..."
                    : "Submit Application"}
                </button>
              </form>
            </div>
          )}

          <section className="stats-grid">
            <div className="stat-card">
              <span>Total Applications</span>
              <strong>{applications.length}</strong>
            </div>

            <div className="stat-card">
              <span>Submitted</span>
              <strong>
                {
                  applications.filter(
                    (a) => a.status === "SUBMITTED"
                  ).length
                }
              </strong>
            </div>

            <div className="stat-card">
              <span>Approved</span>
              <strong>
                {
                  applications.filter(
                    (a) => a.status === "APPROVED"
                  ).length
                }
              </strong>
            </div>

            <div className="stat-card">
              <span>Rejected</span>
              <strong>
                {
                  applications.filter(
                    (a) => a.status === "REJECTED"
                  ).length
                }
              </strong>
            </div>
          </section>

          <section className="applications-section">
            <h3>My Loan Applications</h3>

            {loadingApplications ? (
              <div className="empty-state">
                Loading applications...
              </div>
            ) : applications.length === 0 ? (
              <div className="empty-state">
                No loan applications yet.
              </div>
            ) : (
              <div className="application-list">
                {applications.map((application) => (
                  <div
                    className="application-card"
                    key={application.id}
                  >
                    <div className="application-top">
                      <div>
                        <h4>
                          Application #{application.id}
                        </h4>

                        <p>
                          {application.purpose} Loan
                        </p>
                      </div>

                      <span
                        className={`status ${application.status
                          .toLowerCase()
                          .replace("_", "-")}`}
                      >
                        {application.status}
                      </span>
                    </div>

                    <div className="application-details">

                      <div>
                        <span>Loan Amount</span>
                        <strong>
                          ₹{" "}
                          {application.loanAmount.toLocaleString(
                            "en-IN"
                          )}
                        </strong>
                      </div>

                      <div>
                        <span>Risk Score</span>
                        <strong>
                          {application.riskScore ?? "Pending"}
                        </strong>
                      </div>

                      <div>
                        <span>Risk Category</span>
                        <strong>
                          {application.riskCategory ??
                            "Pending"}
                        </strong>
                      </div>

                    </div>

                    <div className="risk-factors">
                      <h5>Risk Factors</h5>

                      {application.riskFactors?.map(
                        (factor, index) => (
                          <div key={index}>
                            ✓ {factor}
                          </div>
                        )
                      )}
                    </div>

                    {application.aiRiskExplanation && (
                      <div className="ai-explanation">
                        <h5>🤖 AI Risk Explanation</h5>

                        <p>
                          {application.aiRiskExplanation}
                        </p>
                      </div>
                    )}

                  </div>
                ))}
              </div>
            )}
          </section>
        </main>
      </div>
    );
  }

  return (
    <div className="app-container">
      <div className="auth-card">

        <div className="brand-section">
          <div className="logo">C</div>

          <h1>CredScope</h1>

          <p>
            AI-Powered Credit Risk Assessment
          </p>
        </div>

        <div className="auth-tabs">

          <button
            className={!isRegister ? "active" : ""}
            onClick={() => {
              setIsRegister(false);
              setMessage("");
            }}
          >
            Login
          </button>

          <button
            className={isRegister ? "active" : ""}
            onClick={() => {
              setIsRegister(true);
              setMessage("");
            }}
          >
            Register
          </button>

        </div>

        <form onSubmit={handleAuth}>

          {isRegister && (
            <div className="form-group">
              <label>Full Name</label>

              <input
                type="text"
                placeholder="Enter your name"
                value={name}
                onChange={(e) =>
                  setName(e.target.value)
                }
                required
              />
            </div>
          )}

          <div className="form-group">
            <label>Email</label>

            <input
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) =>
                setEmail(e.target.value)
              }
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>

            <input
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) =>
                setPassword(e.target.value)
              }
              required
            />
          </div>

          <button
            className="submit-button"
            type="submit"
            disabled={loading}
          >
            {loading
              ? "Please wait..."
              : isRegister
              ? "Create Account"
              : "Sign In"}
          </button>

        </form>

        {message && (
          <div className="message">
            {message}
          </div>
        )}

        <div className="footer-text">
          Secure authentication • AI-assisted underwriting
        </div>

      </div>
    </div>
  );
}

export default App;