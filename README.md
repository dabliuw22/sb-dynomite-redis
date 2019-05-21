# DynomiteDB and Redis with Spring Boot

1. Install Package for Ubuntu:
```
sudo apt-get update
sudo apt-get install libtool autoconf automake libssl-dev
```

2. Clone Dynomite:
```
git clone git@github.com:Netflix/dynomite.git
```

3. Make Dynomite:
```
cd $DYNOMITE_HOME
autoreconf -fvi
./configure --enable-debug=yes
make
```

4. Redis for docker.
```
docker pull redis:alpine
docker run --name redis-6379 -p 6379:6379 -d redis:alpine
docker run --name redis-6380 -p 6380:6379 -d redis:alpine
docker run --name redis-6381 -p 6381:6379 -d redis:alpine
docker run --name redis-6382 -p 6382:6379 -d redis:alpine
```

5. Create configuration files for datacenter `dc1`:

Create `dc1-rack1-node1.yml` file in `$DYNOMITE_HOME/conf/`:
```
dyn_o_mite:
  datacenter: dc1
  rack: rack1
  dyn_listen: 0.0.0.0:7379
  dyn_seeds:
  - 127.0.0.1:7380:rack1:dc1:2147483647
  - 127.0.0.1:7381:rack2:dc1:0
  - 127.0.0.1:7382:rack2:dc1:2147483647
  listen: 0.0.0.0:8379
  servers:
  - 127.0.0.1:6379:1
  tokens: '0'
  secure_server_option: datacenter
  pem_key_file: $DYNOMITE_HOME/conf/dynomite.pem
  data_store: 0
  stats_listen: 0.0.0.0:22222
  read_consistency : DC_ONE
  write_consistency : DC_ONE
```

Create `dc1-rack1-node2.yml` file in `$DYNOMITE_HOME/conf/`:
```
dyn_o_mite:
  datacenter: dc1
  rack: rack1
  dyn_listen: 0.0.0.0:7380
  dyn_seeds:
  - 127.0.0.1:7379:rack1:dc1:0
  - 127.0.0.1:7381:rack2:dc1:0
  - 127.0.0.1:7382:rack2:dc1:2147483647
  listen: 0.0.0.0:8380
  servers:
  - 127.0.0.1:6380:1
  tokens: '2147483647'
  secure_server_option: datacenter
  pem_key_file: $DYNOMITE_HOME/conf/dynomite.pem
  data_store: 0
  stats_listen: 0.0.0.0:22223
  read_consistency : DC_ONE
  write_consistency : DC_ONE
```

Create `dc1-rack2-node1.yml` file in `$DYNOMITE_HOME/conf/`:
```
dyn_o_mite:
  datacenter: dc1
  rack: rack2
  dyn_listen: 0.0.0.0:7381
  dyn_seeds:
  - 127.0.0.1:7379:rack1:dc1:0
  - 127.0.0.1:7380:rack1:dc1:2147483647
  - 127.0.0.1:7382:rack2:dc1:2147483647
  listen: 0.0.0.0:8381
  servers:
  - 127.0.0.1:6381:1
  tokens: '0'
  secure_server_option: datacenter
  pem_key_file: $DYNOMITE_HOME/conf/dynomite.pem
  data_store: 0
  stats_listen: 0.0.0.0:22224
  read_consistency : DC_ONE
  write_consistency : DC_ONE
```

Create `dc1-rack2-node2.yml` file in `$DYNOMITE_HOME/conf/`:
```
dyn_o_mite:
  datacenter: dc1
  rack: rack2
  dyn_listen: 0.0.0.0:7382
  dyn_seeds:
  - 127.0.0.1:7379:rack1:dc1:0
  - 127.0.0.1:7380:rack1:dc1:2147483647
  - 127.0.0.1:7381:rack2:dc1:0
  listen: 0.0.0.0:8382
  servers:
  - 127.0.0.1:6382:1
  tokens: '2147483647'
  secure_server_option: datacenter
  pem_key_file: $DYNOMITE_HOME/conf/dynomite.pem
  data_store: 0
  stats_listen: 0.0.0.0:22225
  read_consistency : DC_ONE
  write_consistency : DC_ONE
```

6. Run Dynomite Cluster:
```
$DYNOMITE_HOME/src/dynomite -c $DYNOMITE_HOME/conf/dc1-rack1-node1.yml
$DYNOMITE_HOME/src/dynomite -c $DYNOMITE_HOME/conf/dc1-rack1-node2.yml
$DYNOMITE_HOME/src/dynomite -c $DYNOMITE_HOME/conf/dc1-rack2-node1.yml
$DYNOMITE_HOME/src/dynomite -c $DYNOMITE_HOME/conf/dc1-rack2-node2.yml
```

7. Run project.