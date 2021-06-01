# Georg

[![Build Status](https://travis-ci.com/Naturhistoriska/Georg.svg?branch=master)](https://travis-ci.com/Naturhistoriska/Georg)
[![codecov](https://codecov.io/gh/Naturhistoriska/Georg/branch/master/graph/badge.svg)](https://codecov.io/gh/Naturhistoriska/Georg)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Georg is a web application designed to support georeferencing, or, the process of obtaining geographic coordinates from a locality description, for natural history collections data. It allows a user to select among suggested matches to the entered text string or by choosing a map point and then selecting a named place based on geographic proximity to the marker. Georg currently supports single-locality queries as well as limited batch processing for uploaded lists of localities.

Georeferencing is a prerequisite to using natural history collections data in research or visualization that relies on geospatial interpretations. At natural history collections and herbaria, locality data often represents rigorous interpretation  of expedition or collecting data. Our intention is to facilitate use of these interpretations while encouraging greater refinement of existing georeferencing workflows.

Data capture at natural history collections often takes place with an emphasis on particular geographic regions, taxonomic groups, or expeditions. Customized subsets of localities compiled under these georeferencing themes are used in Georg to assign coordinates in the context of relevant interpretations available from the biodiversity informatics community. This is also likely to improve feedback on the original data through more rigorous evaluation under novel georeferencing themes, a process that is otherwise limited to basic validation prior to data use or visualization.

## How Georg works
 
Georg consists of four parts:

* georg-pelias,
* georg-service,
* georg-ui
* georg-data.

**[georg-pelias](https://github.com/Naturhistoriska/georg-pelias.git)**

> Forked out from [Pelias/Docker](https://github.com/pelias/docker.git). It is a search engine for places over the Nordic countries and possible to expand over the world. georg-pelias is dockerized. Currently data imported into georg-pelias are:

* [Who's on first](https://whosonfirst.org/),
* [OpenAddress](https://openaddresses.io/)
* [OpenStreetMaps](https://www.openstreetmap.org/#map=4/62.99/17.64).
* [GBIF Sweden](http://www.gbif.se/ipt/)
  * [Entomological Collections (NHRS), Swedish Museum of Natural History (NRM)](<(https://doi.org/10.15468/fpzyjx)>)
  * [Phanerogamic Botanical Collections (S)](<(https://doi.org/10.15468/yo3mmu)>)
  * [Botany (UPS)](https://doi.org/10.15468/ufmslw)
* [Sweden's Virtual Herbarium (SVH)](https://github.com/mossnisse/Virtuella-Herbariet)

**[georg-service](https://github.com/Naturhistoriska/Georg/tree/master/georg-backend)**

> API build on top of georg-pelias. It allows user to send a text with locality information, or coordinates and get back location suggestions (in JSON format). API is also possible for users to upload a csv file with list of locality information, and get back the best match location suggestion for each locality (in both JSON or CSV format).
> Current API provides five endpoints:

* search
* reverse geocoding (with latitude and longitude)
* reverse geocoding (with coordinates as a text in dd, dms, ddm format )
* suggest
* batch upload

**[georg-ui](https://github.com/Naturhistoriska/Georg/tree/master/georg-ui)**

> A web based application that allows user to type in text with locality information and get back location suggestions, and display location on map. georg-ui is also allows user type in coordinates or click on map for reverse geo-coding. The batch upload inteface allows user to select csv file with list of locality information from local computer and upload that file to [API](https://georg.nrm.se/swagger-ui/index.html?url=/api/swagger.json) for georeferencing. [georg-ui](https://github.com/Naturhistoriska/Georg/tree/master/georg-ui) uses leaflet map component for displaying search results. Currently georg-ui uses three map layers:

* OpenStreetMap
* Lantmäteriet topografish
* Lantmäeriet nedtonad
> It is possible to add additional map layers into the map.

**[georg-data](https://github.com/Naturhistoriska/georg-data)**

> A tool for processing customized dataset before importing them into georg-pelias

### How to run georg API with CLI
Georg API works with Restish. Restish is a CLI for interacting with REST API.
Reference: **[Restish](https://rest.sh/#/guide?id=guide)** for installation and configuration

Installation
> brew tap danielgtaylor/restish && brew install restish
restish --version

Comfiguration:
>restish api configure **<#name_of_service>**
ex: restish api configure georg
? Base URI **<type in url>**
Save and exit

Basic usage:
Examples:
> restish -o json q text=val georg/api/search
restish -o json -q text=val georg/api/autocomplete
restish -o json -q lat=53.348190 -q lng=-1.744850 georg/api/reverse
restish post -o json georg/api/upload?type=json <georg_batch.csv

Output:
> $ restish -o json -q text=val -q size=1 georg-stage/api/search

<details>
  <summary>Click to expand</summary>

```{
  "body": {
    "bbox": [
      -2.693038,
      53.72626,
      -2.693038,
      53.72626
    ],
    "features": [
      {
        "geometry": {
          "coordinates": [
            -2.693038,
            53.72626
          ],
          "type": "Point"
        },
        "properties": {
          "accuracy": "point",
          "addendum": {
            "osm": {
              "phone": "+44 1772 313001",
              "website": "https://www.vjhughesoptometrist.co.uk/",
              "wheelchair": "limited"
            }
          },
          "confidence": 1,
          "coordinates": {
            "dd": [
              "53.726260",
              "-2.693038"
            ],
            "ddm": [
              "53°43.576' N",
              "2°41.582' W"
            ],
            "dms": [
              "53°43'34.5\" N",
              "2°41'34.9\" W"
            ],
            "rt90": [
              "6115236",
              "285694"
            ],
            "sweref99": [
              "6099636",
              "-661542"
            ]
          },
          "country": "United Kingdom",
          "country_a": "GBR",
          "country_gid": "whosonfirst:country:85633159",
          "county": "Lancashire",
          "county_a": "LAN",
          "county_gid": "whosonfirst:county:1360698817",
          "gid": "openstreetmap:venue:node/6207526900",
          "housenumber": "15",
          "id": "node/6207526900",
          "label": "Val Hughes, Farington, England, United Kingdom",
          "layer": "venue",
          "localadmin": "Farington",
          "localadmin_gid": "whosonfirst:localadmin:404434425",
          "locality": "Farington",
          "locality_gid": "whosonfirst:locality:1360755763",
          "macroregion": "England",
          "macroregion_gid": "whosonfirst:macroregion:404227469",
          "match_type": "exact",
          "name": "Val Hughes",
          "postalcode": "PR5 5RU",
          "region": "Lancashire",
          "region_gid": "whosonfirst:region:1360698567",
          "source": "openstreetmap",
          "source_id": "node/6207526900",
          "street": "Hope Terrace"
        },
        "type": "Feature"
      }
    ],
    "geocoding": {
      "attribution": "http://api:4000/attribution",
      "engine": {
        "author": "Mapzen",
        "name": "Pelias",
        "version": "1.0"
      },
      "query": {
        "lang": {
          "defaulted": true,
          "iso6391": "en",
          "iso6393": "eng",
          "name": "English",
          "via": "default"
        },
        "layers": [
          "venue",
          "street",
          "country",
          "macroregion",
          "region",
          "county",
          "localadmin",
          "locality",
          "borough",
          "neighbourhood",
          "continent",
          "empire",
          "dependency",
          "macrocounty",
          "macrohood",
          "microhood",
          "disputed",
          "postalcode",
          "ocean",
          "marinearea"
        ],
        "parsed_text": {
          "subject": "val*"
        },
        "parser": "pelias",
        "private": false,
        "querySize": 20,
        "size": 1,
        "text": "val*"
      },
      "timestamp": 1622532827308,
      "version": "0.2",
      "warnings": [
        "performance optimization: excluding 'address' layer"
      ]
    },
    "type": "FeatureCollection"
  },
  "headers": {
    "Access-Control-Allow-Origin": "*",
    "Content-Length": "1970",
    "Content-Type": "application/json",
    "Date": "Tue, 01 Jun 2021 07:33:47 GMT",
    "Server": "nginx/1.13.10",
    "Strict-Transport-Security": "max-age=31536000"
  },
  "links": {},
  "proto": "HTTP/2.0",
  "status": 200
}
```
</details>


### How does georg work in practice?

* We gather locality information for georeferenced locations from different data sources.
* We process the information to make the locations searchable before we import them into Georg.
* When you search, Georg compiles and ranks the result so that you get the most relevant search hits.

> The locations are displayed on a map, with coordinates provided in a number of formats so that you can copy them into your system.
> For each location, you can view the information in JSON format. We have made an example of how to use the JSON information to geocode directly from FileMaker: FileMaker example (Github).

## License

> Georg and its source code is distributed under the [MIT license](https://choosealicense.com/licenses/mit/).

## Funding
Georg development was funded in 2019 by the [Swedish Taxonomy Initiative](https://www.artdatabanken.se/en/verksamhet/svenska-artprojektet/) and in 2021 by [SYNTHESYS+](https://www.synthesys.info).
