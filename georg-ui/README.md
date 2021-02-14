# georg-ui

## Setup locally for development

This guide will help you clone and setup the repository and then start the
application with hot reloading for development.

## Prerequisites

You need node.js and vuejs to run the ui

```
node: v10.15.1
vue 4.2.3
```

## Setup Node.js and yarn

We are using nvm (node version manager) to manage node versions. It is possible
to run node in other ways as long as you have the correct node version
(10.15.1). Node supports different package managers. In this project we are
using yarn with version 1.10.1 (it is important to have this specific version).

## Install nvm

Follow the guide to [Install nvm](https://github.com/nvm-sh/nvm#installation)

## Install Node.js version 10.15.1

```
nvm install 10.15.1
```

## Set Node v10.15.1 as default

```
nvm alias default v10.15.1
```

## Install yarn

[Install yarn](https://classic.yarnpkg.com/en/docs/install#mac-stable)

## Clone and setup the repository

```
https://github.com/Naturhistoriska/Georg.git
cd Georg/georg-ui
```

## Install and setup

```
yarn install
```

## Start UI and run:

```
yarn serve
http://localhost:8080/
```

### Compiles and minifies for production

```
yarn build
```

### Run your unit tests

```
yarn test:unit
```

### Run your end-to-end tests

```
yarn test:e2e
```

### Lints and fixes files

```
yarn lint
```

### Customize configuration

See [Vue CLI configuration reference](https://cli.vuejs.org/config/).
