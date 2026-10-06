#!/usr/bin/env python3
"""Add standard Javadoc comments to public and protected API in src/main/java."""

from __future__ import annotations

import re
import sys
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable, Optional

try:
    import javalang
except ImportError:
    print("javalang is required: pip install javalang", file=sys.stderr)
    sys.exit(1)

ROOT = Path(__file__).resolve().parents[1]
SOURCE_ROOT = ROOT / "src" / "main" / "java"
PKG_ROOT = SOURCE_ROOT / "org" / "ejavdge"

TYPE_WORDS = {
    "Verdict": "verdict",
    "Text": "text",
    "Bytes": "bytes",
    "Effect": "effect",
    "App": "application",
    "Location": "location",
    "Out": "output sink",
    "DocPath": "document path",
    "HttpSpec": "HTTP request specification",
    "WebDriver": "web driver",
    "WebResource": "web resource",
    "ContestResource": "contest resource",
    "Credentials": "credentials",
    "Duration": "duration",
    "ByteFile": "byte file",
    "Program": "program",
    "XmlEngine": "XML engine",
    "XmlSelection": "XML selection",
    "Items": "item sequence",
    "Num": "number",
    "Context": "request context",
    "EnvVariable": "environment variable",
    "InvariantViolation": "invariant violation",
    "Logger": "logger",
    "List": "list",
}

PACKAGE_BLURBS = {
    "app": "Executable applications for ejudge contest workflows.",
    "app.scenario": "Multi-step scenarios composed from effects and drivers.",
    "app.setup": "Default wiring for drivers, engines, and environment-backed settings.",
    "auth": "Authentication and session handling for ejudge.",
    "contest": "Contest pages and form resources on the ejudge web client.",
    "dom": "XML/HTML document selection utilities.",
    "dom.engine": "Engines that evaluate document selections.",
    "dom.path": "Composable XPath expressions for document navigation.",
    "domain": "Core contest domain types.",
    "domain.problem": "Problem catalog, statements, and attachments.",
    "domain.report": "Run reports and judging feedback.",
    "domain.run": "Run identifiers and probe results.",
    "domain.solution": "Solution sources, languages, and sample testing.",
    "domain.tokens": "Session and contest tokens.",
    "effect": "Side effects and control-flow helpers.",
    "error": "Library-specific error types.",
    "file": "File-backed program sources and downloads.",
    "items": "Lazy sequences and collection transforms.",
    "scalar.bytes": "Byte sequences and UTF-8 encodings.",
    "scalar.num": "Numeric values and parsing.",
    "scalar.text": "Text values, templates, and string operations.",
    "scalar.text.palette": "ANSI color wrappers for terminal text.",
    "web.context": "HTTP request context entries (problem, language, run id).",
    "web.driver": "Abstractions for sending HTTP requests.",
    "web.driver.jdk.socket": "JDK socket implementation of the web driver.",
    "web.driver.jdk.socket.body": "HTTP response body reading policies.",
    "web.driver.jdk.stream": "Stream utilities for HTTP payloads.",
    "web.media": "Cookies, forms, and multipart helpers for ejudge pages.",
    "web.resource": "Parsed HTTP resources and URL components.",
    "web.spec": "HTTP/1.1 request line and message assembly.",
    "web.spec.body": "HTTP message bodies.",
    "web.spec.body.multipart": "Multipart form encoding.",
    "web.spec.header": "HTTP header fields.",
    "web.spec.method": "HTTP method request templates.",
    "workspace.env": "Environment variable access.",
    "workspace.out": "Console and structured output sinks.",
}


def split_camel(name: str) -> str:
    parts = re.sub(r"([a-z])([A-Z])", r"\1 \2", name).replace("_", " ")
    return parts.lower()


def type_phrase(type_name: str) -> str:
    simple = type_name.split(".")[-1]
    if simple in TYPE_WORDS:
        return TYPE_WORDS[simple]
    return split_camel(simple)


def param_phrase(param_name: str, type_name: Optional[str]) -> str:
    if type_name:
        from_type = TYPE_WORDS.get(type_name.split(".")[-1])
        if from_type:
            return from_type
    if len(param_name) == 1:
        return f"the {param_name!r} argument"
    return split_camel(param_name)


def type_name_of(node) -> str:
    if node is None:
        return "void"
    if isinstance(node, javalang.tree.BasicType):
        return node.name
    if isinstance(node, javalang.tree.ReferenceType):
        parts = []
        if node.name:
            if isinstance(node.name, str):
                parts.append(node.name)
            else:
                parts.append(".".join(node.name))
        if node.arguments:
            args = ", ".join(type_name_of(a.type) for a in node.arguments)
            parts[-1] = f"{parts[-1]}<{args}>"
        if node.sub_type:
            parts.append(type_name_of(node.sub_type))
        return ".".join(parts) if parts else "Object"
    if isinstance(node, javalang.tree.TypeArgument):
        return type_name_of(node.type)
    return str(node)


def is_public_or_protected(modifiers: set[str]) -> bool:
    return "public" in modifiers or "protected" in modifiers


def is_api_type(modifiers: set[str], nested_in_interface: bool) -> bool:
    if "private" in modifiers:
        return False
    if is_public_or_protected(modifiers):
        return True
    return nested_in_interface


def is_api_member(modifiers: set[str], in_interface: bool) -> bool:
    if "private" in modifiers:
        return False
    if is_public_or_protected(modifiers):
        return True
    return in_interface


def implements_phrase(types: list) -> str:
    if not types:
        return ""
    names = [type_name_of(t) for t in types]
    if len(names) == 1:
        return f" Implements {{@link {names[0]}}}."
    joined = ", ".join(f"{{@link {n}}}" for n in names)
    return f" Implements {joined}."


def class_summary(name: str, is_interface: bool, implements, extends) -> str:
    lower = split_camel(name)
    if name.endswith("App"):
        return f"Application entry point that {lower.replace(' app', '')}."
    if name.endswith("IT") or name.endswith("Test"):
        return f"Support type for {lower}."
    if is_interface:
        if name in ("Effect", "App", "Text", "Bytes", "Items", "DocPath", "Out", "HttpSpec"):
            mapping = {
                "Effect": "Side effect that can be performed against the environment.",
                "App": "Runnable application action.",
                "Text": "Immutable UTF-16 text value.",
                "Bytes": "Immutable byte sequence.",
                "Items": "Lazy sequence of items materialized on demand.",
                "DocPath": "XPath expression fragment for XML/HTML selection.",
                "Out": "Structured output sink.",
                "HttpSpec": "HTTP message fragment represented as bytes.",
            }
            return mapping.get(name, f"Contract for {lower}.")
        return f"Contract for {lower}."
    if "Of" in name or name.endswith("Of"):
        return f"{name} wrapper or view over its constructor arguments."
    return f"{split_camel(name).capitalize()}."


def method_summary(name: str, is_constructor: bool, owner: str, return_type: str) -> str:
    if is_constructor:
        return f"Creates a new {{@code {owner}}}."
    if name == "perform":
        return "Performs this effect."
    if name == "run":
        return "Runs this application."
    if name == "content" or name == "contents":
        return "Returns the underlying content."
    if name == "bytes":
        return "Returns the HTTP message as bytes."
    if name == "view":
        return "Returns the XPath expression."
    if name == "equals":
        return "Compares this value to another object."
    if name == "hashCode":
        return "Returns a hash code for this value."
    if return_type != "void":
        return f"Returns the result of {{@code {name}}}."
    return f"Executes {{@code {name}}}."


def format_javadoc(lines: list[str], indent: str) -> str:
    if len(lines) == 1:
        return f"{indent}/**\n{indent} * {lines[0]}\n{indent} */\n"
    body = "\n".join(f"{indent} * {line}" if line else f"{indent} *" for line in lines)
    return f"{indent}/**\n{body}\n{indent} */\n"


def throws_lines(throws) -> list[str]:
    if not throws:
        return []
    result = []
    for t in throws:
        tn = type_name_of(t)
        if tn == "InvariantViolation":
            result.append("@throws InvariantViolation if an invariant is violated")
        else:
            result.append(f"@throws {tn} if the operation fails")
    return result


@dataclass
class Insertion:
    line: int  # 1-based line to insert before (after scanning annotations)
    text: str


def leading_annotation_line(lines: list[str], decl_line: int) -> int:
    """Return the first line index (0-based) where doc comment should be inserted."""
    idx = decl_line - 1
    scan = idx - 1
    while scan >= 0:
        stripped = lines[scan].strip()
        if stripped.startswith("@"):
            scan -= 1
            continue
        if stripped == "":
            scan -= 1
            continue
        break
    return scan + 1


def has_javadoc(lines: list[str], insert_at: int) -> bool:
    if insert_at <= 0:
        return False
    prev = insert_at - 1
    while prev >= 0 and lines[prev].strip() == "":
        prev -= 1
    if prev < 0:
        return False
    if lines[prev].strip().endswith("*/"):
        # walk back to /**
        while prev >= 0 and "/**" not in lines[prev]:
            prev -= 1
        return prev >= 0
    return False


def package_name_for_dir(package_dir: Path) -> str:
    rel = package_dir.relative_to(PKG_ROOT)
    if rel.parts:
        return "org.ejavdge." + ".".join(rel.parts)
    return "org.ejavdge"


def document_type(
    type_node,
    lines: list[str],
    insertions: list[Insertion],
    nested_in_interface: bool,
) -> None:
    modifiers = set(type_node.modifiers or [])
    if not is_api_type(modifiers, nested_in_interface):
        return
    is_interface = isinstance(type_node, javalang.tree.InterfaceDeclaration)
    decl_line = type_node.position.line
    insert_at = leading_annotation_line(lines, decl_line)
    if not has_javadoc(lines, insert_at):
        indent_match = re.match(r"^(\s*)", lines[decl_line - 1])
        indent = indent_match.group(1) if indent_match else ""
        summary = class_summary(
            type_node.name,
            is_interface,
            getattr(type_node, "implements", None),
            getattr(type_node, "extends", None),
        )
        insertions.append(
            Insertion(insert_at + 1, format_javadoc([summary], indent))
        )

    in_interface = is_interface
    for member in type_node.body:
        if isinstance(member, javalang.tree.MethodDeclaration):
            mods = set(member.modifiers or [])
            if not is_api_member(mods, in_interface):
                continue
            decl_line = member.position.line
            insert_at = leading_annotation_line(lines, decl_line)
            if has_javadoc(lines, insert_at):
                continue
            indent_match = re.match(r"^(\s*)", lines[decl_line - 1])
            indent = indent_match.group(1) if indent_match else ""
            ret = type_name_of(member.return_type)
            doc_lines = [method_summary(member.name, False, type_node.name, ret)]
            for param in member.parameters:
                ptype = type_name_of(param.type)
                doc_lines.append(
                    f"@param {param.name} the {param_phrase(param.name, ptype)}"
                )
            if ret != "void":
                doc_lines.append(f"@return the {type_phrase(ret)}")
            doc_lines.extend(throws_lines(member.throws))
            insertions.append(
                Insertion(insert_at + 1, format_javadoc(doc_lines, indent))
            )
        elif isinstance(member, javalang.tree.ConstructorDeclaration):
            mods = set(member.modifiers or [])
            if not is_api_member(mods, in_interface):
                continue
            decl_line = member.position.line
            insert_at = leading_annotation_line(lines, decl_line)
            if has_javadoc(lines, insert_at):
                continue
            indent_match = re.match(r"^(\s*)", lines[decl_line - 1])
            indent = indent_match.group(1) if indent_match else ""
            doc_lines = [method_summary(member.name, True, type_node.name, "void")]
            for param in member.parameters:
                ptype = type_name_of(param.type)
                doc_lines.append(
                    f"@param {param.name} the {param_phrase(param.name, ptype)}"
                )
            doc_lines.extend(throws_lines(member.throws))
            insertions.append(
                Insertion(insert_at + 1, format_javadoc(doc_lines, indent))
            )
        elif isinstance(member, javalang.tree.FieldDeclaration):
            mods = set(member.modifiers or [])
            if not is_api_member(mods, in_interface):
                continue
            for decl in member.declarators:
                if decl.position is None:
                    continue
                decl_line = decl.position.line
                insert_at = leading_annotation_line(lines, decl_line)
                if has_javadoc(lines, insert_at):
                    continue
                indent_match = re.match(r"^(\s*)", lines[decl_line - 1])
                indent = indent_match.group(1) if indent_match else ""
                ftype = type_name_of(member.type)
                doc_lines = [f"The {type_phrase(ftype)} {{@code {decl.name}}}."]
                insertions.append(
                    Insertion(insert_at + 1, format_javadoc(doc_lines, indent))
                )
        elif isinstance(
            member,
            (
                javalang.tree.ClassDeclaration,
                javalang.tree.InterfaceDeclaration,
                javalang.tree.EnumDeclaration,
            ),
        ):
            document_type(member, lines, insertions, nested_in_interface=in_interface)


def process_with_javalang(path: Path, source: str) -> Optional[str]:
    lines = source.splitlines(keepends=True)
    try:
        tree = javalang.parse.parse(source)
    except Exception:
        return None

    insertions: list[Insertion] = []
    for path, type_node in tree.filter(javalang.tree.TypeDeclaration):
        if len(path) != 2:
            continue
        document_type(type_node, lines, insertions, nested_in_interface=False)

    if not insertions:
        return source

    # Insert from bottom to top (line numbers are 1-based insertion points)
    insertions.sort(key=lambda i: i.line, reverse=True)
    for ins in insertions:
        idx = ins.line - 1
        lines.insert(idx, ins.text)
    return "".join(lines)


def params_from_signature(line: str) -> list[tuple[str, str]]:
    m = re.search(r"\((.*)\)", line)
    if not m:
        return []
    inside = m.group(1).strip()
    if not inside:
        return []
    params = []
    for chunk in inside.split(","):
        chunk = chunk.strip()
        chunk = re.sub(r"^final\s+", "", chunk)
        parts = chunk.split()
        if len(parts) >= 2:
            params.append((parts[-1], parts[-2]))
    return params


def process_with_regex(path: Path, source: str) -> str:
    """Fallback for sources that javalang cannot parse (text blocks, pattern matching)."""
    lines = source.splitlines(keepends=True)
    insertions: list[Insertion] = []

    patterns = [
        (
            re.compile(r"^(\s*)(public|protected)\s+(final\s+)?(class|interface|enum)\s+(\w+)"),
            "type",
        ),
        (
            re.compile(r"^(\s*)(public|protected)\s+(final\s+)?class\s+(\w+)\s*\("),
            "constructor_named",
        ),
        (
            re.compile(
                r"^(\s*)(public|protected)\s+(?!class|interface|enum)([\w.<>,\s\[\]]+?)\s+(\w+)\s*\("
            ),
            "method",
        ),
    ]

    for i, line in enumerate(lines):
        for regex, kind in patterns:
            m = regex.match(line)
            if not m:
                continue
            insert_at = leading_annotation_line(lines, i + 1)
            if has_javadoc(lines, insert_at):
                break
            indent = m.group(1)
            if kind == "type":
                name = m.group(5)
                doc = format_javadoc([class_summary(name, m.group(4) == "interface", [], None)], indent)
            elif kind == "constructor_named":
                name = m.group(4)
                doc_lines = [f"Creates a new {{@code {name}}}."]
                for pname, ptype in params_from_signature(line):
                    doc_lines.append(f"@param {pname} the {param_phrase(pname, ptype)}")
                doc = format_javadoc(doc_lines, indent)
            else:
                name = m.group(4)
                ret = m.group(3).strip().split()[-1]
                doc_lines = [method_summary(name, False, "", ret)]
                for pname, ptype in params_from_signature(line):
                    doc_lines.append(f"@param {pname} the {param_phrase(pname, ptype)}")
                if ret != "void":
                    doc_lines.append(f"@return the {type_phrase(ret)}")
                doc = format_javadoc(doc_lines, indent)
            insertions.append(Insertion(insert_at + 1, doc))
            break

    if not insertions:
        return source
    insertions.sort(key=lambda x: x.line, reverse=True)
    for ins in insertions:
        lines.insert(ins.line - 1, ins.text)
    return "".join(lines)


def ensure_package_info(package_dir: Path) -> None:
    package_name = package_name_for_dir(package_dir)
    pkg_file = package_dir / "package-info.java"
    rel = package_name.removeprefix("org.ejavdge.").removeprefix("org.ejavdge")
    if rel.startswith("."):
        rel = rel[1:]
    blurb = PACKAGE_BLURBS.get(rel, f"Support types for {rel.replace('.', ' ')}.")
    content = f"/**\n * {blurb}\n */\npackage {package_name};\n"
    pkg_file.write_text(content)


def main() -> int:
    java_files = sorted(SOURCE_ROOT.rglob("*.java"))
    java_files = [p for p in java_files if p.name != "package-info.java"]
    changed = 0
    for path in java_files:
        original = path.read_text()
        updated = process_with_javalang(path, original)
        if updated is None:
            updated = process_with_regex(path, original)
        if updated != original:
            path.write_text(updated)
            changed += 1

    packages = sorted({p.parent for p in java_files})
    for package_dir in packages:
        ensure_package_info(package_dir)

    print(f"Updated {changed} Java files; package-info in {len(packages)} directories.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
