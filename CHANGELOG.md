# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## version - yyyy-mm-dd

### Added

### Changed

### Fixed


## 0.4.0 - 2020-06-18

### Added

* Search results from three additional datasets:
  - the botany collection of the Museum of Evolution in Uppsala (obtained from GBIF)
  - the entomological collection at the Swedish Museum of Natural History (obtained from GBIF)
  - a dataset from Swedish Virtual Herbarium with "socknar"
* Scale bar visible on map.
* Search result location coordinates displayed in different formats:
  - Degrees, minutes, seconds (WGS84 DMS)
  - Degrees, decimal minutes (WGS84 DDM)
  - Decimal degrees (WGS84 DD)
  - RT90
  – SWEREF99 TM
* Code for preprocessing data, wich is kept in a separate repository: https://github.com/naturhistoriska/georg-data


### Changed

* Updated information on the about page ("Om Georg"). 
* Improved search.


## 0.3.0 - unknown

### Added

* Ability to add uncertainty radius for a custom location ("Din plats").
* Search results from one GBIF dataset. The first GBIF datasets used by Georg represents the vascular plant collection at the Swedish Museum of Natural History.
* Version number is visible on the about page ("Om Georg")


### Fixed

* Coordinates in decimal degrees will be consistently displayed with 6 decimals.


## 0.2.0 - 2020-04-24

### Added

* Ability to view all records in the result list on the map.
* Ability to add a custom location on the map and view the coordinates for that location.
* Active record is highlighted on the map (when hovering over the result list or when clicking on a marker icon in the list)
* A detail view of an individual record in the result list.


## 0.1.0 - 2020-03-31

Initial release.

### Added

* Search field for searching on place names.
* Search results from Pelias based on Who's On First data for Sweden.
* Maps displayed using the Leaflet library.
* Result list for displaying up to 10 records with administrative divistion and coordinates.
* Ability to see one selected record from the result list on the map.
