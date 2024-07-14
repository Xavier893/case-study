import React, { useState, useEffect } from "react";
import axios from "axios";

const ClientInfo = () => {
	const [client, setClient] = useState(null);

	useEffect(() => {
		axios
			.get("${process.env.REACT_APP_API_URL}/api/clients/me", {
				withCredentials: true,
			})
			.then((response) => {
				setClient(response.data);
			})
			.catch((error) => console.error("Error fetching client info:", client));
	}, []);

	if (!client) {
		return <div>Loading client info...</div>;
	}

	return (
		<div>
			<h1>Client Name: {client.name}</h1>
			<h2>Client Email: {client.email}</h2>
			<h2>Client Id: {client.id}</h2>
		</div>
	);
};

export default ClientInfo;
