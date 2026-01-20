import { useState } from 'react';
import axios from 'axios';
import './styles/login.css'; 

// 1. CAMBIO AQUÍ: Agregamos 'onSwitchToRegister'
const Login = ({ onLoginSuccess, onSwitchToRegister }) => {
    const [correo, setCorreo] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(null);

        try {
            const res = await axios.post('http://localhost:8080/api/usuarios/login', {
                correo: correo,
                password: password 
            });

            if (res.data) {
                localStorage.setItem('usuario', JSON.stringify(res.data));
                onLoginSuccess(res.data);
            }
        } catch (err) {
            console.error(err);
            setError("Credenciales incorrectas o error de conexión.");
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h2>Bienvenido a la Biblioteca</h2>
                <p>Por favor, inicia sesión para continuar</p>
                
                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label>Correo </label>
                        <input 
                            type="email" 
                            placeholder="ejemplo@ipn.mx"
                            value={correo}
                            onChange={(e) => setCorreo(e.target.value)}
                            required 
                        />
                    </div>

                    <div className="form-group">
                        <label>Contraseña</label>
                        <input 
                            type="password" 
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required 
                        />
                    </div>

                    {error && <div className="error-alert">{error}</div>}

                    <button type="submit" className="btn-login">Ingresar</button>
                </form>

                {/* 2. CAMBIO AQUÍ: Sección para ir al Registro */}
                <div style={{ marginTop: '20px', borderTop: '1px solid #eee', paddingTop: '15px', textAlign: 'center' }}>
                    <p style={{ marginBottom: '10px', fontSize: '0.9rem' }}>¿No tienes una cuenta?</p>
                    <button 
                        type="button" 
                        onClick={onSwitchToRegister}
                        style={{ 
                            background: 'none', 
                            border: '1px solid #0066cc', 
                            color: '#0066cc', 
                            padding: '5px 15px',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '0.9rem'
                        }}
                    >
                        Crear cuenta nueva
                    </button>
                </div>

            </div>
        </div>
    );
};

export default Login;