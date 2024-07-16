import React from "react";
import { Link, useNavigate } from "react-router-dom";

const NavbarComponent = ({ isAuthenticated, client }) => {
	const navigate = useNavigate();

	const handleLogout = async () => {
		try {
			const response = await fetch(
				`https://cpbackend.azurewebsites.net/api/clients/me/logout`,
				{
					method: "POST",
					credentials: "include",
					headers: {
						"Content-Type": "application/json",
					},
				}
			);
			if (response.ok) {
				navigate("/");
				window.location.reload();
			} else {
				console.error("Logout failed");
			}
		} catch (error) {
			console.error("Logout failed", error);
		}
	};

	const handleLogin = () => {
		window.location.href = "/oauth2/authorization/google";
	};

	return (
		<nav className="navbar navbar-light bg-body-secondary">
			<div className="container d-flex justify-content-between align-items-center">
				<Link className="navbar-brand" to="/">
					Home
				</Link>
				<div className="d-flex align-items-center">
					<ul className="navbar-nav me-auto mb-2 mb-lg-0 d-flex align-items-center">
						<li className="nav-item me-3">
							<Link className="nav-link" to="/orders">
								Orders
							</Link>
						</li>
					</ul>
					{isAuthenticated ? (
						<button className="btn btn-outline-danger" onClick={handleLogout}>
							Logout
						</button>
					) : (
						<button className="btn btn-outline-success" onClick={handleLogin}>
							Login
						</button>
					)}
				</div>
			</div>
		</nav>
	);
};

export default NavbarComponent;
