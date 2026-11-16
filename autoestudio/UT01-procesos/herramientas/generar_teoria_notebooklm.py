"""Convierte el documento Markdown de teoría a un PDF con índice navegable."""
from pathlib import Path
import sys
import re
import html

LIBS = Path.home() / "AppData/Local/Temp/opencode/pdf-libs"
if LIBS.exists():
    sys.path.insert(0, str(LIBS))

from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, PageBreak, KeepTogether,
)
from reportlab.platypus.tableofcontents import TableOfContents
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib import colors
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont

ROOT = Path(__file__).resolve().parents[1]
SOURCE = Path(sys.argv[1]).resolve() if len(sys.argv) > 1 else ROOT / "repaso/Teoria_PSP_UT01_NotebookLM.md"
OUTPUT = SOURCE.with_suffix(".pdf")
GLOSSARY = SOURCE.stem.startswith("Terminologia_")
DOCUMENT_TITLE = "Terminología de procesos" if GLOSSARY else "Teoría fundamental de procesos"

for name, filename in [("Body", "segoeui.ttf"), ("Bold", "segoeuib.ttf")]:
    pdfmetrics.registerFont(TTFont(name, "C:/Windows/Fonts/" + filename))
pdfmetrics.registerFontFamily("Body", normal="Body", bold="Bold")

NAVY, TEAL = colors.HexColor("#14283f"), colors.HexColor("#007f88")
body = ParagraphStyle("Body", fontName="Body", fontSize=10.2, leading=15.5,
                      textColor=NAVY, spaceAfter=9)
h1 = ParagraphStyle("Chapter", fontName="Bold", fontSize=18, leading=23,
                    textColor=NAVY, spaceBefore=15, spaceAfter=12,
                    keepWithNext=True)
h2 = ParagraphStyle("Sub", fontName="Bold", fontSize=12, leading=17,
                    textColor=TEAL, spaceBefore=10, spaceAfter=7,
                    keepWithNext=True)
title = ParagraphStyle("Cover", fontName="Bold", fontSize=30, leading=38,
                       textColor=NAVY, spaceAfter=22)
small = ParagraphStyle("Small", parent=body, fontSize=9, leading=13)
bullet = ParagraphStyle("Bullet", parent=body, leftIndent=13,
                        firstLineIndent=-10, spaceAfter=6)

class GuideDoc(SimpleDocTemplate):
    def afterFlowable(self, flowable):
        if isinstance(flowable, Paragraph) and hasattr(flowable, "anchor"):
            text = flowable.getPlainText()
            self.canv.bookmarkPage(flowable.anchor)
            self.canv.addOutlineEntry(text, flowable.anchor, 0)
            self.notify("TOCEntry", (0, text, self.page, flowable.anchor))

def furniture(c, doc):
    w, h = doc.pagesize
    c.setFillColor(TEAL)
    c.rect(0, h-8, w, 8, fill=1, stroke=0)
    c.setFillColor(colors.HexColor("#526176"))
    c.setFont("Body", 8)
    c.drawString(43, h-29, "PSP · UT01 | TEORÍA PARA NOTEBOOKLM")
    c.drawString(43, 25, "Procesos, sistema operativo y Java")
    c.drawRightString(w-43, 25, str(doc.page))

toc = TableOfContents()
toc.levelStyles = [ParagraphStyle(
    "TOC", fontName="Body", fontSize=10, leading=16, textColor=NAVY,
    spaceBefore=6, spaceAfter=4)]
story = [
    Spacer(1, 65),
    Paragraph(DOCUMENT_TITLE, title),
    Paragraph("PSP · UT01", h1),
    Paragraph("Documento fuente para NotebookLM", h2),
    Paragraph("Definiciones, usos, diferencias y errores frecuentes." if GLOSSARY else "Explicaciones completas, relaciones entre conceptos, ejemplos razonados y preguntas de repaso.", body),
    Spacer(1, 18),
    Paragraph("Alcance: procesos, comparación con hilos, concurrencia, estados, planificación, comunicación y control de procesos con Java en Windows.", body),
    Paragraph("Preparado para estudiar y transformar el contenido en audio sin tener que leer código línea a línea. No es un temario completo de todo el módulo PSP.", body),
    Paragraph("Versión de estudio · noviembre de 2026", small),
    PageBreak(),
    Paragraph("Índice navegable", h1),
    Paragraph("Los títulos y números del índice enlazan a sus secciones. El PDF contiene texto seleccionable y marcadores.", small),
    toc,
    PageBreak(),
]
paragraph_lines = []
chapter = 0

def flush():
    if paragraph_lines:
        text = " ".join(paragraph_lines)
        story.append(Paragraph(html.escape(text), body))
        paragraph_lines.clear()

for line in SOURCE.read_text(encoding="utf-8").splitlines():
    if line.startswith("# "):
        continue
    if line.startswith("## "):
        flush()
        chapter += 1
        heading = Paragraph(html.escape(line[3:]), h1)
        heading.anchor = f"section-{chapter}"
        story.append(heading)
    elif line.startswith("### "):
        flush()
        story.append(Paragraph(html.escape(line[4:]), h2))
    elif not line.strip():
        flush()
    elif line.startswith("- ") or re.match(r"^\d+\.\s", line):
        flush()
        text = "• " + line[2:] if line.startswith("- ") else line
        story.append(Paragraph(html.escape(text), bullet))
    else:
        paragraph_lines.append(line.strip())
flush()

doc = GuideDoc(str(OUTPUT), pagesize=(595.276, 841.89),
    leftMargin=43, rightMargin=43, topMargin=49, bottomMargin=48,
    title="PSP UT01 · " + DOCUMENT_TITLE,
    author="Material de estudio PSP", subject="Fuente de teoría para NotebookLM")
doc.multiBuild(story, onFirstPage=furniture, onLaterPages=furniture)
print(OUTPUT)
