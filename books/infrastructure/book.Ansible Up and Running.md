# Ansible: Up and Running

# 1. Introduction
- A Note About Versions
- Ansible: What Is It Good For?
- How Ansible Works
- What’s So Great About Ansible?
  - Simple
  - Powerful
  - Secure
- Is Ansible Too Simple?
- What Do I Need to Know?
- What Isn’t Covered
- Moving Forward

# 2. Installation and Setup
- Installing Ansible
  - Loose Dependencies
  - Running Ansible in Containers
  - Ansible Development
- Setting Up a Server for Testing/设置测试服务器
  - Using Vagrant to Set Up a Test Server
  - Telling Ansible About Your Servers
  - Simplifying with the `ansible.cfg` File
  - Kill Your Darlings
- Convenient Vagrant Configuration Options
  - Port Forwarding and Private IP Addresses
  - Enabling Agent Forwarding
- The Docker Provisioner
- The Ansible Local Provisioner
- When the Provisioner Runs
- Vagrant Plug-ins
  - Hostmanager: `vagrant-hostmanager`
  - VBGuest: `vagrant-vbguest`
- VirtualBox Customization
- Vagrantfile Is Ruby
- Production Setup

# 3. Playbooks/剧本: A Beginning
- Preliminaries
- A Very Simple Playbook
  - Specifying an NGINX Config File
  - Creating a Web Page
  - Creating a Group
- Running the Playbook
- Playbooks Are YAML
  - Start of Document
  - End of File
  - Comments
  - Indentation and Whitespace
  - Strings
  - Booleans
  - Lists
  - Dictionaries
  - Multiline Strings
  - Pure YAML Instead of String Arguments
- Anatomy of a Playbook/剧本
- Plays/表演
  - Tasks/任务
  - Modules/模块: `package`, `copy`, `file`, `service`, `tempalte`
  - Viewing Ansible Module Documentation: `ansible-doc`
  - Putting It All Together
- Did Anything Change? Tracking Host State
- Getting Fancier: TLS Support
  - Generating a TLS Certificate
  - Variables
  - Quoting in Ansible Strings
  - Generating the NGINX Configuration Template
  - Loop
  - Handlers
  - A Few Things to Keep in Mind About Handlers
  - Testing
  - Validation
  - The Playbook
  - Running the Playbook

# 4. Inventory/库存清单: Describing Your Servers
- Inventory/Hosts Files
  - Preliminaries: Multiple Vagrant Machines
- Behavioral Inventory Parameters
  - Changing Behavioral Parameter Defaults
- Groups and Groups and Groups
  - Example: Deploying a Django App
  - Aliases and Ports
  - Groups of Groups
  - Numbered Hosts (Pets Versus Cattle)
- Hosts and Group Variables: Inside the Inventory
- Host and Group Variables: In Their Own Files
- Dynamic Inventory
  - Inventory Plug-ins
  - Amazon EC2
  - Azure Resource Manager
  - The Interface for a Dynamic Inventory Script
  - Writing a Dynamic Inventory Script
- Breaking the Inventory into Multiple Files
- Adding Entries at Runtime with `add_host` and `group_by`
  - `add_host`
  - `group_by`

# 5. Variables and Facts/变量和事实
- Defining Variables in Playbooks
  - Defining Variables in Separate Files
  - Directory Layout
- Viewing the Values of Variables
  - Variable Interpolation
- Registering Variables
- Facts
  - Viewing All Facts Associated with a Server
  - Viewing a Subset of Facts
  - Any Module Can Return Facts or Info
  - Local Facts
  - Using `set_fact` to Define a New Variable
- Built-In Variables
  - `hostvars`
  - `inventory_hostname`
  - `groups`
- Extra Variables on the Command Line
- Precedence

# 6. Introducing Mezzanine: Our Test Application
- Why Is Deploying to Production Complicated?
- Postgres: The Database
- Gunicorn: The Application Server
- NGINX: The Web Server
- Supervisor: The Process Manager

# 7. Deploying Mezzanine with Ansible
- Listing Tasks in a Playbook
- Organization of Deployed Files
- Variables and Secret Variables
- Installing Multiple Packages
- Adding the Become Clause to a Task
- Updating the `apt` Cache
- Checking Out the Project Using Git
- Installing Mezzanine and Other Packages into a Virtual Environment
- Complex Arguments in Tasks: A Brief Digression
- Configuring the Database
- Generating the `local_settings.py` File from a Template
- Running `django-manage` Commands
- Running Custom Python Scripts in the Context of the Application
  - Setting Service Configuration Files
- Enabling the NGINX Configuration
- Installing TLS Certificates
- Installing Twitter Cron Job
- The Full Playbook
- Running the Playbook Against a Vagrant Machine
- Troubleshooting
  - Cannot Check Out Git Repository
  - Cannot Reach 192.168.33.10.nip.io
  - Bad Request (400)

# 8. Debugging Ansible Playbooks/调试
- Humane Error Messages
- Debugging SSH Issues
- Common SSH Challenges
  - `PasswordAuthentication no`
  - SSH as a Different User
  - Host Key Verification Failed
  - Private Networks
- The `debug` Module
- Playbook Debugger
- The `assert` Module
- Checking Your Playbook Before Execution
  - Syntax Check: `--syntax-check`
  - List Hosts: `--list-hosts`
  - List Tasks: `--list-tasks`
  - Check Mode: `--check`/`-C`
  - Diff (Show File Changes): `--diff`/`-D`
  - Tags: `--tags`/`-t`, `--skip-tags`
  - Limits: `--limit`

# 9. Roles/角色: Scaling Up Your Playbooks
- Basic Structure of a Role
- Example: Deploying Mezzanine with Roles
  - Using Roles in Your Playbooks
  - Pre-Tasks and Post-Tasks
  - A `database` Role for Deploying the Database
  - A `mezzanine` Role for Deploying Mezzanine
- Creating Role Files and Directories with `ansible-galaxy`
- Dependent Roles
- Ansible Galaxy
  - Web Interface
  - Command-Line Interface
  - Role Requirements in Practice
  - Contributing Your Own Role

`roles/database`
```shell
defaults/                可以被覆盖的变量
  main.yml
files/                   上传到主机的文件和脚本
  pg_hba.conf
handlers/                变更通知对应的动作
  main.yml
meta/                    角色信息
  main.yml
tasks/                   角色的动作
  main.yml
templates/               上传到主机的Jinja2模板
  postgres.conf.j2
vars/                    通常不应该被覆盖的变量
  main.yml
```

# 10. Complex Playbooks/复杂剧本
- Dealing with Badly Behaved Commands
- Filters
  - The default Filter
  - Filters for Registered Variables
  - Filters That Apply to Filepaths
  - Writing Your Own Filter
- Lookups
  - file
  - pipe
  - env
  - password
  - template
  - csvfile
  - dig
  - redis
  - Writing Your Own Lookup Plug-in
- More Complicated Loops
  - With Lookup Plug-in
  - with_lines
  - with_fileglob
  - with_dict
  - Looping Constructs as Lookup Plug-ins
- Loop Controls
  - Setting the Variable Name
  - Labeling the Output
- Imports and Includes
  - Dynamic Includes
  - Role Includes
  - Role Flow Control
- Blocks
- Error Handling with Blocks
- Encrypting Sensitive Data with ansible-vault
  - Multiple Vaults with Different Passwords

# 11. Customizing Hosts, Runs, and Handlers/设置主机, 任务运行和处理器
- Patterns for Specifying Hosts
- Limiting Which Hosts Run
- Running a Task on the Control Machine
- Manually Gathering Facts
- Retrieving an IP Address from the Host
- Running on One Host at a Time
- Running on a Batch of Hosts at a Time
- Running Only Once
- Limiting Which Tasks Run
  - step
  - start-at-task
  - Running Tags
  - Skipping Tags
- Running Strategies
  - Linear
  - Free
- Advanced Handlers
  - Handlers in Pre- and Post-Tasks
  - Flush Handlers
  - Meta Commands
  - Handlers Notifying Handlers
  - Handlers Listen
  - The SSL Case for the listen Feature

# 12. Managing Windows Hosts/Windows主机
- Connection to Windows
- PowerShell
- Windows Modules
- Our Java Development Machine
- Adding a Local User
- Windows Features
- Installing Software with Chocolatey
- Configuration of Java
- Updating Windows

# 13. Ansible and Containers/容器
- Kubernetes
- Docker Application Life Cycle
- Registries
- Ansible and Docker
- Connecting to the Docker Daemon
- Example Application: Ghost
- Running a Docker Container on Our Local Machine
- Building an Image from a Dockerfile
- Pushing Our Image to the Docker Registry
- Orchestrating Multiple Containers on Our Local Machine
- Querying Local Images
- Deploying the Dockerized Application
  - Provisioning MySQL
  - Deploying the Ghost Database
  - Frontend
  - Frontend: Ghost
  - Frontend: NGINX
  - Cleaning Out Containers

# 14. Quality Assurance with Molecule/质量保证
- Installation and Setup
- Configuring Molecule Drivers
- Creating an Ansible Role
- Scenarios
  - Desired State
  - Configuring Scenarios in Molecule
  - Managing Virtual Machines
  - Managing Containers
- Molecule Commands
- Linting
  - YAMLlint
  - ansible-lint
  - ansible-later
- Verifiers
  - Ansible
  - Goss
  - TestInfra

# 15. Collections/集合
- Installing Collections
- Listing Collections
- Using Collections in a Playbook
- Developing a Collection

# 16. Creating Images/创建镜像
- Creating Images with Packer
  - Vagrant VirtualBox VM
  - Combining Packer and Vagrant
  - Cloud Images
  - Google Cloud Platform
  - Azure
  - Amazon EC2
  - The Playbook
- Docker Image: GCC 11

# 17. Cloud Infrastructure/云基础设施
- Terminology
  - Instance
  - Amazon Machine Image
  - Tags
- Specifying Credentials
  - Environment Variables
  - Configuration Files
- Prerequisite: Boto3 Python Library
- Dynamic Inventory
  - Inventory Caching
  - Other Configuration Options
- Defining Dynamic Groups with Tags
  - Applying Tags to Existing Resources
  - Nicer Group Names
- Virtual Private Clouds
- Configuring ansible.cfg for Use with ec2
- Launching New Instances
- EC2 Key Pairs
  - Creating a New Key
  - Uploading Your Public Key
- Security Groups
  - Permitted IP Addresses
  - Security Group Ports
- Getting the Latest AMI
- Create a New Instance and Add It to a Group
- Waiting for the Server to Come Up
- Putting It All Together
- Specifying a Virtual Private Cloud
  - Dynamic Inventory and VPC

# 18. Callback Plug-ins/回调插件
- Stdout Plug-ins
  - ARA
  - debug
  - default
  - dense
  - json
  - minimal
  - null
  - oneline
- Notification and Aggregate Plug-ins
  - Python Requirements
  - foreman
  - jabber
  - junit
  - log_plays
  - logentries
  - logstash
  - mail
  - profile_roles
  - profile_tasks
  - say
  - slack
  - splunk
  - timer

# 19. Custom Modules/自定义模块
- Example: Checking That You Can Reach a Remote Server
  - Using the Script Module Instead of Writing Your Own
  - can_reach as a Module
- Should You Develop a Module?
- Where to Put Your Custom Modules
- How Ansible Invokes Modules
  - Generate a Standalone Python Script with the Arguments (Python Only)
  - Copy the Module to the Host
  - Create an Arguments File on the Host (Non-Python Only)
  - Invoke the Module
- Expected Outputs
  - Output Variables That Ansible Expects
- Implementing Modules in Python
  - Parsing Arguments
  - Accessing Parameters
  - Importing the AnsibleModule Helper Class
  - Argument Options
  - AnsibleModule Initializer Parameters
  - Returning Success or Failure
  - Invoking External Commands
  - Check Mode (Dry Run)
- Documenting Your Module
- Debugging Your Module
- Implementing the Module in Bash
- Specifying an Alternative Location for Bash

# 20. Making Ansible Go Even Faster
- SSH Multiplexing and ControlPersist/SSH多路复用
  - Manually Enabling SSH Multiplexing
  - SSH Multiplexing Options in Ansible
- More SSH Tuning
  - Algorithm Recommendations
- Pipelining/流水线
  - Enabling Pipelining
  - Configuring Hosts for Pipelining
- Mitogen for Ansible
- Fact Caching
  - JSON File Fact-Caching Backend
  - Redis Fact-Caching Backend
  - Memcached Fact-Caching Backend
- Parallelism
- Concurrent Tasks with Async

# 21. Networking and Security/网络和安全性
- Network Management
  - Supported Vendors
  - Ansible Connection for Network Automation
  - Privileged Mode
  - Network Inventory
  - Network Automation Use Cases
- Security
  - Comply with Compliance?
  - Secured, but Not Secure
  - Shadow IT
  - Sunshine IT
  - Zero Trust

# 22. CI/CD and Ansible/持续集成和持续部署
- Continuous Integration
  - Elements in a CI System
  - Jenkins and Ansible
  - Running CI for Ansible Roles
- Staging
- Ansible Plug-in
- Ansible Tower Plug-in

# 23. Ansible Automation Platform/Ansible自动化平台
- Subscription Models
  - Ansible Automation Platform Trial
- What Ansible Automation Platform Solves
  - Access Control
  - Projects
  - Inventory Management
  - Run Jobs by Job Templates
- RESTful API
- AWX.AWX
  - Installation
  - Create an Organization
  - Create an Inventory
  - Running a Playbook with a Job Template
- Using Containers to Run Ansible
  - Creating Execution Environments

# 24. Best Practices/最佳实践
- Simplicity, Modularity, and Composability
- Organize Content
- Decouple Inventories from Projects
- Decouple Roles and Collections
- Playbooks
- Code Style
- Tag and Test All the Things
- Desired State
- Deliver Continuously
- Security
- Deployment
- Performance Indicators
- Benchmark Evidence
- Final Words

# See Also
* [Vagrant: Up and Running](./book.Vagrant%20Up%20and%20Running.md)

modues used in book
```
file
user
ping
command
package
service
template
lineinfile
debug
uri
add_host
group_by
shell
stat
setup
service_facts
set_fact
apt
git
pip

locale_gen
postgresql_user
postgresql_db
django_manage
script
cron

authorized_key
assert

docker_container
docker_image
docker_login
docker_image_info
mysql_db
mysql_user
```

plugins used in book
- debug callback


nip.io: https://nip.io/
```shell
➜  ch05 nslookup 127.0.0.1.nip.io
Server:         8.8.8.8
Address:        8.8.8.8#53

Non-authoritative answer:
Name:   127.0.0.1.nip.io
Address: 127.0.0.1
```