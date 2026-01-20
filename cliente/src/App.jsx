import { useState, useEffect } from 'react';
import BuscadorLibros from './BuscadorLibros';
import Login from './Login';
import Registro from './Registro';
import Perfil from './Perfil'; 
import PanelBibliotecario from './PanelBibliotecario';
import './styles/buscador.css'; 

function App() {
    const [usuario, setUsuario] = useState(null);
    const [mostrarRegistro, setMostrarRegistro] = useState(false);
    const [vistaActual, setVistaActual] = useState('buscador'); 

    useEffect(() => {
        const usuarioGuardado = localStorage.getItem('usuario');
        if (usuarioGuardado) {
            setUsuario(JSON.parse(usuarioGuardado));
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('usuario');
        setUsuario(null);
        setMostrarRegistro(false);
        setVistaActual('buscador'); 
    };

    if (!usuario) {
        if (mostrarRegistro) return <Registro onSwitchToLogin={() => setMostrarRegistro(false)} />;
        return <Login onLoginSuccess={(u) => { 
            localStorage.setItem('usuario', JSON.stringify(u)); 
            setUsuario(u); 
        }} onSwitchToRegister={() => setMostrarRegistro(true)} />;
    }

    return (
        <div className="app-container">
            <nav className='navbar' style={{ padding: '10px 20px', background: '#fff', boxShadow: '0 2px 5px rgba(0,0,0,0.1)', display: 'flex', justifyContent: 'space-between' }}>
                
                {/* LADO IZQUIERDO: Logo y Botones de Navegación */}
                <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
                    <h3 style={{ margin: 0, color: '#0066cc' }}>Biblioteca</h3>
                    
                    {/* BOTÓN BUSCADOR */}
                    <button 
                        onClick={() => setVistaActual('buscador')}
                        className={`btn ${vistaActual === 'buscador' ? 'btn-primary' : 'btn-outline-primary'}`}
                        style={{ border: 'none', background: vistaActual === 'buscador' ? '#e7f1ff' : 'transparent', color: '#0066cc', fontWeight: 'bold' }}
                    >
                        Buscar Libros
                    </button>
                    
                    {/* BOTÓN PERFIL */}
                    <button 
                        onClick={() => setVistaActual('perfil')}
                        className={`btn ${vistaActual === 'perfil' ? 'btn-primary' : 'btn-outline-primary'}`}
                        style={{ border: 'none', background: vistaActual === 'perfil' ? '#e7f1ff' : 'transparent', color: '#0066cc', fontWeight: 'bold' }}
                    >
                        Mi Perfil
                    </button>

                    {/* BOTÓN ADMIN (CORREGIDO Y MOVIDO AQUÍ DENTRO) */}
                    {usuario.rol === 'ADMIN' && (
                        <button
                            onClick={() => setVistaActual('admin')}
                            className={`btn ${vistaActual === 'admin' ? 'btn-primary' : 'btn-outline-primary'}`}
                            style={{ border: 'none', background: vistaActual === 'admin' ? '#e7f1ff' : 'transparent', color: '#0066cc', fontWeight: 'bold' }}>
                                Gestión (Admin)
                        </button>
                    )}
                </div>

                {/* LADO DERECHO: Info Usuario y Logout */}
                <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                    <span className='rol-badge'>{usuario.rol}</span>
                    <span>Hola, <strong>{usuario.nombre}</strong></span>
                    <button onClick={handleLogout} className='logout-btn'>Salir</button>
                </div>
            </nav>

            {/* CONTENIDO CAMBIANTE */}
            <div className="contenido-principal" style={{ padding: '20px' }}>
                {vistaActual === 'buscador' && <BuscadorLibros usuario={usuario} />}
                {vistaActual === 'perfil' && <Perfil usuario={usuario} />}
                
                {/* MOVIDO AQUÍ DENTRO para respetar el padding */}
                {usuario.rol === 'ADMIN' && vistaActual === 'admin' && (
                    <PanelBibliotecario />
                )}
            </div>

        </div>
    );
}

export default App;