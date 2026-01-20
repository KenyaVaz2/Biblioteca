import { useState, useEffect } from 'react';
import axios from 'axios';
import './styles/perfil.css'; // (Crearemos este CSS en el paso 3)

const Perfil = ({ usuario }) => {
    const [prestamos, setPrestamos] = useState([]);
    const [multas, setMultas] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const cargarDatos = async () => {
            try {
                // 1. Cargar Préstamos Activos
                // Endpoint que ya tenías: /api/prestamos/usuario/{id}
                const resPrestamos = await axios.get(`http://localhost:8080/api/prestamos/usuario/${usuario.idUsuario}`);
                setPrestamos(resPrestamos.data);

                // 2. Cargar Multas (Endpoint nuevo del Paso 1)
                const resMultas = await axios.get(`http://localhost:8080/api/multas/usuario/${usuario.idUsuario}`);
                setMultas(resMultas.data);

            } catch (error) {
                console.error("Error cargando perfil:", error);
            } finally {
                setLoading(false);
            }
        };

        if (usuario) {
            cargarDatos();
        }
    }, [usuario]);

    // Calcular si tiene multas pendientes (Bloqueo)
    const tieneDeudaPendiente = multas.some(m => m.estadoPago === 'Pendiente');
    const totalDeuda = multas
        .filter(m => m.estadoPago === 'Pendiente')
        .reduce((sum, m) => sum + m.monto, 0);

    if (loading) return <div className="text-center mt-5">Cargando perfil...</div>;

    return (
        <div className="perfil-container container mt-4">
            
            {/* ENCABEZADO */}
            <div className="perfil-header mb-4">
                <h2>Mi Perfil</h2>
                <p className="text-muted">Gestión de préstamos y estado de cuenta</p>
                
                {tieneDeudaPendiente && (
                    <div className="alert alert-danger">
                        <strong>⚠️ ATENCIÓN:</strong> Tienes multas pendientes por <strong>${totalDeuda}</strong>. 
                        No podrás solicitar nuevos libros hasta regularizar tu situación con el bibliotecario.
                    </div>
                )}
            </div>

            <div className="row">
                
                {/* SECCIÓN 1: MIS PRÉSTAMOS */}
                <div className="col-md-12 mb-5">
                    <div className="card shadow-sm">
                        <div className="card-header bg-primary text-white">
                            <h5 className="mb-0">📚 Libros en mi poder</h5>
                        </div>
                        <div className="card-body">
                            {prestamos.length === 0 ? (
                                <p>No tienes préstamos activos actualmente.</p>
                            ) : (
                                <table className="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID Préstamo</th>
                                            <th>ID Ejemplar</th>
                                            <th>Fecha Inicio</th>
                                            <th>Fecha Límite</th>
                                            <th>Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {prestamos.map(p => (
                                            <tr key={p.idPrestamo}>
                                                <td>#{p.idPrestamo}</td>
                                                <td>Libro #{p.idEjemplar}</td>
                                                <td>{p.fechaInicio}</td>
                                                {/* Resaltar fecha en rojo si ya pasó */}
                                                <td style={{ color: new Date(p.fechaDevolucion) < new Date() ? 'red' : 'black', fontWeight: 'bold' }}>
                                                    {p.fechaDevolucion}
                                                </td>
                                                <td>
                                                    <span className="badge bg-success">Activo</span>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                </div>

                {/* SECCIÓN 2: HISTORIAL DE MULTAS */}
                <div className="col-md-12">
                    <div className="card shadow-sm border-danger">
                        <div className="card-header bg-danger text-white">
                            <h5 className="mb-0">💰 Multas y Sanciones</h5>
                        </div>
                        <div className="card-body">
                            {multas.length === 0 ? (
                                <p className="text-success">¡Felicidades! Tienes un historial limpio.</p>
                            ) : (
                                <table className="table">
                                    <thead>
                                        <tr>
                                            <th>ID Multa</th>
                                            <th>Monto</th>
                                            <th>Estado</th>
                                            <th>ID Préstamo Asociado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {multas.map(m => (
                                            <tr key={m.idMulta} className={m.estadoPago === 'Pendiente' ? 'table-danger' : 'table-light'}>
                                                <td>#{m.idMulta}</td>
                                                <td>${m.monto}</td>
                                                <td>
                                                    {m.estadoPago === 'Pendiente' 
                                                        ? <span className="badge bg-danger">PENDIENTE</span>
                                                        : <span className="badge bg-secondary">PAGADO</span>
                                                    }
                                                </td>
                                                <td>#{m.prestamo ? m.prestamo.idPrestamo : '-'}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                </div>

            </div>
        </div>
    );
};

export default Perfil;