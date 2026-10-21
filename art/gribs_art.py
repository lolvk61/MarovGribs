# -*- coding: utf-8 -*-
"""Marov Gribs: модели, текстуры и превью в Blender.

Собрать сцену заново из описаний MOBS/SPRITES ниже (перезаписывает art/marovgribs.blend):
    blender -b --factory-startup --python art/gribs_art.py -- build

Экспорт из .blend, который ты поправил руками:
    blender -b art/marovgribs.blend --python art/gribs_art.py -- export

Как устроена сцена: у каждого моба есть пустышка-корень (custom property "geo_class"),
под ней пустышки-кости (свойство "part"; это ModelPart в игре, точка пустышки = шарнир),
под костями кубы (свойства "paint", "col", "col2", "face", "gill" задают раскраску).
Кубы можно двигать и масштабировать, кости двигать и вращать. Развёртка текстур
считается заново при каждом экспорте, рисовать UV руками не нужно.

1 метр в Blender = 1 блок, 1 пиксель модели = 1/16 м. Перед моба смотрит на -Y (вид спереди, Numpad 1).

Экспорт пишет:
  src/main/resources/assets/marovgribs/textures/entity/<моб>.png
  src/main/resources/assets/marovgribs/textures/block|item/<гриб>.png
  src/main/java/ru/marov/gribs/client/model/geo/<Моб>Geo.java
  art/previews/*.png
"""
import math
import os
import random
import struct
import sys
import zlib

import bpy
from mathutils import Quaternion, Vector

ART = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(ART)
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "marovgribs")
GEO_DIR = os.path.join(ROOT, "src", "main", "java", "ru", "marov", "gribs", "client", "model", "geo")
BLEND = os.path.join(ART, "marovgribs.blend")
PREVIEWS = os.path.join(ART, "previews")
PX = 1.0 / 16.0


# ---------------------------------------------------------------------------
# Описания мобов. Координаты как в Minecraft: пиксели, Y вниз, земля на y=24,
# перед моба на -Z, левый бок моба на +X.
# part: (имя, родитель, шарнир, поворот в градусах (x, y, z), кубы)
# ---------------------------------------------------------------------------

def cube(origin, size, paint, col, col2="", face="", gill=""):
    return {"origin": origin, "size": size, "paint": paint, "col": col, "col2": col2, "face": face, "gill": gill}


BALL, BALL_DOTS = "EDE6CF", "A68F62"

MOBS = [
    {"name": "hopling", "geo_class": "HoplingGeo", "x": -1.3, "parts": [
        ("body", None, (0, 24, 0), (0, 0, 0), [
            cube((-2, -6, -2), (4, 6, 4), "stem", "E9E3C6", face="cute"),
            cube((-2.5, -2, -2.5), (5, 2, 5), "stem", "DCD4B4"),
        ]),
        ("cap", "body", (0, -6, 0), (0, 0, 0), [
            cube((-4, -4, -4), (8, 4, 8), "cap", "6DBE34", "B6E86A", gill="D9EBB0"),
            cube((-3, -5, -3), (6, 1, 6), "cap_top", "6DBE34", "B6E86A"),
        ]),
    ]},
    {"name": "puffer", "geo_class": "PufferGeo", "x": 0.0, "parts": [
        ("body", None, (0, 21, 0), (0, 0, 0), [
            cube((-5, -9, -5), (10, 9, 10), "ball", BALL, BALL_DOTS),
            cube((-4, -10, -4), (8, 1, 8), "ball", BALL, BALL_DOTS),
            cube((-4, 0, -4), (8, 1, 8), "ball", "DCD3B8", BALL_DOTS),
            cube((-6, -8, -4), (1, 7, 8), "ball", "E2DAC1", BALL_DOTS),
            cube((5, -8, -4), (1, 7, 8), "ball", "E2DAC1", BALL_DOTS),
            cube((-4, -8, 5), (8, 7, 1), "ball", "E2DAC1", BALL_DOTS),
            cube((-4, -8, -6), (8, 7, 1), "ball", BALL, BALL_DOTS, face="angry"),
        ]),
        ("mouth", "body", (0, -10, 0), (0, 0, 0), [
            cube((-1.5, -1, -1.5), (3, 1, 3), "hole", "D8CDB0"),
        ]),
        ("right_leg", None, (-2.5, 21, 0), (0, 0, 0), [
            cube((-1.5, 0, -1.5), (3, 3, 3), "leg", "CFC4A2"),
        ]),
        ("left_leg", None, (2.5, 21, 0), (0, 0, 0), [
            cube((-1.5, 0, -1.5), (3, 3, 3), "leg", "CFC4A2"),
        ]),
    ]},
    {"name": "agaric_guard", "geo_class": "AgaricGuardGeo", "x": 1.5, "parts": [
        ("body", None, (0, 18, 0), (0, 0, 0), [
            cube((-3.5, -9, -3), (7, 9, 6), "stem", "F1ECDD", face="grumpy"),
            cube((-4, -9, -3.5), (8, 1, 7), "skirt", "FFFFFF"),
        ]),
        ("cap", "body", (0, -9, 0), (0, 0, 0), [
            cube((-7, -4, -7), (14, 4, 14), "cap", "D7322B", "FFFFFF"),
            cube((-5, -6, -5), (10, 2, 10), "cap_top", "D7322B", "FFFFFF"),
        ]),
        ("right_arm", None, (-5, 11, 0), (0, 0, 0), [
            cube((-1.5, -1, -1.5), (3, 7, 3), "leg", "E6E0CE"),
        ]),
        ("left_arm", None, (5, 11, 0), (0, 0, 0), [
            cube((-1.5, -1, -1.5), (3, 7, 3), "leg", "E6E0CE"),
        ]),
        ("right_leg", None, (-2, 18, 0), (0, 0, 0), [
            cube((-1.5, 0, -1.5), (3, 6, 3), "leg", "E6E0CE"),
        ]),
        ("left_leg", None, (2, 18, 0), (0, 0, 0), [
            cube((-1.5, 0, -1.5), (3, 6, 3), "leg", "E6E0CE"),
        ]),
    ]},
]

# Лица рисуются на передней грани куба: (первая строка, сетка). k глаз, b бровь, m рот, p румянец, w блик.
FACES = {
    "cute": (1, ["k..k",
                 "p..p",
                 ".mm."]),
    "angry": (1, [".b....b.",
                  "..b..b..",
                  ".kk..kk.",
                  ".kw..wk.",
                  "........",
                  "..mmmm.."]),
    "grumpy": (2, [".b...b.",
                   "..b.b..",
                   "..k.k..",
                   "..k.k..",
                   ".......",
                   "..mmm..",
                   ".m...m."]),
}
FACE_COLORS = {"k": "1E1A16", "b": "3A2A1E", "m": "6A3428", "p": "F2A1A1", "w": "FFFFFF"}

# ---------------------------------------------------------------------------
# Спрайты 16x16 для блоков и предметов. "." прозрачный.
# ---------------------------------------------------------------------------
EMPTY = "................"
SPRITES = {
    "block/hopcap": ({"k": "2F5E17", "G": "6DBE34", "g": "4E9426", "L": "B6E86A", "S": "E9E3C6", "s": "C9C1A0"}, [
        EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        ".....kkkkkk.....",
        "....kGLGGGGk....",
        "...kGLLGGLGGk...",
        "..kGGGGGGLLGGk..",
        "..kggggggggggk..",
        "....kksSSskk....",
        "......SSSs......",
        ".....SSSs.......",
        "......SSSs......",
        ".......SSSs.....",
        ".....sSSSSSs....",
    ]),
    "block/glowcap": ({"k": "0F4F4B", "C": "3FD8C8", "c": "23A39A", "W": "D8FFF8", "S": "A8DCD2", "s": "7FB5AC"}, [
        EMPTY, EMPTY, EMPTY,
        "....kk..........",
        "...kCWk.........",
        "..kCCWCk........",
        "..kWCCCk........",
        ".kCCCWCCk.......",
        ".kCWCCCWk.......",
        "kccccccccck.....",
        "....sSs....kk...",
        "....sSs...kCWk..",
        "....sSs..kCCCWk.",
        "....sSs..kcccck.",
        "....sSs....sS...",
        "...sSSSs...sSs..",
    ]),
    "block/puffball": ({"k": "7A6544", "P": "EDE6CF", "p": "CFC4A2", "D": "A68F62", "H": "FFFFF6"}, [
        EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        ".....kkkkkk.....",
        "....kHHPPPPk....",
        "...kPHPPPDPPk...",
        "..kPPPPPPPPPpk..",
        "..kPDPPPPPDPpk..",
        "..kPPPPPPPPPpk..",
        "..kpPPPDPPPppk..",
        "...kpPPPPPppk...",
        "....kpppppkk....",
        ".....kkkkkk.....",
    ]),
    "block/fly_agaric": ({"k": "5A0F0C", "R": "D7322B", "r": "9E1F1A", "W": "FFFFFF", "G": "E8DEC0", "S": "F1ECDD", "s": "CFC7B0"}, [
        EMPTY, EMPTY, EMPTY, EMPTY,
        ".....kkkkkk.....",
        "...kkRRWRRRkk...",
        "..kRWRRRRRWRRk..",
        ".kRRRRRWWRRRRRk.",
        ".kRWWRRRRRRWRRk.",
        ".krrrrrrrrrrrrk.",
        "...kGGGGGGGGk...",
        "......sSSs......",
        ".....sSSSSs.....",
        "......sSSs......",
        "......sSSs......",
        ".....sSSSSs.....",
    ]),
    "item/spore_bomb": ({"t": "D9D2C0", "k": "7A6544", "P": "EDE6CF", "p": "CFC4A2", "D": "A68F62", "H": "FFFFF6", "g": "9BC46A"}, [
        EMPTY,
        ".......tt.......",
        "......t..t......",
        ".......tt.......",
        "......kkkk......",
        "....kkPHPPkk....",
        "...kPHHPPDPPk...",
        "..kPHPPPPPPDpk..",
        "..kPPPDPPPPPpk..",
        "..kPPPPPPDPPpk..",
        "..kpPDPPPPPppk..",
        "...kpPPPPPppk...",
        "....kkppppkk....",
        "......kkkk......",
        ".g..........g...",
        "....g.....g.....",
    ]),
    "item/marov_stew": ({"d": "5E3A17", "b": "8B5A2B", "B": "A8743F", "u": "C98A4B", "U": "E0A866", "G": "6DBE34",
                         "C": "3FD8C8", "P": "EDE6CF", "R": "D7322B", "W": "FFFFFF", "S": "E9E3C6"}, [
        EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
        "....RWR....GG...",
        "...RRRRR..GGGG..",
        ".....S..C..S..P.",
        ".dUUUSUCCUUSUPPd",
        "dbuuUuuuuUuuuubd",
        "dBbbbbbbbbbbbbbd",
        ".dBbbbbbbbbbbbd.",
        "..dBbbbbbbbbbd..",
        "...ddbbbbbbdd...",
        ".....dddddd.....",
        EMPTY,
    ]),
}
PLANT_SPRITES = ["block/hopcap", "block/glowcap", "block/puffball", "block/fly_agaric"]

# Облака спор: цвета как в SporeClouds.java, радиусы как в игре.
CLOUDS = [("puffball", "E6DCC0", 2.5), ("puffer", "B5C46E", 3.0), ("spore_bomb", "BDB6A8", 3.0)]
PUFF_VARIANTS = 4
PUFF_SIZE = 32  # комочек ~2 блока, 32 пикселя = та же плотность пикселей, что у блоков
PUFF_TONES = (0.7, 0.8, 0.9, 1.0)


# ---------------------------------------------------------------------------
# Пиксели и PNG
# ---------------------------------------------------------------------------

def rgba(h, a=255):
    h = h.lstrip("#")
    return (int(h[0:2], 16), int(h[2:4], 16), int(h[4:6], 16), a)


def mul(c, f):
    return (min(255, max(0, int(c[0] * f))), min(255, max(0, int(c[1] * f))), min(255, max(0, int(c[2] * f))), c[3])


class Tex:
    def __init__(self, w, h):
        self.w, self.h = w, h
        self.px = [[(0, 0, 0, 0)] * w for _ in range(h)]

    def set(self, x, y, c):
        if 0 <= x < self.w and 0 <= y < self.h:
            self.px[y][x] = c

    def fill(self, rect, fn):
        x0, y0, w, h = rect
        for y in range(h):
            for x in range(w):
                self.set(x0 + x, y0 + y, fn(x, y))

    def save(self, path):
        os.makedirs(os.path.dirname(path), exist_ok=True)
        raw = b"".join(b"\x00" + bytes(v for p in row for v in p) for row in self.px)

        def chunk(tag, data):
            return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

        with open(path, "wb") as f:
            f.write(b"\x89PNG\r\n\x1a\n")
            f.write(chunk(b"IHDR", struct.pack(">IIBBBBB", self.w, self.h, 8, 6, 0, 0, 0)))
            f.write(chunk(b"IDAT", zlib.compress(raw, 9)))
            f.write(chunk(b"IEND", b""))


def sprite_tex(key):
    palette, rows = SPRITES[key]
    assert len(rows) == 16, f"{key}: {len(rows)} строк вместо 16"
    tex = Tex(16, 16)
    for y, row in enumerate(rows):
        assert len(row) == 16, f"{key}, строка {y}: {len(row)} символов вместо 16"
        for x, ch in enumerate(row):
            if ch != ".":
                tex.set(x, y, rgba(palette[ch]))
    return tex


# ---------------------------------------------------------------------------
# Раскраска кубов по box UV (как CubeListBuilder.texOffs(u, v).addBox(...))
# ---------------------------------------------------------------------------

def box_rects(u, v, w, h, d):
    return {
        "top": (u + d, v, w, d),
        "bottom": (u + d + w, v, w, d),
        "west": (u, v + d, d, h),           # правый бок моба (-X)
        "north": (u + d, v + d, w, h),      # перед
        "east": (u + d + w, v + d, d, h),   # левый бок моба (+X)
        "south": (u + 2 * d + w, v + d, w, h),
    }


SIDES = ("west", "north", "east", "south")


def scatter_spots(tex, rect, color, rng, density, max_size, avoid_last_row=False):
    x0, y0, fw, fh = rect
    usable_h = fh - 1 if avoid_last_row and fh > 1 else fh
    for _ in range(max(1, round(fw * fh * density))):
        size = rng.randint(1, max_size)
        if fw < size or usable_h < size:
            size = 1
        if fw < 1 or usable_h < 1:
            return
        sx, sy = rng.randint(0, fw - size), rng.randint(0, usable_h - size)
        for yy in range(size):
            for xx in range(size):
                if size == 2 and (xx, yy) == (1, 1) and rng.random() < 0.4:
                    continue
                tex.set(x0 + sx + xx, y0 + sy + yy, mul(color, 1 + rng.uniform(-0.03, 0.03)))


def paint_cube(tex, uv, size, props, rng):
    w, h, d = size
    rects = box_rects(uv[0], uv[1], w, h, d)
    col = rgba(props["col"])
    col2 = rgba(props["col2"]) if props.get("col2") else None
    paint = props["paint"]

    def noise(c, a=0.05):
        return mul(c, 1 + rng.uniform(-a, a))

    if paint in ("cap", "cap_top"):
        for f in SIDES:
            fh = rects[f][3]
            tex.fill(rects[f], lambda x, y: noise(mul(col, 0.78) if y == fh - 1 and fh > 1 else col))
            if col2:
                scatter_spots(tex, rects[f], col2, rng, 1 / 9, 2 if fh >= 3 else 1, avoid_last_row=True)
        tex.fill(rects["top"], lambda x, y: noise(col))
        if col2:
            scatter_spots(tex, rects["top"], col2, rng, 1 / 7, 2)
        if paint == "cap":
            gill = rgba(props.get("gill") or "E8DEC0")
            _, _, fw, fh = rects["bottom"]
            cx, cy = (fw - 1) / 2, (fh - 1) / 2

            def gills(x, y):
                if max(abs(x - cx), abs(y - cy)) < min(fw, fh) * 0.2:
                    return mul(gill, 0.78)
                sector = int((math.atan2(y - cy, x - cx) + math.pi) / (2 * math.pi) * 16)
                return noise(mul(gill, 0.84 if sector % 2 else 1.0), 0.03)

            tex.fill(rects["bottom"], gills)
        else:
            tex.fill(rects["bottom"], lambda x, y: mul(col, 0.7))

    elif paint in ("stem", "leg"):
        streaks = [1 + rng.uniform(-0.06, 0.04) for _ in range(2 * (w + d) + 1)]
        for f in SIDES:
            fh = rects[f][3]
            dark_bottom = paint == "leg"
            tex.fill(rects[f], lambda x, y: noise(mul(col, streaks[x % len(streaks)] * (0.85 if dark_bottom and y == fh - 1 else 1)), 0.03))
        tex.fill(rects["top"], lambda x, y: noise(mul(col, 0.92), 0.03))
        tex.fill(rects["bottom"], lambda x, y: noise(mul(col, 0.78), 0.03))

    elif paint == "ball":
        for f in SIDES:
            tex.fill(rects[f], lambda x, y: noise(col, 0.04))
        _, _, fw, fh = rects["top"]
        tex.fill(rects["top"], lambda x, y: noise(mul(col, 1.06 if x + y < (fw + fh) / 2 else 1.0), 0.04))
        tex.fill(rects["bottom"], lambda x, y: noise(mul(col, 0.85), 0.04))
        if col2:
            for f in SIDES + ("top",):
                scatter_spots(tex, rects[f], col2, rng, 1 / 16, 1)

    elif paint == "hole":
        for f in SIDES + ("bottom",):
            tex.fill(rects[f], lambda x, y: noise(col, 0.04))
        _, _, fw, fh = rects["top"]
        cx, cy = (fw - 1) / 2, (fh - 1) / 2
        tex.fill(rects["top"], lambda x, y: rgba("3A2C1C") if max(abs(x - cx), abs(y - cy)) < 0.75 else rgba("6B5638"))

    elif paint == "skirt":
        for f in SIDES:
            fh = rects[f][3]
            tex.fill(rects[f], lambda x, y: noise(mul(col, 0.86 if (y == fh - 1 and x % 2 == 0) else 0.97), 0.02))
        tex.fill(rects["top"], lambda x, y: noise(mul(col, 0.9), 0.02))
        tex.fill(rects["bottom"], lambda x, y: noise(mul(col, 0.82), 0.02))

    else:
        raise ValueError(f"неизвестный paint: {paint}")

    face = props.get("face")
    if face:
        first_row, grid = FACES[face]
        x0, y0, fw, fh = rects["north"]
        off = (fw - len(grid[0])) // 2
        for gy, row in enumerate(grid):
            for gx, ch in enumerate(row):
                if ch != ".":
                    tex.set(x0 + off + gx, y0 + first_row + gy, rgba(FACE_COLORS[ch]))


def puff_sheet():
    """Комочки облака: PUFF_VARIANTS штук PUFF_SIZE x PUFF_SIZE в ряд. Серые: цвет облаку даёт игра."""
    size = PUFF_SIZE
    tex = Tex(size * PUFF_VARIANTS, size)
    light = Vector((-0.45, -0.6, 0.65)).normalized()  # свет сверху слева (y в текстуре вниз)
    half = size / 2
    for k in range(PUFF_VARIANTS):
        rng = random.Random(f"puff/{k}")
        phases = [rng.uniform(0, 2 * math.pi) for _ in range(3)]
        base = half * (0.8 - 0.04 * (k % 2))
        for y in range(size):
            for x in range(size):
                dx, dy = x + 0.5 - half, y + 0.5 - half
                d = math.hypot(dx, dy)
                ang = math.atan2(dy, dx)
                r = base + half * (0.09 * math.sin(3 * ang + phases[0]) + 0.06 * math.sin(5 * ang + phases[1])
                                   + 0.035 * math.sin(7 * ang + phases[2]))
                if d > r:
                    continue
                n = Vector((dx / r, dy / r, math.sqrt(max(0.0, 1 - (d / r) ** 2))))
                value = 0.66 + 0.34 * max(0.0, n.dot(light))
                value = min(PUFF_TONES, key=lambda t: abs(t - value))
                edge = d > r - 1.5
                c = int(255 * value * (0.95 if edge else 1.0))
                tex.set(size * k + x, y, (c, c, c, 200 if edge else 245))
    return tex


def mod_icon():
    """Иконка мода 128x128 (README и список модов): мухомор (спрайт x3) в круглой эмблеме, сетка 64x64 по 2 пикселя."""
    grid, cell = 64, 2
    bg, inner, outer = rgba("1F2A1E"), rgba("355A30"), rgba("2B4727")
    ring, ring_dark = rgba("C9A35E"), rgba("7A5E36")
    small = Tex(grid, grid)
    c = (grid - 1) / 2
    for y in range(grid):
        for x in range(grid):
            d = math.hypot(x - c, y - c)
            if d <= 22.5:
                small.set(x, y, inner)
            elif d <= 26.5:
                small.set(x, y, outer)
            elif d <= 29.0:
                small.set(x, y, ring)
            elif d <= 30.6:
                small.set(x, y, ring_dark)
            else:
                small.set(x, y, bg)
    sprite = sprite_tex("block/fly_agaric")
    for y in range(48):
        for x in range(48):
            p = sprite.px[y // 3][x // 3]
            if p[3]:
                small.set(x + 8, y + 2, p)
    icon = Tex(grid * cell, grid * cell)
    for y in range(icon.h):
        for x in range(icon.w):
            icon.px[y][x] = small.px[y // cell][x // cell]
    return icon


def tinted(tex, color, alpha=0.9):
    out = Tex(tex.w, tex.h)
    tint = rgba(color)
    for y in range(tex.h):
        for x in range(tex.w):
            r, g, b, a = tex.px[y][x]
            out.px[y][x] = (r * tint[0] // 255, g * tint[1] // 255, b * tint[2] // 255, int(a * alpha))
    return out


def cloud_puffs(radius, seed):
    """Раскладка комочков, как в SporeCloudRenderer.java: (x, y, z, размер, вариант, поворот)."""
    rng = random.Random(seed)
    count = max(7, min(14, round(4 + radius * 2.5)))
    golden = math.pi * (3 - math.sqrt(5))
    puffs = []
    for i in range(count):
        t = (i + 0.5) / count
        dy = 1 - 2 * t
        ring = math.sqrt(1 - dy * dy)
        theta = i * golden
        dist = radius * (0.25 + 0.45 * rng.random())
        size = radius * (0.7 + 0.35 * rng.random())
        spin = (rng.random() - 0.5) * 0.5
        variant = rng.randrange(PUFF_VARIANTS)
        puffs.append((math.cos(theta) * ring * dist, radius * 0.5 + dy * radius * 0.3, math.sin(theta) * ring * dist,
                      size, variant, spin))
    return puffs


def pack(items):
    """Полочная упаковка прямоугольников box UV. items: [(ключ, ширина, высота)]."""
    width = 64
    while True:
        if max(i[1] for i in items) > width:
            width *= 2
            continue
        x = y = shelf = 0
        pos = {}
        for key, rw, rh in sorted(items, key=lambda i: (-i[2], -i[1])):
            if x + rw > width:
                x, y, shelf = 0, y + shelf, 0
            pos[key] = (x, y)
            x += rw
            shelf = max(shelf, rh)
        height = 32
        while height < y + shelf:
            height *= 2
        if height > width * 2:
            width *= 2
            continue
        return width, height, pos


# ---------------------------------------------------------------------------
# Сборка сцены
# ---------------------------------------------------------------------------

def mc_to_blender_local(v, top_level):
    x, y, z = v
    return Vector((x * PX, z * PX, ((24 - y) if top_level else -y) * PX))


def new_collection(name):
    col = bpy.data.collections.get(name) or bpy.data.collections.new(name)
    if col.name not in bpy.context.scene.collection.children:
        bpy.context.scene.collection.children.link(col)
    return col


def box_mesh(name, origin, size):
    ox, oy, oz = origin
    w, h, d = size
    x0, x1 = ox * PX, (ox + w) * PX
    y0, y1 = oz * PX, (oz + d) * PX
    z0, z1 = -(oy + h) * PX, -oy * PX
    verts = [(x0, y0, z0), (x1, y0, z0), (x1, y1, z0), (x0, y1, z0),
             (x0, y0, z1), (x1, y0, z1), (x1, y1, z1), (x0, y1, z1)]
    faces = [(0, 3, 2, 1), (4, 5, 6, 7), (0, 1, 5, 4), (1, 2, 6, 5), (2, 3, 7, 6), (3, 0, 4, 7)]
    mesh = bpy.data.meshes.new(name)
    mesh.from_pydata(verts, [], faces)
    mesh.update()
    return mesh


def build_scene():
    for obj in list(bpy.data.objects):
        bpy.data.objects.remove(obj, do_unlink=True)
    for coll in list(bpy.data.collections):
        bpy.data.collections.remove(coll)

    for mob in MOBS:
        coll = new_collection(mob["name"])
        root = bpy.data.objects.new(mob["name"], None)
        root.empty_display_type = "ARROWS"
        root.empty_display_size = 0.3
        root.location = (mob["x"], 0, 0)
        root["geo_class"] = mob["geo_class"]
        coll.objects.link(root)
        parts = {}
        for order, (pname, parent, pivot, rot, cubes) in enumerate(mob["parts"]):
            empty = bpy.data.objects.new(f"{mob['name']}:{pname}", None)
            empty.empty_display_type = "PLAIN_AXES"
            empty.empty_display_size = 0.08
            empty["part"] = pname
            empty["order"] = order
            coll.objects.link(empty)
            empty.parent = parts[parent] if parent else root
            empty.location = mc_to_blender_local(pivot, parent is None)
            empty.rotation_mode = "XZY"
            rx, ry, rz = (math.radians(a) for a in rot)
            empty.rotation_euler = (rx, rz, -ry)
            parts[pname] = empty
            for i, c in enumerate(cubes):
                name = f"{mob['name']}:{pname}:{i}"
                obj = bpy.data.objects.new(name, box_mesh(name, c["origin"], c["size"]))
                for key in ("paint", "col", "col2", "face", "gill"):
                    obj[key] = c[key]
                obj["order"] = i
                coll.objects.link(obj)
                obj.parent = empty

    plants = new_collection("plants")
    for i, key in enumerate(PLANT_SPRITES):
        name = key.split("/")[1]
        x = -2.1 + i * 1.4
        for k, angle in enumerate((45, -45)):
            mesh = bpy.data.meshes.new(f"plant:{name}:{k}")
            hw = 0.5
            mesh.from_pydata([(-hw, 0, 0), (hw, 0, 0), (hw, 0, 1), (-hw, 0, 1)], [], [(0, 1, 2, 3)])
            mesh.uv_layers.new(name="UV")
            for li, uv in enumerate([(0, 0), (1, 0), (1, 1), (0, 1)]):
                mesh.uv_layers.active.data[li].uv = uv
            obj = bpy.data.objects.new(f"plant:{name}:{k}", mesh)
            obj.location = (x, -2.2, 0)
            obj.rotation_euler = (0, 0, math.radians(angle))
            obj["sprite"] = key
            plants.objects.link(obj)

    scene = bpy.context.scene
    helpers = new_collection("preview")
    ground_mesh = bpy.data.meshes.new("ground")
    s = 6
    ground_mesh.from_pydata([(-s, -s, 0), (s, -s, 0), (s, s, 0), (-s, s, 0)], [], [(0, 1, 2, 3)])
    ground = bpy.data.objects.new("ground", ground_mesh)
    ground_mesh.materials.append(simple_material("ground", "6FA34A"))
    helpers.objects.link(ground)

    sun = bpy.data.objects.new("sun", bpy.data.lights.new("sun", "SUN"))
    sun.data.energy = 3.5
    sun.data.angle = math.radians(8)
    sun.rotation_euler = (math.radians(48), 0, math.radians(28))
    helpers.objects.link(sun)

    cam = bpy.data.objects.new("camera", bpy.data.cameras.new("camera"))
    cam.data.type = "ORTHO"
    helpers.objects.link(cam)
    scene.camera = cam


# ---------------------------------------------------------------------------
# Материалы и рендер
# ---------------------------------------------------------------------------

def enable_nodes(idblock):
    try:
        idblock.use_nodes = True
    except (AttributeError, TypeError):
        pass


def principled(mat):
    enable_nodes(mat)
    nodes = mat.node_tree.nodes
    bsdf = next((n for n in nodes if n.type == "BSDF_PRINCIPLED"), None)
    if bsdf is None:
        bsdf = nodes.new("ShaderNodeBsdfPrincipled")
        out = next(n for n in nodes if n.type == "OUTPUT_MATERIAL")
        mat.node_tree.links.new(bsdf.outputs[0], out.inputs[0])
    bsdf.inputs["Roughness"].default_value = 0.85
    return bsdf


def simple_material(name, color):
    mat = bpy.data.materials.get(name) or bpy.data.materials.new(name)
    c = rgba(color)
    principled(mat).inputs["Base Color"].default_value = (srgb_to_linear(c[0]), srgb_to_linear(c[1]), srgb_to_linear(c[2]), 1)
    return mat


def srgb_to_linear(v):
    v /= 255.0
    return v / 12.92 if v <= 0.04045 else ((v + 0.055) / 1.055) ** 2.4


def texture_material(name, png_path, glow=0.0):
    mat = bpy.data.materials.get(name) or bpy.data.materials.new(name)
    bsdf = principled(mat)
    nodes, links = mat.node_tree.nodes, mat.node_tree.links
    img_node = next((n for n in nodes if n.type == "TEX_IMAGE"), None) or nodes.new("ShaderNodeTexImage")
    img = bpy.data.images.load(png_path, check_existing=True)
    img.reload()
    img_node.image = img
    img_node.interpolation = "Closest"
    links.new(img_node.outputs["Color"], bsdf.inputs["Base Color"])
    links.new(img_node.outputs["Alpha"], bsdf.inputs["Alpha"])
    if glow > 0:
        links.new(img_node.outputs["Color"], bsdf.inputs["Emission Color"])
        bsdf.inputs["Emission Strength"].default_value = glow
    return mat


def setup_render(scene):
    for engine in ("BLENDER_EEVEE", "BLENDER_EEVEE_NEXT", "CYCLES"):
        try:
            scene.render.engine = engine
            break
        except TypeError:
            continue
    if scene.render.engine == "CYCLES":
        scene.cycles.samples = 48
    scene.view_settings.view_transform = "Standard"
    scene.render.film_transparent = False
    # Blender пишет в PNG метаданные (путь к .blend, дату, имя компьютера) — превью лежат в публичном репо.
    for attr in dir(scene.render):
        if attr.startswith("use_stamp"):
            try:
                setattr(scene.render, attr, False)
            except (AttributeError, TypeError):
                pass
    scene.render.image_settings.file_format = "PNG"
    world = scene.world or bpy.data.worlds.new("World")
    scene.world = world
    enable_nodes(world)
    bg = next((n for n in world.node_tree.nodes if n.type == "BACKGROUND"), None)
    if bg:
        bg.inputs[0].default_value = (0.55, 0.68, 0.82, 1)
        bg.inputs[1].default_value = 0.9


CAMERA_DIRECTION = Vector((0.75, -1.6, 0.85)).normalized()


def build_cloud_previews(puff_png):
    """Облака спор для превью: плоскости-комочки, повёрнутые к камере, как билборды в игре."""
    coll = new_collection("spore_clouds")
    for obj in list(coll.objects):
        bpy.data.objects.remove(obj, do_unlink=True)
    facing = CAMERA_DIRECTION.to_track_quat("Z", "Y")
    sheet = puff_sheet()
    cache = os.path.join(ART, ".cache")
    objs = []
    for c, (name, color, radius) in enumerate(CLOUDS):
        png = os.path.join(cache, f"puff_{name}.png")
        tinted(sheet, color).save(png)
        mat = texture_material("cloud:" + name, png)
        origin = Vector((20 + c * 7.5, 0, 0))
        for i, (x, y, z, size, variant, spin) in enumerate(cloud_puffs(radius, f"cloud/{name}")):
            mesh = bpy.data.meshes.new(f"cloud:{name}:{i}")
            h = size / 2
            mesh.from_pydata([(-h, -h, 0), (h, -h, 0), (h, h, 0), (-h, h, 0)], [], [(0, 1, 2, 3)])
            mesh.uv_layers.new(name="UV")
            u0, u1 = variant / PUFF_VARIANTS, (variant + 1) / PUFF_VARIANTS
            for li, uv in enumerate([(u0, 0), (u1, 0), (u1, 1), (u0, 1)]):
                mesh.uv_layers.active.data[li].uv = uv
            mesh.materials.append(mat)
            obj = bpy.data.objects.new(mesh.name, mesh)
            obj.location = origin + Vector((x, z, y))
            obj.rotation_mode = "QUATERNION"
            obj.rotation_quaternion = facing @ Quaternion((0, 0, 1), spin)
            obj["cloud"] = name
            coll.objects.link(obj)
            objs.append(obj)
    ground_mesh = bpy.data.meshes.get("cloud_ground") or bpy.data.meshes.new("cloud_ground")
    if not ground_mesh.vertices:
        ground_mesh.from_pydata([(14, -8, 0), (42, -8, 0), (42, 8, 0), (14, 8, 0)], [], [(0, 1, 2, 3)])
        ground_mesh.materials.append(simple_material("ground", "6FA34A"))
    ground = bpy.data.objects.new("cloud_ground", ground_mesh)
    ground["cloud"] = "ground"
    coll.objects.link(ground)
    return objs


def render_to(path, target, scale, res):
    scene = bpy.context.scene
    cam = scene.camera
    cam.data.ortho_scale = scale
    direction = CAMERA_DIRECTION
    cam.location = target + direction * 10
    cam.rotation_euler = (-direction).to_track_quat("-Z", "Y").to_euler()
    cam.data.clip_end = 100
    scene.render.resolution_x, scene.render.resolution_y = res
    scene.render.filepath = path
    bpy.ops.render.render(write_still=True)


# ---------------------------------------------------------------------------
# Экспорт
# ---------------------------------------------------------------------------

def sorted_children(obj, key):
    return sorted((c for c in obj.children if key in c), key=lambda c: (c.get("order", 0), c.name))


def jf(v):
    v = round(v, 3)
    if v == 0:
        v = 0.0
    s = f"{v:.3f}".rstrip("0")
    return (s + "0" if s.endswith(".") else s) + "F"


def collect_mob(root):
    """Собирает кости и кубы моба в координатах Minecraft."""
    parts = []

    def walk(obj, parent_obj, parent_name):
        rel = parent_obj.matrix_world.inverted() @ obj.matrix_world
        t = rel.to_translation()
        e = rel.to_euler("XZY")
        top = parent_name is None
        pivot = (t.x * 16, (24 - t.z * 16) if top else -t.z * 16, t.y * 16)
        rot = (e.x, -e.z, e.y)
        inv = obj.matrix_world.inverted()
        cubes = []
        for cobj in sorted_children(obj, "paint"):
            m = inv @ cobj.matrix_world
            pts = [m @ v.co for v in cobj.data.vertices]
            lo = Vector((min(p.x for p in pts), min(p.y for p in pts), min(p.z for p in pts)))
            hi = Vector((max(p.x for p in pts), max(p.y for p in pts), max(p.z for p in pts)))
            origin = (lo.x * 16, -hi.z * 16, lo.y * 16)
            size = tuple(max(1, round(s)) for s in ((hi.x - lo.x) * 16, (hi.z - lo.z) * 16, (hi.y - lo.y) * 16))
            props = {k: str(cobj.get(k, "")) for k in ("paint", "col", "col2", "face", "gill")}
            cubes.append({"obj": cobj, "origin": origin, "size": size, "props": props, "lo": lo, "hi": hi, "part_inv": inv})
        name = str(obj["part"])
        parts.append({"name": name, "parent": parent_name, "pivot": pivot, "rot": rot, "cubes": cubes})
        for child in sorted_children(obj, "part"):
            walk(child, obj, name)

    for child in sorted_children(root, "part"):
        walk(child, root, None)
    return parts


def assign_uvs(c, uv, tex_w, tex_h):
    """Ставит на меш куба развёртку, совпадающую с box UV в игре (для превью в Blender)."""
    mesh = c["obj"].data
    if not mesh.uv_layers:
        mesh.uv_layers.new(name="UV")
    layer = mesh.uv_layers.active.data
    w, h, d = c["size"]
    rects = box_rects(uv[0], uv[1], w, h, d)
    lo, hi = c["lo"], c["hi"]
    span = hi - lo
    span = Vector((span.x or 1, span.y or 1, span.z or 1))
    m = c["part_inv"] @ c["obj"].matrix_world
    rot = m.to_3x3()
    for poly in mesh.polygons:
        n = (rot @ poly.normal).normalized()
        axis = max(range(3), key=lambda i: abs(n[i]))
        if axis == 0:
            face = "east" if n.x > 0 else "west"
        elif axis == 1:
            face = "south" if n.y > 0 else "north"
        else:
            face = "top" if n.z > 0 else "bottom"
        rx, ry, rw, rh = rects[face]
        for li in poly.loop_indices:
            p = m @ mesh.vertices[mesh.loops[li].vertex_index].co
            fx = (p.x - lo.x) / span.x
            fy = (p.y - lo.y) / span.y
            fz = (p.z - lo.z) / span.z
            s, t = {
                "north": (fx, 1 - fz),
                "south": (1 - fx, 1 - fz),
                "west": (1 - fy, 1 - fz),
                "east": (fy, 1 - fz),
                "top": (fx, 1 - fy),
                "bottom": (fx, fy),
            }[face]
            layer[li].uv = ((rx + s * rw) / tex_w, 1 - (ry + t * rh) / tex_h)


def write_geo(root, parts, tex_w, tex_h, uvs):
    cls = str(root["geo_class"])
    lines = [
        "// Сгенерировано art/gribs_art.py из art/marovgribs.blend. Руками не править: перезапишется при экспорте.",
        "package ru.marov.gribs.client.model.geo;",
        "",
        "import net.minecraft.client.model.geom.PartPose;",
        "import net.minecraft.client.model.geom.builders.CubeListBuilder;",
        "import net.minecraft.client.model.geom.builders.LayerDefinition;",
        "import net.minecraft.client.model.geom.builders.MeshDefinition;",
        "import net.minecraft.client.model.geom.builders.PartDefinition;",
        "",
        f"public final class {cls} {{",
        f"    public static final int TEXTURE_WIDTH = {tex_w};",
        f"    public static final int TEXTURE_HEIGHT = {tex_h};",
        "",
        f"    private {cls}() {{",
        "    }",
        "",
        "    public static LayerDefinition create() {",
        "        MeshDefinition mesh = new MeshDefinition();",
        "        PartDefinition root = mesh.getRoot();",
    ]
    for p in parts:
        var = "p_" + p["name"]
        parent_var = "p_" + p["parent"] if p["parent"] else "root"
        boxes = "CubeListBuilder.create()"
        for c in p["cubes"]:
            u, v = uvs[id(c)]
            o, s = c["origin"], c["size"]
            boxes += (f"\n                .texOffs({u}, {v}).addBox({jf(o[0])}, {jf(o[1])}, {jf(o[2])}, "
                      f"{jf(s[0])}, {jf(s[1])}, {jf(s[2])})")
        px, py, pz = p["pivot"]
        rx, ry, rz = p["rot"]
        if any(abs(a) > 1e-5 for a in (rx, ry, rz)):
            pose = f"PartPose.offsetAndRotation({jf(px)}, {jf(py)}, {jf(pz)}, {jf(rx)}, {jf(ry)}, {jf(rz)})"
        else:
            pose = f"PartPose.offset({jf(px)}, {jf(py)}, {jf(pz)})"
        lines.append(f'        PartDefinition {var} = {parent_var}.addOrReplaceChild("{p["name"]}", {boxes},')
        lines.append(f"                {pose});")
    lines += [
        f"        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);",
        "    }",
        "}",
        "",
    ]
    # Неиспользуемые переменные частей — нормально для генерированного кода.
    os.makedirs(GEO_DIR, exist_ok=True)
    with open(os.path.join(GEO_DIR, cls + ".java"), "w", encoding="utf-8", newline="\n") as f:
        f.write("\n".join(lines))
    return cls


def save_blend(scene):
    """Сохраняет только сцену и всё, на что она ссылается, с относительными путями.

    Обычное «Сохранить как» кладёт в файл и окно Blender с параметрами последних операций
    (там всплывает папка C:\\Users\\<имя>\\Documents), а .blend лежит в публичном репозитории.
    """
    scene.render.filepath = "//previews/"
    bpy.data.libraries.write(BLEND, set(bpy.data.scenes), path_remap="RELATIVE_ALL", compress=True)


def export():
    scene = bpy.context.scene
    bpy.context.view_layer.update()  # иначе matrix_world у только что созданных объектов ещё не посчитаны
    setup_render(scene)
    os.makedirs(PREVIEWS, exist_ok=True)
    report = []

    for key in SPRITES:
        sprite_tex(key).save(os.path.join(ASSETS, "textures", key + ".png"))
    puff_sheet().save(os.path.join(ASSETS, "textures", "entity", "spore_puff.png"))
    mod_icon().save(os.path.join(ASSETS, "icon.png"))
    build_cloud_previews(os.path.join(ASSETS, "textures", "entity", "spore_puff.png"))

    roots = [o for o in scene.objects if "geo_class" in o]
    for root in roots:
        mob = root.name
        parts = collect_mob(root)
        cubes = [c for p in parts for c in p["cubes"]]
        tex_w, tex_h, pos = pack([(id(c), 2 * (c["size"][0] + c["size"][2]), c["size"][2] + c["size"][1]) for c in cubes])
        tex = Tex(tex_w, tex_h)
        for p in parts:
            for i, c in enumerate(p["cubes"]):
                rng = random.Random(f"{mob}/{p['name']}/{i}")
                paint_cube(tex, pos[id(c)], c["size"], c["props"], rng)
        png = os.path.join(ASSETS, "textures", "entity", mob + ".png")
        tex.save(png)
        mat = texture_material("mob:" + mob, png)
        for c in cubes:
            assign_uvs(c, pos[id(c)], tex_w, tex_h)
            c["obj"].data.materials.clear()
            c["obj"].data.materials.append(mat)
        cls = write_geo(root, parts, tex_w, tex_h, pos)
        report.append(f"{mob}: {len(parts)} частей, {len(cubes)} кубов, текстура {tex_w}x{tex_h} -> {cls}.java")

    for obj in scene.objects:
        if "sprite" in obj:
            key = str(obj["sprite"])
            glow = 2.0 if key.endswith("glowcap") else 0.0
            mat = texture_material("sprite:" + key, os.path.join(ASSETS, "textures", key + ".png"), glow)
            obj.data.materials.clear()
            obj.data.materials.append(mat)

    # Превью: каждый моб отдельно и общий снимок.
    mob_objs = {r.name: [r] + list(r.children_recursive) for r in roots}
    plant_objs = [o for o in scene.objects if "sprite" in o]
    for name, objs in mob_objs.items():
        for other, oo in mob_objs.items():
            for o in oo:
                o.hide_render = other != name
        for o in plant_objs:
            o.hide_render = True
        meshes = [o for o in objs if o.type == "MESH"]
        pts = [o.matrix_world @ Vector(corner) for o in meshes for corner in o.bound_box]
        lo = Vector((min(p.x for p in pts), min(p.y for p in pts), min(p.z for p in pts)))
        hi = Vector((max(p.x for p in pts), max(p.y for p in pts), max(p.z for p in pts)))
        size = max((hi - lo).length, 0.3)
        render_to(os.path.join(PREVIEWS, name + ".png"), (lo + hi) / 2, size * 1.35, (640, 640))
    for oo in mob_objs.values():
        for o in oo:
            o.hide_render = False
    for o in plant_objs:
        o.hide_render = False
    render_to(os.path.join(PREVIEWS, "all.png"), Vector((-0.15, -1.1, 0.45)), 6.2, (1280, 800))
    render_to(os.path.join(PREVIEWS, "spore_clouds.png"), Vector((27.5, 0, 1.3)), 25.0, (1280, 560))

    save_blend(scene)
    print("\n".join(["", "Marov Gribs: экспорт готов"] + report))


def main():
    args = sys.argv[sys.argv.index("--") + 1:] if "--" in sys.argv else []
    mode = args[0] if args else "export"
    if mode == "build":
        build_scene()
    elif mode != "export":
        raise SystemExit(f"неизвестный режим {mode}: нужен build или export")
    export()


main()
