# Vagrant: Up and Running

# 1. An Introduction to Vagrant
- Why Vagrant?
- The Tao of Vagrant
- Alternatives to Vagrant
  - Plain Desktop Virtualization
  - Containers
  - Cloud
- Vagrant Versions
- Setting Up Vagrant
  - Installing VirtualBox
  - Installing Vagrant
    - Mac OS X
    - Windows
    - Linux
  - Common Mistakes
- Improper PATH Configuration
- Conflicting RubyGems Installation
- Using Vagrant Without VirtualBox
- Help!

# 2. Your First Vagrant Machine
- Up and Running
- The `Vagrantfile`
  - V1 versus V2 Configuration
- Boxes
- Up
- Working with the Vagrant Machine/使用Vagrant机器
  - State of the Vagrant Machine
  - SSH
  - Shared Filesystem/共享文件系统
  - Basic Networking/基本网络
  - Teardown
    - Suspend/暂停
    - Halt/停机
    - Destroy/销毁


# 3. Provisioning Your Vagrant VM
- Why Automated Provisioning?/自动配置
- Supported Provisioners
- Manually Setting Up Apache
- Automated Provisioner Basics
  - Shell Scripts
  - Chef
  - Puppet
- Multiple Provisioners
- “No Provision” Mode
- In-Depth Provisioner Usage
  - Shell Scripts
    - Inline scripts
    - Run-once scripts
  - Chef Server
  - Puppet

# 4. Networking in Vagrant
- Forwarded Ports/转发端口
  - Pros and Cons
  - Basic Usage
  - Collision Detection and Correction
  - TCP versus UDP
- Host-Only Networking/主机网络
  - Pros and Cons
  - Basic Usage
  - Guest Operating System Dependency
- Bridged Networking/桥接网络
  - Pros and Cons
  - Basic Usage
- Composing Networking Options
- NAT Requirement As the First Network Interface

# 5. Modeling Multimachine Clusters/多机器集群
- Running Multiple Virtual Machines
- Controlling Multiple Machines
- Communication Between Machines
  - Host-Only Networks
  - Bridged Networks
- Real Example: MySQL

# 6. Boxes
- Why Boxes?
- Box Format
- Basic Box Management with Vagrant
- Creating New Boxes from an Existing Environment/从已有环境创建Box
- Creating New Boxes from Scratch/从零开始创建Box
  - Creating the VirtualBox Machine
  - Configuring the Operating System
  - Installing VirtualBox Guest Additions
  - Additional Software
  - Minimizing the Final Box Size
  - Packaging It Up
  - Setting Vagrantfile Defaults

# 7. Extending Vagrant with Plug-Ins/插件
- Extensible Features
- Managing Vagrant Plug-Ins
- Plug-In Development Basics
  - Plug-In Definition
  - Plug-In Components
  - Error Handling
  - Vagrant Internals
- A Basic Plug-In Development Environment
- Developing a Custom Command/自定义命令
  - Component
  - Implementation
  - Working with the Virtual Machine
  - Working with Multimachine Environments
  - Parsing Command-Line Options
- Adding New Configuration Options/添加新配置选项
  - Component
  - Implementation
  - Accessing the Configuration
  - Configuration Merging
  - Validation
- Adding a Custom Provisioner/自定义供应者
  - Component
  - Implementation
  - Provisioner Configuration
  - Configuring the Machine
- Modifying Existing Vagrant Behavior/许改已有Vagrant行为
  - Component
  - Implementation
  - Useful Keys in the Action Environment
  - Learning More
- Other Plug-In Components/其他插件组件
- Packaging the Plug-In/打包组件

# A. Vagrant Environmental Variables/环境变量
- VAGRANT_CWD
- VAGRANT_HOME
- VAGRANT_LOG
- VAGRANT_NO_PLUGINS
- VAGRANT_VAGRANTFILE

# B. Vagrant Configuration Reference/配置参考

# C. Troubleshooting and Debugging
- IRC
- Mailing List/Google Group
- Professional Support