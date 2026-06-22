package com.tp.jpa;

import com.tp.jpa.model.entities.Categoria;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Repositorios para los módulos
        CategoriaRepository categoriaRepo = new CategoriaRepository();
        ProductoRepository productoRepo = new ProductoRepository();

        // Menú Principal Estructurado
        while (true) {
            System.out.println("\n-- Menú Principal --");
            System.out.println("1) Gestion de Categorías");
            System.out.println("2) Gestion de Productos");
            System.out.println("3) Reportes");
            System.out.println("0) Salir");
            System.out.print("Elige una opción: ");

            int seleccion;
            try {
                seleccion = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("\nError: Ingrese un número válido (0, 1, 2 o 3).");
                continue;
            }
            if (seleccion == 0) {
                System.out.println("Saliendo del programa.");
                break;
            }
            switch (seleccion) {
                // SUBMENÚ CATEGORÍAS
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
                                    System.out.println("¡Categoría guardada exitosamente! ID: " + categoriaGuardada.getId());                                }
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
                                        // Al no haber un break acá, el while vuelve a empezar
                                    }
                                }

                                break;

                            case 3:
                                System.out.println("\n--- MODIFICAR CATEGORÍA ---");
                                List<Categoria> categoriasActivas = categoriaRepo.listarActivos();
                                System.out.println("Categorías disponibles: ");
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

                // SUBMENÚ PRODUCTOS

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
                                List<Producto> productosActivos = productoRepo.listarActivos();
                                System.out.println("Productos disponibles: ");
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


                // SUBMENÚ REPORTES

                case 3:
                    while (true) {
                        System.out.println("\n--- SUBMENÚ REPORTES ---");
                        System.out.println("1) Ver productos por categoría");
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
                                List<Categoria> listaCategoriasReporte = categoriaRepo.listarActivos();
                                if (listaCategoriasReporte.isEmpty()) {
                                    System.out.println("No hay categorías activas en el sistema.");
                                    break;
                                }
                                System.out.println("Categorías disponibles:");
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

                            default:
                                System.out.println("Opción no válida. Intenta de nuevo.");
                        }
                    }
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }
}