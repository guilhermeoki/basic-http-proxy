# basic-http-proxy
A basic HTTP Layer 7 Web Proxy with support to SNI

## What is it?

basic-http-proxy is a http layer 7 proxy using jetty and with support to SNI.

## How to build the project

We're using maven as the package manager.

To build the project, just run

```
mvn clean install
```

## How to run

java -jar target/basic-http-proxy-1.0.1-jar-with-dependencies.jar

## How to install using Ansible

You can set up the inventories/<your_environment>/inventory.ini with your hosts

Then, you need to run

```
ansible-playbook --become iac/router.yaml -i inventories/<your_environment>/inventory.ini
```

That installation set up basic-http-proxy running on 80 and 443 ports and an nginx instance running on ports 5001 and 5002.

