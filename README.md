<div align="center">

<img src="src/main/resources/assets/marovgribs/icon.png" width="96" alt="Marov Gribs">

# Marov Gribs

**Mushrooms with abilities for Minecraft.** Bouncy, glowing and blinding mushrooms grow all over
the world — and some of them walk: hopping hoplings, puffers that burst into spore clouds and
agaric guards that defend their patch.

<img src="art/previews/all.png" width="560" alt="Mushrooms and mushroom mobs">

![build](https://github.com/lolvk61/MarovGribs/actions/workflows/build.yml/badge.svg)

NeoForge • Minecraft 1.21.1 • Java 21 • [Download](../../releases/latest)

[English](#english) | [Русский](#русский)

</div>

---

## English

### Features
- **Hopcap** — tosses whoever steps on it into the air and cushions the landing. Sneak to walk
  over it. Eat it for Jump Boost II.
- **Glowcap** — lights up caves and grows even on bare stone. Eat it for Night Vision.
- **Puffball** — a natural trap: puffs a blinding, slowing spore cloud at anything that walks
  into it, then refills. Sneak past it safely. Eat it for Slow Falling.
- **Fly Agaric** — Strength II, at the price of nausea and poison.
- **Hopling** — a passive walking hopcap. Hops around, takes no fall damage, flees in huge
  leaps, breeds on hopcaps and now and then sheds a fresh one.
- **Puffer** — a hostile puffball. Waddles up, swells and bursts into a poison cloud without
  dying; hit it in melee and it puffs at once.
- **Agaric Guard** — a neutral fly agaric warrior. Pick a fly agaric near one and the whole
  group comes at you with poisonous punches.
- **Spore clouds** — puffy clouds that pop out, breathe and fade away, and affect everything
  inside them, not just the ground.
- **Spore Bomb** and **Marov Stew** — a throwable spore cloud and a stew with the best of all
  four mushrooms.
- Mushrooms spread on their own and sprout from bone meal; every item explains its ability in
  its tooltip (English and Russian).

### Installation
1. Install [NeoForge](https://neoforged.net/) for Minecraft **1.21.1**.
2. Drop the [mod jar](../../releases/latest) into your `mods` folder.

### Building
```
./gradlew build                # the mod -> build/libs/
./gradlew runGameTestServer    # in-game tests
```

---

## Русский

Мод про грибы со способностями в духе Alex's Mobs: грибы растут по всему миру и дают
эффекты, а некоторые из них ходят сами.

### Грибы
Все грибы медленно расползаются сами, от костной муки рядом вырастает ещё один.

| Гриб | Где растёт | Способность | Если съесть |
|------|------------|-------------|-------------|
| **Прыгунок** | леса, березняки, луга, грибные поля | подбрасывает того, кто наступит, и гасит урон от падения; крадучись не срабатывает | прыгучесть II, 30 с |
| **Светогриб** | пещеры верхнего мира, растёт и на камне | светится (уровень 12), летят искорки | ночное зрение, 60 с |
| **Дождевик** | равнины, луга, болота, тёмный лес | пыхает облаком спор: слепота и замедление II всем, кто войдёт; перезаряжается 5 с | плавное падение, 15 с |
| **Мухомор** | тёмный лес, тайга, березняки, грибные поля | если сорвать рядом с мухоморычами, они нападут | сила II, но тошнота и яд |

### Мобы
| Моб | Где появляется | Поведение |
|-----|----------------|-----------|
| **Прыгунец** | леса, луга, грибные поля | мирный, ходит прыжками, не получает урона от падения, при опасности убегает огромными скачками; размножается прыгунками и иногда сам роняет прыгунок |
| **Пыхтун** | тёмный лес, болота, старые тайги, пышные пещеры | враждебный: подходит, раздувается и выпускает облако спор (яд и слепота), сам при этом не умирает; если ударить вблизи — пыхает сразу |
| **Мухоморыч** | тёмный лес, старые тайги, грибные поля | нейтральный страж: нападает, если ударить его или сорвать мухомор рядом (16 блоков), зовёт на помощь всю группу; удар отравляет |

### Облака спор
Дождевик, пыхтун и споровая бомба выпускают пушистое облачко: оно пыхает, «дышит» и через
несколько секунд тает. Эффекты действуют на всё внутри облака, а не только у земли. Цвет
зависит от источника: кремовое у дождевика, жёлто-зелёное ядовитое у пыхтуна, серое у бомбы.

<img src="art/previews/spore_clouds.png" width="560" alt="Облака спор">

### Предметы
| Предмет | Рецепт | Что делает |
|---------|--------|------------|
| **Споровая бомба** | 2 дождевика + нить → 2 бомбы | бросается, облако ослепляет, замедляет и ослабляет |
| **Похлёбка Марова** | миска + все четыре гриба | прыгучесть, ночное зрение, плавное падение и сила без отравы |

Всё есть во вкладке «Грибы Марова» в творческом режиме, вместе с яйцами призыва. У каждого
предмета есть подсказка, что он делает.

### Как быстро всё посмотреть
Творческий мир, затем:

```
/summon marovgribs:hopling
/summon marovgribs:puffer
/summon marovgribs:agaric_guard
```

### Модели и текстуры в Blender
Всё нарисовано скриптом [art/gribs_art.py](art/gribs_art.py) в Blender, сцена лежит в
[art/marovgribs.blend](art/marovgribs.blend).

1. Открой `art/marovgribs.blend`. Каждый моб — пустышка-корень, под ней пустышки-кости
   (`hopling:cap` и т. п., точка пустышки — шарнир), под костями кубы.
2. Двигай и масштабируй кубы, двигай и вращай кости. Цвета и раскраска — в Object Properties →
   Custom Properties (`col`, `col2`, `paint`, `face`).
3. Сохрани и экспортируй:

```
blender -b art/marovgribs.blend --python art/gribs_art.py -- export
```

Экспорт сам раскладывает развёртку, рисует текстуры мобов, комочки облаков и иконку, пишет
Java-классы геометрии в `client/model/geo` и обновляет превью в `art/previews`. Классы в `geo`
руками не правь: они перезаписываются. Анимации живут в `client/model/*Model.java`. Спрайты
грибов 16×16 описаны в скрипте (словарь `SPRITES`, по символу на пиксель).

Пересобрать сцену с нуля из описаний в скрипте (затрёт ручные правки в `.blend`):

```
blender -b --factory-startup --python art/gribs_art.py -- build
```

### Как добавить новый гриб
1. Спрайт в `SPRITES` (и описание в `MOBS`, если нужен моб) в `art/gribs_art.py`, затем экспорт.
2. Блок в `registry/ModBlocks.java` (наследник `GribBlock` со своей способностью), предмет с
   едой в `registry/ModItems.java`.
3. Ресурсы: blockstate, модели блока и предмета, строки в `lang/ru_ru.json` и `lang/en_us.json`
   (ключ `.desc` — подсказка), лут, генерация в `data/marovgribs/worldgen` и
   `neoforge/biome_modifier`.
4. Тест в `gametest/GribTests.java`.

### Сборка
```
./gradlew build                # мод   -> build/libs/
./gradlew runGameTestServer    # игровые тесты
./gradlew runClient            # клиент для разработки
```
Нужен Java 21.

---

*Вдохновлено Alex's Mobs. Модели, текстуры и иконка нарисованы скриптом в Blender.
Лицензия кода — MIT.*
