# Changelog

## 4.0.11 — Minecraft 1.21.3 / NeoForge and Fabric

### Fixes

- **Undoing a destruction no longer duplicates the blocks.** The Destruction core hands the drops of every block it
  breaks to your inventory, but undoing the destruction put the block back without taking them away, so every undo
  left a free copy of each block. Undo now takes back from your inventory the items the destruction handed out, and
  it can only be done while you still carry them. When the drops went to a Void Sack (or what the sack could not hold
  fell in the world), they cannot be taken back and that destruction can no longer be undone.

## 4.0.11 — Minecraft 1.21.3 / NeoForge and Fabric (Español)

### Corregido

- **Deshacer una destruccion ya no duplica los bloques.** El nucleo de Destruccion entrega a tu inventario los drops
  de cada bloque que rompe, pero al deshacer se devolvia el bloque sin quitar esos objetos, asi que cada deshacer
  dejaba una copia gratis de cada bloque. Ahora deshacer te quita del inventario lo que la destruccion habia entregado,
  y solo se puede hacer mientras lo sigas llevando encima. Cuando los drops fueron a una Bolsa del Vacio (o lo que la
  bolsa no pudo guardar cayo al suelo), no se pueden recuperar y esa destruccion ya no se puede deshacer.

## 4.0.10 — Minecraft 1.21.3 / NeoForge and Fabric

### Fixes

- **Shulker boxes placed with the wand kept no contents.** A shulker box that held items came out empty when the
  wand placed it, so its contents were lost. The wand now moves the data of the item (contents, custom name and
  similar) into the placed block, the same way placing the block by hand does.

## 4.0.10 — Minecraft 1.21.3 / NeoForge and Fabric (Español)

### Corregido

- **Las cajas de shulker colocadas con la varita perdian su contenido.** Una caja de shulker con objetos salia
  vacia al colocarla con la varita, y el contenido se perdia. Ahora la varita pasa los datos del objeto (contenido,
  nombre personalizado y similares) al bloque colocado, igual que al colocarlo a mano.

## 4.0.9 — Minecraft 1.21.3 / NeoForge and Fabric

### Fixes

- **The wand no longer places unlimited blocks while you carry a container.** Any carried item that exposes an item
  inventory (backpacks and similar) made the wand treat every block as available. The wand now counts what your
  inventory and the containers in it really hold, and stops when it runs out.

- **Items taken from carried containers are now really consumed.** On Fabric the wand reported the blocks as taken but
  the backpack kept them, so they were never used up. The container is now updated when the wand takes blocks from it.

## 4.0.9 — Minecraft 1.21.3 / NeoForge and Fabric (Español)

### Corregido

- **La varita ya no coloca bloques ilimitados mientras llevas un contenedor.** Cualquier objeto con inventario
  (mochilas y similares) hacia que la varita diera por disponibles todos los bloques. Ahora cuenta lo que de verdad
  hay en tu inventario y en los contenedores que llevas, y se detiene cuando se acaba.

- **Los objetos que se toman de los contenedores ahora se consumen de verdad.** En Fabric la varita daba los bloques
  por tomados pero la mochila los conservaba, asi que nunca se gastaban. Ahora el contenedor se actualiza cuando la
  varita toma bloques de el.

## 4.0.7 — Minecraft 1.21.3 / NeoForge and Fabric

### Additions

- **Fabric build for Minecraft 1.21.3.** This version now ships a NeoForge jar and a Fabric
  jar built from the same code. The Fabric build supports the recipe viewers (JEI, REI, EMI) that exist
  for this version. The integrations with other mods (storage mods, Curios, Botania) are NeoForge only.

- **Botania Revived support is back** (NeoForge): the Black Hole Talisman and the Rod of the Lands and
  Depths supply blocks to the wand, consuming one block per placement.

- **The Exchange Core now has an advancement.** It was the only core without one.

### Fixes

- **The Destruction core captures drops with the Void Sack again.** The sack is found anywhere in the
  inventory, not only in your hand. An active sack stores the drops, or sends them to its linked
  container. An inactive sack that is linked to a container still sends to it. Without either, the
  drops go straight to your inventory.

- **The Void Sack advancement showed the wrong text.** Its title and description were swapped, so the
  toast showed the whole sentence as the title. It also left the Destruction Core name untranslated in
  every language. The title is now "Void Sack" and the sentence is the description.

## 4.0.7 — Minecraft 1.21.3 / NeoForge and Fabric (Español)

### Anadido

- **Build de Fabric para Minecraft 1.21.3.** Esta version trae ahora un jar de NeoForge y
  otro de Fabric hechos con el mismo codigo. El build de Fabric soporta los visores de recetas (JEI, REI,
  EMI) que existen para esta version. Las integraciones con otros mods (mods de almacenamiento, Curios,
  Botania) son solo de NeoForge.

- **Vuelve el soporte de Botania Revived** (NeoForge): el Black Hole Talisman y la Rod of the Lands y
  Depths suministran bloques a la varita, consumiendo un bloque por colocacion.

- **El Nucleo de Intercambio ahora tiene avance.** Era el unico nucleo que no tenia.

### Corregido

- **El nucleo de Destruccion vuelve a capturar los drops con la Bolsa del Vacio.** La bolsa se encuentra
  en cualquier parte del inventario, no solo en la mano. Una bolsa activa guarda los drops, o los envia a
  su contenedor vinculado. Una bolsa inactiva vinculada a un contenedor sigue enviando a el. Sin ninguna
  de las dos cosas, los drops van directo al inventario.

- **El avance de la Bolsa del Vacio mostraba el texto equivocado.** El titulo y la descripcion estaban
  intercambiados, asi que el aviso mostraba la frase completa como titulo. Ademas dejaba sin traducir el
  nombre del Destruction Core en todos los idiomas. Ahora el titulo es "Bolsa del Vacio" y la frase es la
  descripcion.
