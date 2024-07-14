import React, { useState, useEffect } from "react";
import Navbar from "../components/Navbar";
import ProductList from "../components/ProductList";

const HomePage = ({ isAuthenticated, client }) => {
	const [isAuthenticated, setIsAuthenticated] = useState(false);

	useEffect(() => {
		const checkAuth = async () => {
			try {
				const response = await fetch(
					`${process.env.REACT_APP_API_URL}/api/clients/me`,
					{
						credentials: "include",
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
