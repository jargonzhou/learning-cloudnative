from parser import JsonSchemaParser
import json
import pandas as pd

COMMON_DIR = ("D:/workspace/github/application-store/data-engineering/elastic/elasticsearch/rest-api-spec/src/main"
              "/resources")
SCHEMA = COMMON_DIR + "/schema.json"
JSON_DIR = COMMON_DIR + "/rest-api-spec/api"

if __name__ == '__main__':
    parser = JsonSchemaParser(JSON_DIR)

    apis = parser.parse()
    for api in apis:
        print(api)
        print()
