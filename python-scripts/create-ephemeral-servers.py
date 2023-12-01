from __future__ import annotations

from os import environ

from hcloud import Client
from hcloud.images import Image
from hcloud.server_types import ServerType
import json

token = environ["HCLOUD_TOKEN"]
client = Client(token=token)

config_file = open('./configuration.json')
config = json.load(config_file)

for server in config['ephemeral']:
    print(server.serverType)

response = client.servers.create(
    name="raman-py-server",
    server_type=ServerType("cx11"),
    image=Image(name="ubuntu-20.04"),
)
server = response.server
print(server)
print("Root Password" + response.root_password)

# List your servers
servers = client.servers.get_all()
for server in servers:
    print(f"{server.id=} {server.name=} {server.status=}")