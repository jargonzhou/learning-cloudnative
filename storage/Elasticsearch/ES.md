# Elasticsearch

## 拼写纠错

BK树详解
https://codeantenna.com/a/ZvloF7MDWc

## 查询DSL

*查询重写*: 从Lucene的角度来看, 所谓的查询重写就是把费时的原始查询类型实例改写成一组性能更高的查询类型实例, 从而加快执行速度.

*过滤器*
查询与过滤的差异:
  过滤的唯一目的是用特定筛选条件来缩小结果范围, 不影响文档得分.
  查询不仅缩小结果范围, 还会影响文档的得分.

过滤器持有的关于文档的唯一重要信息是该文档是否匹配这个过滤器, 仅仅一个标记.
过滤器通过返回一个DocIdSet的数据结构来提供这类匹配信息.
在使用过滤器时, 过滤结果不依赖于查询, 因此过滤结果可以被缓存起来供后续查询使用.
每个Lucene索引段都有一个过滤结果缓存.

某些时候使用后置过滤post_filter时, 查询的执行速度没有预期的快: search -> term query -> term filter -> result.
使用过滤查询(filtered query) `query: { filtered: {query: {}, filter: {}}}` 让Lucene库能够只收集被过滤通过的结果: search -> term filter -> term query -> result.

*查询方式分类*
1. 基本查询
  允许针对索引的一部分进行检索, 输入数据可以分析也可以不做分析. 不支持在内部再嵌套其他查询.
  match: 需要分析输入内容确不需要完整Lucene查询语法支持, 是否接受用户输入文本的场景.
  match_all: 常用语对所有索引内容进行归类处理的场景.
  term: 无需对输入进行分析的查询方式.
2. 组合查询
  查询中可以包含其他查询和过滤器.
  bool: 将多个查询用布尔逻辑组织在一起; 结果文档的最终得分为所有子查询得分的和.
  dis_max: 结果文档的得分与最高权重的子查询得分高度相关.
3. 无分析查询
  不分析输入, 直接原样传递给Lucene.
  term: 词项查询.
  prefix: 前缀, 无需分析.
4. 全文检索查询
  根据索引映射配置对输入进行分析,, 支持可被Lucene识别的查询语法.
  match
  query_string
  simple_query_string
5. 模式匹配查询
  支持各种通配符.
  prefix
  regexp
  wildcard
6. 支持相似度操作的查询
  支持近似词语的匹配.
  fuzzy_like_this, more_like_this
7. 支持打分操作的查询
  允许在查询时修改打分计算过程.
  function_score
8. 位置敏感的查询
  允许使用索引中存储的词项位置信息.
  span_term
9. 结构敏感查询
  基于结构数据, 例如父子分档结构.
  nested, has_child, has_parent, top_children

优化: *使用doc values来优化查询*
doc values是Lucene中基于列的数据结构, 它们不将数据保存在倒排索引中, 而是保存在一个基于文档的数据结构中并存储在磁盘上, 在索引文档时就计算好.

## 缓存

*过滤器缓存*
索引级别, 节点级别(默认)

*字段数据缓存*
查询涉及非倒排uninverted数据操作时, 加载相关字段的全部数据到内存.
这种缓存可用于切面计算/聚合/脚本计算/基于字段值的排序等.
Lucene的docvalues几乎与字段数据缓存一样快, 而且在索引期计算并于索引文件一起存储, 并不耗费很多内存.

*查询分片缓存*
为每个分片缓存本地查询结果.

## 分页查询

elasticsearch 分页的三种方式
https://zhuanlan.zhihu.com/p/344769338

from + size 的优点是简单，缺点是在深度分页的场景下系统开销比较大。
  from定义了目标数据的偏移值，size定义当前返回的数目。
  因为es是基于分片的，假设有5个分片，from=100，size=10.则会根据排序规则从5个分片中各取回100条数据，然后汇总成500条数据然后再选择最后的10条数据。
scroll api 方案也很高效，但是它基于快照，不能用在实时性高的业务场景，且官方已不建议使用。
  scroll 类似于sql中的cursor，使用scroll，每次只能获取一页的内容，然后会返回一个scroll_id。
  根据返回的这个scroll_id可以不断地获取下一页的内容，所以scroll并不适用于有跳页的情景
search after 可以实时高效的进行分页查询，但是它只能做下一页这样的查询场景，不能随机的指定页数查询。
  search_after 分页的方式是根据上一页的最后一条数据来确定下一页的位置，同时在分页请求的过程中，如果有索引数据的增删改查，这些变更也会实时的反映到游标上。
  但是需要注意，因为每一页的数据依赖于上一页最后一条数据，所以无法跳页请求。
  为了找到每一页最后一条数据，每个文档必须有一个全局唯一值，官方推荐使用 _uid 作为全局唯一值，其实使用业务层的 id 也可以。
  使用search_after必须要设置from=0。

## ElasticSearch集群检查,master选举,扩容缩容

https://blog.51cto.com/u_14861909/5442039

Elasticsearch 是如何选举出 master 的
https://www.nosuchfield.com/2019/03/18/How-Elasticsearch-elected-the-master/

选举的基本原则
ES 针对当前集群中所有的 Master Eligible Node 进行选举得到 master 节点，为了避免出现 Split-brain 现象，ES 选择了分布式系统常见的 quorum（多数派）思想，也就是只有获得了超过半数选票的节点才能成为 master。在 ES 中使用 discovery.zen.minimum_master_nodes 属性设置 quorum，这个属性一般设置为 eligibleNodesNum / 2 + 1。

*如何触发一次选举*
当满足如下条件是，集群内就会发生一次 master 选举
  当前 master eligible 节点不是 master
  当前 master eligible 节点与其它的节点通信无法发现 master
  集群中无法连接到 master 的 master eligible 节点数量已达到 discovery.zen.minimum_master_nodes 所设定的值

*如何选举*
  当某个节点决定要进行一次选举是，它会实现如下操作
  寻找 `clusterStateVersion` 比自己高的 master eligible 的节点，向其发送选票
  如果 clusterStateVersion 一样，则计算自己能找到的 master eligible 节点（包括自己）中*节点 id 最小*的一个节点，向该节点发送选举投票
  如果一个节点收到足够多的投票（即 minimum_master_nodes 的设置），并且它也向自己投票了，那么该节点成为 master 开始发布集群状态
下面我们用一个实际的例子来解释选举流程，假设有 node_a 和 node_b，node_a 向 node_b 发送选票。
如果 node_b 已经是 master，则 node_b 就把 node_a 加入集群，之后 node_b 发布最新的集群状态，此时 node_a 会被包含在最新的集群状态里面。
如果 node_b 正在进行选举，则 node_b 会把这次投票记录下来，之后 node_b 可能成为 master 或者继续等待选票。node_a 等待 node_b 发送最新的集群状态或者超时触发下一次投票。
如果 node_b 认为自己不会成为 master，则拒绝这次投票，node_a 将触发下一次投票。

