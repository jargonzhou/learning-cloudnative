Internals
---

# Introduction

A playground of RDF store implementation, main focus on:

- File organization
- RDF triple access path
- Query with SPARQL


# Project Structure

## rdfstore-commons

utilities

## rdfstore-filesystem

access path: catalog, data file, log

## rdfstore-buffer

buffer management

## rdfstore-query

query parser: SPARQL, Gremlin

## rdfstore-protocol

network packet data format

## rdfstore-server

bootstrap, management, monitoring

## rdfstore-client

client tool
