
# 数据目录, client
data_dir = "/data/nomad/data"

bind_addr = "0.0.0.0" 

server {
  enabled = true
  # 节点
  bootstrap_expect = 1
}

client {
  enabled = true

  # 挂载目录配置
  host_volume "filebeat-data" {
    path      = "/data/filebeat/data"
    read_only = false
  }

  host_volume "nomad-alloc" {
    path      = "/data/nomad/data/alloc"
    read_only = true
  }


}

plugin "docker" {
  # 插件配置
  config {
    infra_image = "registry.aliyuncs.com/google_containers/pause-amd64:3.1"
    gc {
      # 是否在job死亡后删除镜像
       image = false
    }
  }
}





