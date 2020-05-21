
# 接口说明


## 1 前置管理模块

### 1.1 新增节点前置


#### 1.1.1 传输协议规范
* 网络传输协议：使用HTTP协议
* 请求地址： **/front/new**
* 请求方式：POST
* 请求头：Content-type: application/json
* 返回格式：JSON

#### 1.1.2 请求参数

***1）入参表***

| 序号 | 输入参数    | 类型          | 可为空 | 备注                                       |
|------|-------------|---------------|--------|-------------------------------|
| 1    | frontIp     | string        | 否     | 前置ip                                     |
| 2    | frontPort   | int           | 否     | 前置服务端口                               |
| 3    | agency      | int           | 否     | 所属机构                               |

***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/front/new
```

```
{
    "frontIp": "127.0.0.1",
    "frontPort": "5002",
    "agency": "test"
}
```


#### 1.1.3 返回参数

***1）出参表***

| 序号 | 输出参数   | 类型          | 可为空 | 备注                       |
| ---- | ---------- | ------------- | ------ | -------------------------- |
| 1    | code       | Int           | 否     | 返回码，0：成功 其它：失败 |
| 2    | message    | String        | 否     | 描述                       |
| 3    |            | Object        |        | 节点信息对象               |
| 3.1  | frontId    | Int           | 否     | 前置编号                   |
| 3.2  | frontIp    | string        | 否     | 前置ip                     |
| 3.3  | frontPort  | Int           | 否     | 前置端口                   |
| 3.4  | nodeId     | string        | 否     | 节点编号                   |
| 3.5  | agency     | string        | 否     | 所属机构                   |
| 3.6  | createTime | LocalDateTime | 是     | 落库时间                   |
| 3.7  | modifyTime | LocalDateTime | 是     | 修改时间                   |

***2）出参示例***
* 成功：
```
{
  "code": 0,
  "message": "success",
  "data": {
    "frontId": 100001,
    "nodeId": "944607f7e83efe2ba72476dc39a269a910811db8caac34f440dd9c9dd8ec2490b8854b903bd6c9b95c2c79909649977b8e92097c2f3ec32232c4f655b5a01850",
    "frontIp": "127.0.0.1",
    "frontPort": 5002,
    "agency": "test",
    "createTime": null,
    "modifyTime": null
  }
}
```

* 失败：
```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```


### 1.2 获取所有前置列表 


#### 1.2.1 传输协议规范
* 网络传输协议：使用HTTP协议
* 请求地址：**/front/list?frontId={frontId}&groupId={groupId}**
* 请求方式：GET
* 返回格式：JSON

#### 1.2.2 请求参数

***1）入参表***

| 序号 | 输入参数    | 类型          | 可为空 | 备注                 |
|------|-------------|---------------|--------|-------------------------------|
| 1     | frontId       | Int           | 是     | 前置编号                  |
| 2     | groupId       | Int           | 是     | 群组编号                |


***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/front/list
```


#### 1.2.3 返回参数 

***1）出参表***

| 序号 | 输出参数    | 类型          |        | 备注                                       |
|------|-------------|---------------|--------|-------------------------------|
| 1     | code          | Int           | 否     | 返回码，0：成功 其它：失败 |
| 2     | message       | String        | 否     | 描述                       |
| 3     | totalCount    | Int           | 否     | 总记录数                   |
| 4     | data          | List          | 否     | 组织列表                   |
| 4.1   |               | Object        |        | 节点信息对象               |
| 4.1.1 | frontId       | Int           | 否     | 前置编号                   |
| 4.1.2 | frontIp       | string        | 否     | 前置ip                     |
| 4.1.3 | frontPort     | Int           | 否     | 前置端口                   |
| 4.1.4 | nodeId | string | 否 | 节点编号 |
| 4.1.5 | agency | string | 否 | 所属机构 |
| 4.1.6 | createTime    | LocalDateTime | 否     | 落库时间                   |
| 4.1.7 | modifyTime    | LocalDateTime | 否     | 修改时间                   |

***2）出参示例***
* 成功：
```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "frontId": 100001,
      "nodeId": "944607f7e83efe2ba72476dc39a269a910811db8caac34f440dd9c9dd8ec2490b8854b903bd6c9b95c2c79909649977b8e92097c2f3ec32232c4f655b5a01850",
      "frontIp": "127.0.0.1",
      "frontPort": 5002,
      "agency": "test",
      "createTime": "2020-05-20 20:22:35",
      "modifyTime": "2020-05-20 20:22:35"
    }
  ],
  "totalCount": 1
}
```

* 失败：
```
{
   "code": 102000,
   "message": "system exception",
   "data": {}
}
```


### 1.3 删除前置信息

#### 1.3.1 传输协议规范
* 网络传输协议：使用HTTP协议
* 请求地址：**/front/{frontId}**
* 请求方式：DELETE
* 请求头：Content-type: application/json
* 返回格式：JSON

#### 1.3.2 请求参数

***1）入参表***

| 序号 | 输入参数    | 类型          | 可为空 | 备注                                       |
|------|-------------|---------------|--------|-------------------------------|
| 1    | frontId    | int    | 否     | 前置编号                   |


***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/front/100001
```


#### 1.3.3 返回参数 

***1）出参表***

| 序号 | 输出参数    | 类型          |        | 备注                                       |
|------|-------------|---------------|--------|-------------------------------|
| 1    | code       | Int    | 否     | 返回码，0：成功 其它：失败 |
| 2    | message    | String | 否     | 描述                       |
| 3    | data       | object | 是     | 返回信息实体（空）         |


***2）出参示例***
* 成功：
```
{
    "code": 0,
    "data": {},
    "message": "Success"
}
```

* 失败：
```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

## 2 群组管理模块

### 2.1 获取群组列表

#### 2.1.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址：**/group/list**
- 请求方式：GET
- 返回格式：JSON

#### 2.1.2 请求参数

***1）入参表***

无

***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/group/list
```

#### 2.1.3 返回参数 

***1）出参表***

| 序号  | 输出参数         | 类型          |      | 备注                       |
| ----- | ---------------- | ------------- | ---- | -------------------------- |
| 1     | code             | Int           | 否   | 返回码，0：成功 其它：失败 |
| 2     | message          | String        | 否   | 描述                       |
| 3     | totalCount       | Int           | 否   | 总记录数                   |
| 4     | data             | List          | 否   | 列表                       |
| 4.1   |                  | Object        |      | 信息对象                   |
| 4.1.1 | groupId          | Int           | 否   | 群组编号                   |
| 4.1.2 | groupName        | String        | 否   | 群组名称                   |
| 4.1.3 | genesisBlockHash | String        | 否   | 创世块hash                 |
| 4.1.4 | groupStatus      | Int           | 否   | 群组状态                   |
| 4.1.5 | nodeCount        | Int           | 否   | 节点个数                   |
| 4.1.6 | createTime       | LocalDateTime | 否   | 落库时间                   |
| 4.1.7 | modifyTime       | LocalDateTime | 否   | 修改时间                   |

***2）出参示例***

- 成功：

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "groupId": 1,
      "groupName": "group1",
      "genesisBlockHash": "0x7bc361d7d8e078ea9e8f352f2b856d6ea76ab1b9522f4b09853c861d0ed0779f",
      "groupStatus": 1,
      "nodeCount": 2,
      "createTime": "2020-05-20 20:22:35",
      "modifyTime": "2020-05-20 20:31:38"
    },
    {
      "groupId": 2,
      "groupName": "group2",
      "genesisBlockHash": "0x1208de0d47dcba9447d304039d1e4512dd4ce740ec408ef83c5f7ee2aefc7468",
      "groupStatus": 1,
      "nodeCount": 2,
      "createTime": "2020-05-20 20:22:36",
      "modifyTime": "2020-05-20 20:31:38"
    }
  ],
  "totalCount": 2
}
```

- 失败：

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

## 3 区块管理模块

### 3.1 查询区块列表

#### 3.1.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址：**/block/list/{groupId}/{pageNumber}/{pageSize}}?blockHash={blockHash}&blockNumber={blockNumber}**
- 请求方式：GET
- 返回格式：JSON

#### 3.1.2 请求参数

***1）入参表***

| 序号 | 输入参数    | 类型       | 可为空 | 备注       |
| ---- | ----------- | ---------- | ------ | ---------- |
| 1    | groupId     | Int        | 否     | 当前所属链 |
| 2    | pageSize    | Int        | 否     | 每页记录数 |
| 3    | pageNumber  | Int        | 否     | 当前页码   |
| 4    | blockHash   | String     | 是     | 区块hash   |
| 5    | blockNumber | BigInteger | 是     | 块高       |

***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/block/list/1/1/2?blockHash=
```

#### 3.1.3 返回参数 

***1）出参表***

| 序号  | 输出参数       | 类型          |      | 备注                       |
| ----- | -------------- | ------------- | ---- | -------------------------- |
| 1     | code           | Int           | 否   | 返回码，0：成功 其它：失败 |
| 2     | message        | String        | 否   | 描述                       |
| 3     | totalCount     | Int           | 否   | 总记录数                   |
| 4     | data           | List          | 是   | 区块列表                   |
| 4.1   |                | Object        |      | 区块信息对象               |
| 4.1.1 | blockHash      | String        | 否   | 块hash                     |
| 4.1.2 | blockNumber    | BigInteger    | 否   | 块高                       |
| 4.1.3 | blockTimestamp | LocalDateTime | 否   | 出块时间                   |
| 4.1.4 | transCount     | Int           | 否   | 交易数                     |
| 4.1.5 | sealerIndex    | Int           | 否   | 打包节点索引               |
| 4.1.6 | sealer         | String        | 否   | 打包节点                   |
| 4.1.7 | createTime     | LocalDateTime | 否   | 创建时间                   |
| 4.1.8 | modifyTime     | LocalDateTime | 否   | 修改时间                   |

***2）出参示例***

- 成功：

```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 26,
      "blockHash": "0x1d0a57a6ee2b73e537ef6d929c8d0bdb2a9799dd6357f04dc5f38e4e0c6c5ac2",
      "blockNumber": 35,
      "blockTimestamp": "2020-05-13 19:47:37",
      "transCount": 1,
      "sealerIndex": 0,
      "sealer": "944607f7e83efe2ba72476dc39a269a910811db8caac34f440dd9c9dd8ec2490b8854b903bd6c9b95c2c79909649977b8e92097c2f3ec32232c4f655b5a01850",
      "createTime": "2020-05-20 20:22:41",
      "modifyTime": "2020-05-20 20:22:41"
    },
    {
      "id": 8,
      "blockHash": "0x4c29bb921f4bf346ad1f92704e225f6323c85f16f2fa4eb0e3f126355ff9fa12",
      "blockNumber": 34,
      "blockTimestamp": "2020-05-13 19:12:20",
      "transCount": 1,
      "sealerIndex": 0,
      "sealer": "944607f7e83efe2ba72476dc39a269a910811db8caac34f440dd9c9dd8ec2490b8854b903bd6c9b95c2c79909649977b8e92097c2f3ec32232c4f655b5a01850",
      "createTime": "2020-05-20 20:22:41",
      "modifyTime": "2020-05-20 20:22:41"
    }
  ],
  "totalCount": 36
}
```

- 失败：

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

### 3.2 根据块高查询区块信息

#### 3.2.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址：**/block/blockByNumber/{groupId}/{blockNumber}**
- 请求方式：GET
- 返回格式：JSON

#### 3.2.2 请求参数

***1）入参表***

| 序号 | 输入参数    | 类型       | 可为空 | 备注     |
| ---- | ----------- | ---------- | ------ | -------- |
| 1    | groupId     | Int        | 否     | 群组编号 |
| 2    | blockNumber | BigInteger | 是     | 块高     |

***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/block/blockByNumber/1/1
```

#### 3.2.3 返回参数 

***1）出参表***

| 序号      | 输出参数            | 类型       |      | 备注                       |
| --------- | ------------------- | ---------- | ---- | -------------------------- |
| 1         | code                | Int        | 否   | 返回码，0：成功 其它：失败 |
| 2         | message             | String     | 否   | 描述                       |
| 3         |                     | Object     |      | 区块信息对象               |
| 3.1       | number              | BigInteger | 否   | 块高                       |
| 3.2       | hash                | String     | 否   | 区块hsah                   |
| 3.3       | parentHash          | String     | 否   | 父块hash                   |
| 3.4       | nonce               | String     | 否   | 随机数                     |
| 3.5       | sealer              | String     | 否   | 打包节点索                 |
| 3.6       | logsBloom           | String     | 否   | log的布隆过滤值            |
| 3.7       | transactionsRoot    | String     | 否   |                            |
| 3.8       | stateRoot           | String     | 否   |                            |
| 3.9       | difficulty          | String     | 否   |                            |
| 3.10      | totalDifficulty     | String     | 否   |                            |
| 3.11      | extraData           | String     | 否   |                            |
| 3.12      | size                | Int        | 否   |                            |
| 3.13      | gasLimit            | long       | 否   | 限制gas值                  |
| 3.14      | gasUsed             | long       | 否   | 已使用的gas值              |
| 3.15      | timestamp           | String     | 否   | 出块时间                   |
| 3.16      | gasLimitRaw         | String     | 否   |                            |
| 3.17      | timestampRaw        | String     | 否   |                            |
| 3.18      | gasUsedRaw          | String     | 否   |                            |
| 3.19      | numberRaw           | String     | 否   |                            |
| 3.20      | transactions        | List       | 否   |                            |
| 3.20.1    |                     | Object     |      | 交易信息对象               |
| 3.20.1.1  | hash                | String     | 否   | 交易hash                   |
| 3.20.1.2  | blockHash           | String     | 否   | 区块hash                   |
| 3.20.1.3  | blockNumber         | BigInteger | 否   | 所属块高                   |
| 3.20.1.4  | transactionIndex    | Int        | 否   | 在区块中的索引             |
| 3.20.1.5  | from                | String     | 否   | 交易发起者                 |
| 3.20.1.6  | to                  | String     | 否   | 交易目标                   |
| 3.20.1.7  | value               | String     | 否   |                            |
| 3.20.1.8  | gasPrice            | long       | 否   |                            |
| 3.20.1.9  | gas                 | long       | 否   |                            |
| 3.20.1.10 | input               | String     | 否   |                            |
| 3.20.1.11 | v                   | Int        | 否   |                            |
| 3.20.1.12 | nonceRaw            | String     | 否   |                            |
| 3.20.1.13 | blockNumberRaw      | String     | 否   |                            |
| 3.20.1.14 | transactionIndexRaw | String     | 否   |                            |
| 3.20.1.15 | gasPriceRaw         | String     | 否   |                            |
| 3.20.1.16 | gasRaw              | String     | 否   |                            |

***2）出参示例***

- 成功：

```
{
  "code": 0,
  "message": "success",
  "data": {
    "number": 1,
    "hash": "0xadcd64e3744c4b71eb628598c5455409be152e5d721d04da3503ffc169c322ca",
    "parentHash": "0x7bc361d7d8e078ea9e8f352f2b856d6ea76ab1b9522f4b09853c861d0ed0779f",
    "nonce": "0",
    "sealer": "0x1",
    "logsBloom": "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
    "transactionsRoot": "0x710710af3bcf4ff06ab330a4eff36ce5da5e0d787d44a4431357e30cd585bc76",
    "stateRoot": "0x793cd1bcafc6cb16300eb62bd52c61c5d2f72ca4ff7087c158c1c5f2eb3074a8",
    "difficulty": 0,
    "totalDifficulty": 0,
    "extraData": [],
    "size": 0,
    "gasLimit": 0,
    "gasUsed": 0,
    "timestamp": "1587110719512",
    "gasLimitRaw": "0x0",
    "timestampRaw": "0x171872bb018",
    "gasUsedRaw": "0x0",
    "numberRaw": "0x1",
    "transactions": [
      {
        "hash": "0x72c8fd1724b308f596e373c432814f37b1058ac241ceaa4e19b1f0b22dfe63fb",
        "blockHash": "0xadcd64e3744c4b71eb628598c5455409be152e5d721d04da3503ffc169c322ca",
        "blockNumber": 1,
        "transactionIndex": 0,
        "from": "0x95824169cf64b29a9bcd5036fe0fa78ca0ecaa6f",
        "to": null,
        "value": 0,
        "gasPrice": 1,
        "gas": 100000000,
        "input": "0x608060405234801561001057600080fd5b506040805190810160405280600581526020017f48656c6c6f0000000000000000000000000000000000000000000000000000008152506000908051906020019061005c929190610062565b50610107565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100a357805160ff19168380011785556100d1565b828001600101855582156100d1579182015b828111156100d05782518255916020019190600101906100b5565b5b5090506100de91906100e2565b5090565b61010491905b808211156101005760008160009055506001016100e8565b5090565b90565b610373806101166000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063299f7f9d146100515780633590b49f146100e1575b600080fd5b34801561005d57600080fd5b5061006661014a565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100a657808201518184015260208101905061008b565b50505050905090810190601f1680156100d35780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156100ed57600080fd5b50610148600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506101ec565b005b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101e25780601f106101b7576101008083540402835291602001916101e2565b820191906000526020600020905b8154815290600101906020018083116101c557829003601f168201915b5050505050905090565b7f05432a43e07f36a8b98100b9cb3631e02f8e796b0a06813610ce8942e972fb81816040518080602001828103825283818151815260200191508051906020019080838360005b8381101561024e578082015181840152602081019050610233565b50505050905090810190601f16801561027b5780820380516001836020036101000a031916815260200191505b509250505060405180910390a1806000908051906020019061029e9291906102a2565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102e357805160ff1916838001178555610311565b82800160010185558215610311579182015b828111156103105782518255916020019190600101906102f5565b5b50905061031e9190610322565b5090565b61034491905b80821115610340576000816000905550600101610328565b5090565b905600a165627a7a72305820663f556d1fabbdfa0c83c4df6bbcd7d78c47cf47a9def109f71085e1518c04310029",
        "v": 0,
        "nonceRaw": "0x3ba172d6bbd82f45b297d12fae27013943100719889b8bc0723309d4a40b3aa",
        "blockNumberRaw": "0x1",
        "transactionIndexRaw": "0x0",
        "gasPriceRaw": "0x1",
        "gasRaw": "0x5f5e100"
      }
    ]
  }
}
```

- 失败：

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

## 4 交易信息模块


### 4.1 查询交易信息列表

#### 4.1.1 传输协议规范

* 网络传输协议：使用HTTP协议
* 请求地址：
```
/transaction/list/{groupId}/{pageNumber}/{pageSize}?transHash={transHash}&blockNumber={blockNumber}
```
* 请求方式：GET
* 返回格式：JSON

#### 4.1.2 请求参数

***1）入参表***

| 序号 | 输入参数    | 类型          | 可为空 | 备注                                       |
|------|-------------|---------------|--------|-------------------------------|
| 1     | groupId         | Int           | 否     | 所属群组编号               |
| 2 | pageNumber | Int | 否 | 当前页码 |
| 3 | pageSize | Int | 否 | 每页记录数 |
| 4     | transHash   | String        | 是     | 交易hash                   |
| 5     | blockNumber     | BigInteger    | 是     | 块高                       |


***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/transaction/list/1/1/2?transHash=0x4933b1e0a7d6913a2179b879cdf716096d8da1c162fe400a492b0d61259e2ab2
```


#### 4.1.3 返回参数 

***1）出参表***

| 序号 | 输出参数    | 类型          |        | 备注                                       |
|------|-------------|---------------|--------|-------------------------------|
| 1     | code            | Int           | 否     | 返回码，0：成功 其它：失败 |
| 2     | message         | String        | 否     | 描述                       |
| 3     | totalCount      | Int           | 否     | 总记录数                   |
| 4     | data            | List          | 否     | 交易信息列表               |
| 4.1   |                 | Object        |        | 交易信息对象               |
| 4.1.1 | transHash       | String        | 否     | 交易hash                   |
| 4.1.2 | transFrom | String     | 否     | 发送方              |
| 4.1.3 | transTo | String | 否 | 接收方 |
| 4.1.4 | input | String | 否 | 输入信息 |
| 4.1.5 | blockNumber     | BigInteger    | 否     | 所属块高                   |
| 4.1.6 | blockTimestamp | LocalDateTime | 否 | 所属块出块时间 |
| 4.1.7 | auditFlag | Int           | 否     | 是否已统计（1-未审计，2-已审计） |
| 4.1.8 | createTime      | LocalDateTime | 否     | 落库时间                   |
| 4.1.9 | modifyTime      | LocalDateTime | 否     | 修改时间                   |


***2）出参示例***
* 成功：
```
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 26,
      "transHash": "0x4933b1e0a7d6913a2179b879cdf716096d8da1c162fe400a492b0d61259e2ab2",
      "transFrom": "0x95824169cf64b29a9bcd5036fe0fa78ca0ecaa6f",
      "transTo": "0xd221da074ac2f34d6b453d3d456576c45e3f0843",
      "input": "0x3590b49f000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000047465737400000000000000000000000000000000000000000000000000000000",
      "blockNumber": 35,
      "blockTimestamp": "2020-05-13 19:47:37",
      "auditFlag": 1,
      "createTime": "2020-05-20 20:22:41",
      "modifyTime": "2020-05-20 20:22:41"
    }
  ],
  "totalCount": 1
}
```

* 失败：
```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

### 4.2 根据交易hash查询交易信息 

#### 4.2.1 传输协议规范

- 网络传输协议：使用HTTP协议
- 请求地址：**/transaction/transInfo/{groupId}/{transHash}**
- 请求方式：GET
- 返回格式：JSON

#### 4.2.2 参数信息详情

请求参数

***1）入参表***

| 序号 | 输入参数  | 类型   | 可为空 | 备注         |
| ---- | --------- | ------ | ------ | ------------ |
| 1    | groupId   | Int    | 否     | 所属群组编号 |
| 2    | transHash | String | 是     | 交易hash     |

***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/transaction/transInfo/1/0x4933b1e0a7d6913a2179b879cdf716096d8da1c162fe400a492b0d61259e2ab2
```

#### 4.2.3 返回参数 

***1）出参表***

| 序号 | 输出参数            | 类型       |      | 备注                       |
| ---- | ------------------- | ---------- | ---- | -------------------------- |
| 1    | code                | Int        | 否   | 返回码，0：成功 其它：失败 |
| 2    | message             | String     | 否   | 描述                       |
| 3    |                     | Object     |      | 交易信息对象               |
| 3.1  | hash                | String     | 否   | 交易hash                   |
| 3.2  | blockHash           | String     | 否   | 区块hash                   |
| 3.3  | blockNumber         | BigInteger | 否   | 所属块高                   |
| 3.4  | transactionIndex    | Int        | 否   | 在区块中的索引             |
| 3.5  | from                | String     | 否   | 交易发起者                 |
| 3.6  | to                  | String     | 否   | 交易目标                   |
| 3.7  | value               | String     | 否   |                            |
| 3.8  | gasPrice            | long       | 否   |                            |
| 3.9  | gas                 | long       | 否   |                            |
| 3.10 | input               | String     | 否   |                            |
| 3.11 | v                   | Int        | 否   |                            |
| 3.12 | nonceRaw            | String     | 否   |                            |
| 3.13 | blockNumberRaw      | String     | 否   |                            |
| 3.14 | transactionIndexRaw | String     | 否   |                            |
| 3.15 | gasPriceRaw         | String     | 否   |                            |
| 3.16 | gasRaw              | String     | 否   |                            |

***2）出参示例***

- 成功：

```
{
  "code": 0,
  "message": "success",
  "data": {
    "hash": "0x4933b1e0a7d6913a2179b879cdf716096d8da1c162fe400a492b0d61259e2ab2",
    "blockHash": "0x1d0a57a6ee2b73e537ef6d929c8d0bdb2a9799dd6357f04dc5f38e4e0c6c5ac2",
    "blockNumber": 35,
    "transactionIndex": 0,
    "from": "0x95824169cf64b29a9bcd5036fe0fa78ca0ecaa6f",
    "to": "0xd221da074ac2f34d6b453d3d456576c45e3f0843",
    "value": 0,
    "gasPrice": 1,
    "gas": 100000000,
    "input": "0x3590b49f000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000047465737400000000000000000000000000000000000000000000000000000000",
    "v": 0,
    "nonceRaw": "0x1a00f64575c78412465016b2efb7f80bece2474db763e4f014ce8bf1fa5aa50",
    "blockNumberRaw": "0x23",
    "transactionIndexRaw": "0x0",
    "gasPriceRaw": "0x1",
    "gasRaw": "0x5f5e100"
  }
}
```

- 失败：

```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

### 4.3 根据交易hash查询交易回执 

#### 4.3.1 传输协议规范

* 网络传输协议：使用HTTP协议
* 请求地址：**/transaction/receipt/{groupId}/{transHash}**
* 请求方式：GET
* 返回格式：JSON

#### 4.3.2 请求参数

***1）入参表***

| 序号 | 输入参数    | 类型          | 可为空 | 备注                                       |
|------|-------------|---------------|--------|-------------------------------|
| 1     | groupId         | Int           | 否     | 群组编号               |
| 2     | transHash | String        | 是     | 交易hash                   |


***2）入参示例***

```
http://localhost:5009/WeBASE-Data-Collect/transaction/receipt/1/0x4933b1e0a7d6913a2179b879cdf716096d8da1c162fe400a492b0d61259e2ab2
```


#### 4.3.3 返回参数 

***1）出参表***

| 序号 | 输出参数    | 类型          |        | 备注                                       |
|------|-------------|---------------|--------|-------------------------------|
| 1     | code            | Int           | 否     | 返回码，0：成功 其它：失败 |
| 2     | message         | String        | 否     | 描述                       |
| 3     |                 | Object        |        | 交易信息对象               |
| 3.1 | transactionHash       | String        | 否     | 交易hash                   |
| 3.2 | transactionIndex         | Int           | 否     | 在区块中的索引               |
| 3.2 | blockHash         | String           | 否     | 区块hash               |
| 3.3 | blockNumber     | BigInteger    | 否     | 所属块高                   |
| 3.4 | cumulativeGasUsed  | Int           | 否     |                |
| 3.5 | gasUsed      | Int | 否     | 交易消耗的gas                   |
| 3.6 | contractAddress      | String | 否     | 合约地址                   |
| 3.7 | status      | String | 否     | 交易的状态值                   |
| 3.8 | from      | String | 否     | 交易发起者                   |
| 3.9 | to      | String | 否     | 交易目标                   |
| 3.10 | output      | String | 否     | 交易输出内容                   |
| 3.11 | logs      | String | 否     | 日志                   |
| 3.12 | logsBloom      | String | 否     | log的布隆过滤值                   |


***2）出参示例***
* 成功：
```
{
  "code": 0,
  "message": "success",
  "data": {
    "transactionHash": "0x4933b1e0a7d6913a2179b879cdf716096d8da1c162fe400a492b0d61259e2ab2",
    "transactionIndex": 0,
    "blockHash": "0x1d0a57a6ee2b73e537ef6d929c8d0bdb2a9799dd6357f04dc5f38e4e0c6c5ac2",
    "blockNumber": 35,
    "cumulativeGasUsed": 0,
    "gasUsed": 34949,
    "contractAddress": "0x0000000000000000000000000000000000000000",
    "status": "0x0",
    "from": "0x95824169cf64b29a9bcd5036fe0fa78ca0ecaa6f",
    "to": "0xd221da074ac2f34d6b453d3d456576c45e3f0843",
    "output": "0x",
    "logs": [
      {
        "logIndex": null,
        "transactionIndex": null,
        "transactionHash": null,
        "blockHash": null,
        "blockNumber": null,
        "address": "0xd221da074ac2f34d6b453d3d456576c45e3f0843",
        "data": "0x000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000047465737400000000000000000000000000000000000000000000000000000000",
        "topics": [
          "0x05432a43e07f36a8b98100b9cb3631e02f8e796b0a06813610ce8942e972fb81"
        ]
      }
    ],
    "logsBloom": "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100000004000000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000000000000080000000000000000000000000000000000000000000000000000000000000000000000000000000000"
  }
}
```

* 失败：
```
{
    "code": 102000,
    "message": "system exception",
    "data": {}
}
```

