# How to setup server for georg

This guide will help you setup server for georg.

## Setup mount

First we mount external space for pelias data.
We mount data into /mnt

```
cd /mnt
sudo mkdir data
sudo nano /etc/sftab
```

Add following at the end of sftab file

```
#NFS
<server url>:/nrmgeorg /mnt/<mount point (ex: data)> nfs rw,bg,hard,nointr,rsize=32768,wsize=32768,tcp,nfsvers=3,timeo=600,actimeo=600 0 0

cat /etc/fstab
```

It should display:

```
# /etc/fstab: static file system information.
#
# Use 'blkid' to print the universally unique identifier for a
# device; this may be used with UUID= as a more robust way to name devices
# that works even if disks are added and removed. See fstab(5).
#
# <file system> <mount point>   <type>  <options>       <dump>  <pass>
/dev/mapper/ubuntu--vg-root /               ext4    errors=remount-ro 0       1
/dev/mapper/ubuntu--vg-swap_1 none            swap    sw              0       0
#NFS
<server url>:/nrmgeorg /mnt/data nfs rw,bg,hard,nointr,rsize=32768,wsize=32768,tcp,nfsvers=3,timeo=600,actimeo=600 0 0
```

Install nfs-common

```
sudo apt install nfs-common
```

Check mount

```
sudo mount -a -v
```

It should display

```
/                        : ignored
none                     : ignored
mount.nfs: trying text-based options 'bg,hard,nointr,rsize=32768,wsize=32768,tcp,nfsvers=3,timeo=600,actimeo=600,addr=192.168.113.120'
mount.nfs: prog 100003, trying vers=3, prot=6
mount.nfs: trying 192.168.113.120 prog 100003 vers 3 prot TCP port 2049
mount.nfs: prog 100005, trying vers=3, prot=6
mount.nfs: trying 192.168.113.120 prog 100005 vers 3 prot TCP port 635
/mnt/data                : successfully mounted
```

## Setup docker

### Install Docker CE

#### Setup reposirtory

```
sudo apt-get update

sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
```

```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

```
sudo apt-key fingerprint 0EBFCD88

sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
```

#### Install Docker

```
sudo apt-get install docker-ce
```

After installation, to check installation, run

```
sudo apt-get install docker-ce
```

#### Setup docker user

To setup docker user, run:

```
sudo usermod -aG docker $USER
```

Then ctl-d to logout from server and login server again
Run

```
docker info
```

To verify docker user is set

#### Install docker-compose

To install docker-compose, run:

```
sudo curl -L "https://github.com/docker/compose/releases/download/1.25.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```

To set docker-compose execution, run

```
sudo chmod +x /usr/local/bin/docker-compose
```

## Setup georg-pelias

### Setup georg-pelias repository

cd to directory where georg, and georg-pelias will be installed
Clone the georg-pelias repository

```
git clone https://github.com/Naturhistoriska/georg-pelias.git
cd georg-pelias
sudo ln -s “$(pwd)/pelias” /usr/local/bin/pelias
which pelias
```

It should display

```
/usr/local/bin/pelias`
```

### Create project for UK

```
cd projects
mkdir uk
```

### Configure environment

```
nano .env
```

Add following into .env file

```
COMPOSE_PROJECT_NAME=pelias
DATA_DIR=/mnt/data/pelias/uk
```

Create docker-compose.yml file

```
version: "3"
networks:
  default:
    driver: bridge
services:
  libpostal:
    image: pelias/libpostal-service
    container_name: pelias_libpostal
    user: "${DOCKER_USER}"
    restart: always
    ports: ["4400:4400"]
  schema:
    image: pelias/schema:master
    container_name: pelias_schema
    user: "${DOCKER_USER}"
    volumes:
      - "./pelias.json:/code/pelias.json"
  api:
    image: pelias/api:master
    container_name: pelias_api
    user: "${DOCKER_USER}"
    restart: always
    environment: ["PORT=4000"]
    ports: ["4000:4000"]
    volumes:
      - "./pelias.json:/code/pelias.json"
  placeholder:
    image: pelias/placeholder:master
    container_name: pelias_placeholder
    user: "${DOCKER_USER}"
    restart: always
    environment: ["PORT=4100"]
    ports: ["4100:4100"]
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
      - "./blacklist/:/data/blacklist"
  whosonfirst:
    image: pelias/whosonfirst:master
    container_name: pelias_whosonfirst
    user: "${DOCKER_USER}"
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
      - "./blacklist/:/data/blacklist"
  openstreetmap:
    image: pelias/openstreetmap:master
    container_name: pelias_openstreetmap
    user: "${DOCKER_USER}"
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
      - "./blacklist/:/data/blacklist"
  openaddresses:
    image: pelias/openaddresses:master
    container_name: pelias_openaddresses
    user: "${DOCKER_USER}"
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
      - "./blacklist/:/data/blacklist"
  transit:
    image: pelias/transit:master
    container_name: pelias_transit
    user: "${DOCKER_USER}"
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
  csv-importer:
    image: pelias/csv-importer:master
    container_name: pelias_csv_importer
    user: "${DOCKER_USER}"
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
      - "./blacklist/:/data/blacklist"
  polylines:
    image: pelias/polylines:master
    container_name: pelias_polylines
    user: "${DOCKER_USER}"
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
  interpolation:
    image: pelias/interpolation:master
    container_name: pelias_interpolation
    user: "${DOCKER_USER}"
    restart: always
    environment: ["PORT=4300"]
    ports: ["4300:4300"]
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
  pip:
    image: pelias/pip-service:master
    container_name: pelias_pip-service
    user: "${DOCKER_USER}"
    restart: always
    environment: ["PORT=4200"]
    ports: ["4200:4200"]
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "${DATA_DIR}:/data"
  elasticsearch:
    image: pelias/elasticsearch:7.5.1
    container_name: pelias_elasticsearch
    user: "${DOCKER_USER}"
    restart: always
    ports: ["9200:9200", "9300:9300"]
    volumes:
      - "${DATA_DIR}/elasticsearch:/usr/share/elasticsearch/data"
    ulimits:
      memlock:
        soft: -1
        hard: -1
      nofile:
        soft: 65536
        hard: 65536
    cap_add: ["IPC_LOCK"]
  fuzzy-tester:
    image: pelias/fuzzy-tester:master
    container_name: pelias_fuzzy_tester
    user: "${DOCKER_USER}"
    restart: "no"
    command: "--help"
    volumes:
      - "./pelias.json:/code/pelias.json"
      - "./test_cases:/code/pelias/fuzzy-tester/test_cases"
  kibana:
    image: docker.elastic.co/kibana/kibana:7.5.2
    container_name: kibana
    ports:
      - 5601:5601
    environment:
      ELASTICSEARCH_URL: http://pelias_elasticsearch:9200
      ELASTICSEARCH_HOSTS: http://pelias_elasticsearch:9200
```

Create pelias.json

```
{
  "logger": {
    "level": "info",
    "timestamp": false
  },
  "esclient": {
    "apiVersion": "7.5",
    "hosts": [{ "host": "elasticsearch" }]
  },
  "elasticsearch": {
    "settings": {
      "index": {
        "refresh_interval": "10s",
        "number_of_replicas": "0",
        "number_of_shards": "3"
      }
    }
  },
  "acceptance-tests": {
    "endpoints": {
      "docker": "http://api:4000/v1/"
    }
  },
  "api": {
    "targets": {
      "auto_discover": true
    },
    "services": {
      "pip": { "url": "http://pip:4200" },
      "libpostal": { "url": "http://libpostal:4400" },
      "placeholder": { "url": "http://placeholder:4100" },
      "interpolation": { "url": "http://interpolation:4300" }
    }
  },
  "imports": {
    "adminLookup": {
      "enabled": true
    },
    "csv": {
      "datapath": "/data/csv",
      "files": []
    },
    "geonames": {
      "datapath": "/data/geonames",
      "countryCode": "GB"
    },
    "openstreetmap": {
      "download": [
        {
          "sourceURL": "http://download.geofabrik.de/europe/great-britain/england-latest.osm.pbf"
        },
  {
          "sourceURL": "http://download.geofabrik.de/europe/great-britain/scotland-latest.osm.pbf"
        },
  {
          "sourceURL": "http://download.geofabrik.de/europe/great-britain/wales-latest.osm.pbf"
        }
      ],
      "leveldbpath": "/tmp",
      "datapath": "/data/openstreetmap",
      "import": [
        {
          "filename": "england-latest.osm.pbf"
        },
  {
          "filename": "scotland-latest.osm.pbf"
        },
  {
          "filename": "wales-latest.osm.pbf"
        }
      ]
    },
    "openaddresses": {
      "datapath": "/data/openaddresses",
      "files": [
      ]
    },
    "polyline": {
      "datapath": "/data/polylines",
      "files": ["extract.0sv"]
    },
    "whosonfirst": {
      "datapath": "/data/whosonfirst",
      "importPostalcodes": false,
      "countryCode": ["GB"],
      "importPlace": [
        "85633159"
      ]
    }
  }
}
```

### Create data directory for in mount

```
cd /mnt/data
mkdir /pelias
mkdir /uk
```

Make sure /mnt/data/pelias/uk has right permission

### Pull all pelias images

```
docker image ls
pelias compose pull
```

### Start the Elasticsearch Service

```
pelias elastic start
pelias compose ps
pelias elastic wait
pelias elastic create
```

### Download WhosonFirst DATA

```
sudo pelias download wof
```

### Download Open Street Map (OSM) DATA

```
pelias download osm
```

### Prepare Polyline data

```
pelias prepare polylines
```

### Prepare Interpolation

```
pelias prepare interpolation
```

### Prepare Placeholder

```
pelias prepare placeholder
```

### Import WOF Data

```
pelias import wof
```

### Import OpenStreetMap Data

```
pelias import osm
```

### Import Polyline Data

```
pelias import polylines
```

Start up pelias

```
sudo pelias compose up
sudo pelias compose ps
```

### Search Endpoint

```
curl -G http://localhost:4000/v1/search --data-urlencode "text=1901 Main St"
```

## Setup proxy

cd to directory where georg, and georg-pelias will be installed

```
mkdir proxy
cd proxy
```

### Create docker-compose.yml file

```
nano docker-compose.yml

version: '3.7'
services:
  proxy:
    image: jwilder/nginx-proxy:0.7.0
    container_name: proxy
    restart: always
    init: true
    ports:
      - 80:80
      - 443:443
    volumes:
      - /var/run/docker.sock:/tmp/docker.sock
      - ./certs:/etc/nginx/certs
      - ./nginx-proxy/nginx-proxy.conf:/etc/nginx/conf.d/nginx-proxy.conf
```

### Add certificates

```
mkdir certs
```

Add certieficates into certs directory

### Configure nginx-proxy

```
mkdir nginx-proxy
cd nginx-proxy
```

Create nginx-proxy.conf file

```
nano nginx-proxy.conf

client_max_body_size 1000m;
```

### Startup proxy

cd to nginx-proxy directory and run

```
docker-compose up -d
docker-compose ps
```

## Configure webserver

cd to the directory where georg will be installed

```
mkdir webserver
cd webserver
nano app.conf

server {
  server_name georg-stage.nrm.se

  proxy_set_header    Host            $host;
  proxy_set_header    X-Real-IP       $remote_addr;
  proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_set_header    X-Forwarded-Host $server_name;
  proxy_set_header    X-Forwarded-Proto https;
  proxy_redirect off;
  port_in_redirect off;

  location /api {
    add_header Access-Control-Allow-Origin *;
    proxy_pass http://api:8180/api;
  }

  location /swagger-ui {
    add_header Access-Control-Allow-Origin *;
    proxy_pass http://api:8180/swagger-ui;
  }

  location / {
    add_header Access-Control-Allow-Origin *;
    proxy_pass http://ui:80;
  }
}
```

## Setup Georg for UK

### Clone georg-pelias repository

```
git clone https://github.com/Naturhistoriska/Georg.git
cd Georg
git checkout ida/uk-development
cd georg-ui
```

### Configure environment

Congure georg API url at VUE_APP_GEORG_API

```
nano .env
VUE_APP_GEORG_API=https://georg-stage.nrm.se/api/

VUE_APP_NHRS_NRM_KEY=9940af5a-3271-4e6a-ad71-ced986b9a9a5
VUE_APP_S_FBO_KEY=0943f690-fde5-11dd-83f4-b8a03c50a862
VUE_APP_UPPSALA_BOTANY_KEY=c1a13bf0-0c71-11dd-84d4-b8a03c50a862

VUE_APP_GBIF_DATASET=https://www.gbif.org/dataset/
VUE_APP_GBIF_OCCURRENCE=https://www.gbif.org/occurrence/
VUE_APP_GBIF_API=https://api.gbif.org/v1/

VUE_APP_LANTMATERIET_TOKEN=caaa7a34-6eab-3214-b264-333b96bb4919

VUE_APP_I18N_LOCALE=en
VUE_APP_I18N_FALLBACK_LOCALE=en
VUE_APP_I18N_SUPPORTED_LOCALES=en,sv
```

### Install make

```
sudo apt install make
```

### Build georg image

```
make build
```

After build, check docker images

```
docker images
```

It should display:

```
REPOSITORY                        TAG             IMAGE ID       CREATED              SIZE
georgnrm/georg-ui                 v1.2.2          eb7a56c95c13   About a minute ago   25.9MB
```

### Start up georg

cd to directory where Georg is installed and run

```
docker-compose up -d
docker-compose ps
```

Now you should be able to run georg from browser.
