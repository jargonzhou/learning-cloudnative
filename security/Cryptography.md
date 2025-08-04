# Cryptography


Books:
- Wong, David. **Real-World Cryptography**. 2021. Manning.
# Terminology
- symmetric cryptography/secret key cryptography - 对称密码学/密钥密码学
- symmetric encryption algorithm: cipher - 对称加密算法
- cryptographers: the people who build cryptographic primitives
- cryptanalysts: the people who break cryptographic primitives

- AES: Advanced Encryption Standard

- Kerckhoffs' principle: 算法公开, 只需密钥是保密的

- key distribution: 密钥分发
- asymmetric cryptography/publick key cryptography - 非对称密码学/公钥密码学
  - 没有解决信任问题: 公钥确实是属于某参与方的
- key exchange: 密钥交换
  - DH key exchange algorithm: Diffile-Hellman - 用于在两方之间建立共用的秘密
  - 密钥交换开始时各方需要使用相同的一组参数
- secret: 保密的, 秘密
- MIMT attacker: man-in-the-middle

- asymmetric encryption: 不对称加密
- RSA: Ron Rivest, Adi Shamir, Leonard Adleman
  - a public key encryption algorithm: public key, private key - 任何人可以使用公钥加密, 但只有私钥拥有者可以解密
  - a digital sinature scheme: 数字签名方案 - 用于建立信任. 私钥拥有者使用私钥签名, 任何人使用公钥验证签名
- unforgeable: 不可伪造的


另一种分类:
- math-base construction: 依赖于诸如因子分解的数学问题. 例: RSA算法.
- heuristic-based construction: 依赖于密码分析员的观察和统计分析. 例: AES算法.

密码学原语(primitive)和协议提供的属性:
- confidentiality: 机密性.
- authentication: 身份认证.
# Hash Functions

more: https://en.wikipedia.org/wiki/Hash_function

```shell
# 例: SHA-256 
!echo -n "hello" | openssl dgst -sha256
!echo -n "hello" | openssl dgst -sha256
!echo -n "hella" | openssl dgst -sha256
```
哈希函数的安全属性:
- pre-image resistance: 原像抗性, 不可逆, 单向 
  - 不能从输出恢复输入
- second pre-image resistance: 第二原像抗性, 完整性(integrity)
  - 给定输入和输出, 不能找到另一个产生相同输出的输入
- collision resistance: 抗碰撞性 
  - 不能找到产生相同输出的两个不同的输入
broken:
- MD5: 2004
- SHA-1: 2016

SHA-2: Merkle-Damgard
- Secure Hash Algorithm 2, 2001, NIST
- versions: SHA-224, SHA-256, SHA-384, SHA-512, SHA-512/224, SHA-512/256

SHA-3: Sponge
- 2015, NIST
- versions: SHA-3-224, SHA-3-256, SHA-3-384, SHA-3-512

SHA-3 XOF(extendable output function)
- SHAKE
- cSHAKE
  - TupleHash

```shell
# example: TupleHash
# Windows
!echo -n "Alice""Bob""100""15" | openssl dgst -sha3-256
!echo -n "Alice""Bob""1001""5" | openssl dgst -sha3-256

# WSL
# ➜  ~ echo -n "Alice""Bob""100""15" | openssl dgst -sha3-256
# SHA3-256(stdin)= 34d6b397c7f2e8a303fc8e39d283771c0397dad74cef08376e27483efc29bb02
# ➜  ~ echo -n "Alice""Bob""1001""5" | openssl dgst -sha3-256
# SHA3-256(stdin)= 34d6b397c7f2e8a303fc8e39d283771c0397dad74cef08376e27483efc29bb02

# ➜  ~ echo -n "Alice""||Bob""||100""||15" | openssl dgst -sha3-256
# SHA3-256(stdin)= 8becd187abba6816ff99f6e97e941910a1f2e8c4a0db68815a28c681575fc039
# ➜  ~ echo -n "Alice""||Bob""||1001""||5" | openssl dgst -sha3-256
# SHA3-256(stdin)= bad466c2cf21f043013c45384059a791289283dd469765d6d43cbb1d22155f4f
```
# MAC: Message Authentication Code

more: https://en.wikipedia.org/wiki/Message_authentication_code
消息验证码: 组合使用一个哈希函数和一个密钥, 用于保护数据的完整性.
- 哈希函数: 提供身份验证和数据完整性, 但基于额外的可信通道(trusted channel)
- 密钥: 提供机密性和身份认证.
- 用于创建可信通道.

MAC(Key, Message) -> Authentication tag

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/08/MAC.svg/1322px-MAC.svg.png" width="800"/>

HMAC: hash-based message authentication code


MAC的安全属性:
- MACs are resistance against forgery of authentication tags - 阻止在新的消息上伪造生成authentication tag
- an authentication tag need to be of a mimimum length to be secure - 通常128-bit
  - 碰撞: MAC(key, X) = MAC(key, Y)
- message can be replayed if authenticated naively - 消息重放
  - 解决方法: 在消息中添加序列号
- verifying an authentication is prone to bugs - 常量实现内完成验证
  - 攻击: timing attack

```python
import hashlib
import base64
import hmac

print(hmac.new(b"nonbase64key", "password".encode(), hashlib.sha256).hexdigest())
b"nonbase64key".hex()
```

```shelll
# !echo -n "password" | openssl sha256 -hex -mac HMAC -macopt hexkey:{b"nonbase64key".hex()}

# WSL
# ➜  ~ echo -n "password" | openssl sha256 -hex -mac HMAC -macopt hexkey:6e6f6e6261736536346b6579
# SHA256(stdin)= 8c84b58e026bf7169d16bd11ac047a86dd535958f7fec16560c7204efbbed2f8
```

# Authenticated Encryption
symmetric key: 对称密钥

Encrypt(key, plaintext) -> ciphertext

Decrypt(key, ciphertext) -> plaintext
AES: Advanced Encryption Standard, NIST, 1997
- versions: AES-128, AES-192, AES-256
- block ciphter: key, 128-bit plaintext, output 128-bit ciphertext

加密不是128-bit的数据时, 需要
- padding
  - PKCS#7: 补齐的字节的值是需要补齐的长度
- a mode of operation: 拆分为16-byte的块
  - ECB: electronic codebook - 加密是确定性的, 单独加密每个块, 结果密文中可能有重复的模式
  - CBC: cipher block chaining - 使用初始化向量IV(intialization vector), 以"随机化"加密
    - IV需要以明文形式与密文一起传输.

ASE-CBC-HMAC: 密文或IV被篡改对解密造成影响
- 使用HMAC对IV和密文生成authentication tag, 避免被篡改

AEAD: authenticated encryption with associated data
- AES-GCM: Galois/Counter Mode, GMAC, NIST, 2007
- ChaCha20-Poly1305

AEAD Encrypt(key, plaintext, associated-data) -> ciphertext+tag

> library: 
> 
> - JavaScript, the Web Crypto API
> - Java: Bouncy Castle [AES.GCM](https://javadoc.io/doc/org.bouncycastle/bcprov-jdk15on/latest/org/bouncycastle/jcajce/provider/symmetric/AES.GCM.html)
# Key Exchanges
密钥交换:
- 用途: 让参与方就一个共享的秘密达成一致, 然后用于使用带身份验证的加密算法的加密通信.
- 公钥, 私钥: 拿自己的私钥和他人的公钥生成密钥
- Diffie-Hellman (DH) key exchange
- Elliptic Curve Diffie-Hellman (ECDH) key exchange

theory: SKIP

action: workbench\DataEngineering\codes\data-engineering-java\infrastructure\cryptography\src\test\java\com\spike\dataengineering\cryptography\DHTest.java

# Asymmetric Encryption
hybrid encryption: 混合加密
- 混合: 非对称加密, 带身份认证的加密
- 解决问题: 非对称加密在可加密数据的大小和速度存在局限性
- 非对称密钥对生成算法: Generate asymmetric key pair(Security paramenter) -> Private key, Public Key
- 非对称加密: Asymmetric encryption(Message, Public Key) -> Ciphertext
- 非对称解密: Asymmetric descryption(Ciphertext, Private Key) -> Plaintext or error
密钥交换, 密钥封装:
- (B) Generate symmetric key(Security parameter) -> Symmetric key
- (B) Asymmetric encryption(Symmetric key, A's public key) -> Encrypted symmetric key
- (B) - Encrypted symmetric key -> (A)
- (A) Asymmetric decryption(Encrypted symmetric key, A's private key) -> Symmetric key


混合加密:
- 同上面的密钥交换, and
- (B) Authenticated encryption(Symmetric key, Message) -> Ciphertext
- (B) - Encrypted symmetric key, Ciphertext -> (A)
> library:
>
> Tink: Google - [link](https://github.com/tink-crypto/tink-java)

混合加密例: ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM
- ECIES: Elliptic Curve Integrated Encryption Scheme, 一个混合加密标准
- P256: NIST标准化的椭圆曲线(elliptic curve)
- HKDF: 密钥导出函数
- HMAC: 消息验证码
- SHA-256: 哈希函数
- AES-128-GCM: AES-GCM带身份验证的加密算法

action: workbench\DataEngineering\codes\data-engineering-java\infrastructure\cryptography\src\test\java\com\spike\dataengineering\cryptography\HybridEncryptionTest.java


ECIES:
- (B) Genrate (EC)DH key pair(Security parameter) -> Private Key, Public Key
- (B) Key exchange(Private Key, A's Public Key) -> shared secret as symmetric key
- (B) Authenticated encryption(Message, symmetric key) -> Ciphtertext
- (B) - Publikc key, Ciphtertext -> (A)
- (A) Key exchange(Private Key, B's Public Key) -> shared secret as symmetric key
- (A) Authenticated descrytion(Ciphertext, symmetric key) -> Message or error

# Signature, Zero-Knowledge Proof
digitial signatures/signature schemes:
- 密钥对生成算法
- 签名算法: 使用私钥和消息, 产生签名
- 验证算法: 使用公钥, 消息, 签名, 返回成功或失败

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Private_key_signing.svg/1920px-Private_key_signing.svg.png" width="400"/>

性质:
- 保证消息完整性: 如果修改了消息, 产生的签名无效
- 不可抵赖性

> library: 
> 
> pyca/cryptograph - [link](https://cryptography.io/en/latest/)
使用场景:
- 带身份验证的密钥交换: 无法解决主动的中间人攻击
- PKI: Public Key Infrastructure, 公钥基础设施 - authority
!pip install cryptography
from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from cryptography.exceptions import InvalidSignature

private_key = Ed25519PrivateKey.generate()
public_key = private_key.public_key()

message = b"the message"

signature = private_key.sign(message)

try:
  public_key.verify(signature, message)
  print("valid")
except InvalidSignature:
  print("invalid")
ZKPs: Zero-Knowledge Proofs, 零知识证明

example: 要证明知道给定 $Y = g^{x}$ 中的 $x$
- 只需要传递 $x$ - the witness 证据

Schnorr identification protocol: 一个交互式的零知识证明
- (A) i will prove that i know $x$ in $Y = g^{x} mod p$
- (A) commitment: a random value $R = g^{x}$
- (B) challenge: a random value $c$
- (A) a hidden witness: $s = k + c \times x$
- (B) verify: $g^{s} = Y^{c} \time R$

Schnorr signature: 非交互式        <- 文中没有讲清楚: ref [Schnorr协议：非交互零知识身份证明和数字签名](https://zhuanlan.zhihu.com/p/107752440) and [wikipedia](https://en.wikipedia.org/wiki/Schnorr_signature)
- (A) commitment: $R = g^{x}$
- (A) random challenge: $c = HASH(R, msg)$
- (A) a hidden witness: $s = k + c \times x$
签名算法:
- RSA
- DSA: Digital Signature Algorithm, NIST 1991
- ECDSA: Elliptic Curve Digital Signature Algorithm
- EdDSA: Edwards-curve Digital Signature ALgorithm, 2008
# Randomness, Secrets
随机性:
- 用例: secret keys, nonces, IVs, prime numbers, challenges, ...
- 来源: 硬件中断的时间(例如鼠标移动), 软件中断的时间, 硬盘寻道时间, ...

TRNGs: true random number generators

PRNG: pseudorandom number generator, 伪随机数生成器
- seed
- 密码学安全的PRNG属性: 
  - 确定性: 使用相同的seed产生相同的随机数序列
  - 随机不可区分的: 无法区分数是从一组可能的数中PRNG生成的还是挑选出来的. 根据生成的随机数无法恢复PRNG的内部状态.

实践中获取随机性:
- noise source
- cleaning and mixing
- PRNGs

```shell
# /dev/random
➜  ~ dd if=/dev/urandom bs=16 count=1 2> /dev/null | xxd -p
95a0816e638b83b83c9f961a2b6901b2

# system call
getrandom
```
key derivation: derive several secrets from one secret

KDF: key derivation function
- HKDF: HMAC-base key derivation function, RFC 5869
管理key和secret
- key management
  - key rotation
  - key revocation
decentralize trust with threshold cryptography - ???
- secret sharing/secret splitting
- multi-signature
- DKG: distributed key generation

# Protocols

- secure transport
- end-to-end encryption
- user authentication
- cryptocurrency
- hardware cryptography
- post-quantum cryptograph
- next-generation cryptography
- cryptography failed: when and where