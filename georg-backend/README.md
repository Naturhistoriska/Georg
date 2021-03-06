# georg-service
georg-service is a web service build on top of [georg-pelias](https://github.com/Naturhistoriska/georg-pelias.git). It serves as API for Georg Application. The API provides endpoints for forward geocoding, reverse geocoding and batch upload.

george-service is written by JAVA 8. We use maven 3 to build the project.
This documentation will help you clone, setup the repository, and run the application.
## Prerequisites
```
Java 8
Maven 3.5.0
Docker
docker-compose
georg-pelias
```
## Setup
### Clone the repository
```
git clone https://github.com/Naturhistoriska/Georg.git
```
### Move into directory
```
cd georg-backend
```
### Build
```
mvn clean package
```
### Run
### Start georg-pelias
Follow the instructions on [georg-pelias github repository](https://github.com/Naturhistoriska/georg-pelias) to setup and run georg-pelias
### Configurations:
georg-service is build with wildfly-swarm microservices framework. It builds an uber JAR that includes the minimal Java EE APIs and implementations necessary to run the service. georg-service uses external configurations, a project-initdata.yml file to config system and environment variables.

The configuration project-initdata.yml file is located at the root of georg-backend

```
## YAML Template.
---
swarm:
  port:
    offset: 100
  pelias:
    path: http://localhost:4000/v1/
```
Open the file, replace swarm:pelias:path to your local pelias instance. The swarm:port:offset is the offset of wildfly AS port number. By default wildfly runs at port 8080.

#### Start API
```
java -jar georg-service/target/georgApi-thorntail.jar -Sinitdata
```
georg-service API starts at localhost port 8180
## How to use API
Refer to [API Documention](https://georg.nrm.se/swagger-ui/index.html?url=/api/swagger.json) for available endpoints

Currently API provides following five endpoints:
* Forward geocoding (/api/search)
* Autocomplete (/api/autocomplete)
* Reverse geocoding (/api/reverse)
* Reverse geocoding with coordinates (/api/coorinates)
* Batch upload (/api/upload)
> Forward geocoding
A free text search to find a place by searching for an address or name.
This search takes query parameters and options:
* text - locality information (This query parameter is required)
* sources - data sources are imported into pelias (Optional).
    * georg-pelias currently supports following data sources (georg-pelias only supports datasources over the nordic countries. It is possible to expand datasources over the world):
        * Who's on First (wof)
        * OpenAddresses (oa)
        * OpenStreetMap (osm)
        * GBIF (gbif)
        * swe-virtual-herbarium (swe-virtual-herbarium)
* layers - subset of datasource (Optional).
    * georg-pelias supports following layers:
        * venue
        * street
        * locality
        * localadmin
        * postalcode
        * neighbourhood
        * county
        * region
        * macrohood
        * borough
        * country
        * macroregion
        * dependency
        * nhrs-nrm (From GBIF)
        * uppsala-botany (From GBIF)
        * s-fbo (From GBIF)
        * socken (From swe-virtual-herbarium)
* countryCode - Search for specific country (Optional)
    * At present georg-pelias only can georference locations in the nordic countries. By default, the search searches locations in all the nordic countries. By add countryCode swe, the search searches locations only in Sweden
* size - define number of suggestions shall be returned from API. The default size is 10
> Autocomplete
With autocomplete, user can type in text from georg-ui and get a real-time result suggestions back without having to type the whole location. Autocomplete takes same query parameters and options as Forward geocoding.

> Reverse geocoding
Find what is located at a certain coordinate location. Reverse geocoding are not working on custom datasets based on limition of Pelias.

Reverse geocoding takes following query parameters:
* lat - latitude
* lng - longitude
> Reverse geocoding with coordinates
Same as Reverse geocoding, this endpoint allows user send in coordinats (latitude and longitude) as a whole text in different format. The supported formats are: dd, dms, and ddm. The latitude and longitude shall be separated by comma or space. ex:
   * '59.350828, 17.938927',
   * '59°21'03.0" N 17°56'20.1" E',
   * '59°21.050' N 17°56.336' E'
> Batch upload
This endpoint consumes MULTIPART_FORM_DATA which allows user to upload a csv file with minimum of two columns: Id, SourceLocality, and get best match result for each locality back as csv or json file. You can find sample csv templet from [github](https://github.com/Naturhistoriska/Georg/blob/master/data/georg_batch.csv).

To download sample csv templet, run:
cURL -o /{path_to_directory}/sample.csv https://raw.githubusercontent.com/Naturhistoriska/Georg/master/data/georg_batch.csv

Batch upload takes query parameters:
* type - the type of file returned by API. Currently API supports two type of files to returned from API: csv and json
### Examples access API
> Access API via web browser:
```
http://localhost:8180/search?text=stockholm
http://localhost:8180/autocomplete?text=stockholm
http://localhost:8180/reverse?lat=59.450880&lng=17.93102
http://localhost:8180/coordinates?coordinates=59.450880,17.93102
```
> Access API with command line:
```
curl http://localhost:8180/search?text=stock | json_pp
curl http://localhost:8180/autocomplete?text=stockholm | json_pp
curl http://localhost:8180/reverse?lat=59.450880&lng=17.93102 | json_pp
curl http://localhost:8180/coordinates?coordinates=59.450880,17.93102 | json_pp
```
> For batch upload:
```
curl -F file=@/Users/idali/georg_batch.csv  http://localhost:8180/upload?type=json | json_pp
```

### Build docker image
Goto georg-service directory and run:
```
cd georg-service
make build
```
### Release docker image to Docker hub
```
make release
```
