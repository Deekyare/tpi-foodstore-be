package com.tp.jpa;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Pedido;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.model.entities.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Instanciación de los repositorios
        CategoriaRepository categoriaRepo = new CategoriaRepository();
        ProductoRepository productoRepo = new ProductoRepository();
        UsuarioRepository usuarioRepo = new UsuarioRepository();
        PedidoRepository pedidoRepo = new PedidoRepository();

        // Menú Principal Estructurado
        while (true) {
            System.out.println("\n-- Menú Principal --");
            System.out.println("1) Gestion de Categorías");
            System.out.println("2) Gestion de Productos");
            System.out.println("3) Gestion de Usuarios");
            System.out.println("4) Gestion de Pedidos");
            System.out.println("5) Reportes");
            System.out.println("0) Salir");
            System.out.print("Elige una opción: ");

            int seleccion;
            try {
                seleccion = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("\nError: Ingrese un número válido.");
                continue;
            }
            if (seleccion == 0) {
                System.out.println("Saliendo del programa.");
                break;
            }
            switch (seleccion) {

                // =================== SUBMENÚ CATEGORÍAS =======================

                case 1:
                    while (true) {
                        System.out.println("\n--- SUBMENÚ CATEGORÍAS ---");
                        System.out.println("1) Alta de categoría");
                        System.out.println("2) Baja de categoría");
                        System.out.println("3) Modificar una categoría");
                        System.out.println("4) Listar las categorías activas");
                        System.out.println("0) Volver al menú principal");
                        System.out.print("Elige una opción: ");

                        int opcionCategoria;
                        try {
                            opcionCategoria = Integer.parseInt(scanner.nextLine());
                        } catch (Exception e) {
                            System.out.println("\nError: Ingrese un número válido.");
                            continue;
                        }

                        if (opcionCategoria == 0) {
                            System.out.println("Saliendo del submenú de categorías...");
                            break;
                        }

                        switch (opcionCategoria) {
                            case 1:
                                System.out.println("\n--- Para dar de alta una categoría ingrese: ---");
                                System.out.print("- Nombre de la categoría: ");
                                String nombreCat = scanner.nextLine();
                                if (nombreCat.trim().isEmpty()) {
                                    System.out.println("El nombre es requerido. Alta cancelada");
                                } else {
                                    System.out.print("- Descripción: ");
                                    String descripcionCat = scanner.nextLine();
                                    Categoria nuevaCategoria = Categoria.builder()
                                            .nombre(nombreCat)
                                            .descripcion(descripcionCat)
                                            .build();

                                    Categoria categoriaGuardada = categoriaRepo.guardar(nuevaCategoria);
                                    System.out.println("¡Categoría guardada exitosamente! ID: " + categoriaGuardada.getId());
                                }
                                break;

                            case 2:
                                while (true) {
                                    System.out.print("Id de la categoría a dar de baja (o en blanco para cancelar): ");
                                    String idCatInput = scanner.nextLine();

                                    if (idCatInput.trim().isEmpty()) {
                                        System.out.println("Baja cancelada.");
                                        break;
                                    }
                                    try {
                                        Long idCat = Long.parseLong(idCatInput);
                                        Optional<Categoria> categoriaOpt = categoriaRepo.buscarPorId(idCat);

                                        if (categoriaOpt.isPresent() && !categoriaOpt.get().isEliminado()) {
                                            categoriaRepo.eliminarLogico(idCat);
                                            System.out.println("¡Categoria dada de baja con éxito!");
                                            break;
                                        } else {
                                            System.out.println("\nError: ID no encontrado. Intente nuevamente.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nError: Ingrese un número válido.");
                                    }
                                }
                                break;

                            case 3:
                                System.out.println("\n--- MODIFICAR CATEGORÍA ---");
                                System.out.println("Categorías disponibles: ");
                                List<Categoria> categoriasActivas = categoriaRepo.listarActivos();
                                for (Categoria cat : categoriasActivas) {
                                    System.out.println("- ID: " + cat.getId() + " - Nombre: " + cat.getNombre());
                                }

                                System.out.print("\nIngrese el Id de la categoría a modificar: ");
                                String idCatMod = scanner.nextLine();

                                if (idCatMod.trim().isEmpty()) {
                                    System.out.println("\nError: El Id es requerido para la modificación.");
                                } else {
                                    try {
                                        Long idCat = Long.parseLong(idCatMod);
                                        Optional<Categoria> categoriaOpt = categoriaRepo.buscarPorId(idCat);

                                        if (categoriaOpt.isPresent() && !categoriaOpt.get().isEliminado()) {
                                            Categoria categoriaAModificar = categoriaOpt.get();

                                            System.out.println("\nValores Actuales:");
                                            System.out.println("- Nombre: " + categoriaAModificar.getNombre());
                                            System.out.println("- Descripción: " + categoriaAModificar.getDescripcion());

                                            System.out.println("\nIngrese los nuevos valores (deje en blanco para mantener el actual):");

                                            System.out.print("Nuevo Nombre: ");
                                            String nuevoNombre = scanner.nextLine();
                                            if (!nuevoNombre.trim().isEmpty()) {
                                                categoriaAModificar.setNombre(nuevoNombre);
                                            }
                                            System.out.print("Nueva Descripción: ");
                                            String nuevaDescripcion = scanner.nextLine();
                                            if (!nuevaDescripcion.trim().isEmpty()) {
                                                categoriaAModificar.setDescripcion(nuevaDescripcion);
                                            }
                                            categoriaRepo.guardar(categoriaAModificar);
                                            System.out.println("La categoría '" + categoriaAModificar.getNombre() + "' ha sido modificada correctamente.");
                                        } else {
                                            System.out.println("\nError: No existe ninguna categoría activa con el ID proporcionado. Intente nuevamente.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nError: Debe ingresar un número válido para el ID.");
                                    }
                                }
                                break;

                            case 4:
                                List<Categoria> todosCategorias = categoriaRepo.listarActivos();
                                if (todosCategorias.isEmpty()) {
                                    System.out.println("No hay categorías en el sistema.");
                                } else {
                                    System.out.println("\n- Categorías disponibles -");
                                    for (Categoria cat : todosCategorias) {
                                        System.out.println("\nId: " + cat.getId() +
                                                "\nNombre: " + cat.getNombre() +
                                                "\nDescripción: " + cat.getDescripcion());
                                    }
                                }
                                break;

                            default:
                                System.out.println("Opción no válida. Intenta de nuevo.");
                        }
                    }
                    break;

                // =================== SUBMENÚ PRODUCTOS =======================

                case 2:
                    while (true) {
                        System.out.println("\n--- SUBMENÚ PRODUCTOS ---");
                        System.out.println("1) Alta de nuevo producto");
                        System.out.println("2) Baja de producto");
                        System.out.println("3) Modificar un producto");
                        System.out.println("4) Listar los productos activos");
                        System.out.println("0) Volver al menú principal");
                        System.out.print("Elige una opción: ");

                        int opcionProductos;
                        try {
                            opcionProductos = Integer.parseInt(scanner.nextLine());
                        } catch (Exception e) {
                            System.out.println("\nError: Ingrese un número válido.");
                            continue;
                        }
                        if (opcionProductos == 0) {
                            System.out.println("Saliendo del submenú de productos...");
                            break;
                        }
                        switch (opcionProductos) {
                            case 1:
                                System.out.println("\n--- ALTA DE NUEVO PRODUCTO ---");
                                List<Categoria> categoriasActivasProd = categoriaRepo.listarActivos();
                                if (categoriasActivasProd.isEmpty()) {
                                    System.out.println("\nError: No hay categorías activas. Debe crear una desde el menú de Categorías primero.");
                                    break;
                                }
                                System.out.println("\nCategorías disponibles:");
                                for (Categoria cat : categoriasActivasProd) {
                                    System.out.println("- ID: " + cat.getId() + " - Nombre: " + cat.getNombre());
                                }
                                System.out.print("\nIngrese el ID de la categoría para el producto a ingresar: ");
                                Long idCatSeleccionada;
                                try {
                                    idCatSeleccionada = Long.parseLong(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("\nError: ID inválido. Alta cancelada.");
                                    break;
                                }
                                Optional<Categoria> catSeleccionada = categoriaRepo.buscarPorId(idCatSeleccionada);
                                if (catSeleccionada.isEmpty() || catSeleccionada.get().isEliminado()) {
                                    System.out.println("\nError: Categoría no encontrada o dada de baja. Alta cancelada.");
                                    break;
                                }
                                Categoria categoriaSeleccionada = catSeleccionada.get();

                                System.out.print("- Ingrese el nombre del producto: ");
                                String nombreProd = scanner.nextLine();
                                if (nombreProd.trim().isEmpty()) {
                                    System.out.println("\nError: El nombre es requerido. Alta cancelada.");
                                    break;
                                }
                                System.out.print("- Descripción: ");
                                String descripcionProd = scanner.nextLine();
                                System.out.print("- Precio: ");
                                Double precioProd;
                                try {
                                    precioProd = Double.parseDouble(scanner.nextLine());
                                    if (precioProd <= 0) {
                                        System.out.println("\nError: El precio debe ser mayor a 0. Alta cancelada.");
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("\nError: Formato de precio inválido. Alta cancelada.");
                                    break;
                                }
                                System.out.print("- Stock: ");
                                int stockProd;
                                try {
                                    stockProd = Integer.parseInt(scanner.nextLine());
                                    if (stockProd < 0) {
                                        System.out.println("\nError: El stock no puede ser negativo. Alta cancelada.");
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("\nError: Formato de stock inválido. Alta cancelada.");
                                    break;
                                }
                                Producto nuevoProd = Producto.builder()
                                        .nombre(nombreProd)
                                        .descripcion(descripcionProd)
                                        .precio(precioProd)
                                        .stock(stockProd)
                                        .categoria(categoriaSeleccionada)
                                        .disponible(true)
                                        .build();

                                productoRepo.guardar(nuevoProd);
                                System.out.println("\n¡Producto guardado exitosamente!");
                                break;
                            case 2:
                                System.out.println("\n--- BAJA DE PRODUCTOS ---");
                                System.out.print("Id del producto a dar de baja: ");
                                String idProdInput = scanner.nextLine();

                                if (idProdInput.trim().isEmpty()) {
                                    System.out.println("\nError: El Id es requerido para la baja. Baja cancelada.");
                                } else {
                                    try {
                                        Long idProd = Long.parseLong(idProdInput);
                                        Optional<Producto> productoOpt = productoRepo.buscarPorId(idProd);

                                        if (productoOpt.isPresent() && !productoOpt.get().isEliminado()) {
                                            String nombreProducto = productoOpt.get().getNombre();
                                            productoRepo.eliminarLogico(idProd);
                                            System.out.println("\nÉxito: El producto '" + nombreProducto + "' ha sido dado de baja correctamente.");
                                        } else {
                                            System.out.println("\nError: No existe ningún producto activo con el ID proporcionado. Intente nuevamente.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nError: Debe ingresar un número válido para el ID.");
                                    }
                                }
                                break;
                            case 3:
                                System.out.println("\n--- MODIFICAR PRODUCTO ---");
                                System.out.println("Productos disponibles: ");
                                List<Producto> productosActivos = productoRepo.listarActivos();

                                for (Producto prod : productosActivos) {
                                    System.out.println("- ID: " + prod.getId() + " - Nombre: " + prod.getNombre());
                                }
                                System.out.print("\nIngrese el Id del producto a modificar: ");
                                String idProdMod = scanner.nextLine();

                                if (idProdMod.trim().isEmpty()) {
                                    System.out.println("\nError: El Id es requerido para la modificación.");
                                } else {
                                    try {
                                        Long idProd = Long.parseLong(idProdMod);
                                        Optional<Producto> productoOpt = productoRepo.buscarPorId(idProd);

                                        if (productoOpt.isPresent() && !productoOpt.get().isEliminado()) {
                                            Producto productoAModificar = productoOpt.get();

                                            System.out.println("\nValores Actuales:");
                                            System.out.println("- Nombre: " + productoAModificar.getNombre());
                                            System.out.println("- Precio: $" + productoAModificar.getPrecio());
                                            System.out.println("- Stock: " + productoAModificar.getStock());
                                            System.out.println("\nIngrese los nuevos valores (deje en blanco para mantener el actual):");
                                            System.out.print("Nuevo Nombre: ");
                                            String nuevoNombre = scanner.nextLine();
                                            if (!nuevoNombre.trim().isEmpty()) {
                                                productoAModificar.setNombre(nuevoNombre);
                                            }
                                            System.out.print("Nuevo Precio: ");
                                            String nuevoPrecioStr = scanner.nextLine();
                                            if (!nuevoPrecioStr.trim().isEmpty()) {
                                                try {
                                                    Double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                                                    if (nuevoPrecio > 0) {
                                                        productoAModificar.setPrecio(nuevoPrecio);
                                                    } else {
                                                        System.out.println("\nError: El precio no puede ser <= 0. Se mantuvo el precio anterior.");
                                                    }
                                                } catch (NumberFormatException e) {
                                                    System.out.println("\nError: Formato de precio inválido. Se mantuvo el precio anterior.");
                                                }
                                            }
                                            System.out.print("Nuevo Stock: ");
                                            String nuevoStockStr = scanner.nextLine();
                                            if (!nuevoStockStr.trim().isEmpty()) {
                                                try {
                                                    int nuevoStock = Integer.parseInt(nuevoStockStr);
                                                    if (nuevoStock >= 0) {
                                                        productoAModificar.setStock(nuevoStock);
                                                    } else {
                                                        System.out.println("\nError: El stock no puede ser negativo. Se mantuvo el stock anterior.");
                                                    }
                                                } catch (NumberFormatException e) {
                                                    System.out.println("\nError: Formato de stock inválido. Se mantuvo el stock anterior.");
                                                }
                                            }
                                            productoRepo.guardar(productoAModificar);
                                            System.out.println("El producto '" + productoAModificar.getNombre() + "' ha sido modificado correctamente.");
                                        } else {
                                            System.out.println("\nError: No existe ningún producto activo con el ID proporcionado. Intente nuevamente.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nError: Debe ingresar un número válido para el ID.");
                                    }
                                }
                                break;
                            case 4:
                                List<Producto> todosProductos = productoRepo.listarActivos();
                                if (todosProductos.isEmpty()) {
                                    System.out.println("\nNo hay productos en el sistema.");
                                } else {
                                    System.out.println("\n- Listado de todos los productos -");
                                    for (Producto prod : todosProductos) {
                                        System.out.println("\nId: " + prod.getId() +
                                                "\nNombre: " + prod.getNombre() +
                                                "\nPrecio: $" + prod.getPrecio() +
                                                "\nStock: " + prod.getStock() +
                                                "\nCategoría: " + prod.getCategoria().getNombre());
                                    }
                                }
                                break;
                            default:
                                System.out.println("Opción no válida. Intenta de nuevo.");
                        }
                    }
                    break;

                // ==================== SUBMENÚ USUARIOS ======================

                case 3:
                    while (true) {
                        System.out.println("\n--- SUBMENÚ USUARIOS ---");
                        System.out.println("1) Alta de usuario");
                        System.out.println("2) Baja de usuario");
                        System.out.println("3) Modificar un usuario");
                        System.out.println("4) Listar los usuarios activos");
                        System.out.println("5) Buscar por mail");
                        System.out.println("0) Volver al menú principal");
                        System.out.print("Elige una opción: ");

                        int opcionUsuario;
                        try {
                            opcionUsuario = Integer.parseInt(scanner.nextLine());
                        } catch (Exception e) {
                            System.out.println("\nError: Ingrese un número válido.");
                            continue;
                        }

                        if (opcionUsuario == 0) {
                            System.out.println("Saliendo del submenú de usuarios...");
                            break;
                        }

                        switch (opcionUsuario) {
                            case 1:
                                System.out.println("\n--- Para dar de alta un usuario ingrese: ---");
                                System.out.print("- Mail: ");
                                String emailUsu = scanner.nextLine().trim();

                                if (emailUsu.isEmpty()) {
                                    System.out.println("El mail es requerido. Alta cancelada.");
                                    break;
                                }

                                // Validar que el mail sea unico
                                Optional<Usuario> existente = usuarioRepo.buscarPorMail(emailUsu);
                                if (existente.isPresent()) {
                                    System.out.println("\nError: Ya existe un usuario activo registrado con ese mail. Alta cancelada.");
                                    break;
                                }

                                System.out.print("- Nombre: ");
                                String nombreUsu = scanner.nextLine();
                                System.out.print("- Apellido: ");
                                String apellidoUsu = scanner.nextLine();
                                System.out.print("- Celular: ");
                                String telefonoUsu = scanner.nextLine();
                                System.out.print("- Contraseña: ");
                                String contraseniaUsu = scanner.nextLine();

                                System.out.print("- Ingrese su rol (ADMIN / USUARIO): ");
                                String rolInput = scanner.nextLine().trim().toUpperCase();
                                Rol rolUsu = Rol.USUARIO; // Valor por defecto seguro
                                if (rolInput.equals("ADMIN")) {
                                    rolUsu = Rol.ADMIN;
                                }

                                Usuario nuevoUsuario = Usuario.builder()
                                        .nombre(nombreUsu)
                                        .apellido(apellidoUsu)
                                        .mail(emailUsu)
                                        .celular(telefonoUsu)
                                        .contrasenia(contraseniaUsu)
                                        .rol(rolUsu)
                                        .build();

                                Usuario usuarioGuardado = usuarioRepo.guardar(nuevoUsuario);
                                System.out.println("¡Usuario guardado exitosamente! ID: " + usuarioGuardado.getId());
                                break;

                            case 2:
                                while (true) {
                                    System.out.print("Id del usuario a dar de baja (o en blanco para cancelar): ");
                                    String idUsuarioInput = scanner.nextLine();

                                    if (idUsuarioInput.trim().isEmpty()) {
                                        System.out.println("Baja cancelada.");
                                        break;
                                    }
                                    try {
                                        Long idUsuario = Long.parseLong(idUsuarioInput);
                                        Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorId(idUsuario);

                                        if (usuarioOpt.isPresent() && !usuarioOpt.get().isEliminado()) {
                                            usuarioRepo.eliminarLogico(idUsuario);
                                            System.out.println("¡El usuario " + usuarioOpt.get().getNombre() + " fue dado de baja con éxito!");
                                            break;
                                        } else {
                                            System.out.println("\nError: ID no encontrado o ya dado de baja. Intente nuevamente.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nError: Ingrese un número válido.");
                                    }
                                }
                                break;

                            case 3:
                                System.out.println("\n--- MODIFICAR USUARIO ---");
                                System.out.println("Usuarios disponibles: ");

                                List<Usuario> usuariosActivos = usuarioRepo.listarActivos();
                                if (usuariosActivos.isEmpty()) {
                                    System.out.println("No hay usuarios activos para modificar.");
                                    break;
                                }
                                for (Usuario usuario : usuariosActivos) {
                                    System.out.println("- ID: " + usuario.getId() + " - Nombre: " + usuario.getNombre());
                                }

                                System.out.print("\nIngrese el Id del usuario a modificar: ");
                                String idUsuarioMod = scanner.nextLine();

                                if (idUsuarioMod.trim().isEmpty()) {
                                    System.out.println("\nError: El Id es requerido para la modificación.");
                                } else {
                                    try {
                                        Long idUsuario = Long.parseLong(idUsuarioMod);
                                        Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorId(idUsuario);

                                        if (usuarioOpt.isPresent() && !usuarioOpt.get().isEliminado()) {
                                            Usuario usuarioAModificar = usuarioOpt.get();

                                            System.out.println("\nValores Actuales:");
                                            System.out.println("- Nombre: " + usuarioAModificar.getNombre());
                                            System.out.println("- Apellido: " + usuarioAModificar.getApellido());
                                            System.out.println("- Mail: " + usuarioAModificar.getMail());
                                            System.out.println("- Celular: " + usuarioAModificar.getCelular());
                                            System.out.println("- Contraseña: " + usuarioAModificar.getContrasenia());
                                            System.out.println("- Rol: " + usuarioAModificar.getRol());

                                            System.out.println("\nIngrese los nuevos valores (deje en blanco para mantener el actual):");

                                            System.out.print("Nuevo Nombre: ");
                                            String nuevoNombre = scanner.nextLine();
                                            if (!nuevoNombre.trim().isEmpty()) {
                                                usuarioAModificar.setNombre(nuevoNombre);
                                            }
                                            System.out.print("Nuevo Apellido: ");
                                            String nuevoApellido = scanner.nextLine();
                                            if (!nuevoApellido.trim().isEmpty()) {
                                                usuarioAModificar.setApellido(nuevoApellido);
                                            }
                                            System.out.print("Nuevo mail: ");
                                            String nuevoMail = scanner.nextLine().trim();
                                            if (!nuevoMail.isEmpty()) {
                                                Optional<Usuario> mailExistente = usuarioRepo.buscarPorMail(nuevoMail);
                                                if (mailExistente.isPresent() && !mailExistente.get().getId().equals(usuarioAModificar.getId())) {
                                                    System.out.println("Error: El mail indicado se encuentra registrado. No se realizó la modificación.");
                                                } else {
                                                    usuarioAModificar.setMail(nuevoMail);
                                                }
                                            }
                                            System.out.print("Nuevo Celular: ");
                                            String nuevoCelular = scanner.nextLine();
                                            if (!nuevoCelular.trim().isEmpty()) {
                                                usuarioAModificar.setCelular(nuevoCelular);
                                            }
                                            System.out.print("Nueva Contraseña: ");
                                            String nuevaContrasenia = scanner.nextLine();
                                            if (!nuevaContrasenia.trim().isEmpty()) {
                                                usuarioAModificar.setContrasenia(nuevaContrasenia);
                                            }
                                            System.out.print("Nuevo Rol (ADMIN / USUARIO): ");
                                            String nuevoRol = scanner.nextLine().trim().toUpperCase();
                                            if (!nuevoRol.isEmpty()) {
                                                if (nuevoRol.equals("ADMIN") || nuevoRol.equals("USUARIO")) {
                                                    usuarioAModificar.setRol(Rol.valueOf(nuevoRol));
                                                } else {
                                                    System.out.println("Rol inválido. Se conservó el anterior.");
                                                }
                                            }
                                            usuarioRepo.guardar(usuarioAModificar);
                                            System.out.println("El usuario '" + usuarioAModificar.getNombre() + "' ha sido modificado correctamente.");
                                        } else {
                                            System.out.println("\nError: No existe ningún usuario activo con el ID proporcionado. Intente nuevamente.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("\nError: Debe ingresar un número válido para el ID.");
                                    }
                                }
                                break;

                            case 4:
                                List<Usuario> todosUsuarios = usuarioRepo.listarActivos();
                                if (todosUsuarios.isEmpty()) {
                                    System.out.println("No hay usuarios en el sistema.");
                                } else {
                                    System.out.println("\n- Usuarios disponibles -");
                                    for (Usuario usuario : todosUsuarios) {
                                        System.out.println("\nId: " + usuario.getId() +
                                                " | Nombre: " + usuario.getNombre() + " " + usuario.getApellido() +
                                                " | Mail: " + usuario.getMail() +
                                                " | Rol: " + usuario.getRol());
                                    }
                                }
                                break;

                            case 5:
                                System.out.println("\n--- BUSCAR USUARIO POR EMAIL  ---");
                                System.out.print("Ingrese el mail del usuario a buscar: ");
                                String mailBuscar = scanner.nextLine().trim();

                                if (mailBuscar.isEmpty()) {
                                    System.out.println("Búsqueda cancelada.");
                                    break;
                                }
                                Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorMail(mailBuscar);
                                if (usuarioOpt.isPresent()) {
                                    Usuario usuario = usuarioOpt.get();
                                    System.out.println("\n--- USUARIO ENCONTRADO ---");
                                    System.out.println("ID: " + usuario.getId());
                                    System.out.println("Nombre Completo: " + usuario.getNombre()+ " " + usuario.getApellido());
                                    System.out.println("Email: " + usuario.getMail());
                                    System.out.println("Celular: " + usuario.getCelular());
                                    System.out.println("Rol: " + usuario.getRol());
                                } else {
                                    System.out.println("No se encontró ningún usuario activo registrado con el mail: " + mailBuscar);
                                }
                                break;

                            default:
                                System.out.println("Opción no válida. Intenta de nuevo.");
                        }
                    }
                    break; // Corta el case 3 del menú principal


                // ================== SUBMENÚ PEDIDOS ========================

                case 4:
                    while (true) {
                        System.out.println("\n--- SUBMENÚ PEDIDOS ---");
                        System.out.println("1) Alta de pedido");
                        System.out.println("2) Cambiar estado de un pedido");
                        System.out.println("3) Baja lógica de un pedido");
                        System.out.println("4) Listar los pedidos activos");
                        System.out.println("5) Ver pedidos por usuario");
                        System.out.println("6) Ver pedidos por estado");
                        System.out.println("0) Volver al menú principal");
                        System.out.print("Elige una opción: ");

                        int opcionPedidos;
                        try {
                            opcionPedidos = Integer.parseInt(scanner.nextLine());
                        } catch (Exception e) {
                            System.out.println("\nError: Ingrese un número válido.");
                            continue;
                        }

                        if (opcionPedidos == 0) {
                            System.out.println("Saliendo del submenú de pedidos...");
                            break;
                        }

                        switch (opcionPedidos) {
                            case 1:
                                System.out.println("\n--- ALTA DE NUEVO PEDIDO ---");
                                List<Usuario> usuariosActivos = usuarioRepo.listarActivos();
                                if (usuariosActivos.isEmpty()) {
                                    System.out.println("\nError: No hay usuarios activos en el sistema. Debe registrar uno primero.");
                                    break;
                                }

                                System.out.println("Usuarios activos disponibles:");
                                for (Usuario u : usuariosActivos) {
                                    System.out.println("- ID: " + u.getId() + " | Cliente: " + u.getNombre() + " " + u.getApellido());
                                }
                                System.out.print("Seleccione el ID del usuario para asociar al pedido: ");
                                Long idUsuarioElegido;
                                try {
                                    idUsuarioElegido = Long.parseLong(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: ID inválido. Operación cancelada.");
                                    break;
                                }

                                Optional<Usuario> usuarioOptPed = usuarioRepo.buscarPorId(idUsuarioElegido);
                                if (usuarioOptPed.isEmpty() || usuarioOptPed.get().isEliminado()) {
                                    System.out.println("Error: Usuario no encontrado o dado de baja. Operación cancelada.");
                                    break;
                                }

                                System.out.println("\nFormas de Pago disponibles:");
                                System.out.println("1) EFECTIVO");
                                System.out.println("2) TARJETA");
                                System.out.println("3) TRANSFERENCIA");
                                System.out.print("Seleccione una opción: ");
                                String formaPagoOp = scanner.nextLine().trim();
                                FormaPago formaPagoElegida;
                                if (formaPagoOp.equals("1")) formaPagoElegida = FormaPago.EFECTIVO;
                                else if (formaPagoOp.equals("2")) formaPagoElegida = FormaPago.TARJETA;
                                else if (formaPagoOp.equals("3")) formaPagoElegida = FormaPago.TRANSFERENCIA;
                                else {
                                    System.out.println("Opción inválida. Alta cancelada.");
                                    break;
                                }

                                // Listas temporales en memoria para garantizar atomicidad
                                List<Long> listTempProduc = new ArrayList<>();
                                List<Integer> ListTemporalCant = new ArrayList<>();

                                while (true) {
                                    List<Producto> productosActivosPed = productoRepo.listarActivos();
                                    if (productosActivosPed.isEmpty()) {
                                        System.out.println("No hay productos activos registrados en el catálogo.");
                                        break;
                                    }

                                    System.out.println("\nCatálogo de Productos Disponibles:");
                                    for (Producto p : productosActivosPed) {
                                        if (p.isDisponible()) {
                                            System.out.println("- ID: " + p.getId() +
                                                    " | " + p.getNombre() +
                                                    " | Categoría: " + p.getCategoria().getNombre() +
                                                    " | Precio: $" + p.getPrecio() +
                                                    " | Stock: " + p.getStock());
                                        }
                                    }

                                    System.out.print("\nIngrese el ID del producto a agregar (o presione Enter para finalizar la selección): ");
                                    String idProdInput = scanner.nextLine().trim();
                                    if (idProdInput.isEmpty()) {
                                        break;
                                    }

                                    Long idProdPed;
                                    try {
                                        idProdPed = Long.parseLong(idProdInput);
                                    } catch (NumberFormatException e) {
                                        System.out.println("ID inválido. Intente nuevamente.");
                                        continue;
                                    }

                                    Optional<Producto> prodOptPed = productoRepo.buscarPorId(idProdPed);
                                    if (prodOptPed.isEmpty() || prodOptPed.get().isEliminado() || !prodOptPed.get().isDisponible()) {
                                        System.out.println("Error: El producto no existe, está dado de baja o no está disponible en este momento.");
                                        continue;
                                    }

                                    Producto productoSeleccionado = prodOptPed.get();
                                    System.out.print("Ingrese la cantidad requerida: ");
                                    int cantidadPed;
                                    try {
                                        cantidadPed = Integer.parseInt(scanner.nextLine());
                                        if (cantidadPed <= 0) {
                                            System.out.println("La cantidad debe ser mayor a 0.");
                                            continue;
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("El valor ingresado debe ser un número positivo.");
                                        continue;
                                    }

                                    if (productoSeleccionado.getStock() < cantidadPed) {
                                        System.out.println("Error: Stock insuficiente. El total actual es de: " + productoSeleccionado.getStock());
                                        continue;
                                    }

                                    // Guardamos temporalmente en memoria
                                    listTempProduc.add(idProdPed);
                                    ListTemporalCant.add(cantidadPed);
                                    System.out.println("¡" + productoSeleccionado.getNombre() + " x" + cantidadPed + " cargado temporalmente!");

                                    System.out.print("¿Desea seguir agregando más ítems al pedido? (S/N): ");
                                    String continuar = scanner.nextLine().trim().toUpperCase();
                                    if (continuar.equals("N")) {
                                        break;
                                    }
                                }
                                if (listTempProduc.isEmpty()) {
                                    System.out.println("El pedido debe contener al menos una línea de detalle. Operación cancelada.");
                                    break;
                                }
                                // Proceso de alta atomica
                                EntityManager emPed = JPAUtil.getEntityManager();
                                EntityTransaction txPedido = emPed.getTransaction();
                                try {
                                    txPedido.begin();

                                    // Recuperamos al usuario en este EntityManager
                                    Usuario usuarioGestionado = emPed.find(Usuario.class, idUsuarioElegido);

                                    Pedido nuevoPedido = Pedido.builder()
                                            .fecha(LocalDate.now())
                                            .estado(Estado.PENDIENTE)
                                            .formaPago(formaPagoElegida)
                                            .build();

                                    for (int i = 0; i < listTempProduc.size(); i++) {
                                        Long pId = listTempProduc.get(i);
                                        int cant = ListTemporalCant.get(i);

                                        Producto prodGestionado = emPed.find(Producto.class, pId);

                                        if (prodGestionado.getStock() < cant) {
                                            throw new RuntimeException("El stock de: "+ prodGestionado.getNombre() +" se actualizó recientemente. ");
                                        }

                                        // Descontamos inventario sobre el objeto gestionado
                                        prodGestionado.setStock(prodGestionado.getStock() - cant);

                                        // Añadimos el detalle al pedido
                                        nuevoPedido.addDetallePedido(cant, prodGestionado);
                                    }

                                    nuevoPedido.calcularTotal();
                                    usuarioGestionado.addPedido(nuevoPedido);

                                    // Al persistir Pedido, la cascada guarda automáticamente los detalles.
                                    emPed.persist(nuevoPedido);

                                    txPedido.commit(); // Confirmamos los cambios

                                    System.out.println("\n=======================================================");
                                    System.out.println("El PEDIDO FUE REALIZADO EXITOSAMENTE!");
                                    System.out.println("ID Asignado: " + nuevoPedido.getId());
                                    System.out.println("Fecha de Registro: " + nuevoPedido.getFecha());
                                    System.out.println("Cliente: " + usuarioGestionado.getNombre() + " " + usuarioGestionado.getApellido());
                                    System.out.println("Forma de Pago: " + nuevoPedido.getFormaPago());
                                    System.out.println("Total a Facturar: $" + nuevoPedido.getTotal());
                                    System.out.println("=======================================================");

                                } catch (Exception e) {
                                    if (txPedido.isActive()) {
                                        txPedido.rollback(); // revierte lo realizado
                                    }
                                    System.out.println("\n Rollback ejecutado: La transacción falló. Detalle: " + e.getMessage());
                                } finally {
                                    emPed.close();
                                }
                                break;

                            case 2:
                                System.out.println("\n--- CAMBIAR ESTADO DE UN PEDIDO ---");
                                System.out.println("Pedidos disponibles en el sistema:");

                                List<Pedido> pedidosDisponibles = pedidoRepo.listarActivos();
                                if (pedidosDisponibles.isEmpty()) {
                                    System.out.println("No se registran pedidos activos en el sistema para poder modificar.");
                                    break;
                                }

                                for (Pedido p : pedidosDisponibles) {
                                    String clienteNombre = usuarioRepo.buscarPorPedidoId(p.getId())
                                            .map(u -> u.getNombre() + " " + u.getApellido())
                                            .orElse("Desconocido");

                                    System.out.println("- ID: " + p.getId() + " | Fecha: " + p.getFecha() + " | Cliente: " + clienteNombre + " | Estado Actual: " + p.getEstado() + " | Total: $" + p.getTotal());
                                }

                                System.out.print("\nIngrese el ID del pedido a modificar: ");
                                try {
                                    Long idPedEstado = Long.parseLong(scanner.nextLine());
                                    Optional<Pedido> pedSelec = pedidoRepo.buscarPorId(idPedEstado);
                                    if (pedSelec.isPresent() && !pedSelec.get().isEliminado()) {
                                        Pedido pedido = pedSelec.get();

                                        System.out.println("\nPedido seleccionado ID: " + pedido.getId() + " (Estado actual: " + pedido.getEstado() + ")");
                                        System.out.println("Seleccione el nuevo estado:");
                                        System.out.println("1) PENDIENTE\n2) CONFIRMADO\n3) TERMINADO\n4) CANCELADO");
                                        System.out.print("Opción: ");
                                        String opcSelec = scanner.nextLine().trim();
                                        if (opcSelec.equals("1")) pedido.setEstado(Estado.PENDIENTE);
                                        else if (opcSelec.equals("2")) pedido.setEstado(Estado.CONFIRMADO);
                                        else if (opcSelec.equals("3")) pedido.setEstado(Estado.TERMINADO);
                                        else if (opcSelec.equals("4")) pedido.setEstado(Estado.CANCELADO);
                                        else {
                                            System.out.println("Opción inválida. No se aplicaron modificaciones.");
                                            break;
                                        }
                                        pedidoRepo.guardar(pedido);
                                        System.out.println("¡Pedido modificado con éxito! Pedido ID " + pedido.getId() + " actualizado a " + pedido.getEstado());
                                    } else {
                                        System.out.println("Error: No se encontró ningún pedido activo con el ID indicado.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: Debe ingresar un valor numérico válido.");
                                }
                                break;
//
                            case 3:
                                System.out.println("\n--- BAJA LÓGICA DE UN PEDIDO ---");
                                System.out.print("Ingrese el ID del pedido a dar de baja: ");
                                try {
                                    Long idPedBaja = Long.parseLong(scanner.nextLine());
                                    Optional<Pedido> pedABajar = pedidoRepo.buscarPorId(idPedBaja);
                                    if (pedABajar.isPresent() && !pedABajar.get().isEliminado()) {
                                        double totalPed = pedABajar.get().getTotal();
                                        pedidoRepo.eliminarLogico(idPedBaja);
                                        System.out.println("Baja Exitosa! Pedido ID " + idPedBaja + " removido lógicamente. Monto del pedido: $" + totalPed);
                                    } else {
                                        System.out.println("Error: ID no encontrado o pedido inactivo.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: El ID debe ser un número entero.");
                                }
                                break;

                            case 4:
                                System.out.println("\n--- LISTAR PEDIDOS ACTIVOS ---");
                                System.out.println("- Listado de todos los pedidos activos -");
                                List<Pedido> pedidosActivosLista = pedidoRepo.listarActivos();
                                if (pedidosActivosLista.isEmpty()) {
                                    System.out.println("No se registran pedidos activos en el sistema.");
                                } else {
                                    for (Pedido p : pedidosActivosLista) {
                                        String clienteNombre = usuarioRepo.buscarPorPedidoId(p.getId())
                                                .map(u -> u.getNombre() + " " + u.getApellido())
                                                .orElse("Desconocido");

                                        System.out.println("ID: " + p.getId() + " | Fecha: " + p.getFecha() + " | Estado: " + p.getEstado() + " | Pago: " + p.getFormaPago() + " | Cliente: " + clienteNombre + " | Total: $" + p.getTotal());
                                    }
                                }
                                break;

                            case 5:
                                System.out.println("\n--- PEDIDOS POR USUARIO ---");
                                List<Usuario> usuariosActivosLista = usuarioRepo.listarActivos();
                                if (usuariosActivosLista.isEmpty()) {
                                    System.out.println("No hay usuarios registrados para filtrar.");
                                    break;
                                }
                                for (Usuario u : usuariosActivosLista) {
                                    System.out.println("- ID: " + u.getId() + " | Cliente: " + u.getNombre() + " " + u.getApellido());
                                }
                                System.out.print("Seleccione el ID del usuario: ");
                                try {
                                    Long idUsuarioPed = Long.parseLong(scanner.nextLine());
                                    List<Pedido> pedidosDelUsuario = usuarioRepo.buscarPedidosPorUsuario(idUsuarioPed);
                                    if (pedidosDelUsuario.isEmpty()) {
                                        System.out.println("El usuario seleccionado no tiene compras ni pedidos activos.");
                                    } else {
                                        System.out.println("\nPedidos del usuario:");
                                        for (Pedido p : pedidosDelUsuario) {
                                            System.out.println("- ID: " + p.getId() + " | Fecha: " + p.getFecha() + " | Estado: " + p.getEstado() + " | Total: $" + p.getTotal());
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: Formato de ID incorrecto.");
                                }
                                break;

                            case 6:
                                System.out.println("\n--- PEDIDOS FILTRADOS POR ESTADO ---");
                                System.out.println("Seleccione el estado para la consulta:");
                                System.out.println("1) PENDIENTE\n2) CONFIRMADO\n3) TERMINADO\n4) CANCELADO");
                                System.out.print("Opción: ");
                                String estadoSelec = scanner.nextLine().trim();
                                Estado estadoFiltro;
                                if (estadoSelec.equals("1")) estadoFiltro = Estado.PENDIENTE;
                                else if (estadoSelec.equals("2")) estadoFiltro = Estado.CONFIRMADO;
                                else if (estadoSelec.equals("3")) estadoFiltro = Estado.TERMINADO;
                                else if (estadoSelec.equals("4")) estadoFiltro = Estado.CANCELADO;
                                else {
                                    System.out.println("Opción inválida.");
                                    break;
                                }

                                List<Pedido> pedidosPorEstado = pedidoRepo.buscarPorEstado(estadoFiltro);
                                if (pedidosPorEstado.isEmpty()) {
                                    System.out.println("Informativo: No hay pedidos activos en estado: " + estadoFiltro);
                                } else {
                                    System.out.println("\nPedidos activos en estado " + estadoFiltro + ":");
                                    for (Pedido p : pedidosPorEstado) {
                                        String clienteNombre = "Desconocido";
                                        for (Usuario u : usuarioRepo.listarActivos()) {
                                            if (u.getPedidos().contains(p)) {
                                                clienteNombre = u.getNombre() + " " + u.getApellido();
                                                break;
                                            }
                                        }
                                        System.out.println("- ID: " + p.getId() + " | Fecha: " + p.getFecha() + " | Cliente: " + clienteNombre + " | Total: $" + p.getTotal());
                                    }
                                }
                                break;

                            default:
                                System.out.println("Opción no válida. Intenta de nuevo.");
                        }
                    }
                    break;

                // =================== SUBMENÚ REPORTES =====================
                case 5:
                    while (true) {
                        System.out.println("\n--- SUBMENÚ REPORTES ---");
                        System.out.println("1) Ver productos por categoría");
                        System.out.println("2) Ver pedidos por usuarios");
                        System.out.println("3) Ver pedidos por estado");
                        System.out.println("4) Ver total facturado");
                        System.out.println("0) Volver al menú principal");
                        System.out.print("Elige una opción: ");

                        int opcionReportes;
                        try {
                            opcionReportes = Integer.parseInt(scanner.nextLine());
                        } catch (Exception e) {
                            System.out.println("\nError: Ingrese un número válido.");
                            continue;
                        }

                        if (opcionReportes == 0) {
                            System.out.println("Saliendo del submenú de reportes...");
                            break;
                        }

                        switch (opcionReportes) {
                            case 1:
                                System.out.println("\n- Listar productos por categoria -");
                                System.out.println("Categorías disponibles:");
                                List<Categoria> listaCategoriasReporte = categoriaRepo.listarActivos();
                                if (listaCategoriasReporte.isEmpty()) {
                                    System.out.println("No hay categorías activas en el sistema.");
                                    break;
                                }

                                for (Categoria cat : listaCategoriasReporte) {
                                    System.out.println("- ID: " + cat.getId() + " - Nombre: " + cat.getNombre());
                                }
                                System.out.print("\nIngrese el ID de la categoría para ver sus productos: ");
                                try {
                                    Long idCatIngr = Long.parseLong(scanner.nextLine());
                                    Optional<Categoria> catIngrOpt = categoriaRepo.buscarPorId(idCatIngr);
                                    if (catIngrOpt.isPresent() && !catIngrOpt.get().isEliminado()) {
                                        String nombreCatElegida = catIngrOpt.get().getNombre();
                                        List<Producto> productosFiltrados = categoriaRepo.buscarProductosPorCategoria(idCatIngr);

                                        if (productosFiltrados.isEmpty()) {
                                            System.out.println("La categoría seleccionada '" + nombreCatElegida + "' no tiene productos activos asociados.");
                                        } else {
                                            System.out.println("\n--- Productos de la Categoría '" + nombreCatElegida + "' ---");
                                            for (Producto prod : productosFiltrados) {
                                                System.out.println("ID: " + prod.getId() +
                                                        " - Nombre: " + prod.getNombre() +
                                                        " - Precio: $" + prod.getPrecio() +
                                                        " - Stock: " + prod.getStock());
                                            }
                                        }
                                    } else {
                                        System.out.println("\nError: No existe ninguna categoría activa con el ID proporcionado. Intente nuevamente.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("\nError: Debe ingresar un número válido.");
                                }
                                break;

                            case 2: //ver pedidos por usuarios
                                System.out.println("\n- Listar pedidos por usuario -");
                                System.out.println("Usuarios disponibles:");
                                List<Usuario> usuariosActivos = usuarioRepo.listarActivos();
                                if (usuariosActivos.isEmpty()) {
                                    System.out.println("No hay usuarios activos registrados en el sistema.");
                                    break;
                                }
                                for (Usuario u : usuariosActivos) {
                                    System.out.println("- ID: " + u.getId() + " - Nombre: " + u.getNombre() + " " + u.getApellido());
                                }
                                System.out.print("\nIngrese el ID del usuario para consultar sus pedidos: ");
                                try {
                                    Long idUsuarioIngresado = Long.parseLong(scanner.nextLine());
                                    Optional<Usuario> usuarioSelec = usuarioRepo.buscarPorId(idUsuarioIngresado);
                                    if (usuarioSelec.isPresent() && !usuarioSelec.get().isEliminado()) {
                                        List<Pedido> pedidosPorUsuario = usuarioRepo.buscarPedidosPorUsuario(idUsuarioIngresado);
                                        if (pedidosPorUsuario.isEmpty()) {
                                            System.out.println("El usuario seleccionado no tiene registrados pedidos.");
                                        } else {
                                            System.out.println("\n- Historial de Pedidos Activos de " + usuarioSelec.get().getNombre() + " -");
                                            for (Pedido p : pedidosPorUsuario) {
                                                System.out.println("ID: " + p.getId() + " | Fecha: " + p.getFecha() + " | Estado: " + p.getEstado() + " | Total: $" + p.getTotal());
                                            }
                                        }
                                    } else {
                                        System.out.println("\nError: No existe ningún usuario con el ID proporcionado.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("\nError: Debe ingresar un ID numérico válido.");
                                }
                                break;
                            case 3: // Pedidos por estado
                                System.out.println("\n- Listar pedidos por estado -");
                                System.out.println("Seleccione el estado para filtrar:");
                                System.out.println("1) PENDIENTE\n2) CONFIRMADO\n3) TERMINADO\n4) CANCELADO");
                                System.out.print("Opción: ");
                                String estSelec = scanner.nextLine().trim();
                                Estado estadoFiltro;
                                if (estSelec.equals("1")) estadoFiltro = Estado.PENDIENTE;
                                else if (estSelec.equals("2")) estadoFiltro = Estado.CONFIRMADO;
                                else if (estSelec.equals("3")) estadoFiltro = Estado.TERMINADO;
                                else if (estSelec.equals("4")) estadoFiltro = Estado.CANCELADO;
                                else {
                                    System.out.println("Opción inválida.");
                                    break;
                                }

                                List<Pedido> pedidosPorEstado = pedidoRepo.buscarPorEstado(estadoFiltro);
                                if (pedidosPorEstado.isEmpty()) {
                                    System.out.println("No se registran pedidos activos en estado: " + estadoFiltro);
                                } else {
                                    System.out.println("\n--- Pedidos Activos en Estado [" + estadoFiltro + "] ---");
                                    for (Pedido p : pedidosPorEstado) {
                                        System.out.println("ID: " + p.getId() + " | Fecha: " + p.getFecha() + " | Total: $" + p.getTotal());
                                    }
                                }
                                break;

                            case 4: // Total Facturado con estado "TERMINADO"
                                System.out.println("\n- Total Facturado para pedidos terminados -");
                                List<Pedido> pedidosTerminados = pedidoRepo.buscarPorEstado(Estado.TERMINADO);
                                double totalFacturado = 0.0;
                                for (Pedido p : pedidosTerminados) {
                                    if (p.getTotal() != null) {
                                        totalFacturado += p.getTotal();
                                    }
                                }
                                System.out.println("\n=======================================================");
                                // Uso de Locale.US y dos decimales
                                System.out.println(String.format(java.util.Locale.US, "Total facturado: $%.2f", totalFacturado));
                                System.out.println("=======================================================");
                                break;

                            default:
                                System.out.println("Opción no válida. Intenta de nuevo.");
                                break;
                        }
                    }
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }
}