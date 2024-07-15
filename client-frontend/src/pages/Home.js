import React, { useState, useEffect } from "react";
import Navbar from "../components/Navbar";
import ProductList from "../components/ProductList";

const HomePage = ({ isAuthenticated, client }) => {
	const [isAuthenticated, setIsAuthenticated] = useState(false);

	useEffect(() => {
		const checkAuth = async () => {
			try {
				const response = await fetch(
					`https://cpbackend.azurewebsites.net/api/clients/me`,
					{
						credentials: "include",
						headers: {
							"Content-Type": "application/json",
						},
					}
				);
				if (response.ok) {
					setIsAuthenticated(true);
				} else {
					setIsAuthenticated(false);
				}
			} catch (error) {
				console.error("Failed to fetch:", error);
				setIsAuthenticated(false);
			}
		};
		checkAuth();
	}, []);

	return (
		<div>
			<Navbar isAuthenticated={isAuthenticated} />
			<div className="container mt-4">
				<ProductList />
			</div>
		</div>
	);
};

export default HomePage;
