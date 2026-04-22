# Vagrant
* https://github.com/hashicorp/vagrant

Vagrant is a tool for building and distributing development environments.

```shell
# Windows
$ which vagrant
/c/Program Files/Vagrant/bin/vagrant
$ vagrant --version
Vagrant 2.4.9
```

# Concepts
- Box
- Provisioning
- Provider
- Plugin

# CLI
* https://developer.hashicorp.com/vagrant/docs/cli

# Box
* [Discover Vagrant Boxes](https://portal.cloud.hashicorp.com/vagrant/discover)
  * cloud-image

# Vagrantfile
* https://developer.hashicorp.com/vagrant/docs/vagrantfile

The primary function of the Vagrantfile is to describe the type of machine required for a project, and how to configure and provision these machines. Vagrantfiles are called Vagrantfiles because the actual literal filename for the file is `Vagrantfile`.

The syntax of Vagrantfiles is Ruby.

```shell
config.vm
config.ssh
config.winrm
config.winssh
config.vagrant
```

# Environmental Variables
* https://developer.hashicorp.com/vagrant/docs/other/environmental-variables

```shell
VAGRANT_CWD
VAGRANT_HOME # default: ~/.vagrant.d
VAGRANT_LOG
VAGRANT_NO_PLUGINS
VAGRANT_VAGRANTFILE # default: Vagrantfile
# ...
```
