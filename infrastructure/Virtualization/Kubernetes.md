# Kubernetes

# CNI
Kubernetes utilizes Container Network Interface (CNI) plugins to manage networking for pods within a cluster. These plugins are responsible for assigning IP addresses, routing traffic between pods, and enforcing network policies.

Here is a list of prominent Kubernetes network plugins:
- **Calico**: A popular solution known for its flexibility, network performance, and advanced network administration features. It supports BGP routing and can operate as an underlay or overlay network (IP-in-IP, VXLAN).
  - https://github.com/projectcalico/calico
- **Flannel**: A simple and lightweight overlay network solution based on VXLAN, suitable for many common Kubernetes deployments. It assigns subnets to nodes and manages IP addresses.
Weave Net: An overlay network from Weaveworks that creates a mesh network connecting all cluster nodes, offering built-in encryption and network policies.
- **Cilium**: Leverages eBPF for deep network and security visibility, offering high scalability and security features, including inter-cluster service mesh capabilities. It uses VXLAN for its overlay network.
- **Multus CNI:** A meta-plugin that allows for attaching multiple network interfaces to a single pod, enabling the use of various CNI plugins simultaneously (e.g., Calico, Cilium, SR-IOV).
- **Amazon VPC CNI**: Optimized for AWS environments, it uses Amazon EC2 elastic network interfaces (ENIs) to assign IP addresses to pods within the VPC.
- **Azure Network Fabric CNI**: The CNI implementation optimized for Azure Kubernetes Service (AKS).
- **Google Kubernetes Engine CNI**: The CNI implementation optimized for Google Kubernetes Engine (GKE).
- **OVN-Kubernetes**: Built on Open vSwitch (OVS) and Open Virtual Networking (OVN), it provides an overlay-based networking implementation with support for both Linux and Windows. 
- **Romana**: A Layer 3 CNI plugin that supports network policy enforcement for Kubernetes.
- **Juniper Contrail / TungstenFabric**: Provides an overlay SDN solution for multi-cloud and hybrid cloud environments, offering features like network policy enforcement and service chaining.
- **Canal**: A combination of Calico and Flannel, providing both network policy enforcement and a simple overlay network.