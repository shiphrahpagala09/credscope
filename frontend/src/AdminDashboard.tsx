import { useEffect, useState } from "react";

const API_URL = "http://localhost:8080";

type Application = {
  id: number;
  applicantName: string;
  loanAmount: number;
  purpose: string;
  riskScore: number | null;
  riskCategory: string | null;
  status: string;
  createdAt: string;
};

function AdminDashboard() {
  const [applications, setApplications] = useState<Application[]>([]);
  const [loading, setLoading] = useState(true);

  const adminName = localStorage.getItem("name") || "Admin";

  useEffect(() => {
    loadApplications();
  }, []);

  const loadApplications = async () => {
    const token = localStorage.getItem("token");

    try {
      const response = await fetch(
        `${API_URL}/api/applications`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Unable to load applications");
      }

      const data = await response.json();
      setApplications(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.clear();
    window.location.reload();
  };

  const total = applications.length;

  const approved = applications.filter(
    (a) => a.status === "APPROVED"
  ).length;

  const rejected = applications.filter(
    (a) => a.status === "REJECTED"
  ).length;

  const underReview = applications.filter(
    (a) => a.status === "UNDER_REVIEW"
  ).length;

  const submitted = applications.filter(
    (a) => a.status === "SUBMITTED"
  ).length;

  const assessedApplications = applications.filter(
    (a) => a.riskScore !== null
  );

  const averageRisk =
    assessedApplications.length > 0
      ? Math.round(
          assessedApplications.reduce(
            (sum, a) => sum + (a.riskScore || 0),
            0
          ) / assessedApplications.length
        )
      : 0;

  const totalLoanAmount = applications.reduce(
    (sum, a) => sum + a.loanAmount,
    0
  );

  return (
    <div className="dashboard-page">

      <header className="dashboard-header">

        <div>
          <h1>CredScope</h1>
          <p>AI-Powered Credit Risk Assessment</p>
        </div>

        <div className="header-right">
          <span>Hi, {adminName}</span>

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
            <h2>Admin Dashboard</h2>

            <p>
              Monitor the complete loan portfolio and
              risk assessment activity.
            </p>
          </div>

        </div>

        <section className="stats-grid">

          <div className="stat-card">
            <span>Total Applications</span>
            <strong>{total}</strong>
          </div>

          <div className="stat-card">
            <span>Under Review</span>
            <strong>{underReview}</strong>
          </div>

          <div className="stat-card">
            <span>Approved</span>
            <strong>{approved}</strong>
          </div>

          <div className="stat-card">
            <span>Rejected</span>
            <strong>{rejected}</strong>
          </div>

          <div className="stat-card">
            <span>Submitted</span>
            <strong>{submitted}</strong>
          </div>

          <div className="stat-card">
            <span>Average Risk Score</span>
            <strong>{averageRisk}</strong>
          </div>

          <div className="stat-card">
            <span>Total Loan Value</span>
            <strong>
              ₹{totalLoanAmount.toLocaleString("en-IN")}
            </strong>
          </div>

        </section>

        <section className="applications-section">

          <h3>Loan Portfolio</h3>

          {loading ? (
            <div className="empty-state">
              Loading applications...
            </div>
          ) : applications.length === 0 ? (
            <div className="empty-state">
              No applications found.
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
                        Applicant: {application.applicantName}
                      </p>

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
                        ₹{application.loanAmount.toLocaleString(
                          "en-IN"
                        )}
                      </strong>
                    </div>

                    <div>
                      <span>Risk Score</span>
                      <strong>
                        {application.riskScore ?? "N/A"}
                      </strong>
                    </div>

                    <div>
                      <span>Risk Category</span>
                      <strong>
                        {application.riskCategory ?? "N/A"}
                      </strong>
                    </div>

                  </div>

                </div>

              ))}

            </div>
          )}

        </section>

      </main>

    </div>
  );
}

export default AdminDashboard;