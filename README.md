<div align="center">

<img src="src/main/resources/assets/marovgribs/icon.png" width="96" alt="Marov Gribs">

# Marov Gribs

**Mushrooms with abilities for Minecraft.** Bouncy, glowing, blinding and shade-giving mushrooms
grow all over the world — and many of them walk: hoplings, puffers, agaric guards, dyeable
russulas, an ore-hoarding miner, and the Death Cap boss of the dark forest.

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
- **Chanterelle** — a mushroom with a fox's tail, eat it for Speed. Let a fox take one in its
  mouth and it turns into **FlynnFlyTaggart**: a fearless fox in a marine's helmet that follows
  you and hunts monsters, with a frenzy after every kill.
- **Parasol mushroom** — bone meal grows the sprout into a huge parasol 4–5 blocks tall.
  Zombies and skeletons caught in daylight run for its shade. There is also a walking one,
  4.6 blocks tall: undead under its cap don't burn.
- **Chanterelle** mob — a little funnel mushroom with a wagging fox tail; skittish like a fox,
  tempted and bred with sweet berries.
- **Russula** — a sheep-like mushroom that comes in dye colours. Dye it, shear it for caps of
  its colour; grind a cap in a **Mortar and Pestle** for two dyes (other mushrooms work too).
- **Miner Mushroom** — digs tunnels underground, collects ore in its backpack and lights the
  way with glowcaps. Walls itself in when it sees you; dig it out to get the ore.
- **Cordyceps Zombie** — a few wild zombies come out overgrown: a bit tougher and faster.
- **Death Cap** — the dark forest boss: deadly spore bursts, rooting threads, summoned minions,
  and a rage that heals it on mycelium. Drops its cap, a helmet that keeps poison off.
- **Forest Mycelium** grows outside mushroom islands; mushrooms on it spread five times faster.
  Dense beds of one mushroom come alive and spawn its keeper mob.
- Once in a long while an agaric guard is born with a smiley face.
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
| **Лисичка** | тайга, роща | гриб с лисьим хвостом; если лиса возьмёт её в рот, превратится во FlynnFlyTaggart | скорость, 30 с |
| **Гриб-зонтик** | равнины, саванна, луга | костная мука выращивает из ростка огромный зонтик в 4–5 блоков; в его тени прячется нежить | сытный |

### Грибница
Своя грибница, которая встречается не только на грибных островах: пятнами в тёмном лесу,
тайгах, старом березняке и на болотах. Расползается по земле, как мицелий; грибы на ней
разрастаются в пять раз быстрее. С неё в шёлковом касании падает она сама, иначе земля.

### Оживающие грядки
Если рядом растёт много одинаковых грибов (10+ в квадрате 9×9), один из них иногда
превращается в своего хранителя. Больше двух хранителей одного вида на грядку не будет.

| Много… | Появляется |
|--------|------------|
| мухоморов | мухоморыч |
| прыгунков | прыгунец |
| дождевиков | пыхтун |
| светогрибов | гриб-шахтёр |
| лисичек | лисичка |
| ростков гриба-зонтика | гриб-зонтик |

### Мобы
| Моб | Где появляется | Поведение |
|-----|----------------|-----------|
| **Прыгунец** | леса, луга, грибные поля | мирный, ходит прыжками, не получает урона от падения, при опасности убегает огромными скачками; размножается прыгунками и иногда сам роняет прыгунок |
| **Пыхтун** | тёмный лес, болота, старые тайги, пышные пещеры | враждебный: подходит, раздувается и выпускает облако спор (яд и слепота), сам при этом не умирает; если ударить вблизи — пыхает сразу |
| **Мухоморыч** | тёмный лес, старые тайги, грибные поля | нейтральный страж: нападает, если ударить его или сорвать мухомор рядом (16 блоков), зовёт на помощь всю группу; удар отравляет. Один из 200 рождается со смайликом вместо злой морды |
| **Сыроежка** | равнины, леса, березняки, луга, тайга | как овца: шляпка разных цветов, красится красителем, стрижётся ножницами (1–3 сыроежки её цвета), отрастает, когда она пощиплет траву; размножается костной мукой |
| **Гриб-шахтёр** | под землёй, ниже y=40, в темноте | мирный: сам роет тоннели (с поверхности — лестницей вниз), собирает руду в рюкзак и ставит светогрибы. Увидев игрока или получив удар, замуровывается булыжником и ждёт, пока всё стихнет. Если до него докопаться и победить — высыпет всю собранную руду. Копает только природные блоки, постройки не трогает |
| **Гриб-зонтик** | равнины, саванна, луга (редко) | мирный великан высотой около 4,6 блока с пёстрой змеиной ножкой; медленно бродит. Нежить под его шляпкой не горит на солнце, а днём сама приходит к нему в тень. Роняет ростки зонтика |
| **Лисичка** | тайга, роща | оранжевый гриб-воронка с лисьим хвостом, хвост виляет. Пугливая, как лиса: убегает от игрока, если тот не крадётся. Приманивается и размножается сладкими и светящимися ягодами. Роняет лисички |
| **Зомби с кордицепсом** | вместо 6% обычных зомби | чуть крепче, быстрее и больнее бьёт; роняет кордицепс |
| **FlynnFlyTaggart** | из лисы, взявшей лисичку | бесстрашная лиса в каске морпеха: не спит, никого не боится, ходит за игроком, который был рядом при превращении, охотится на монстров (кроме криперов), после каждого убийства на 5 с ускоряется и усиливается |

### Босс: бледная поганка
Редко появляется ночью в тёмном лесу, не больше одной на округу (128 блоков). 250 здоровья,
полоска босса.

- Бьёт ядовито, каждые несколько секунд выпускает смертельное облако спор (яд II, иссушение,
  тошнота).
- Опутывает цель нитями грибницы: замедление V и яд.
- На 2/3 здоровья зовёт трёх пыхтунов, на 1/3 — двух мухоморычей.
- Ниже половины здоровья впадает в ярость: атакует чаще, превращает землю вокруг в грибницу
  и лечится, стоя на ней. Уведи её с грибницы.
- Иммунна к яду, иссушению, тошноте и замедлению. Роняет **шляпу поганки**: шлем, через который
  не берут яд и облака спор.

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
| **Ступка с пестиком** | палка + 3 камня буквой V | на верстаке растирает гриб в 2 красителя и изнашивается на 1 (64 раза) |
| **Краситель из ступки** | ступка + сыроежка | цвет сыроежки; ещё мухомор → красный, лисичка → оранжевый, прыгунок → лаймовый, светогриб → бирюзовый, дождевик → белый |
| **Кордицепс** | падает с зомби с кордицепсом | если съесть: спешка на 60 с и скорость на 30 с |
| **Шляпа поганки** | падает с босса | шлем: яд и облака спор не действуют |

Всё есть во вкладке «Грибы Марова» в творческом режиме, вместе с яйцами призыва и сыроежками
всех 16 цветов. У каждого предмета есть подсказка, что он делает.

### Как быстро всё посмотреть
Творческий мир, затем:

```
/summon marovgribs:russula
/summon marovgribs:miner_grib
/summon marovgribs:death_cap
/summon marovgribs:flynn_fox
/summon marovgribs:cordyceps_zombie
/summon marovgribs:parasol_grib
/summon marovgribs:chanterelle_grib
/summon marovgribs:agaric_guard ~ ~ ~ {Smiley:1b}
```

Чтобы увидеть превращение, брось лисичку рядом с лисой. Чтобы вырастить зонтик, посади росток
на землю и полей костной мукой.

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
