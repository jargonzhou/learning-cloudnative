# podman
* https://podman.io/
* https://github.com/containers/podman

License: Apache 2.0, Language: Go.

Podman: A tool for managing OCI containers and pods

Podman (the POD MANager) is a tool for managing containers and images, volumes mounted into those containers, and pods made from groups of containers. Podman runs containers on Linux, but can also be used on Mac and Windows systems using a Podman-managed virtual machine. Podman is based on libpod, a library for container lifecycle management that is also contained in this repository. The libpod library provides APIs for managing containers, pods, container images, and volumes.

```shell
# WSL 2
sudo apt install -y runc
sudo apt install -y slirp4netns

# https://github.com/containers/podman/issues/21249
echo 'deb http://download.opensuse.org/repositories/devel:/kubic:/libcontainers:/unstable/xUbuntu_22.04/ /' | sudo tee /etc/apt/sources.list.d/devel:kubic:libcontainers:unstable.list
curl -fsSL https://download.opensuse.org/repositories/devel:kubic:libcontainers:unstable/xUbuntu_22.04/Release.key | gpg --dearmor | sudo tee /etc/apt/trusted.gpg.d/devel_kubic_libcontainers_unstable.gpg > /dev/null
sudo apt update
sudo apt install podman
```


```shell
➜  ~ podman version
Client:       Podman Engine
Version:      4.6.2
API Version:  4.6.2
Go Version:   go1.18.1
Built:        Thu Jan  1 08:00:00 1970
OS/Arch:      linux/amd64

➜  ~ podman run -d -p 8080:80/tcp docker.io/library/httpd
a5ca1df4be3adf71138bec656a40df2abe6b5de6d8bc3102fe9090c5e2ccb29e
➜  ~ podman ps
CONTAINER ID  IMAGE                           COMMAND           CREATED        STATUS        PORTS                 NAMES
a5ca1df4be3a  docker.io/library/httpd:latest  httpd-foreground  4 seconds ago  Up 5 seconds  0.0.0.0:8080->80/tcp  quirky_jackson

➜  ~ podman container stop a5ca1df4be3a
a5ca1df4be3a
➜  ~ podman container rm a5ca1df4be3a
a5ca1df4be3a

➜  ~ podman images
REPOSITORY               TAG         IMAGE ID      CREATED      SIZE
docker.io/library/httpd  latest      95e97c51ad04  2 weeks ago  120 MB
➜  ~ podman image rm -f 95e97c51ad04
Untagged: docker.io/library/httpd:latest
Deleted: 95e97c51ad0493de94e31f59bb02af16af664acb68afbb84f2b0e6df0508ed1c
```

# podman-py
* https://github.com/containers/podman-py

```shell
pip install podman

# Podman socket activation: https://github.com/containers/podman/blob/main/docs/tutorials/socket_activation.md
➜  podman git:(main) ✗ systemctl --user start podman.socket
➜  podman git:(main) ✗ python ex_podman_client.py
Release:  4.6.2
Compatible API:  1.41
Podman API:  4.6.2

<Image: 'docker.io/library/httpd:latest'> 95e97c51ad0493de94e31f59bb02af16af664acb68afbb84f2b0e6df0508ed1c

<Image: 'ghcr.io/ansible/creator-ee:latest'> ddb78cf5ef525faf46344166172f3ddba05e680ff28341d41168a93d7c51fd55

<Container: 8c290939b5> 8c290939b589136658594046938937cdd03edc0132319ad04bb237a7e0289168

<Container: 8c290939b5> running

['AppArmorProfile', 'Args', 'BoundingCaps', 'Config', 'ConmonPidFile', 'Created', 'Dependencies', 'Driver', 'EffectiveCaps', 'ExecIDs', 'GraphDriver', 'HostConfig', 'HostnamePath', 'HostsPath', 'Id', 'Image', 'ImageDigest', 'ImageName', 'IsInfra', 'IsService', 'KubeExitCodePropagation', 'MountLabel', 'Mounts', 'Name', 'Namespace', 'NetworkSettings', 'OCIConfigPath', 'OCIRuntime', 'Path', 'PidFile', 'Pod', 'ProcessLabel', 'ResolvConfPath', 'RestartCount', 'Rootfs', 'State', 'StaticDir', 'lockNumber']
{
    "ImagesSize": 1007721135,
    "Images": [
        {
            "Repository": "docker.io/library/httpd",
            "Tag": "latest",
            "ImageID": "95e97c51ad0493de94e31f59bb02af16af664acb68afbb84f2b0e6df0508ed1c",
            "Created": "2026-03-16T22:23:01.522892039Z",
            "Size": 120211238,
            "SharedSize": 0,
            "UniqueSize": 120211238,
            "Containers": 1
        },
        {
            "Repository": "ghcr.io/ansible/creator-ee",
            "Tag": "latest",
            "ImageID": "ddb78cf5ef525faf46344166172f3ddba05e680ff28341d41168a93d7c51fd55",
            "Created": "2024-02-08T13:57:59.985571012Z",
            "Size": 887509897,
            "SharedSize": 0,
            "UniqueSize": 887509897,
            "Containers": 0
        }
    ],
    "Containers": [
        {
            "ContainerID": "8c290939b589136658594046938937cdd03edc0132319ad04bb237a7e0289168",
            "Image": "95e97c51ad0493de94e31f59bb02af16af664acb68afbb84f2b0e6df0508ed1c",
            "Command": [
                "httpd-foreground"
            ],
            "LocalVolumes": 0,
            "Size": 120222278,
            "RWSize": 11040,
            "Created": "2026-04-01T12:12:09.80525916+08:00",
            "Status": "running",
            "Names": "goofy_wescoff"
        }
    ],
    "Volumes": []
}
```


# See Also
* [runc](https://github.com/opencontainers/runc): CLI tool for spawning and running containers according to the OCI specification.
* [slirp4netns](https://github.com/rootless-containers/slirp4netns): slirp4netns provides user-mode networking ("slirp") for unprivileged network namespaces.
