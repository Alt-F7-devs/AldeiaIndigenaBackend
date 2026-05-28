import { useState } from "react";

function Login() {
    const [usuario, setUsuario] = useState("");
    const [senha, setSenha] = useState("");

    async function entrar() {
        if (usuario === "" || senha === "") {
            alert("Preencha todos os campos!");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/auth/loginprofessor", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    cpf: usuario, // 👈 aqui você manda como cpf
                    senha: senha,
                }),
            });

            if (!response.ok) {
                throw new Error("Credenciais inválidas");
            }

            const data = await response.json();

            console.log(data);

            // 🔐 salva o tipo
            localStorage.setItem("tipo", data.tipo);

            alert("Login realizado!");

            // 🔀 redirecionamento
            if (data.tipo === "ADMIN") {
                window.location.href = "/admin";
            } else {
                window.location.href = "/professor";
            }

        } catch (error) {
            alert(error.message);
        }
    }

    return (
        <div>
            <h2>Login</h2>

            <input
                type="text"
                placeholder="CPF"
                value={usuario}
                onChange={(e) => setUsuario(e.target.value)}
            />
            <br /><br />

            <input
                type="password"
                placeholder="Senha"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
            />
            <br /><br />

            <button onClick={entrar}>Entrar</button>
        </div>
    );
}

export default Login;