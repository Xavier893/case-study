import React from "react";

const Login = () => {
	const googleLogin = () => {
		window.location.href = `${process.env.REACT_APP_API_URL}/oauth2/authorization/google`;
	};

	return (
		<div className="container">
			<h2>Login</h2>
			<button onClick={googleLogin} className="btn btn-primary">
				Login with Google
			</button>
		</div>
	);
};

export default Login;
