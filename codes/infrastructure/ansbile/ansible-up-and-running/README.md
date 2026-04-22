# Playground of 'Ansible: Up and Running'

# playbooks
```shell
$ vagrant init ubuntu/focal64

$ vagrant up

# cleanup
# $ vagrant destroy -f
```

convention
- `config.vm.network "public_network"`: get `GUEST_HOST_IP` from `vagrant ssh`


SSH
```shell
$ vagrant ssh-config

$ vagrant ssh
Welcome to Ubuntu 20.04.6 LTS (GNU/Linux 5.4.0-216-generic x86_64)
...
vagrant@ubuntu-focal:~$

# or
$ ssh vagrant@GUEST_HOST_IP -p 22 -i .vagrant/machines/default/virtualbox/private_key
vagrant@ubuntu-focal:~$
```

`inventory/vagrant.ini`
```shell
$ mkdir inventory
$ vi inventory/vagrant.ini
```

```shell
# WSL: copy to another folder without /mnt prefix, otherwise
# Permissions 0755 for '.vagrant/machines/default/virtualbox/private_key' are too open.
# in directory: ~/ansible/ansible-up-and-running/playbooks
➜  playbooks chmod 600 .vagrant/machines/default/virtualbox/private_key
➜  playbooks ssh vagrant@GUEST_HOST_IP -p 22 -i .vagrant/machines/default/virtualbox/private_key
vagrant@ubuntu-focal:~$

# ping module
➜  playbooks ansible testserver -i inventory/vagrant.ini -m ping
testserver | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python3.9"
    },
    "changed": false,
    "ping": "pong"
}
# or with ./ansible.cfg
➜  playbooks ansible testserver -m ping
# command module
➜  playbooks ansible testserver -m command -a uptime
testserver | CHANGED | rc=0 >>
 04:46:38 up 36 min,  1 user,  load average: 0.15, 0.06, 0.05
➜  playbooks ansible testserver -a uptime
testserver | CHANGED | rc=0 >>
 04:46:47 up 36 min,  1 user,  load average: 0.13, 0.06, 0.05
➜  playbooks ansible testserver -a "tail /var/log/dmesg"
# -b: as root
➜  playbooks ansible testserver -b -a "tail /var/log/syslog"
# others
ansible testserver -b -m package -a name=nginx
ansible testserver -b -m service -a "name=nginx state=restarted"
```

# ch03

```shell
$ vagrant init ubuntu/focal64
# edit Vagrantfile
$ vagrant up

# cleanup
# $ vagrant destroy -f
```

```shell
➜  ch03 ansible-inventory --graph
@all:
  |--@ungrouped:
  |--@webservers:
  |  |--testserver
```

```shell
➜  ch03 ansible-playbook webservers.yml

PLAY [Configure webserver with nginx] ****************************************************************************************************************

TASK [Gathering Facts] *******************************************************************************************************************************
ok: [testserver]

TASK [Ensure nginx is installed] *********************************************************************************************************************
changed: [testserver]

TASK [Copy nginx config file] ************************************************************************************************************************
changed: [testserver]

TASK [Enable configuraiton] **************************************************************************************************************************
ok: [testserver]

TASK [Copy index.html] *******************************************************************************************************************************
changed: [testserver]

TASK [Restart nginx] *********************************************************************************************************************************
changed: [testserver]

PLAY RECAP *******************************************************************************************************************************************
testserver                 : ok=6    changed=4    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

access `http://GUEST_HOST_IP`
```
Nginx, configured by Ansible
If you can see this, Ansible successfully installed nginx.

Running on testserver
```

# ch04



# ch05

`os-detail.yml`
```
➜  ch05 ansible-playbook os-detail.yml

PLAY [Ansible facts] **********************************************************************************************************************************************************************

TASK [Gathering Facts] ********************************************************************************************************************************************************************
ok: [localhost]

TASK [Print out operating system details] *************************************************************************************************************************************************
ok: [localhost] => {
    "msg": "os_family: Debian, distro: Ubuntu 22.04, kernel: 5.15.153.1-microsoft-standard-WSL2"
}

# ➜ ch05 cat /etc/ansible/facts.d/example.fact
# [book]
# title=Ansible: Up and Running
# authors=Meijer, Hochstein, Moser
# publisher=O'Reilly
TASK [Print ansible_local] *******************************************************************************************************************************
ok: [localhost] => {
    "ansible_local": {
        "example": {
            "book": {
                "authors": "Meijer, Hochstein, Moser",
                "publisher": "O'Reilly",
                "title": "Ansible: Up and Running"
            }
        }
    }
}

PLAY RECAP ********************************************************************************************************************************************************************************
localhost                  : ok=2    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

# ch07

```shell
ansible-playbook --list-tasks mezzanine.yml
```

# ch10

```shell
➜  ch10 ansible-playbook loop.yml

PLAY [Tests] ***************************************************************************************************************************************************************

TASK [Interate over lines in a file] ***************************************************************************************************************************************
ok: [localhost] => (item=Ronald Linn Rivest) => {
    "msg": "Ronald Linn Rivest"
}
ok: [localhost] => (item=Adi Shamir) => {
    "msg": "Adi Shamir"
}
ok: [localhost] => (item=Leonard Max Adleman) => {
    "msg": "Leonard Max Adleman"
}
ok: [localhost] => (item=Whitfield Diffie) => {
    "msg": "Whitfield Diffie"
}
ok: [localhost] => (item=Martin Hellman) => {
    "msg": "Martin Hellman"
}

PLAY RECAP *****************************************************************************************************************************************************************
localhost                  : ok=1    changed=0    unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```

# ch18

```shell
➜  ch18 ansible-doc -t callback slack
> CALLBACK community.general.slack (/home/zhoujiagen/.local/lib/python3.12/site-packages/ansible_collections/community/general/plugins/callback/slack.py)

  This is an ansible callback plugin that sends status updates to a Slack channel during playbook execution.

OPTIONS (red indicates it is required):

   channel  Slack room to post in.
        set_via:
          env:
          - name: SLACK_CHANNEL
          ini:
          - key: channel
            section: callback_slack
        default: '#ansible'
        type: str

   http_agent  HTTP user agent to use for requests to Slack.
        default: null
        type: string

   username  Username to post as.
        set_via:
          env:
          - name: SLACK_USERNAME
          ini:
          - key: username
            section: callback_slack
        default: ansible
        type: str

   validate_certs  Validate the SSL certificate of the Slack server for HTTPS URLs.
        set_via:
          env:
          - name: SLACK_VALIDATE_CERTS
          ini:
          - key: validate_certs
            section: callback_slack
        default: true
        type: bool

   webhook_url  Slack Webhook URL.
        set_via:
          env:
          - name: SLACK_WEBHOOK_URL
          ini:
          - key: webhook_url
            section: callback_slack
        type: str

REQUIREMENTS:  whitelist in configuration, prettytable (python library)


AUTHOR: Unknown (!UNKNOWN)

NAME: slack

TYPE: notification
```


# ch19

```shell
➜  ch19 ansible-playbook can_reach.yml
localhost | SUCCESS => {
    "ansible_facts": {
        "discovered_interpreter_python": "/usr/bin/python3.12"
    },
    "changed": false,
    "msg": "Could reach www.baidu.com:80"
}
```