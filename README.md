# Georg

[![Build Status](https://travis-ci.com/Naturhistoriska/Georg.svg?branch=master)](https://travis-ci.com/Naturhistoriska/Georg)
[![codecov](https://codecov.io/gh/Naturhistoriska/Georg/branch/master/graph/badge.svg)](https://codecov.io/gh/Naturhistoriska/Georg)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Georg is a web application for georeferencing natural history collections. It aims to facilitate georeferencing of museum specimens by taking advantage of digitised information about collecting locations.

The development of Georg is funded through the [Swedish Taxonomy Initiative](https://www.artdatabanken.se/en/verksamhet/svenska-artprojektet/) and [SYNTHESYS+](https://www.synthesys.info).

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

**[georg-service](https://github.com/Naturhistoriska/Georg/tree/master/georg-service)**

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

### How does georg work in practice?

* We gather locality information for georeferenced locations from different data sources.
* We process the information to make the locations searchable before we import them into Georg.
* When you search, Georg compiles and ranks the result so that you get the most relevant search hits.

> The locations are displayed on a map, with coordinates provided in a number of formats so that you can copy them into your system.
> For each location, you can view the information in JSON format. We have made an example of how to use the JSON information to geocode directly from FileMaker: FileMaker example (Github).

## License

> Georg and its source code is distributed under the [MIT license](https://choosealicense.com/licenses/mit/).
