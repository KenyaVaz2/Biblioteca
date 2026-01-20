import { useState } from 'react';
import axios from 'axios';
import './styles/buscador.css';

const BuscadorLibros = ({usuario}) => {

    const [query, setQuery] = useState('');
    const [tipoBusqueda, setTipoBusqueda] = useState('titulo'); 
    const [libros, setLibros] = useState([]);
    const [error, setError] = useState(null);
    const [cargando, setCargando] = useState(false);
    const [libroSeleccionado, setLibroSeleccionado] = useState(null);
    const [mostrarModal, setMostrarModal] = useState(false);
    const [ejemplares, setEjemplares] = useState([]);
    const [cargandoEjemplares, setCargandoEjemplares] = useState(false);


    // Función para búsqueda local (Título, Autor, Género, ISBN)
    const manejarBusquedaLocal = async (e) => {
        e.preventDefault();
        
        // --- CAMBIO AQUÍ: Limpiamos espacios (trim) para arreglar el ISBN ---
        const terminoBusqueda = query.trim();

        if (!terminoBusqueda) return;

        setCargando(true);
        setError(null);
        
        let url = `http://localhost:8080/api/libros/buscar/${tipoBusqueda}?`;
        
        const paramName = tipoBusqueda === 'autor' ? 'nombreAutor' : tipoBusqueda;
        
        // Usamos la variable limpia 'terminoBusqueda' en lugar de 'query'
        url += `${paramName}=${terminoBusqueda}`;

        try {
            const res = await axios.get(url);
            // Manejamos si devuelve un Array (varios) o un Objeto único (ISBN)
            const data = Array.isArray(res.data) ? res.data : [res.data];
            setLibros(data);
        } catch (err) {
            setError('No se encontraron libros en la biblioteca local.');
            setLibros([]);
        } finally {
            setCargando(false);
        }
    };

    const manejarBusquedaExterna = async () => {
        if (!query) {
            alert("Por favor, ingresa un título para la búsqueda externa");
            return;
        }

        setCargando(true);
        setError(null);

        try {
            const res = await axios.get(`http://localhost:8080/api/libros/buscar/interbibliotecario?titulo=${query}`);
            setLibros(res.data);
        } catch (err) {
            setError(err.response?.data || 'Error en la búsqueda externa.');
            setLibros([]);
        } finally {
            setCargando(false);
        }
    };

    const verDetalles = async (libro) => {
        setLibroSeleccionado(libro);
        setMostrarModal(true);
        setEjemplares([]); 
    
        if (libro.fuente) return; 

        setCargandoEjemplares(true);
        try {
            const id = libro.idLibro || libro.id; 
            const res = await axios.get(`http://localhost:8080/api/ejemplares/libro/${id}`);
            setEjemplares(res.data);
        } catch (err) {
            console.error("Error cargando ejemplares:", err);
        } finally {
            setCargandoEjemplares(false);
        }
    };

    // Función para procesar el préstamo
    const manejarSolicitudPrestamo = async (idEjemplar) => {
        if (!usuario || !usuario.idUsuario) {
            alert("Error de sesión. Por favor recarga la página.");
            return;
        }

        const hoy = new Date();
        const devolucion = new Date();
        devolucion.setDate(hoy.getDate() + 7); // 7 días de préstamo

        const fechaInicioStr = hoy.toISOString().split('T')[0];
        const fechaDevolucionStr = devolucion.toISOString().split('T')[0];

        // 3. Preparar el objeto para enviar
        const prestamoDTO = {
            fechaInicio: fechaInicioStr,
            fechaDevolucion: fechaDevolucionStr,
            idUsuario: usuario.idUsuario, 
            idEjemplar: idEjemplar
        };

        try {
            // 4. Enviar petición al Backend
            await axios.post('http://localhost:8080/api/prestamos', prestamoDTO);
            
            // 5. Éxito
            alert("✅ Préstamo realizado con éxito. Tienes 7 días para devolverlo.");
            
            // Recargamos el detalle del libro para actualizar los botones (que salga ocupado)
            if (libroSeleccionado) {
                verDetalles(libroSeleccionado);
            }

        } catch (err) {
            // 6. Manejo de Errores
            if (err.response && err.response.data) {
                alert("❌ Error: " + err.response.data); 
            } else {
                alert("Ocurrió un error al procesar la solicitud.");
                console.error(err);
            }
        }
    };
    
    return (
        <div className="buscador-container">
            <h2>Buscador de Biblioteca</h2>

            {/* --- FORMULARIO DE BÚSQUEDA --- */}
            <form onSubmit={manejarBusquedaLocal} className="search-form">
                <div className="input-group">
                    <select 
                        value={tipoBusqueda} 
                        onChange={(e) => setTipoBusqueda(e.target.value)}
                    >
                        <option value="titulo">Título</option>
                        <option value="autor">Autor</option>
                        <option value="genero">Género</option>
                        <option value="isbn">ISBN</option>
                    </select>

                    <input 
                        type="text" 
                        placeholder={`Buscar por ${tipoBusqueda}...`} 
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                    />
                    
                    <button type="submit">Buscar Local</button>
                </div>

                <button 
                    type="button" 
                    className="btn-external" 
                    onClick={manejarBusquedaExterna}
                >
                    Búsqueda Interbibliotecaria
                </button>
            </form>

            {/* --- MENSAJES DE CARGA Y ERROR --- */}
            {cargando && <p className="loading-text">Cargando resultados...</p>}
            {error && <p className="error-msg">{error}</p>}

            {/* --- GRID DE RESULTADOS (TARJETAS) --- */}
            <div className="resultados-container">
                {libros.length > 0 ? (
                    <div className="resultados-grid">
                        {libros.map((libro, index) => (
                            <div className="libro-card" key={libro.idLibro || libro.bookID || index}>
                                <div className="card-header">
                                    <span className={`badge ${libro.fuente ? 'externo' : 'local'}`}>
                                        {libro.fuente ? libro.fuente : "Local"}
                                    </span>
                                    <h3>{libro.titulo}</h3>
                                </div>

                                <div className="card-body">
                                    <p><strong>Autor:</strong> {
                                        libro.autores && Array.isArray(libro.autores)
                                            ? libro.autores.map(a => a.nombre).join(', ')
                                            : libro.autor || 'Sin autor'
                                    }</p>

                                    {!libro.fuente && (
                                        <p><strong>Género:</strong> {libro.genero || "Sin género"}</p>
                                    )}

                                    <p><strong>Editorial:</strong> {
                                        libro.editorial && typeof libro.editorial === 'object'
                                            ? libro.editorial.nombre
                                            : libro.editorial || "Sin editorial"
                                    }</p>

                                    <p className="isbn-text"><strong>ISBN:</strong> {libro.isbn}</p>
                                </div>

                                <div className="card-footer">
                                    <button className="btn-detalle" onClick={() => verDetalles(libro)}>
                                        Detalles del Libro
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    !cargando && <p className="no-results">No hay resultados para mostrar.</p>
                )}
            </div>

            {/* --- MODAL DE DETALLES --- */}
            {mostrarModal && libroSeleccionado && (
                <div className="modal-overlay" onClick={() => setMostrarModal(false)}>
                    <div className="modal-content" onClick={e => e.stopPropagation()}>
                        <button className="modal-close" onClick={() => setMostrarModal(false)}>&times;</button>
                        
                        <div className="modal-header">
                            <h2>{libroSeleccionado.titulo}</h2>
                            <span className={`badge ${libroSeleccionado.fuente ? 'externo' : 'local'}`}>
                                {libroSeleccionado.fuente || "Acervo Local"}
                            </span>
                        </div>

                        <div className="modal-body">
                            {/* Sección 1: Info General */}
                            <div className="info-section">
                                <h4>Información General</h4>
                                <p><strong>Autor(es):</strong> {
                                    libroSeleccionado.autores && Array.isArray(libroSeleccionado.autores)
                                        ? libroSeleccionado.autores.map(a => a.nombre).join(', ')
                                        : libroSeleccionado.autor || 'Sin autor'
                                }</p>
                                <p><strong>Editorial:</strong> {
                                    libroSeleccionado.editorial?.nombre || libroSeleccionado.editorial || "No disponible"
                                }</p>
                                <p><strong>ISBN:</strong> {libroSeleccionado.isbn}</p>
                                {libroSeleccionado.idioma && <p><strong>Idioma:</strong> {libroSeleccionado.idioma}</p>}
                            </div>

                            {/* Sección 2: Publicación (Opcional) */}
                            {libroSeleccionado.fechaPublicacion && (
                                <div className="info-section">
                                    <h4>Publicación</h4>
                                    <p><strong>Fecha:</strong> {new Date(libroSeleccionado.fechaPublicacion).toLocaleDateString()}</p>
                                </div>
                            )}

                            {/* Sección 3: DISPONIBILIDAD Y UBICACIÓN */}
                            <div className="info-section">
                                <h4>Disponibilidad y Ubicación</h4>

                                {libroSeleccionado.fuente ? (
                                    <p>Este libro pertenece a la biblioteca <strong>{libroSeleccionado.fuente}</strong>. Debes acudir con el bibliotecario para consultar disponibilidad.</p>
                                ) : (
                                    <>
                                        {cargandoEjemplares ? (
                                            <p>Consultando estantes...</p>
                                        ) : ejemplares.length > 0 ? (
                                            <div>
                                                <ul className="lista-ejemplares">
                                                    {ejemplares.map((ej, i) => (
                                                        <li key={i} className="fila-ejemplar">
                                                            <span className="ubicacion">📍 Estante: {ej.ubicacion}</span>
                                                            <span className={`estado-tag ${ej.estado === 'Disponible' ? 'disp' : 'ocup'}`}>
                                                                {ej.estado}
                                                            </span>
                                                        </li>
                                                    ))}
                                                </ul>

                                                {/* Botón de Solicitar */}
                                                <button 
                                                    className="btn-solicitar"
                                                    /* Deshabilitado si no hay disponibles */
                                                    disabled={!ejemplares.some(e => e.estado === 'Disponible')}
                                                    onClick={() => {
                                                        // Buscamos el PRIMER ejemplar que esté disponible para pedir ese
                                                        const ejemplarDisponible = ejemplares.find(e => e.estado === 'Disponible');
                                                        if (ejemplarDisponible) {
                                                            manejarSolicitudPrestamo(ejemplarDisponible.ID_Ejemplar);
                                                        }
                                                    }}
                                                >
                                                    {ejemplares.some(e => e.estado === 'Disponible') 
                                                        ? "Solicitar Préstamo (7 días)" 
                                                        : "No Disponible Actualmente"}
                                                </button>
                                            </div>
                                        ) : (
                                            <p style={{color: '#d9534f', fontWeight: 'bold'}}>No hay copias físicas registradas en el sistema.</p>
                                        )}
                                    </>
                                )}
                            </div>

                        </div>
                    </div>
                </div>
            )}
        </div>
    );

}

export default BuscadorLibros;