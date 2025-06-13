"use client"
import { useState } from "react";
import { useRouter } from "next/navigation";
import "./login.css";

export default function Login() {
    const [userId, setUserId] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);
    const router = useRouter();

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setSuccess("");

        if (!userId || !password) {
            setError("Por favor, preencha todos os campos.");
            return;
        }

        if (userId !== "123456" || password !== "admin1234") {
            setError("ID ou senha incorretos.");
            return;
        }

        setLoading(true);

        setTimeout(() => {
            setSuccess("Login realizado com sucesso!");
            setLoading(false);
            localStorage.setItem("isLoggedIn", "true");
            router.push("/mapa");
        }, 2000);
    };


    return (
        <form className="login-form" onSubmit={handleSubmit}>
            <div className="form-heading">
                <span className="logo-icon">
                    <i className="fas fa-route"></i>
                </span>
                <span className="logo-text">EcoRota</span>
            </div>
            {error && <div className="error-message">{error}</div>}
            {success && <div className="success-message">{success}</div>}
            <div className="form-group">
                <label className="label">ID</label>
                <input
                    type="number"
                    name="number"
                    placeholder="Informe seu ID"
                    value={userId}
                    onChange={(e) => setUserId(e.target.value)}
                />
            </div>
            <div className="form-group">
                <label className="label">Password</label>
                <input
                    type="password"
                    name="password"
                    placeholder="Informe sua senha"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
            </div>
            <div className="forgot-password">
                <a href="#">Esqueceu sua senha?</a>
            </div>
            <button className="submit" type="submit" disabled={loading}>
                {loading ? "Carregando..." : "Login"}
            </button>

        </form>
    );
}