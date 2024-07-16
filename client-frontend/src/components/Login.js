import React from "react";
import { useGoogleLogin } from "@react-oauth/google";

const Login = () => {
	const login = useGoogleLogin({
		clientId: process.env.REACT_APP_GOOGLE_CLIENT_ID,
		onSuccess: (tokenResponse) => {
			console.log(tokenResponse);
			// Send access token to your backend for authentication
		},
		onError: (error) => {
			console.error(error);
		},
	});

	return <button onClick={login}>Login with Google</button>;
};

export default Login;
