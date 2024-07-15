import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const OrdersPage = () => {
	const navigate = useNavigate();
	const [orders, setOrders] = useState([]);
	const [client, setClient] = useState(null);
	const [loading, setLoading] = useState(true);
	const [isAuthenticated, setIsAuthenticated] = useState(false);

	useEffect(() => {
		const fetchClientAndOrders = async () => {
			try {
				const clientResponse = await axios.get(
					`https://cpbackend.azurewebsites.net/api/clients/me/api/clients/me`,
					{ withCredentials: true }
				);
				if (clientResponse.status === 200) {
					setClient(clientResponse.data);
					setIsAuthenticated(true);
					const ordersResponse = await axios.get(
						`https://cpbackend.azurewebsites.net/api/clients/me/api/orders/client/${clientResponse.data.id}`,
						{ withCredentials: true }
					);
					const ordersWithProducts = await Promise.all(
						ordersResponse.data.map(async (order) => {
							const orderItemsWithDetails = await Promise.all(
								order.orderItems.map(async (item, index) => {
									const productResponse = await axios.get(
										`https://cpbackend.azurewebsites.net/api/clients/me/api/products/${item.productId}`
									);
									return {
										...item,
										productName: productResponse.data.name,
										productPrice: productResponse.data.price,
										isEditing: false,
										editQuantity: item.quantity,
										tempId: `${order.id}-${item.productId}`, // Generate a unique id for internal use
									};
								})
							);
							return { ...order, orderItems: orderItemsWithDetails };
						})
					);
					setOrders(ordersWithProducts);
					console.log(ordersWithProducts); // Add this line to log the orders data
				} else {
					setIsAuthenticated(false);
					navigate("/login");
				}
			} catch (error) {
				console.error("Error fetching client or orders:", error);
				setIsAuthenticated(false);
				navigate("/login");
			} finally {
				setLoading(false);
			}
		};

		fetchClientAndOrders();
	}, [navigate]);

	const handleEditItem = (orderId, itemTempId) => {
		setOrders((prevOrders) =>
			prevOrders.map((order) =>
				order.id === orderId
					? {
							...order,
							orderItems: order.orderItems.map((item) =>
								item.tempId === itemTempId ? { ...item, isEditing: true } : item
							),
					  }
					: order
			)
		);
	};

	const handleSaveItem = async (orderId, itemTempId) => {
		const updatedOrders = orders.map((order) => {
			if (order.id === orderId) {
				const updatedOrderItems = order.orderItems.map((item) =>
					item.tempId === itemTempId
						? { ...item, quantity: item.editQuantity, isEditing: false }
						: item
				);
				return { ...order, orderItems: updatedOrderItems };
			}
			return order;
		});

		try {
			const updatedOrder = updatedOrders.find((order) => order.id === orderId);
			const orderToSend = {
				...updatedOrder,
				orderItems: updatedOrder.orderItems.map(({ tempId, ...item }) => item), // Remove tempId before sending
			};
			await axios.put(
				`https://cpbackend.azurewebsites.net/api/clients/me/api/orders/${orderId}`,
				orderToSend,
				{ withCredentials: true }
			);
			setOrders(updatedOrders);
		} catch (error) {
			console.error("Error updating order:", error);
		}
	};

	const handleQuantityChange = (orderId, itemTempId, newQuantity) => {
		setOrders((prevOrders) =>
			prevOrders.map((order) =>
				order.id === orderId
					? {
							...order,
							orderItems: order.orderItems.map((item) =>
								item.tempId === itemTempId
									? { ...item, editQuantity: newQuantity }
									: item
							),
					  }
					: order
			)
		);
	};

	const handleRemoveItem = async (orderId, itemTempId) => {
		const updatedOrders = orders.map((order) => {
			if (order.id === orderId) {
				const updatedOrderItems = order.orderItems.filter(
					(item) => item.tempId !== itemTempId
				);
				return { ...order, orderItems: updatedOrderItems };
			}
			return order;
		});

		try {
			const updatedOrder = updatedOrders.find((order) => order.id === orderId);
			const orderToSend = {
				...updatedOrder,
				orderItems: updatedOrder.orderItems.map(({ tempId, ...item }) => item), // Remove tempId before sending
			};
			await axios.put(
				`https://cpbackend.azurewebsites.net/api/clients/me/api/orders/${orderId}`,
				orderToSend,
				{ withCredentials: true }
			);
			setOrders(updatedOrders);
		} catch (error) {
			console.error("Error removing item from order:", error);
		}
	};

	const handleDeleteOrder = async (orderId) => {
		try {
			await axios.delete(
				`https://cpbackend.azurewebsites.net/api/clients/me/api/orders/${orderId}`,
				{
					withCredentials: true,
				}
			);
			setOrders((prevOrders) =>
				prevOrders.filter((order) => order.id !== orderId)
			);
		} catch (error) {
			console.error("Error deleting order:", error);
		}
	};

	const calculateTotalPrice = (orderItems) => {
		return orderItems.reduce(
			(total, item) => total + item.productPrice * item.quantity,
			0
		);
	};

	if (loading) {
		return <div>Loading...</div>;
	}

	if (!isAuthenticated) {
		return <div>You need to log in to view this page.</div>;
	}

	return (
		<div className="container mt-4">
			<h1>My Orders</h1>
			{orders.length === 0 ? (
				<p>No orders found.</p>
			) : (
				<div className="row">
					{orders.map((order) => (
						<div className="col-md-12 mb-4" key={order.id}>
							<div className="card h-100">
								<div className="card-header">
									<h3>
										Total Price: R
										{calculateTotalPrice(order.orderItems).toFixed(2)}
									</h3>
								</div>
								<div className="card-body">
									<table className="table">
										<thead>
											<tr>
												<th>Product Name</th>
												<th>Quantity</th>
												<th>Price</th>
												<th>Actions</th>
											</tr>
										</thead>
										<tbody>
											{order.orderItems.map((item) => (
												<tr key={item.tempId}>
													<td>{item.productName}</td>
													<td>
														{item.isEditing ? (
															<input
																type="number"
																value={item.editQuantity}
																onChange={(e) =>
																	handleQuantityChange(
																		order.id,
																		item.tempId,
																		parseInt(e.target.value, 10)
																	)
																}
																className="form-control form-control-sm"
															/>
														) : (
															item.quantity
														)}
													</td>
													<td>R{item.productPrice.toFixed(2)}</td>
													<td>
														{item.isEditing ? (
															<>
																<button
																	className="btn btn-success btn-sm me-2"
																	onClick={() =>
																		handleSaveItem(order.id, item.tempId)
																	}
																>
																	Save
																</button>
																<button
																	className="btn btn-danger btn-sm"
																	onClick={() =>
																		handleRemoveItem(order.id, item.tempId)
																	}
																>
																	Remove
																</button>
															</>
														) : (
															<button
																className="btn btn-primary btn-sm"
																onClick={() =>
																	handleEditItem(order.id, item.tempId)
																}
															>
																Edit
															</button>
														)}
													</td>
												</tr>
											))}
										</tbody>
									</table>
								</div>
								<div className="card-footer d-flex justify-content-between">
									<span>
										Order Date: {new Date(order.orderDate).toLocaleDateString()}
									</span>
									<div>
										<button
											className="btn btn-danger"
											onClick={() => handleDeleteOrder(order.id)}
										>
											Delete Order
										</button>
									</div>
								</div>
							</div>
						</div>
					))}
				</div>
			)}
		</div>
	);
};

export default OrdersPage;
