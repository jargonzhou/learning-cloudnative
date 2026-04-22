class Documentation:
    def __init__(self, url, description):
        self.url = url
        self.description = description

    def __str__(self):
        return f"""URL: {self.url}
Description: {self.description}"""


class ParamPart:
    def __init__(self, type, options, default, deprecated, description, required):
        self.type = type
        self.options = options
        self.default = default
        self.deprecated = deprecated
        self.description = description
        self.required = required

    def __str__(self):
        return f"""
    Type: {self.type}
    Description: {self.description}
    Required: {self.required}
    Options: {self.options}
    Deprecated: {self.deprecated}"""


class NamedParamPart:
    def __init__(self, name: str, part: ParamPart) -> None:
        self.name = name
        self.part = part

    def __str__(self):
        return f"{self.name}: {self.part}"


class Parts:
    def __init__(self, named_param_parts: list[NamedParamPart]) -> None:
        self.named_param_parts = named_param_parts

    def __str__(self):
        return "\n  " + "\n ".join([str(i) for i in self.named_param_parts])


class Deprecated:
    def __init__(self, version: str, description: str) -> None:
        self.version = version
        self.description = description

    def __str__(self):
        return f"""
 Version: {self.version}
 Description: {self.description}
"""


class Path:
    def __init__(self, path: str, methods: list[str], parts: Parts, deprecated: Deprecated) -> None:
        self.path = path
        self.methods = methods
        self.parts = parts
        self.deprecated = deprecated

    def __str__(self):
        _deprecated = ""
        if self.deprecated is not None:
            _deprecated = "\n Deprecated: " + str(self.deprecated)
        _parts = ""
        if self.parts is not None:
            _parts = "\n Parts: " + str(self.parts)
        return f"""
 {" ".join(self.methods)} {self.path}{_deprecated}{_parts}"""


class Headers:
    def __init__(self, accept: str, content_type: list[str]) -> None:
        self.accept = accept
        self.content_type = content_type

    def __str__(self):
        return (f"""
 Accept: {self.accept}
 Content-Type: {self.content_type}""")


class Params:
    def __init__(self, named_param_parts: list[NamedParamPart]) -> None:
        self.named_param_parts = named_param_parts

    def __str__(self):
        return "\n " + "\n ".join([str(i) for i in self.named_param_parts])


class Body:
    def __init__(self, description, required, serialize) -> None:
        self.description = description
        self.required = required
        self.serialize = serialize

    def __str__(self):
        return f"""
 Description: {self.description}
 Required: {self.required}
 Serialize: {self.serialize}"""


class Api:
    def __init__(self, name: str = None,
                 documentation: Documentation = None, stability: str = None, visibility: str = None,
                 feature_flag: str = None,
                 headers: Headers = None, paths: list[Path] = None, params: Params = None, body: Body = None) -> None:
        self.name = name
        self.documentation = documentation
        self.stability = stability
        self.visibility = visibility
        self.feature_flag = feature_flag
        self.headers = headers
        self.paths = paths
        self.params = params
        self.body = body

    def __str__(self):
        _params = ""
        if self.params is not None:
            _params = f"""\nParams: {self.params}"""
        _body = ""
        if self.body is not None:
            _body = f"""\nBody: {self.body}"""
        _path = "\n".join([str(i) for i in (self.paths or [])])
        return f"""Name: {self.name}
Stability: {self.stability}
Path: {_path}
Headers: {self.headers}{_params}{_body}"""
