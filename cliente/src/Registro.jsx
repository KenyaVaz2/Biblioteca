import { useState } from 'react';
// 1. ¡IMPORTANTE! Esta línea conecta el diseño con el código
import './styles/registro.css'; 

function Registro({ onSwitchToLogin }) { 
    const [formData, setFormData] = useState({
        nombre: '',
        correo: '',
        password: '',
        confirmarPassword: ''
    });

    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Validación simple en el cliente
        if (formData.password !== formData.confirmarPassword) {
            setError("Las contraseñas no coinciden ❌");
            return;
        }
        setError(""); // Limpiar errores previos

        try {
            // Asegúrate de que esta URL sea la correcta de tu Backend
            const response = await fetch('http://localhost:8080/api/usuarios', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    nombre: formData.nombre,
                    correo: formData.correo,
                    password: formData.password
                    // No enviamos confirmarPassword al backend
                }),
            });

            if (response.ok) {
                alert("¡Registro exitoso! Ahora inicia sesión.");
                onSwitchToLogin(); // Volver al login
            } else {
                // Si el backend devuelve un error (ej. "Correo ya existe")
                const msg = await response.text();
                setError("Error al registrar: " + msg);
            }

        } catch (err) {
            setError("Error de conexión con el servidor.");
        }
    };

    return (
        // 2. USO DE CLASES CSS: Fíjate cómo usamos 'registro-container', 'registro-card', etc.
        <div className="registro-container">
            <form onSubmit={handleSubmit} className="registro-card">
                <h2>Crear Cuenta</h2>
                
                <div className="form-group">
                    <label className="form-label">Nombre Completo</label>
                    <input 
                        type="text" name="nombre" 
                        className="form-input" // <-- Clase personalizada
                        required onChange={handleChange} 
                    />
                </div>

                <div className="form-group">
                    <label className="form-label">Correo Electrónico</label>
                    <input 
                        type="email" name="correo" 
                        className="form-input" 
                        required onChange={handleChange} 
                    />
                </div>

                <div className="form-group">
                    <label className="form-label">Contraseña</label>
                    <input 
                        type="password" name="password" 
                        className="form-input" 
                        required onChange={handleChange} 
                    />
                </div>

                <div className="form-group">
                    <label className="form-label">Confirmar Contraseña</label>
                    <input 
                        type="password" name="confirmarPassword" 
                        className="form-input" 
                        required onChange={handleChange} 
                    />
                    {/* Mensaje de error en rojo */}
                    {error && <div className="error-text">{error}</div>}
                </div>

                <button type="submit" className="btn-registro">
                    Registrarse
                </button>

                {/* Separador */}
                <hr style={{ margin: '25px 0', border: '0', borderTop: '1px solid #eee' }} />
                
                <div style={{ textAlign: 'center' }}>
                    <p style={{ display: 'inline', marginRight: '10px', color: '#666' }}>
                        ¿Ya tienes cuenta?
                    </p>
                    <button 
                        type="button" 
                        className="link-login" 
                        onClick={onSwitchToLogin}
                    >
                        Iniciar Sesión
                    </button>
                </div>
            </form>
        </div>
    );
}

export default Registro;