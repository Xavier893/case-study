import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ProductList = ({ isAuthenticated }) => {
	const [products, setProducts] = useState([]);
	const [client, setClient] = useState(null);
	const [showToast, setShowToast] = useState(false);
	const [toastMessage, setToastMessage] = useState("");
	const navigate = useNavigate();

	useEffect(() => {
		const fetchClientAndProducts = async () => {
			try {
				const clientResponse = await axios.get(
					`${process.env.REACT_APP_API_URL}/api/clients/me`,
					{ withCredentials: true }
				);
				if (clientResponse.status === 200) {
					setClient(clientResponse.data);
					const productsResponse = await axios.get(
						`${process.env.REACT_APP_API_URL}/api/products`
					);
					const productsData = await productsResponse.data;
					setProducts(productsData);
				} else {
					setClient(null);
				}
			} catch (error) {
				console.error("Error fetching client or products:", error);
				setClient(null);
			}
		};
		fetchClientAndProducts();
	}, []);

	const handleAddToOrder = async (productId, productName) => {
		if (!isAuthenticated) {
			navigate("/login");
			return;
		}

		try {
			// Check if there is an existing order for the client
			const ordersResponse = await axios.get(
				`${process.env.REACT_APP_API_URL}/api/orders/client/${client.id}`,
				{ withCredentials: true }
			);
			const orders = ordersResponse.data;

			if (orders.length > 0) {
				// There is an existing order, update it
				const existingOrder = orders[0]; // Assuming only one open order at a time
				const updatedOrderItems = [...existingOrder.orderItems];
				const existingItem = updatedOrderItems.find(
					(item) => item.productId === productId
				);

				if (existingItem) {
					existingItem.quantity += 1;
				} else {
					updatedOrderItems.push({ productId, quantity: 1 });
				}

				const updatedOrder = {
					...existingOrder,
					orderItems: updatedOrderItems,
				};

				await axios.put(
					`${process.env.REACT_APP_API_URL}/api/orders/${existingOrder.id}`,
					updatedOrder,
					{ withCredentials: true }
				);
				setToastMessage(`${productName} added to existing order`);
				setShowToast(true);
				setTimeout(() => setShowToast(false), 3000); // Hide toast after 3 seconds
			} else {
				// No existing order, create a new one
				const newOrder = {
					clientId: client.id,
					orderDate: new Date().toISOString(),
					orderItems: [{ productId, quantity: 1 }],
				};

				const response = await axios.post(
					`${process.env.REACT_APP_API_URL}/api/orders`,
					newOrder,
					{
						withCredentials: true,
					}
				);
				if (response.status === 201) {
					setToastMessage(`${productName} added to new order`);
					setShowToast(true);
					setTimeout(() => setShowToast(false), 3000); // Hide toast after 3 seconds
				} else {
					console.error("Failed to add product to new order");
				}
			}
		} catch (error) {
			console.error("Error adding product to order:", error);
		}
	};

	return (
		<div className="container mt-4">
			<div className="row">
				{products.map((product) => (
					<div className="col-md-3 mb-4" key={product.id}>
						<div className="card h-100 text-center">
							<div className="card-body">
								<h5 className="card-title">{product.name}</h5>
								<p className="card-text">Price: R{product.price.toFixed(2)}</p>
								<button
									className="btn btn-primary"
									onClick={() => handleAddToOrder(product.id, product.name)}
								>
									Add to Order
								</button>
							</div>
						</div>
					</div>
				))}
			</div>
			<div
				className="toast-container position-fixed bottom-0 end-0 p-3"
				style={{ zIndex: 9999 }}
			>
				<div
					className={`toast ${showToast ? "show" : "hide"}`}
					role="alert"
					aria-live="assertive"
					aria-atomic="true"
				>
					<div className="toast-header">
						<strong className="me-auto">Notification</strong>
						<button
							type="button"
							className="btn-close"
							onClick={() => setShowToast(false)}
						></button>
					</div>
					<div className="toast-body">{toastMessage}</div>
				</div>
			</div>
		</div>
	);
};

export default ProductList;
