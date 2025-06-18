# CruiseShipRoyalCaribs

#  ShipProxy &  ShoreProxy

A lightweight Java-based proxy system that forwards **HTTP requests from a ship to shore** over a single TCP connection — one request at a time!

---

##  What It Does

- Acts as a **proxy server** you can use with `curl` or your browser
- Forwards all requests from **ShipProxy** to **ShoreProxy** over TCP
- Ensures **sequential processing** of requests (one after another)
- Fully **Dockerized** for easy setup

---

##  Components

- **ShipProxy** — listens on port `8080`, accepts proxy requests
- **ShoreProxy** — handles real HTTP(S) requests and sends back responses
- **SharedLib** — common model (`ProxyRequest`, `ProxyResponse`)

---

##  Quick Setup

###  Step 1: Build Docker Images


```bash
# Build shared library
cd sharedlib
mvn clean install -DskipTests
cd ../

# Build ShoreProxy
 docker build -t shoreproxy -f ShoreProxy/Dockerfile .

# Build ShipProxy
docker build -t shipproxy -f ShipProxy/Dockerfile .

###  Step 2: Run Docker Images

```bash
# create Docker Network
docker network create cruise-net

# Run ShoreProxy
docker run -d --name shoreproxy --network cruise-net -p 9002:9002 shoreproxy

# Run ShipProxy
docker run -d --name shipproxy --network cruise-net -p 8080:8080 shipproxy


###  Step 3: Run Test Case

curl -x http://localhost:8080 http://httpforever.com/


###  Step 4: Stop Docker Container

docker stop shipproxy shoreproxy && docker rm shipproxy shoreproxy





