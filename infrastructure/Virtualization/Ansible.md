# Ansible
* https://docs.ansible.com/
* https://github.com/ansible/ansible
* [Ansible Galaxy](https://galaxy.ansible.com/): Galaxy provides pre-packaged units of work known to Ansible as roles and collections.

Ansible is a radically simple IT automation system. It handles configuration management, application deployment, cloud provisioning, ad-hoc task execution, network automation, and multi-node orchestration. Ansible makes complex changes like zero-downtime rolling updates with load balancers easy.

Design Principles
- Have an extremely simple setup process with a minimal learning curve.
- Manage machines quickly and in parallel.
- Avoid custom-agents and additional open ports, be agentless by leveraging the existing SSH daemon/使用已有SSH守护进程而无需agent.
- Describe infrastructure in a language that is both machine and human friendly/使用语言描述基础设施.
- Focus on security and easy auditability/review/rewriting of content/关注安全.
- Manage new remote machines instantly, without bootstrapping any software.
- Allow module development in any dynamic language, not just Python/允许用动态语言开发模块开发.
- Be usable as non-root.
- Be the easiest IT automation system to use, ever.

```shell
# Windows WSL2
$ ➜  ~ pip install ansible
$ ➜  ~ ansible --version
ansible [core 2.20.4]
  config file = None
  configured module search path = ['~/.ansible/plugins/modules', '/usr/share/ansible/plugins/modules']
  ansible python module location = ~/.local/lib/python3.12/site-packages/ansible
  ansible collection location = ~/.ansible/collections:/usr/share/ansible/collections
  executable location = ~/.local/bin/ansible
  python version = 3.12.9 (main, Feb  5 2025, 08:49:01) [GCC 9.4.0] (/usr/bin/python3.12)
  jinja version = 3.1.4
  pyyaml version = 6.0.2 (with libyaml v0.2.5)
```

```shell
ansible            # Define and run a single task 'playbook' against a set of hosts
ansible-community  # the Ansible community package
ansible-config     # View ansible configuration.
ansible-console    # REPL console for executing Ansible tasks.
ansible-doc        # plugin documentation tool
ansible-galaxy     # Perform various Role and Collection related operations.
ansible-inventory  # Show Ansible inventory information, by default it uses the inventory script JSON format
ansible-playbook   # Runs Ansible playbooks, executing the defined tasks on the targeted hosts.
ansible-pull       # pulls playbooks from a VCS repo and executes them on target host
ansible-test       # test runner for testing Ansible collections and roles
ansible-vault      # encryption/decryption utility for Ansible data files
```


# Concepts
* https://docs.ansible.com/projects/ansible/latest/getting_started/basic_concepts.html
* [Glossary](https://docs.ansible.com/projects/ansible/latest/reference_appendices/glossary.html)

<img src="https://docs.ansible.com/projects/ansible/latest/_images/ansible_inv_start.svg" width="400px"/>

most Ansible environments have three main components:
* **Control node/控制节点**: A system on which Ansible is installed. You run Ansible commands such as ansible or ansible-inventory on a control node.
* **Inventory/库存清单**: A list of managed nodes that are logically organized. You create an inventory on the control node to describe host deployments to Ansible.
* **Managed node/被管理节点**: A remote system, or host, that Ansible controls.


**Playbooks/剧本**: They contain Plays (which are the basic unit of Ansible execution). This is both an ‘execution concept’ and how we describe the files on which ansible-playbook operates.
* **Plays/表演**: The main context for Ansible execution, this playbook object maps managed nodes (hosts) to tasks. The Play contains variables, roles and an ordered lists of tasks and can be run repeatedly. It basically consists of an implicit loop over the mapped hosts and tasks and defines how to iterate over them.
  * **Roles/角色**: A limited distribution of reusable Ansible **content (tasks, handlers, variables, plugins, templates and files)** for use inside of a Play. To use any Role resource, the Role itself must be imported into the Play.
  * **Tasks/任务**: The definition of an ‘action’/动作 to be applied to the managed host. You can execute a single task once with an ad hoc command using `ansible` or `ansible-console` (both create a virtual Play).
  * **Handlers/处理器**: A special form of a Task, that only executes when notified by a previous task which resulted in a ‘changed’ status.

**Modules/模块**: The code or binaries that Ansible copies to and executes on each managed node (when needed) to accomplish the action defined in each Task.
- You can invoke a single module with a task, or invoke several different modules in a playbook. 
- Ansible modules are grouped in **collections**.

**Plugins/插件**: Pieces of code that expand Ansible’s core capabilities. Plugins can control how you connect to a managed node (connection plugins/连接插件), manipulate data (filter plugins/过滤插件) and even control what is displayed in the console (callback plugins/回调插件).

**Collections/集合**: A format in which Ansible content is distributed that can contain playbooks, roles, modules, and plugins. You can install and use collections through Ansible Galaxy.

# Configuration
* https://docs.ansible.com/projects/ansible/latest/reference_appendices/config.html
* [Special Variables](https://docs.ansible.com/projects/ansible/latest/reference_appendices/special_variables.html)

`ansible.cfg`
- `ANSIBLE_CONFIG`
- `./ansible.cfg`: in the current directory
- `~/.ansible.cfg`: in home directory
- `/etc/ansible/ansible.cfg` (Linux) or `/usr/local/etc/ansible/ansible.cfg` (*BSD)


# Modules and Plugins
* https://docs.ansible.com/projects/ansible/latest/collections/all_plugins.html

# Ecosystem
* https://docs.ansible.com/ecosystem.html

* **Ansible Core**: The Ansible programming language, automation tooling, and architectural framework.
* **Antsibull Nox**: A nox helper library that simplifies the process of testing Ansible collections through a common interface for various tools.
* **Ansible Builder**: Ansible Builder lets you create Execution Environments, which are container images that act as Ansible control nodes.
* **Ansible developer tools**: An integrated tool kit and framework for creating Ansible automation content, from bootstrapping new projects to setting up ci/cd pipelines.

## Ansible Development Tools (ADT)
* https://github.com/ansible/ansible-dev-tools

tools
- `ansible-core`: Ansible is a radically simple IT automation platform that makes your applications and systems easier to deploy and maintain. Automate everything from code deployment to network configuration to cloud management, in a language that approaches plain English, using SSH, with no agents to install on remote systems.
- `ansible-builder`: a utility for building Ansible execution environments.
- `ansible-creator`: a utility for scaffolding Ansible projects and content with leading practices.
- `ansible-lint`: a utility to identify and correct stylistic errors and anti-patterns in Ansible playbooks and roles.
ansible-navigator a text-based user interface (TUI) for developing and troubleshooting Ansible content with execution environments.
- `ansible-sign`: a utility for signing and verifying Ansible content.
- `molecule`: Molecule aids in the development and testing of Ansible content: collections, playbooks and roles
- `pytest-ansible`: a pytest testing framework extension that provides additional functionality for testing Ansible module and plugin Python code.
- `tox-ansible`: an extension to the tox testing utility that provides additional functionality to check Ansible module and plugin Python code under different Python interpreters and Ansible core versions.
- `ansible-dev-environment`: a utility for building and managing a virtual environment for Ansible content development.

## Ansible Molecule
* https://github.com/ansible/molecule

License: MIT, Language: Python.

Molecule is an Ansible testing framework designed for developing and testing Ansible collections, playbooks, and roles.

See Also
* [Molecule Goss Plugin](https://github.com/ansible-community/molecule-goss): Molecule goss is designed to allow use of goss Cloud for provisioning test resources. - This repository was archived by the owner on Jan 6, 2023. It is now read-only.
* [Testinfra](https://github.com/pytest-dev/pytest-testinfra):Testinfra test your infrastructures. - This project is currently not actively maintained, and responses to issues or pull requests may be delayed for several months.

# See Also
* [Red Hat Ansible Automation Platform](https://docs.redhat.com/en/documentation/red_hat_ansible_automation_platform)
* [Awesome Ansible](https://github.com/ansible-community/awesome-ansible): A collaborative curated list of awesome Ansible resources, tools, roles, tutorials and other related content.
* [Ansible VS Code Extension](https://github.com/ansible/vscode-ansible): Ansible IDE extension: auto-completion and integrating quality assurance tools like ansible-lint, ansible syntax check, yamllint, molecule and ansible-test. - NOT WORK in WSL2.
* [Packer](https://developer.hashicorp.com/packer): Packer is a tool that lets you create identical machine images for multiple platforms from a single source template. Packer can create golden images to use in image pipelines.
* [Mitogen for Ansible](https://mitogen.networkgenomics.com/ansible_detailed.html): Mitogen for Ansible is a completely redesigned UNIX connection layer and module runtime for Ansible. Requiring minimal configuration changes, it updates Ansible’s slow and wasteful shell-centric implementation with pure-Python equivalents, invoked via highly efficient remote procedure calls to persistent interpreters tunnelled over SSH. No changes are required to target hosts.

TODO: Syntax and Semantic of YAML.