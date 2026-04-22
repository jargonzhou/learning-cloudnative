from model import *
import json
from pathlib import Path as PPath


class JsonSchemaParser:
    def __init__(self, api_dir: str) -> None:
        self._api_path = PPath(api_dir)

    def parse(self) -> list[Api]:
        apis = []
        for api_file in self._api_path.rglob("*.json"):
            api_name = api_file.name[:-5]

            if api_name == '_common':
                api_o = json.loads(api_file.read_text())
                api = Api()
                api.name = "_common"
                api.documentation = self.__parse_documentation(
                    api_o['documentation'])
                api.params = self.__parse_params(api_o['params'])
                apis.append(api)
                continue

            api_o = json.loads(api_file.read_text())[api_name]
            api = self.__parse_api(api_o)
            api.name = api_name
            apis.append(api)
        return apis

    def __parse_api(self, api) -> Api:
        result = Api()
        if 'documentation' in api:
            result.documentation = self.__parse_documentation(
                api['documentation'])

        if 'stability' in api:
            result.stability = api['stability']

        if 'visibility' in api:
            result.visibility = api['visibility']

        if 'feature_flag' in api:
            result.feature_flag = api['feature_flag']

        if 'headers' in api:
            result.headers = self.__parse_headers(api['headers'])

        if 'url' in api:
            result.paths = []
            paths = api['url'].get('paths', None)
            for path in paths:
                result.paths.append(self.__parse_path(path))

        if 'params' in api:
            result.params = self.__parse_params(api['params'])

        if 'body' in api:
            result.body = self.__parse_body(api['body'])

        return result

    def __parse_documentation(self, o) -> Documentation | None:
        if o is None:
            return None
        return Documentation(url=o['url'], description=o['description'])

    def __parse_part(self, o) -> ParamPart | None:
        if o is None:
            return None
        return ParamPart(type=o.get('type', None),
                         options=",".join(o.get('options', [])),
                         default=o.get('default', None),
                         deprecated=o.get('deprecated', None),
                         description=o.get('description', None),
                         required=o.get('required', None))

    def __parse_parts(self, o) -> Parts | None:
        if o is None:
            return None
        result = []
        for k in o:
            result.append(NamedParamPart(name=k, part=self.__parse_part(o[k])))
        return Parts(named_param_parts=result)

    def __parse_deprecated(self, o) -> Deprecated | None:
        if o is None:
            return None
        return Deprecated(version=o['version'],
                          description=o['description'])

    def __parse_path(self, o) -> Path | None:
        if o is None:
            return None
        return Path(path=o.get('path', None),
                    methods=o.get('methods', []),
                    parts=self.__parse_parts(o.get('parts', None)),
                    deprecated=self.__parse_deprecated(o.get('deprecated', None)))

    def __parse_headers(self, o) -> Headers | None:
        if o is None:
            return None
        return Headers(accept=",".join(o['accept']),
                       content_type=o.get('content_type', []))

    def __parse_params(self, o) -> Params | None:
        if o is None:
            return None
        params = []
        for k in o:
            params.append(NamedParamPart(name=k, part=self.__parse_part(o[k])))
        return Params(named_param_parts=params)

    def __parse_body(self, o) -> Body | None:
        if o is None:
            return None
        return Body(description=o.get('description', None),
                    required=o.get('required', None),
                    serialize=o.get('serialize', None))
