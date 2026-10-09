# Changelog

## 4.0.0 — Minecraft 1.20.1 / Forge and Fabric

### Additions

- **Fabric port for 1.20.1.** The same gameplay as the Forge build: all the wands and cores, the options
  screen, the keybinds, undo, the block preview, the Void Sack, recipes, advancements and the creative
  tab. JEI, REI and EMI are supported. Shulker boxes, bundles and block inventories work as supply
  containers through Fabric's transfer API.

  Limits of the Fabric build: containers that live inside an item, such as backpacks, are not supported.
  The server configuration is not sent to the clients, so the preview and the durability bar use the
  client's own configuration file. The integrations with Botania, Curios, Applied Energistics 2,
  Sophisticated Backpacks and Refined Storage are Forge only.

### Fixes

- **The Destruction core captures drops with the Void Sack again.** The sack is found anywhere in the
  inventory, not only in your hand. An active sack stores the drops, or sends them to its linked
  container. An inactive sack that is linked to a container still sends to it. Without either, the
  drops go straight to your inventory.

### Changes

- The code shared by both loaders now lives in a common module, and the files are named
  `ConstructionWandsRevived-1.20.1-4.0.0-<Forge|Fabric>.jar`.

## 4.0.0 — Minecraft 1.20.1 / Forge and Fabric (Español)

### Anadido

- **Port a Fabric para 1.20.1.** La misma jugabilidad que el build de Forge: todas las varitas y nucleos,
  la pantalla de opciones, las teclas, deshacer, el preview de bloques, la Bolsa del Vacio, las recetas,
  los avances y la pestana creativa. Se soportan JEI, REI y EMI. Los shulker box, los bundles y los
  inventarios de bloques funcionan como contenedores de suministro mediante la API de transferencia de
  Fabric.

  Limites del build de Fabric: no se soportan los contenedores que viven dentro de un item, como las
  mochilas. La configuracion del servidor no se envia a los clientes, asi que el preview y la barra de
  durabilidad usan el archivo de configuracion del propio cliente. Las integraciones con Botania, Curios,
  Applied Energistics 2, Sophisticated Backpacks y Refined Storage son solo de Forge.

### Corregido

- **El nucleo de Destruccion vuelve a capturar los drops con la Bolsa del Vacio.** La bolsa se encuentra en
  cualquier parte del inventario, no solo en la mano. Una bolsa activa guarda los drops, o los envia a su
  contenedor vinculado. Una bolsa inactiva vinculada a un contenedor sigue enviando a el. Sin ninguna de las
  dos cosas, los drops van directo al inventario.

### Cambios

- El codigo compartido por ambos loaders ahora vive en un modulo comun, y los archivos se llaman
  `ConstructionWandsRevived-1.20.1-4.0.0-<Forge|Fabric>.jar`.
