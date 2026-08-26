# Inventarios de Equipos de Computo

Proyecto en Java (Swing + JDBC + MySQL) para el entregable de POO / Bases de Datos.

## Estructura

```
InventarioUTEZ/
├── sql/
│   └── script_BD.sql   -> crea la base de datos completa
└── src/main/java/
    ├── Main.java     -> arranca la aplicacion (abre el login)
    ├── modelo/       -> clases POJO
    ├── dao/          -> acceso a datos (JDBC)
    └── gui/          -> ventanas Swing
```

## Como ejecutarlo

1. Corre `sql/script_BD.sql` en MySQL. Crea la base `inventarios_equipos_computo` con tablas y catalogos.
2. Agrega el conector "MySQL Connector/J" como libreria del proyecto en tu IDE.
3. En `dao/ConexionBD.java`, ajusta `USUARIO` y `PASSWORD` a los de tu MySQL.
4. Corre `Main.java`. Primero pide iniciar sesion (usuario `root`, contrasena `1234`), y despues abre la ventana principal.

## Funcionalidad

- **Equipos**: registrar, editar, cambiar de ubicacion y eliminar (baja logica: no desaparece de la base, solo se oculta y su historial se conserva).
- **Ubicaciones**: agregar, editar y eliminar (tambien baja logica, por la misma razon).
- **Historial**: cambios de ubicacion y de estado de cada equipo, mas la lista de equipos eliminados.
- **Login**: pantalla de acceso antes de entrar a la aplicacion.
