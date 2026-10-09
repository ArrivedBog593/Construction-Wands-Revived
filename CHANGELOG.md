# Changelog

## 4.0.7 — Minecraft 26.3 / NeoForge and Fabric

### Additions

- **Fabric build for Minecraft 26.3.** This version now ships a NeoForge jar and a Fabric
  jar built from the same code. The Fabric build supports the recipe viewers (JEI, REI, EMI) that exist
  for this version. The integrations with other mods (storage mods, Curios, Botania) are NeoForge only.

- **Applied Energistics 2 support is back** (NeoForge): wireless terminals and portable cells, plus
  AE2 Wireless Terminals and Sophisticated Backpacks.

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

- **Sophisticated Backpacks are read through the same inventory layer as the other containers**, which
  also works with the item transfer API of this version.

- **The Fabric mixin configuration was malformed.** The extra client mixins were joined with a literal
  `\n` instead of a line break, which is not valid JSON.

### Changes

- The files are now named `ConstructionWandsRevived-26.3-4.0.7-<NeoForge|Fabric>.jar`.

## 4.0.7 — Minecraft 26.3 / NeoForge and Fabric (Español)

### Anadido

- **Build de Fabric para Minecraft 26.3.** Esta version trae ahora un jar de NeoForge y
  otro de Fabric hechos con el mismo codigo. El build de Fabric soporta los visores de recetas (JEI, REI,
  EMI) que existen para esta version. Las integraciones con otros mods (mods de almacenamiento, Curios,
  Botania) son solo de NeoForge.

- **Vuelve el soporte de Applied Energistics 2** (NeoForge): terminales inalambricas y celdas portatiles,
  ademas de AE2 Wireless Terminals y Sophisticated Backpacks.

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

- **Las Sophisticated Backpacks se leen con la misma capa de inventarios que el resto de contenedores**,
  lo que tambien funciona con la API de transferencia de items de esta version.

- **La configuracion de mixins de Fabric estaba mal formada.** Los mixins extra del cliente se unian con
  un `\n` literal en vez de un salto de linea, lo cual no es JSON valido.

### Cambios

- Los archivos ahora se llaman `ConstructionWandsRevived-26.3-4.0.7-<NeoForge|Fabric>.jar`.

## 4.0.1 — Minecraft 26.3 / NeoForge

### Additions

- **JEI is back.** JEI now ships for 26.3, so the item descriptions (wand stats, core
  descriptions and the Void Sack entry) show up in JEI again. Reliable Recipe Viewer stays
  supported: both register the same texts and can be installed together.
- **Curios is back.** Wands pull blocks again from containers equipped in Curios slots.

---

## 4.0.1 — Minecraft 26.3 / NeoForge (Español)

### Añadido

- **Vuelve JEI.** JEI ya tiene build para 26.3, así que las descripciones de los ítems
  (estadísticas de las varitas, descripción de los núcleos y la entrada de la Bolsa del Vacío)
  vuelven a salir en JEI. Reliable Recipe Viewer sigue soportado: los dos registran los mismos
  textos y se pueden tener instalados a la vez.
- **Vuelve Curios.** Las varitas vuelven a sacar bloques de los contenedores equipados en slots
  de Curios.

---

## 4.0.0 — Minecraft 26.3 / NeoForge

This release is a port of the 1.21 line, brought over for parity across versions: the fixes and
changes below were developed on 1.21.1 and 26.1 and carried here so every supported version
behaves the same.

Port to Minecraft 26.3, branched from the 26.2 tree. Carries the same fixes as 4.0.3 on
26.2 (modded blocks that store data on the item, block entity data preserved on placement,
redrawn golden wand).

### Additions

- **Wooden wand.** A new entry-tier wand, below stone: 59 uses, 5 blocks per placement, and no
  core slots at all. It is crafted from **cherry or birch planks** only, and its texture uses the
  same silhouette and shading as the rest of the set, in cherry tones.

### Build

- Minecraft `26.3`, NeoForge `26.3.0.0-beta` (declared range: `[26.3.0.0-beta,26.4.0)`).
- **Build system migrated from NeoGradle to ModDevGradle `2.0.147`.** NeoGradle `7.1.38`,
  the newest release, cannot build 26.3: its `neoFormRecompile` step fails while recompiling
  decompiled *vanilla* source (`net/minecraft/core/HolderSet.java`). ModDevGradle uses
  NeoForm Runtime and the artifacts NeoForge already publishes, so that step never runs.
  The previous build script is kept as `build.gradle.neogradle.bak`.

### Ported

- **Minecraft 26.3 replaced GLFW with SDL.** `org.lwjgl.glfw` is no longer on the classpath
  (`lwjgl-sdl` is), so the keybinds now use Mojang's own `InputConstants` key codes instead
  of raw `GLFW.GLFW_KEY_*` constants. Same default keys.
- **Recipes and advancements are now reloadable datapack registries.** `RecipeProvider` takes
  a `BootstrapContext` pair instead of a `PackOutput`, `RecipeProvider.Runner` is gone, and
  `AdvancementProvider` is a `SingleRegistryBootstrap<Advancement>` built from
  `AdvancementSubProvider.Factory` list. Data generation is wired through a
  `RegistrySetBuilder` handed to `DatapackBuiltinEntriesProvider.forReloadableLayer`.
- `RecipeProvider` exposes a `HolderGetter<Item>` (`items`) instead of the full
  `HolderLookup.Provider`, and `ItemPredicate.Builder#of` takes that getter.
- `Player#drop` now requires a `Prediction`; the undo history drops server-side.
- `BundleContents#itemCopyStream` was renamed to `itemCopies`.
- `FriendlyByteBuf`'s `read/writeBlockHitResult` helpers are gone; the preview packet uses
  `BlockHitResult.STREAM_CODEC`.

### Recipe viewer

JEI has no 26.3 build (`jei-26.3-neoforge` is not on the maven), so the item descriptions
are provided through **Reliable Recipe Viewer** instead, which does ship for 26.3.
`RrvIntegration` registers exactly the same texts the JEI plugin did — wand stats, core
descriptions and the Void Sack entry — translated from JEI's `addIngredientInfo` to RRV's
info recipes, rebuilt on every client reload so they follow keybind and config changes.

It is an optional client-side dependency: `ClientHandler` only touches the integration
behind `ModList.isLoaded("rrv")`, so nothing is loaded when RRV is absent.

The JEI plugin is **not deleted** — it stays in `src/disabled-integrations/`, and its
dependency lines in `build.gradle` and `neoforge.mods.toml` are commented out rather than
removed, ready to switch back on when JEI ships for 26.3. Both viewers can coexist then.

RRV is pulled from the Modrinth maven (`maven.modrinth:rrv:8.10.5+26.3-neoforge`) because
`maven.cassian.cc` has not published 8.10.5 yet, and 8.10.5 is the first release with 26.3
support.

### Removed (temporarily)

| Integration | Mod | Reason |
|---|---|---|
| Recipe descriptions | JEI | no 26.3 build; covered by Reliable Recipe Viewer in the meantime |

Sophisticated Backpacks and Sophisticated Core moved from `implementation` to `compileOnly`.
Their published builds declare `minecraft 26.2 or above, and below 26.3`, so FML refuses to
start a dev run with them on the runtime classpath. The integration still compiles and the
published jar is unchanged — those mods were never bundled, and `ContainerRegistrar` already
guards the handler behind `ModList.isLoaded("sophisticatedbackpacks")`. Switch them back to
`implementation` once a 26.3 build ships.

Everything 26.2 already had parked in `src/disabled-integrations/` stays parked. The JEI
plugin joins it there, and its dependency lines in `build.gradle` and `neoforge.mods.toml`
are commented out, ready to switch back on when JEI ships for 26.3.

### Fixes

- **Blocks that draw themselves from model data now render correctly when placed by a wand.**
  Functional Storage drawers were the visible case: a framed drawer placed by the wand appeared
  without its frame until something else forced the chunk to redraw, such as placing a block next
  to it. The wand places blocks server-side only, so the client never runs the placement
  prediction that makes those mods refresh their model data. The wand now tells the client which
  positions to refresh, retrying for the block entities the client has not created yet.

- **The golden wand had no translation.** Its name showed as `item.constructionwand.gold_wand`
  and it had no advancement, in every language.

- **The Void Sack tooltip showed a raw `%1$s`** instead of the toggle key, because the
  translation argument was never passed.

- **Mod icon added**, so the mod shows its own icon in the mods list instead of the default block.

### Documentation

- The README wand table was missing wood, gold and, in some versions, netherite, and several of its
  values did not match the config. It now lists all seven wands with the real values and adds the
  Exchange column. Image links point at this repository instead of the upstream one.
- New images: the wooden and golden wand recipes, and a stats table.

---
