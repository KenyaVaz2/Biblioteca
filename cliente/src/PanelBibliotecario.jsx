import { useState, useEffect } from 'react';
import axios from 'axios';
import './styles/admin.css'; 

const PanelBibliotecario = () => {
    const [seccionActiva, setSeccionActiva] = useState('editoriales'); 

    // --- ESTADOS DE DATOS ---
    const [editoriales, setEditoriales] = useState([]);
    const [autores, setAutores] = useState([]);
    const [libros, setLibros] = useState([]);

    // --- ESTADOS DE EDICIÓN ---
    const [idEditorialEditando, setIdEditorialEditando] = useState(null);
    const [idAutorEditando, setIdAutorEditando] = useState(null);
    const [idLibroEditando, setIdLibroEditando] = useState(null);

    // --- FORMULARIOS ---
    const [formEditorial, setFormEditorial] = useState({ nombre: "", direccion: "", telefono: "", sitioWeb: "" });
    const [formAutor, setFormAutor] = useState({ nombre: "", apellido: "", nacionalidad: "", fecha_nac: "" });
    
    // Formulario Libro
    const [autoresSeleccionados, setAutoresSeleccionados] = useState([]); 
    const [idAutorTemp, setIdAutorTemp] = useState(""); 
    const [formLibro, setFormLibro] = useState({ isbn: "", titulo: "", genero: "", idioma: "", fechaPublicacion: "", idEditorial: "" });

    // Copias
    const [libroParaCopia, setLibroParaCopia] = useState(null); 
    const [formCopia, setFormCopia] = useState({ ubicacion: "", cantidad: 1, fecha_compra: new Date().toISOString().split('T')[0], estado: "Disponible" });
    
    // --- NUEVOS ESTADOS PARA CIRCULACIÓN ---
    const [prestamosActivos, setPrestamosActivos] = useState([]);
    const [multasPendientes, setMultasPendientes] = useState([]);

    // --- CARGA INICIAL ---
    useEffect(() => {
        if (seccionActiva === 'editoriales') cargarEditoriales();
        if (seccionActiva === 'autores') cargarAutores();
        if (seccionActiva === 'libros') cargarLibros();
        // Cargar datos de circulación si esa es la sección activa
        if (seccionActiva === 'circulacion') {
            cargarPrestamosActivos();
            cargarMultasPendientes();
        }
    }, [seccionActiva]);

    // ==========================================
    //              GESTIÓN EDITORIALES
    // ==========================================
    const cargarEditoriales = async () => { try { const res = await axios.get('http://localhost:8080/api/editoriales'); setEditoriales(res.data); } catch (e) {} };

    const guardarEditorial = async (e) => {
        e.preventDefault();
        try {
            if (idEditorialEditando) {
                await axios.put(`http://localhost:8080/api/editoriales/${idEditorialEditando}`, formEditorial);
                alert("Editorial actualizada ✅");
                setIdEditorialEditando(null); 
            } else {
                await axios.post('http://localhost:8080/api/editoriales', formEditorial);
                alert("Editorial creada ✅");
            }
            setFormEditorial({ nombre: "", direccion: "", telefono: "", sitioWeb: "" });
            cargarEditoriales();
        } catch (error) { alert("Error al guardar editorial"); }
    };

    const prepararEdicionEditorial = (editorial) => {
        setFormEditorial({
            nombre: editorial.nombre, direccion: editorial.direccion,
            telefono: editorial.telefono, sitioWeb: editorial.sitioWeb
        });
        setIdEditorialEditando(editorial.id); 
    };

    const eliminarEditorial = async (id) => {
        if (!window.confirm("¿Seguro que deseas eliminar esta editorial?")) return;
        try {
            await axios.delete(`http://localhost:8080/api/editoriales/${id}`);
            alert("Editorial eliminada 🗑️");
            cargarEditoriales();
        } catch (error) {
            alert("❌ NO SE PUEDE ELIMINAR:\nEs probable que existan Libros asignados a esta Editorial.\nDebes borrar los libros primero.");
        }
    };

    // ==========================================
    //              GESTIÓN AUTORES
    // ==========================================
    const cargarAutores = async () => { try { const res = await axios.get('http://localhost:8080/api/autores'); setAutores(res.data); } catch (e) {} };

    const guardarAutor = async (e) => {
        e.preventDefault();
        try {
            if (idAutorEditando) {
                await axios.put(`http://localhost:8080/api/autores/${idAutorEditando}`, formAutor);
                alert("Autor actualizado");
                setIdAutorEditando(null);
            } else {
                await axios.post('http://localhost:8080/api/autores', formAutor);
                alert("Autor creado");
            }
            setFormAutor({ nombre: "", apellido: "", nacionalidad: "", fecha_nac: "" });
            cargarAutores();
        } catch (error) { alert("Error al guardar autor"); }
    };

    const prepararEdicionAutor = (autor) => {
        setFormAutor({
            nombre: autor.nombre, apellido: autor.apellido,
            nacionalidad: autor.nacionalidad, fecha_nac: autor.fecha_nac
        });
        setIdAutorEditando(autor.idAutor);
    };

    const eliminarAutor = async (id) => {
        if (!window.confirm("¿Eliminar autor?")) return;
        try {
            await axios.delete(`http://localhost:8080/api/autores/${id}`);
            alert("Autor eliminado");
            cargarAutores();
        } catch (error) {
            alert("❌ NO SE PUEDE ELIMINAR:\nEste Autor tiene libros registrados.\nElimina los libros donde aparece este autor primero.");
        }
    };

    // ==========================================
    //              GESTIÓN LIBROS
    // ==========================================
    const cargarLibros = async () => { try { const res = await axios.get('http://localhost:8080/api/libros'); setLibros(res.data); } catch (e) {} };

    const agregarAutorALibro = () => {
        if (!idAutorTemp) return;
        const autorEncontrado = autores.find(a => a.idAutor === parseInt(idAutorTemp));
        if (autorEncontrado && !autoresSeleccionados.some(a => a.idAutor === autorEncontrado.idAutor)) {
            setAutoresSeleccionados([...autoresSeleccionados, autorEncontrado]);
        }
    };

    const guardarLibro = async (e) => {
        e.preventDefault();
        if (autoresSeleccionados.length === 0) { alert("Agrega al menos un autor"); return; }

        const payload = {
            isbn: formLibro.isbn, titulo: formLibro.titulo, genero: formLibro.genero,
            idioma: formLibro.idioma, fechaPublicacion: formLibro.fechaPublicacion,
            editorial: { id: formLibro.idEditorial },
            autores: autoresSeleccionados.map(a => ({ idAutor: a.idAutor }))
        };

        try {
            if (idLibroEditando) {
                await axios.put(`http://localhost:8080/api/libros/${idLibroEditando}`, payload);
                alert("Libro actualizado");
                setIdLibroEditando(null);
            } else {
                await axios.post('http://localhost:8080/api/libros', payload);
                alert("Libro registrado");
            }
            setFormLibro({ isbn: "", titulo: "", genero: "", idioma: "", fechaPublicacion: "", idEditorial: "" });
            setAutoresSeleccionados([]);
            cargarLibros();
        } catch (error) { console.error(error); alert("Error al guardar libro"); }
    };

    const prepararEdicionLibro = (libro) => {
        setFormLibro({
            isbn: libro.isbn, titulo: libro.titulo, genero: libro.genero,
            idioma: libro.idioma, fechaPublicacion: libro.fechaPublicacion,
            idEditorial: libro.editorial ? libro.editorial.id : ""
        });
        setAutoresSeleccionados(libro.autores || []); 
        setIdLibroEditando(libro.id); 
    };

    const eliminarLibro = async (id) => {
        if (!window.confirm("¿Estás seguro de eliminar este libro?")) return;
        try {
            await axios.delete(`http://localhost:8080/api/libros/${id}`);
            alert("Libro eliminado");
            cargarLibros();
        } catch (error) {
            alert("❌ NO SE PUEDE ELIMINAR:\nHay EJEMPLARES (Copias físicas) inventariados de este libro.\nDebes borrar primero las copias en el inventario.");
        }
    };

    const guardarEjemplar = async (e) => {
        e.preventDefault();
        const payload = {
            ubicacion: formCopia.ubicacion, cantidad: parseInt(formCopia.cantidad),
            fecha_compra: formCopia.fecha_compra, estado: formCopia.estado,
            libro: { id: libroParaCopia }
        };
        try {
            await axios.post('http://localhost:8080/api/ejemplares', payload);
            alert("Inventario actualizado");
            setLibroParaCopia(null);
        } catch (error) { alert("Error al guardar ejemplares"); }
    };

    // ==========================================
    //         GESTIÓN DE CIRCULACIÓN
    // ==========================================
    
    // 1. Cargar Préstamos
    const cargarPrestamosActivos = async () => {
        try {
            const res = await axios.get('http://localhost:8080/api/prestamos');
            setPrestamosActivos(res.data);
        } catch (error) { console.error("Error cargando préstamos"); }
    };

    // 2. Cargar Multas
    const cargarMultasPendientes = async () => {
        try {
            const res = await axios.get('http://localhost:8080/api/multas/todas'); 
            const pendientes = res.data.filter(m => m.estadoPago === 'Pendiente');
            setMultasPendientes(pendientes);
        } catch (error) { console.error("Error cargando multas"); }
    };

    // 3. Registrar Devolución
    const registrarDevolucion = async (idPrestamo) => {
        if (!window.confirm("¿Confirmar recepción del libro?")) return;
        try {
            const res = await axios.delete(`http://localhost:8080/api/prestamos/${idPrestamo}`);
            alert("Resultado: " + res.data); 
            cargarPrestamosActivos(); 
            cargarMultasPendientes(); 
        } catch (error) {
            alert("Error al registrar devolución");
        }
    };

    // 4. Cobrar Multa
    const cobrarMulta = async (idMulta) => {
        if (!window.confirm("¿Confirmar pago de multa?")) return;
        try {
            await axios.put(`http://localhost:8080/api/multas/${idMulta}/pagar`);
            alert("¡Multa cobrada exitosamente!");
            cargarMultasPendientes();
        } catch (error) {
            alert("Error al procesar el pago");
        }
    };

    // ==========================================
    //              RENDERIZADO
    // ==========================================
    return (
        <div className="admin-container">
            <h2 className="admin-header">Panel de Administración</h2>
            
            <div className="admin-tabs">
                <button className={`tab-btn ${seccionActiva === 'editoriales' ? 'active' : ''}`} onClick={() => {setSeccionActiva('editoriales'); setIdEditorialEditando(null);}}>Editoriales</button>
                <button className={`tab-btn ${seccionActiva === 'autores' ? 'active' : ''}`} onClick={() => {setSeccionActiva('autores'); setIdAutorEditando(null);}}>Autores</button>
                <button className={`tab-btn ${seccionActiva === 'libros' ? 'active' : ''}`} onClick={() => {setSeccionActiva('libros'); setIdLibroEditando(null);}}>Libros</button>
                <button className={`tab-btn ${seccionActiva === 'circulacion' ? 'active' : ''}`} onClick={() => setSeccionActiva('circulacion')}>Circulación</button>
            </div>

            {/* --- SECCIÓN EDITORIALES --- */}
            {seccionActiva === 'editoriales' && (
                <div className="admin-content">
                    <div className="admin-card">
                        <h5 className="card-title">{idEditorialEditando ? '✏️ Editando Editorial' : 'Nueva Editorial'}</h5>
                        <form onSubmit={guardarEditorial}>
                            <div className="form-group"><label>Nombre</label><input className="form-control" type="text" value={formEditorial.nombre} onChange={(e) => setFormEditorial({...formEditorial, nombre: e.target.value})} required /></div>
                            <div className="form-group"><label>Dirección</label><input className="form-control" type="text" value={formEditorial.direccion} onChange={(e) => setFormEditorial({...formEditorial, direccion: e.target.value})} required /></div>
                            <div className="form-group"><label>Teléfono</label><input className="form-control" type="text" maxLength="10" value={formEditorial.telefono} onChange={(e) => setFormEditorial({...formEditorial, telefono: e.target.value})} required /></div>
                            <div className="form-group"><label>Sitio Web</label><input className="form-control" type="text" value={formEditorial.sitioWeb} onChange={(e) => setFormEditorial({...formEditorial, sitioWeb: e.target.value})} required /></div>
                            
                            <div className="d-flex gap-2">
                                <button type="submit" className={`btn-save ${idEditorialEditando ? 'bg-warning text-dark' : ''}`}>
                                    {idEditorialEditando ? 'Actualizar' : 'Guardar'}
                                </button>
                                {idEditorialEditando && <button type="button" className="btn btn-secondary mt-2 w-100" onClick={() => {setIdEditorialEditando(null); setFormEditorial({ nombre: "", direccion: "", telefono: "", sitioWeb: "" })}}>Cancelar</button>}
                            </div>
                        </form>
                    </div>
                    <div className="admin-card">
                        <h5 className="card-title">Editoriales Registradas</h5>
                        <div className="list-scroll">
                            {editoriales.map(ed => (
                                <div key={ed.id} className="list-item">
                                    <div><strong>{ed.nombre}</strong><br/><small className="text-muted">{ed.sitioWeb}</small></div>
                                    <div className="d-flex gap-2">
                                        <button className="btn btn-sm btn-outline-warning" onClick={() => prepararEdicionEditorial(ed)}>✏️</button>
                                        <button className="btn btn-sm btn-outline-danger" onClick={() => eliminarEditorial(ed.id)}>🗑️</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}

            {/* --- SECCIÓN AUTORES --- */}
            {seccionActiva === 'autores' && (
                <div className="admin-content">
                    <div className="admin-card">
                        <h5 className="card-title">{idAutorEditando ? '✏️ Editando Autor' : 'Nuevo Autor'}</h5>
                        <form onSubmit={guardarAutor}>
                            <div className="form-group"><label>Nombre</label><input className="form-control" type="text" value={formAutor.nombre} onChange={(e) => setFormAutor({...formAutor, nombre: e.target.value})} required /></div>
                            <div className="form-group"><label>Apellido</label><input className="form-control" type="text" value={formAutor.apellido} onChange={(e) => setFormAutor({...formAutor, apellido: e.target.value})} required /></div>
                            <div className="form-group"><label>Nacionalidad</label><input className="form-control" type="text" value={formAutor.nacionalidad} onChange={(e) => setFormAutor({...formAutor, nacionalidad: e.target.value})} required /></div>
                            <div className="form-group"><label>Fecha Nacimiento</label><input className="form-control" type="date" value={formAutor.fecha_nac} onChange={(e) => setFormAutor({...formAutor, fecha_nac: e.target.value})} required /></div>
                            
                            <div className="d-flex gap-2">
                                <button type="submit" className={`btn-save ${idAutorEditando ? 'bg-warning text-dark' : ''}`}>
                                    {idAutorEditando ? 'Actualizar' : 'Guardar'}
                                </button>
                                {idAutorEditando && <button type="button" className="btn btn-secondary mt-2 w-100" onClick={() => {setIdAutorEditando(null); setFormAutor({ nombre: "", apellido: "", nacionalidad: "", fecha_nac: "" })}}>Cancelar</button>}
                            </div>
                        </form>
                    </div>
                    <div className="admin-card">
                        <h5 className="card-title">Autores Registrados</h5>
                        <div className="list-scroll">
                            {autores.map(au => (
                                <div key={au.idAutor} className="list-item">
                                    <div><strong>{au.nombre} {au.apellido}</strong><br/><small className="text-muted">{au.nacionalidad}</small></div>
                                    <div className="d-flex gap-2">
                                        <button className="btn btn-sm btn-outline-warning" onClick={() => prepararEdicionAutor(au)}>✏️</button>
                                        <button className="btn btn-sm btn-outline-danger" onClick={() => eliminarAutor(au.idAutor)}>🗑️</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}

            {/* --- SECCIÓN LIBROS --- */}
            {seccionActiva === 'libros' && (
                <div className="admin-content">
                    <div className="admin-card">
                        <h5 className="card-title">{idLibroEditando ? '✏️ Editando Libro' : 'Nuevo Libro'}</h5>
                        <form onSubmit={guardarLibro}>
                            <div className="form-group"><label>Título</label><input className="form-control" required value={formLibro.titulo} onChange={e => setFormLibro({...formLibro, titulo: e.target.value})} /></div>
                            <div className="row">
                                <div className="col-6 form-group"><label>ISBN</label><input className="form-control" required value={formLibro.isbn} onChange={e => setFormLibro({...formLibro, isbn: e.target.value})} /></div>
                                <div className="col-6 form-group"><label>Idioma</label>
                                    <select className="form-control" value={formLibro.idioma} onChange={e => setFormLibro({...formLibro, idioma: e.target.value})}>
                                        <option value="">Selecciona...</option><option value="Español">Español</option><option value="Inglés">Inglés</option>
                                    </select>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-6 form-group"><label>Género</label><input className="form-control" required value={formLibro.genero} onChange={e => setFormLibro({...formLibro, genero: e.target.value})} /></div>
                                <div className="col-6 form-group"><label>Fecha Pub.</label><input type="date" className="form-control" required value={formLibro.fechaPublicacion} onChange={e => setFormLibro({...formLibro, fechaPublicacion: e.target.value})} /></div>
                            </div>
                            <div className="form-group"><label>Editorial</label>
                                <select className="form-control" required value={formLibro.idEditorial} onChange={e => setFormLibro({...formLibro, idEditorial: e.target.value})}>
                                    <option value="">-- Selecciona --</option>
                                    {editoriales.map(ed => (<option key={ed.id} value={ed.id}>{ed.nombre}</option>))}
                                </select>
                            </div>
                            <div className="form-group p-2 border rounded bg-light">
                                <label>Autores</label>
                                <div className="d-flex gap-2">
                                    <select className="form-control" value={idAutorTemp} onChange={e => setIdAutorTemp(e.target.value)}>
                                        <option value="">-- Buscar --</option>{autores.map(au => (<option key={au.idAutor} value={au.idAutor}>{au.nombre} {au.apellido}</option>))}
                                    </select>
                                    <button type="button" className="btn btn-secondary btn-sm" onClick={agregarAutorALibro}>+ Añadir</button>
                                </div>
                                <div className="mt-2">{autoresSeleccionados.map((a, index) => (<span key={index} className="badge bg-primary me-1">{a.nombre} {a.apellido} <span style={{cursor:'pointer'}} onClick={()=>{setAutoresSeleccionados(autoresSeleccionados.filter(aut=>aut.idAutor !== a.idAutor))}}>x</span></span>))}</div>
                            </div>

                            <div className="d-flex gap-2">
                                <button type="submit" className={`btn-save ${idLibroEditando ? 'bg-warning text-dark' : ''}`}>
                                    {idLibroEditando ? 'Actualizar Ficha' : 'Guardar Ficha'}
                                </button>
                                {idLibroEditando && <button type="button" className="btn btn-secondary mt-2 w-100" onClick={() => {setIdLibroEditando(null); setFormLibro({ isbn: "", titulo: "", genero: "", idioma: "", fechaPublicacion: "", idEditorial: "" }); setAutoresSeleccionados([]);}}>Cancelar</button>}
                            </div>
                        </form>
                    </div>

                    <div className="admin-card">
                        <h5 className="card-title">Inventario</h5>
                        <div className="list-scroll">
                            {libros.map(libro => (
                                <div key={libro.id} className="list-item d-flex flex-column align-items-start border-bottom pb-3 mb-3">
                                    <div className="d-flex justify-content-between w-100 align-items-center">
                                        <div>
                                            <strong style={{fontSize: '1.1rem'}}>{libro.titulo}</strong>
                                            <div className="text-muted small">ISBN: {libro.isbn} | Editorial: {libro.editorial?.nombre}</div>
                                        </div>
                                        <div className="d-flex gap-2">
                                            <button className="btn btn-sm btn-outline-success" onClick={() => setLibroParaCopia(libroParaCopia === libro.id ? null : libro.id)}>+ Copias</button>
                                            <button className="btn btn-sm btn-outline-warning" onClick={() => prepararEdicionLibro(libro)}>✏️</button>
                                            <button className="btn btn-sm btn-outline-danger" onClick={() => eliminarLibro(libro.id)}>🗑️</button>
                                        </div>
                                    </div>
                                    {libroParaCopia === libro.id && (
                                        <div className="mt-3 p-3 bg-light rounded w-100 border border-success">
                                            <h6 className="text-success mb-2">Ingresar copias físicas</h6>
                                            <form onSubmit={guardarEjemplar} className="row g-2">
                                                <div className="col-4"><input placeholder="Ubicación" className="form-control form-control-sm" required value={formCopia.ubicacion} onChange={e => setFormCopia({...formCopia, ubicacion: e.target.value})} /></div>
                                                <div className="col-3"><input type="number" placeholder="Cant." className="form-control form-control-sm" required min="1" value={formCopia.cantidad} onChange={e => setFormCopia({...formCopia, cantidad: e.target.value})} /></div>
                                                <div className="col-5"><button type="submit" className="btn btn-success btn-sm w-100">Guardar</button></div>
                                            </form>
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}

            {/* --- NUEVA SECCIÓN: CIRCULACIÓN --- */}
            {seccionActiva === 'circulacion' && (
                <div className="admin-content" style={{ display: 'block' }}>
                    <div className="row">
                        
                        {/* TABLA 1: DEVOLUCIONES */}
                        <div className="col-md-12 mb-4">
                            <div className="admin-card border-primary">
                                <h5 className="card-title text-primary">Recepción de Libros (Préstamos Activos)</h5>
                                {prestamosActivos.length === 0 ? <p>No hay préstamos activos.</p> : (
                                    <div className="list-scroll">
                                        <table className="table table-hover align-middle">
                                            <thead className="table-light">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Título del Libro</th> {/* Ahora sí muestra título */}
                                                    <th>Usuario</th>         {/* Ahora sí muestra nombre */}
                                                    <th>Fecha Límite</th>
                                                    <th>Acción</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {prestamosActivos.map(p => (
                                                    <tr key={p.idPrestamo}>
                                                        <td><span className="badge bg-secondary">#{p.idPrestamo}</span></td>
                                                        <td className="fw-bold text-primary">
                                                            {p.ejemplar?.libro?.titulo || "Cargando..."}
                                                        </td>
                                                        <td>
                                                            {p.usuario?.nombre || "Desconocido"}
                                                        </td>
                                                        <td style={{ color: new Date(p.fechaDevolucion) < new Date() ? 'red' : 'green', fontWeight: 'bold' }}>
                                                            {p.fechaDevolucion}
                                                        </td>
                                                        <td>
                                                            <button className="btn btn-sm btn-success shadow-sm" onClick={() => registrarDevolucion(p.idPrestamo)}>
                                                                Recibir
                                                            </button>
                                                        </td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* TABLA 2: COBRO DE MULTAS */}
                        <div className="col-md-12">
                            <div className="admin-card border-danger">
                                <h5 className="card-title text-danger">💰 Caja / Cobro de Multas</h5>
                                {multasPendientes.length === 0 ? <p className="text-muted">No hay multas pendientes. ¡Todo en orden!</p> : (
                                    <div className="list-scroll">
                                        <table className="table table-hover align-middle">
                                            <thead className="table-light">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Usuario (Deudor)</th> 
                                                    <th>Libro Atrasado</th>   
                                                    <th>Monto</th>
                                                    <th>Acción</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {multasPendientes.map(m => (
                                                    <tr key={m.idMulta}>
                                                        <td>#{m.idMulta}</td>
                                                        <td className="fw-bold">
                                                            {m.prestamo?.usuario?.nombre || "Usuario Eliminado"}
                                                        </td>
                                                        <td className="text-muted fst-italic">
                                                            {m.prestamo?.ejemplar?.libro?.titulo || "Desconocido"}
                                                        </td>
                                                        <td className="text-danger fw-bold fs-5">${m.monto}</td>
                                                        <td>
                                                            <button className="btn btn-sm btn-dark shadow-sm" onClick={() => cobrarMulta(m.idMulta)}>
                                                                Cobrar
                                                            </button>
                                                        </td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>
                                )}
                            </div>
                        </div>

                    </div>
                </div>
            )}

        </div>
    );
};

export default PanelBibliotecario;