# ElasticJobTree
基于当当网的弹性job

###Quartz的缺点

![](https://i.imgur.com/pnpGusk.png)

<pre>
Quartz集群缺陷：
              1）时间规则更改不方便，需同步更改数据库时间规则描述。
              2）Quartz集群当节点不在同一台服务器上，因为时钟的可能不同步导致节点对其他节
                 点状态产生的影响。

           Quartz是Java事实上的任务标准，但Quartz关注点在于定时任务而非数据，并无一套根据
      数据处理而定制化的流程。虽然Quartz可以基于数据库实现作业的高可用，但缺少分布式并行执行
      作业的功能。
</pre>

###ElasticJob

![](https://i.imgur.com/6ambTsF.png)

<pre>
ElasticJob主要功能：

      1）定时任务：
                 基于成熟的定时作业框架Quartz cron表达式执行定时任务。
      2）作业注册中心：
                 基于Zookeeper和其客户端Curator实现的全局作业注册控制中心，用于注册，控制
              ，协调分布式作业执行。
      3）作业分片：
                 将一个任务分片成多个小任务项在服务器上同时执行。
      5）弹性扩容伸缩
                 支持OneOff,Perpetual和SequencePerpetual三种作业模式。
      6）失效转移
                 运行中的作业服务器崩溃不会导致重新分片，只会在下次作业启动时分片，启用失效
            转移功能可以在本次作业过程中，监测其他作业服务器空闲，抓取未完成的孤儿分片项执行。
      7）运行时状态收集：
                 监控作业运行时状态，统计最近一段时间处理的数据成功和失败数量，记录作业上次
            运行开始时间，结束时间和下次运行时间。
      8）作业停止，恢复和禁用
                 用于操作作业启停，并可以禁止某作业运行。
      9）被错过执行的作业重新触发
                 自动记录错过执行的作业，并在上次作业完成后自动触发，
      10）多线程快速处理数据
                 使用多线程处理抓取到的数据，提升吞吐量。
      11）幂等性
                 重复作业任务项判定，不重复执行已运行的作业任务项。由于开启幂等性需要监听作业
             运行状态，对瞬时反复运行的作业队性能有较大影响。
      12）容错处理
                 作业服务器与Zookeeper服务器通信失败则立即停止运行，防止作业注册中心将失效
             的分片项分配给其他作业服务器，而当前作业服务器仍在执行任务，导致重复执行。
      13）Spring支持
      15）提供运维界面，可以管理作业和注册中心
</pre>

<pre>
Elastic Job

      Elastic底层的任务调度还是使用的quartz，通过zookeeper来动态给job节点分片。

      使用elastic-job开发的作业都是zookeeper的客户端，比如我希望3台机器跑job，我们将任务
      分成3片，框架通过zk的协调，最终会让3台机器分别分配到0,1,2的任务片，比如server0-->0，server1-->1，server2-->2，当server0执行时，可以只查询id%3==0的用户，server1执行时，只查询id%3==1的用户，server2执行时，只查询id%3==2的用户。

      任务部署多节点引发重复执行

          在上面的基础上，我们再增加server3，此时，server3分不到任务分片，因为只有3片，已
      经分完了。没有分到任务分片的作业程序将不执行。
 
          如果此时server2挂了，那么server2的分片项会分配给server3，server3有了分片，就会
      替代server2执行。
 
          如果此时server3也挂了，只剩下server0和server1了，框架也会自动把server3的分片随
      机分配给server0或者server1，可能会这样，server0-->0，server1-->1,2。

          这种特性称之为弹性扩容，即elastic-job名称的由来
</pre>

<pre>
Zookeeper分片

      业务迅速发展带来了跑批数据量的急剧增加。单机处理跑批数据已不能满足需要，另考虑到企业处
      理数据的扩展能力，多机跑批势在必行。多机跑批是指将跑批任务分发到多台服务器上执行，多机
      跑批的前提是”数据分片”。elasticJob通过JobShardingStrategy支持分片跑批。

      ElasticJob 默认提供了如下三种分片策略：
             1）AverageAllocationJobShardingStrategy:
                基于平均算法的分片策略；
             2）OdevitySortByNameJobShardingStrategy:
                根据作业名的哈希值奇偶数决定IP升降序算法的分片策略
             3）RotateServerByNameJobShardingStrategy:
                根据作业名的哈希值对服务器列表进行轮转的分片策略
         
             默认使用AverageAllocationJobShardingStrategy分片策略。

      数据库曾片的分片方案：

             
</pre>