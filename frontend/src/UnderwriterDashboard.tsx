import { useEffect, useState } from "react";

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

function UnderwriterDashboard() {
  const [applications, setApplications] = useState<Application[]>([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  const underwriterName =
    localStorage.getItem("name") || "Underwriter";

  useEffect(() => {
    fetchApplications();
  }, []);

  const fetchApplications = async () => {
    const token = localStorage.getItem("token");

    if (!token) {
      setMessage("Please login again.");
      return;
    }

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
      setMessage(
        error instanceof Error
          ? error.message
          : "Something went wrong"
      );
    } finally {
      setLoading(false);
    }
  };

  const updateStatus = async (
    applicationId: number,
    status: string
  ) => {
    const token = localStorage.getItem("token");

    if (!token) {
      return;
    }

    try {
      const response = await fetch(
        `${API_URL}/api/applications/${applicationId}/status`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            status,
          }),
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(
          data.message ||
            data.error ||
            "Unable to update status"
        );
      }

      setMessage(
        `Application #${applicationId} updated to ${status}`
      );

      await fetchApplications();
    } catch (error) {
      setMessage(
        error instanceof Error
          ? error.message
          : "Unable to update application"
      );
    }
  };

  const logout = () => {
    localStorage.clear();
    window.location.reload();
  };

  const submittedCount = applications.filter(
    (application) => application.status === "SUBMITTED"
  ).length;

  const reviewCount = applications.filter(
    (application) => application.status === "UNDER_REVIEW"
  ).length;

  const approvedCount = applications.filter(
    (application) => application.status === "APPROVED"
  ).length;

  const rejectedCount = applications.filter(
    (application) => application.status === "REJECTED"
  ).length;

  return (
    <div className="dashboard-page">

      <header className="dashboard-header">
        <div>
          <h1>CredScope</h1>
          <p>AI-Powered Credit Risk Assessment</p>
        </div>

        <div className="header-right">
          <span>
            Hi, {underwriterName}
          </span>

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
            <h2>Underwriter Dashboard</h2>

            <p>
              Review loan applications and AI-assisted
              risk assessments.
            </p>
          </div>
        </div>

        {message && (
          <div className="dashboard-message">
            {message}
          </div>
        )}

        <section className="stats-grid">

          <div className="stat-card">
            <span>Total Applications</span>
            <strong>
              {applications.length}
            </strong>
          </div>

          <div className="stat-card">
            <span>Submitted</span>
            <strong>
              {submittedCount}
            </strong>
          </div>

          <div className="stat-card">
            <span>Under Review</span>
            <strong>
              {reviewCount}
            </strong>
          </div>

          <div className="stat-card">
            <span>Approved</span>
            <strong>
              {approvedCount}
            </strong>
          </div>

        </section>

        <section className="applications-section">

          <h3>Loan Applications</h3>

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
                        Applicant:{" "}
                        {application.applicantName}
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
                        ₹{" "}
                        {application.loanAmount.toLocaleString(
                          "en-IN"
                        )}
                      </strong>
                    </div>

                    <div>
                      <span>Risk Score</span>

                      <strong>
                        {application.riskScore ??
                          "Pending"}
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

                    <h5>
                      Risk Factors
                    </h5>

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

                      <h5>
                        🤖 AI Risk Explanation
                      </h5>

                      <p>
                        {application.aiRiskExplanation}
                      </p>

                    </div>

                  )}

                  {application.status ===
                    "SUBMITTED" && (

                    <div
                      style={{
                        marginTop: "20px",
                        display: "flex",
                        gap: "10px",
                      }}
                    >

                      <button
                        className="primary-button"
                        onClick={() =>
                          updateStatus(
                            application.id,
                            "UNDER_REVIEW"
                          )
                        }
                      >
                        Start Review
                      </button>

                    </div>
                  )}

                  {application.status ===
                    "UNDER_REVIEW" && (

                    <div
                      style={{
                        marginTop: "20px",
                        display: "flex",
                        gap: "10px",
                      }}
                    >

                      <button
                        className="primary-button"
                        onClick={() =>
                          updateStatus(
                            application.id,
                            "APPROVED"
                          )
                        }
                      >
                        Approve
                      </button>

                      <button
                        className="logout-button"
                        onClick={() =>
                          updateStatus(
                            application.id,
                            "REJECTED"
                          )
                        }
                      >
                        Reject
                      </button>

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

export default UnderwriterDashboard;