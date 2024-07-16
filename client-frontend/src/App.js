import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import NavbarComponent from "./components/Navbar";
import ProductList from "./components/ProductList";
import OrdersPage from "./pages/Orders";

const Login = () => {
	useEffect(() => {
		window.location.href = `/oauth2/authorization/google`;
	}, []);
	return <div>Redirecting to login...</div>;
};

const Home = ({ isAuthenticated, client }) => (
	<div className="container mt-4">
		<ProductList isAuthenticated={isAuthenticated} client={client} />
	</div>
);

const App = () => {
	const [isAuthenticated, setIsAuthenticated] = useState(false);
	const [client, setClient] = useState(null);

	useEffect(() => {
		const checkAuth = async () => {
			try {
				const response = await fetch(
					`https://cpbackend.azurewebsites.net/api/clients/me/`,
					{
						credentials: "include",
					}
				);
				if (response.ok) {
					const data = await response.json();
					setIsAuthenticated(true);
					setClient(data);
				} else {
					setIsAuthenticated(false);
				}
			} catch (error) {
				setIsAuthenticated(false);
			}
		};

		checkAuth();
	}, []);

	return (
		<Router>
			<NavbarComponent isAuthenticated={isAuthenticated} client={client} />
			<Routes>
				<Route
					path="/"
					element={<Home isAuthenticated={isAuthenticated} client={client} />}
				/>
				<Route
					path="/orders"
					element={
						<OrdersPage isAuthenticated={isAuthenticated} client={client} />
					}
				/>
				<Route path="/login" element={<Login />} />
			</Routes>
		</Router>
	);
};

export default App;
